package cja.game.pvp.dwarf

import cja.game.pvp.EpicPvpCard
import cja.game.pvp.EpicPvpGameState
import cja.game.pvp.MoveCard

public class ToughCard : MoveCard {
    constructor(cost : Int, attack : Int, defend : Int) : super("Tough", cost, attack, defend) {
        this.text = "Nothing a little ale won't fix.";
    }

    override fun create(): EpicPvpCard {
        return AncientGrudgeCard();
    }
}
