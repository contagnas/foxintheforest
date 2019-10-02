package foxintheforest.events.woodcutter

import foxintheforest.GameState
import foxintheforest.events.{Event, GetPlayerInput}

case object WoodcutterDraw extends Event {
  override def run(state: GameState): GameState =
    state
      .updateCurrentPlayer(p => p.copy(hand = p.hand + state.deck.head))
      .copy(deck = state.deck.tail)

  override def nextEvent(state: GameState): Event =
    GetPlayerInput[WoodcutterDiscard]
}
