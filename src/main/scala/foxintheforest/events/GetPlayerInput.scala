package foxintheforest.events

import foxintheforest.{CLISupport, GameState, Parseable, Promptable}

import scala.io.StdIn
import scala.util.Random

case class GetPlayerInput[T <: Event: PlayerInput]() extends Event {
  override def run(state: GameState): GameState = state

  override def nextEvent(state: GameState): Event = {
    val event = for {
      event <- PlayerInput[T].getInput(state)
      _ <- event.validationError(state).toLeft(())
    } yield event

    event match {
      case Left(error) =>
        PlayerInput[T].reportError(error)
        GetPlayerInput[T]
      case Right(event) =>
        event
    }
  }
}

trait PlayerInput[A] {
  def getInput(gameState: GameState): Either[String, A]
  def reportError(string: String): Unit
}

object PlayerInput {
  def apply[T: PlayerInput]: PlayerInput[T] = implicitly[PlayerInput[T]]

  implicit def cliSupport[E <: Event: CLISupport]: PlayerInput[E] =
    new PlayerInput[E] {
      override def getInput(state: GameState): Either[String, E] = {
        val moveOptions = Enumerable[E].allValidMoves(state)
        val moveOptionStrings = moveOptions.map(Parseable[E].unparse)
        println(s"${state.currentPlayer}: ${Promptable[E].prompt(state)}")
        moveOptions.size match {
          case n if n <= 0 => throw new IllegalStateException("Expecting player input when none is possible")
          case 1 =>
            println(s"You have only one choice: ${moveOptionStrings.head}. Selecting it.")
            Right(moveOptions.head)
          case _ =>
            println(s"Options: ${moveOptionStrings.mkString("[", "|", "]")}")
            print(">>> ")
            Parseable[E].parseToEither(StdIn.readLine)
        }
      }

      override def reportError(string: String): Unit =
        println(string)
    }
}

