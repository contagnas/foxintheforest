package foxintheforest

import foxintheforest.events.{Enumerable, Event}
import simulacrum.typeclass

@typeclass trait Promptable[E] {
  def prompt(state: GameState): String
}

@typeclass trait Parseable[E] {
  def name: Option[String] = None
  def parse(string: String): Option[E]
  def unparse(e: E): String

  def parseToEither(input: String): Either[String, E] = {
    val errorMessage = name match {
      case Some(n) =>
        s"$input is not a valid $n"
      case None =>
        s"$input is not valid"
    }

    parse(input).toRight(errorMessage)
  }
}

object Parseable {
  private val booleanStrings: Map[String, Boolean] = Map(
    "yes" -> true,
    "y" -> true,
    "ok" -> true,
    "no" -> false,
    "n'" -> false,
  )
  implicit object ParseableBoolean extends Parseable[Boolean] {
    override val name: Option[String] = Some("boolean")

    override def parse(string: String): Option[Boolean] =
      booleanStrings.get(string)

    override def unparse(e: Boolean): String = if (e) "yes" else "no"
  }
}

@typeclass trait CLISupport[E <: Event] extends Promptable[E] with Parseable[E] with Enumerable[E]

