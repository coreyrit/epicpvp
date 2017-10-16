package cja.game.pvp.druid

import cja.game.pvp.EpicPvpCard
import cja.game.pvp.EpicPvpGameState
import cja.game.pvp.MoveCard

public class TanglevinesCard: MoveCard {

    constructor(cost : Int, attack : Int, defend : Int) : super("Tangledvines", cost, attack, defend) {
        this.text = "Aggression phase: Your enemy gains one less aggression.";
    }

    override fun beforeAggressionPhase(state : EpicPvpGameState) {
        state.aggressionDraw--;
    }

    override fun create() : EpicPvpCard {
        return TanglevinesCard(cost, attack, defend);
    }
}
