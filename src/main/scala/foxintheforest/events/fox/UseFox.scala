package foxintheforest.events.fox

import foxintheforest.{CLISupport, GameState, Parseable}
import foxintheforest.events.{Event, FinishPlayingCard, GetPlayerInput, Lead}

case class UseFox(use: Boolean) extends Event {
  override def validationError(state: GameState): Option[InvalidMessage] =
    if (use && state.currentPlayerState.hand.isEmpty)
      Some("You have no cards left to exchange.")
    else None

  override def run(state: GameState): GameState = state

  override def nextEvent(state: GameState): Event =
    if (use) GetPlayerInput[ApplyFox] else FinishPlayingCard
}

object UseFox {
  implicit object CLISupport extends CLISupport[UseFox] {
    override def parse(string: String): Option[UseFox] =
      Parseable[Boolean].parse(string).map(UseFox.apply)

    override def prompt(state: GameState): String = "Use the fox effect?"

    override def allPossibleMoves: Seq[UseFox] =
      Seq(true, false).map(UseFox.apply)

    override def unparse(e: UseFox): String = Parseable[Boolean].unparse(e.use)
  }
}
