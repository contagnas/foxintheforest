package foxintheforest.events

import foxintheforest.GameState
import foxintheforest.components.Rank.{Fox, Woodcutter}
import foxintheforest.events.fox.UseFox
import foxintheforest.events.woodcutter.UseWoodcutter

object TriggerCardEffects extends Event {

  override def run(state: GameState): GameState = state

  override def nextEvent(state: GameState): Event = {
    val leading = state.followCard.isEmpty
    val card = if (leading) state.leadCard.get else state.followCard.get
    card.rank match {
      case Fox => GetPlayerInput[UseFox]
      case Woodcutter => GetPlayerInput[UseWoodcutter]
      case _ => FinishPlayingCard
    }
  }
}
