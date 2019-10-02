package foxintheforest.events

import foxintheforest.components.Card
import foxintheforest.components.Rank._
import foxintheforest.{CLISupport, GameState}
import monocle.macros.syntax.lens._

case class Follow(card: Card) extends Event {
  override def validationError(state: GameState): Option[InvalidMessage] = {
    val hand = state.currentPlayerState.hand
    val leadCard = state.leadCard.get
    val leadMonarch = leadCard.rank == Monarch
    val suitCards = hand.filter(_.suit == leadCard.suit)

    val legalAgainstMonarch = card.suit == leadCard.suit && {
      card.rank == Swan || suitCards.forall(_.rank.value <= card.rank.value)
    }

    if (!hand.contains(card))
      Some(s"You do not have the $card.")
    else if (suitCards.nonEmpty && card.suit != leadCard.suit) {
      Some("You must play the same suit as the lead suit.")
    } else if (leadMonarch && !legalAgainstMonarch && suitCards.nonEmpty) {
      Some("You must play either you highest card or a swan against the Monarch.")
    } else None
  }

  override def run(state: GameState): GameState =
    state.updateCurrentPlayer(p => p.copy(hand = p.hand - card))
      .lens(_.followCard).set(Some(card))

  override def nextEvent(state: GameState): Event =
    TriggerCardEffects
}

object Follow {
  implicit object CLISupport extends CLISupport[Follow] {
    override def parse(string: String): Option[Follow] =
      Card.withNameInsensitiveOption(string).map(Follow.apply)

    override def prompt(state: GameState): String =
      "Select card to follow with."

    override def allValidMoves(gameState: GameState): Set[Follow] =
      Card.values.map(Follow.apply).filter(_.validationError(gameState).isEmpty).toSet

    override def unparse(e: Follow): String = e.card.toString
  }
}
