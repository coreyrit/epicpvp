package cja.game.pvp.human

import cja.game.pvp.CardProcess
import cja.game.pvp.EpicPvpCard
import cja.game.pvp.EpicPvpGameState
import cja.game.pvp.MoveCard

public class MirrorStrikeCard : MoveCard, CardProcess {

    constructor() : super("Mirror Strike", 5, 5, 5) {
        this.text = "After playing moves: This move gains the current text of any of your other moves in play.  This move can not become a permanent.";
    }

    override fun startProcess(state : EpicPvpGameState) {

    }

    // TODO: may need work
    override fun handleClick(state : EpicPvpGameState, ptx: Float, pty: Float): Boolean {
        var obj = state.getObjectAt(ptx, pty);
        if(obj == state.moveArea) {
            var card = state.moveArea.getCardAt(ptx, pty);
            if(card != null && card.highlight) {

                var mirroredCard = card.create() as MoveCard;
                mirroredCard.name = this.name;
                this.copyPropertiesTo(mirroredCard);

                // replace this with mirror
                for(target in state.moveArea.moves.keys) {
                    if(target == this) {
                        var defend = state.moveArea.moves.remove(target);
                        state.moveArea.assignMove(mirroredCard, defend);
                    } else if(state.moveArea.moves.get(target) == this) {
                        state.moveArea.assignMove(target, mirroredCard);
                    }
                }

                // hit its after move phase text - its only fair
                mirroredCard.afterMovePhase(state);

                state.getCardProcesses(this.owner).remove(this);
                state.handleClick(-1f, -1f);
            }
        }

        if(state.countMoves(this.owner) <= 1) { // 1 move means just this card
            state.getCardProcesses(this.owner).remove(this);
            state.handleClick(-1f, -1f);
        }
        return true;
    }

    override fun setHighlights(state : EpicPvpGameState) {
        for(card in state.moveArea.moves.keys) {
            if(card.owner == this.owner && card != this) {
                card.highlight = true;
            } else if(state.moveArea.moves.get(card) != null && state.moveArea.moves.get(card)!!.owner == this.owner && state.moveArea.moves.get(card) != this) {
                state.moveArea.moves.get(card)!!.highlight = true;
            }
        }
    }

    override fun afterMovePhase(state : EpicPvpGameState) {
        state.getCardProcesses(this.owner).add(this);
    }

    override fun create() : EpicPvpCard {
        return MirrorStrikeCard();
    }
}