package cja.game.pvp.human

import cja.game.pvp.CardProcess
import cja.game.pvp.EpicPvpCard
import cja.game.pvp.EpicPvpGameState
import cja.game.pvp.SpecialCard

public class PowerStrikeCard : SpecialCard, CardProcess {

    constructor(pth : String) : super("Power Strike") {
        this.text = "After playing moves: One of your move gains two +1 attack and two +1 defense counters.";
    }

    override fun startProcess(state : EpicPvpGameState) {

    }

    override fun handleClick(state : EpicPvpGameState, ptx: Float, pty: Float): Boolean {
        var obj = state.getObjectAt(ptx, pty);
        if(obj == state.moveArea) {
            var card = state.moveArea.getCardAt(ptx, pty);
            if(card != null && card.highlight) {
                card.attack += 2;
                card.defend += 2;
                state.getCardProcesses(this.owner).remove(this);
                state.handleClick(-1f, -1f);
            }
        }

        if(state.countMoves(this.owner) == 0) {
            state.getCardProcesses(this.owner).remove(this);
            state.handleClick(-1f, -1f);
        }
        return true;
    }

    override fun setHighlights(state : EpicPvpGameState) {
        for(card in state.moveArea.moves.keySet()) {
            if(card.owner == this.owner) {
                card.highlight = true;
            } else if(state.moveArea.moves.get(card) != null && state.moveArea.moves.get(card)!!.owner == this.owner) {
                state.moveArea.moves.get(card)!!.highlight = true;
            }
        }
    }

    override fun afterMovePhase(state : EpicPvpGameState) {
        state.getCardProcesses(this.owner).add(this);
    }

    override fun create() : EpicPvpCard {
        return PowerStrikeCard("");
    }
}
