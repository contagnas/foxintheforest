package foxintheforest

import foxintheforest.Seat.{Player1, Player2}
import foxintheforest.components.Card

case class Player(
  hand: Set[Card] = Set.empty,
  points: Int = 0,
  tricksWon: Int = 0,
  thisRoundPoints: Int = 0
)

sealed trait Seat { self =>
  def otherPlayer: Seat = self match {
    case Player1 => Player2
    case Player2 => Player1
  }
}

object Seat {
  case object Player1 extends Seat
  case object Player2 extends Seat
}

