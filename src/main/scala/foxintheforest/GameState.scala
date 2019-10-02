package foxintheforest

import foxintheforest.Seat.{Player1, Player2}
import foxintheforest.components.Card

import scala.util.Random

case class Constants(
  rng: Random,
  pointsToWin: Int
)

case class GameState(
  deck: Seq[Card],
  players: Map[Seat, Player],
  dealer: Seat,
  currentPlayer: Seat,
  decree: Card,
  leadCard: Option[Card] = None,
  followCard: Option[Card] = None,
  constants: Constants
) {
  val currentPlayerState: Player = players(currentPlayer)
  def updatePlayer(seat: Seat, f: Player => Player): GameState =
    copy(players = players.updatedWith(seat)(_.map(f)))

  def updateCurrentPlayer(f: Player => Player): GameState =
    updatePlayer(currentPlayer, f)
}

object GameState {
  def initialState(constants: Constants): GameState = {
    GameState(
      deck = Card.values,
      Map(Player1 -> Player(), Player2 -> Player()),
      dealer = Player1,
      currentPlayer = Player2,
      decree = null,
      constants = constants
    )
  }
}