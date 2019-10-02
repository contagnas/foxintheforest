package foxintheforest

import foxintheforest.events.{DealCards, Event, GameOver}

import scala.util.Random

object Main extends App {
  val initialState = GameState.initialState(
    Constants(
      rng = new Random(),
      pointsToWin = 21
    )
  )

  LazyList.continually(0)
    .scanLeft[(GameState, Event)]((initialState, DealCards)) {
      case ((state, event), _) =>
        val nextState = event.run(state)
        val nextEvent = event.nextEvent(nextState)
        (nextState, nextEvent)
    }.find {
      case (_, GameOver(result)) =>
        println(result)
        true
      case _ => false
    }
}
