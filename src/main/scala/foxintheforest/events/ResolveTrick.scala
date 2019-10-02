package foxintheforest.events

import foxintheforest.GameState
import foxintheforest.components.Rank.{Swan, Treasure, Witch}
import foxintheforest.components.{Card, Suit}

object ResolveTrick extends Event {
  private def trickWinner(lead: Card, follow: Card, decree: Suit): Card = {
    val cards = List(lead, follow)
    val witchApplies = cards.count(_.rank == Witch) == 1
    val suits: (Suit, Suit) =
      if (witchApplies) {
        if (lead.rank == Witch) (decree, follow.suit)
        else (lead.suit, decree)
      } else {
        (lead.suit, follow.suit)
      }

    suits match {
      case (lead, follow) if lead == follow => cards.maxBy(_.rank.value)
      case (ldecree, _) if ldecree == decree => lead // leading player played a witch
      case (_, fdecree) if fdecree == decree => follow
      case _ => lead
    }
  }

  override def run(state: GameState): GameState = {
    // Leader resolves the trick
    val leader = state.currentPlayer
    val lead = state.leadCard.get
    val follow = state.followCard.get
    val winningCard = trickWinner(state.leadCard.get, state.followCard.get, state.decree.suit)
    val leadWon = lead == winningCard
    val losingCard = if (leadWon) follow else lead
    val tookTrick = if (leadWon) leader else leader.otherPlayer
    val pointsAwarded = List(lead, follow).count(_.rank == Treasure)
    val nextLeader = if (losingCard.rank == Swan) tookTrick.otherPlayer else tookTrick
    state.updatePlayer(
      tookTrick,
      p => p.copy(
        points = p.points + pointsAwarded,
        tricksWon = p.tricksWon + 1,
        thisRoundPoints = p.thisRoundPoints + 1
      )
    ).copy(
      currentPlayer = nextLeader,
      leadCard = None,
      followCard = None
    )
  }

  override def nextEvent(state: GameState): Event =
    if (state.players.values.forall(_.hand.nonEmpty))
      GetPlayerInput[Lead]
    else
      NextRound
}
