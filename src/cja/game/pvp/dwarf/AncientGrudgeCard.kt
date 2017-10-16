package cja.game.pvp.dwarf

import cja.game.pvp.EpicPvpCard
import cja.game.pvp.EpicPvpGameState
import cja.game.pvp.MoveCard

public class AncientGrudgeCard : MoveCard {
    constructor(img : String) : super("Ancient Grudge", 1, 1, 1) {
        this.text = "After playing moves: If your enemy is a Goblin or an Orc this move gains a +1 damage counter.";
    }

    override fun afterMovePhase(state: EpicPvpGameState) {
        if(state.getPlayerRace(!this.owner).name.equals("Goblin") || state.getPlayerRace(!this.owner).name.equals("Orc")) {
            damage++;
        }
    }

    override fun create(): EpicPvpCard {
        return AncientGrudgeCard("");
    }
}
