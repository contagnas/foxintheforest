package foxintheforest.events

import foxintheforest.{GameState, Player}

object NextRound extends Event {
  def scoreRound(player: Player): Player = {
    val pointsEarned = player.tricksWon match {
      case 0 | 1  | 2 | 3 => 6
      case 4 => 1
      case 5 => 2
      case 6 => 3
      case 7 | 8 | 9 => 6
      case 10 | 11 | 12 | 13 => 0
    }

    player.copy(
      points = player.points + pointsEarned,
      tricksWon = 0,
      thisRoundPoints = player.thisRoundPoints + pointsEarned
    )
  }

  override def run(state: GameState): GameState = {
    val dealer = state.dealer.otherPlayer
      val leader = dealer.otherPlayer
      state.copy(
        players = state.players.view.mapValues(scoreRound).toMap,
        dealer = dealer,
        currentPlayer = leader
      )
  }

  override def nextEvent(state: GameState): Event =
    if (state.players.values.exists(_.points >= state.constants.pointsToWin))
      DetermineWinner
    else
      DealCards
}
