package foxintheforest.events.woodcutter

import foxintheforest.events.{Event, FinishPlayingCard}
import foxintheforest.{CLISupport, GameState, Parseable}

case class UseWoodcutter(use: Boolean) extends Event {
  override def run(state: GameState): GameState = state

  override def nextEvent(state: GameState): Event =
    if (use) WoodcutterDraw else FinishPlayingCard
}

object UseWoodcutter {
  implicit object CLISupport extends CLISupport[UseWoodcutter] {
    override def parse(string: String): Option[UseWoodcutter] =
      Parseable[Boolean].parse(string).map(UseWoodcutter.apply)

    override def prompt(state: GameState): String = "Use the woodcutter effect?"

    override def allPossibleMoves: Seq[UseWoodcutter] =
      Seq(true, false).map(UseWoodcutter.apply)

    override def unparse(e: UseWoodcutter): String = Parseable[Boolean].unparse(e.use)
  }
}

