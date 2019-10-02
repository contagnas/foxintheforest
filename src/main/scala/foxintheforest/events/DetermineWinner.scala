package foxintheforest.events

import foxintheforest.GameState
import foxintheforest.Seat.{Player1, Player2}

object DetermineWinner extends Event {
  override def run(state: GameState): GameState = state

  def nextEvent(state: GameState): GameOver = {
    val players = state.players
    val scores = players.view.mapValues(_.points).toMap
    val lastRoundPoints = players.view.mapValues(_.thisRoundPoints).toMap

    val gameResult = {
      if (scores(Player1) > scores(Player2))
        Winner(Player1, scores)
      else if (scores(Player2) > scores(Player1))
        Winner(Player2, scores)
      else if (lastRoundPoints(Player1) > lastRoundPoints(Player2))
        Winner(Player1, scores, Some(lastRoundPoints))
      else if (lastRoundPoints(Player2) > lastRoundPoints(Player1))
        Winner(Player2, scores, Some(lastRoundPoints))
      else
        Tie
    }
    GameOver(gameResult)
  }
}
