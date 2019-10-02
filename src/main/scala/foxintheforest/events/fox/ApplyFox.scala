package foxintheforest.events.fox

import foxintheforest.components.Card
import foxintheforest.events.{Event, FinishPlayingCard}
import foxintheforest.{CLISupport, GameState}

case class ApplyFox(card: Card) extends Event {
  override def validationError(state: GameState): Option[InvalidMessage] =
    if (!state.currentPlayerState.hand.contains(card))
      Some(s"You do not have the $card.")
    else None

  override def run(state: GameState): GameState =
    state.updateCurrentPlayer(
      p => p.copy(hand = (p.hand + state.decree) - card)
    ).copy(decree = card)

  override def nextEvent(state: GameState): Event = FinishPlayingCard
}

object ApplyFox {
  implicit object CLISupport extends CLISupport[ApplyFox] {
    override def parse(string: String): Option[ApplyFox] =
    Card.withNameInsensitiveOption(string).map(ApplyFox.apply)

    override def prompt(state: GameState): String =
      s"Select the card swap with the decree card (${state.decree.toString})."

    override def allPossibleMoves: Seq[ApplyFox] =
      Card.values.map(ApplyFox.apply)

    override def unparse(e: ApplyFox): String = e.card.toString
  }
}
