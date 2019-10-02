package foxintheforest.events

import foxintheforest.{GameState, Seat}

sealed trait GameResult
case object Tie extends GameResult
case class Winner(
  winner: Seat,
  points: Map[Seat, Int],
  tieBreaker: Option[Map[Seat, Int]] = None
) extends GameResult

case class GameOver(result: GameResult) extends Event { self =>
  override def run(state: GameState): GameState = state
  override def nextEvent(state: GameState): Event = self
}
