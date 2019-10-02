package foxintheforest.events.woodcutter

import foxintheforest.components.Card
import foxintheforest.events.fox.ApplyFox
import foxintheforest.events.{Event, FinishPlayingCard}
import foxintheforest.{CLISupport, GameState}

case class WoodcutterDiscard(card: Card) extends Event {
  override def validationError(state: GameState): Option[InvalidMessage] =
    if (!state.currentPlayerState.hand.contains(card))
      Some(s"You do not have the $card.")
    else None

  override def run(state: GameState): GameState =
    state
      .updateCurrentPlayer(p => p.copy(hand = p.hand - card))
      .copy(deck = state.deck.appended(card))

  override def nextEvent(state: GameState): Event = FinishPlayingCard
}

object WoodcutterDiscard {
  implicit object CLISupport extends CLISupport[WoodcutterDiscard] {
    override def parse(string: String): Option[WoodcutterDiscard] =
      Card.withNameInsensitiveOption(string).map(WoodcutterDiscard.apply)

    override def prompt(state: GameState): String =
      s"Select the card to place on the bottom of the draw deck."

    override def allPossibleMoves: Seq[WoodcutterDiscard] =
      Card.values.map(WoodcutterDiscard.apply)

    override def unparse(e: WoodcutterDiscard): String = e.card.toString
  }
}
