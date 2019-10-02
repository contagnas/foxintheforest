package foxintheforest.events

import foxintheforest.GameState

object FinishPlayingCard extends Event {
  override def run(state: GameState): GameState =
    state.copy(currentPlayer = state.currentPlayer.otherPlayer)

  override def nextEvent(state: GameState): Event =
    if (state.followCard.isEmpty) GetPlayerInput[Follow]
    else ResolveTrick
}
