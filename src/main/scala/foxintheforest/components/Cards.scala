package foxintheforest.components

import enumeratum._

sealed abstract class Suit(override val entryName: String) extends EnumEntry

object Suit extends Enum[Suit] {
  case object Bells extends Suit("B")
  case object Keys extends Suit("K")
  case object Moons extends Suit("M")

  override def values: IndexedSeq[Suit] = findValues
}

sealed trait Rank extends EnumEntry {
  val value: Int
  override lazy val entryName: String = value.toString
}

object Rank extends Enum[Rank] {
  case object Swan extends Rank { val value = 1 }
  case object Two extends Rank { val value = 2 }
  case object Fox extends Rank { val value = 3 }
  case object Four extends Rank { val value = 4 }
  case object Woodcutter extends Rank { val value = 5 }
  case object Six extends Rank { val value = 6 }
  case object Treasure extends Rank { val value = 7 }
  case object Eight extends Rank { val value = 8 }
  case object Witch extends Rank { val value = 9 }
  case object Ten extends Rank { val value = 10 }
  case object Monarch extends Rank { val value = 11 }

  override def values: IndexedSeq[Rank] = findValues
}

case class Card(rank: Rank, suit: Suit) extends EnumEntry {
  override def toString: String = rank.entryName + suit.entryName
}

object Card extends Enum[Card] {
  override def values: IndexedSeq[Card] = for {
    suit <- Suit.values
    rank <- Rank.values
  } yield Card(rank, suit)
}



