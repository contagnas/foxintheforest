package foxintheforest.events

import foxintheforest.components.Card
import foxintheforest.{CLISupport, GameState}
import monocle.macros.syntax.lens._

case class Lead(card: Card) extends Event {
  override def validationError(state: GameState): Option[InvalidMessage] =
    if (!state.currentPlayerState.hand.contains(card))
      Some(s"You do not have the $card.")
    else None

  override def run(state: GameState): GameState =
    state.updateCurrentPlayer(p => p.copy(hand = p.hand - card))
      .lens(_.leadCard).set(Some(card))

  override def nextEvent(state: GameState): Event =
    TriggerCardEffects

  override def toString: String = card.toString
}

object Lead {
  implicit object CLISupport extends CLISupport[Lead] {
    override def parse(string: String): Option[Lead] =
      Card.withNameInsensitiveOption(string).map(Lead.apply)

    override def prompt(state: GameState): String =
      "Select card to lead with."

    override def allValidMoves(gameState: GameState): Set[Lead] =
      Card.values.map(Lead.apply).filter(_.validationError(gameState).isEmpty).toSet

    override def unparse(e: Lead): String = e.card.toString
  }
}
