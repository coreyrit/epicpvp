package cja.game.pvp.goblin

import cja.game.pvp.EpicPvpCard
import cja.game.pvp.EpicPvpGameState
import cja.game.pvp.SpecialCard

public class SkitterAwayCard : SpecialCard {

    constructor(pth : String) : super("Skitter Away") {
        this.text = "After playing moves: If you only played one move this turn, block all enemy attacks.";
    }

    override fun afterMovePhase(state : EpicPvpGameState) {
        if(state.movesPlayed == 1) {
            for(card in state.moveArea.moves.keySet()) {
                if(state.moveArea.moves.get(card) == null && card.owner != this.owner) {
                    state.discardMove(card, this);
                }
            }
        }
    }

    override fun create() : EpicPvpCard {
        return SkitterAwayCard("");
    }
}