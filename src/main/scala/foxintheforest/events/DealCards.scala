package foxintheforest.events

import foxintheforest.GameState
import foxintheforest.Seat.{Player1, Player2}
import foxintheforest.components.Card
import monocle.macros.syntax.lens._

object DealCards extends Event {
  override def run(state: GameState): GameState = {
    val deck = state.constants.rng.shuffle(Card.values)
    val decree = deck.head
    val deck1 = deck.tail
    val player1Hand = deck1.take(13).toSet
    val deck2 = deck1.drop(13)
    val player2Hand = deck2.take(13).toSet
    val deck3 = deck2.drop(13)

    state
      .updatePlayer(Player1, _.copy(hand = player1Hand, thisRoundPoints = 0))
      .updatePlayer(Player2, _.copy(hand = player2Hand, thisRoundPoints = 0))
      .lens(_.deck).set(deck3)
      .lens(_.decree).set(decree)
  }

  override def nextEvent(state: GameState): Event = GetPlayerInput[Lead]
}
