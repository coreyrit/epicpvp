package cja.game.pvp.druid

import cja.game.GameButton
import cja.game.GameMessage
import cja.game.pvp.*

public class DruidClass : ClassCard, CardProcess, ButtonsCard {
    var message : GameMessage = GameMessage("Druid Ability", true);
    var button1 : EpicPvpButton = EpicPvpButton(this, "Discard aggression");
    var button2 : EpicPvpButton = EpicPvpButton(this, "Do NOT discard");
    var blockAll : Boolean = false;

    constructor(owner : Boolean) : super(owner, "Druid", 2) {
        this.text = "Before you play moves: Once per turn, you may discard a card from your aggression pile.  " +
                "If it's a Basic Strike, block all attacks on any one move."
    }

    override fun startProcess(state : EpicPvpGameState) {
        blockAll = false;
        state.placeButtons(message, button1, button2, 200f);
    }

    override fun handleClick(state : EpicPvpGameState, ptx: Float, pty: Float): Boolean {
        var obj = state.getObjectAt(ptx, pty);

        if(obj == button1) {
            var aggression = state.getPlayerAggression(this.owner);
            if(aggression.aggression.size > 0) {
                var card = aggression.aggression.removeAt(0);
                state.discardCard(this.owner, card);
                if("Basic Strike".equals(card.name)) {
                    blockAll = true;
                }
            }
            endAction(state);
        } else if(obj == button2) {
            endAction(state);
        }

        return true;
    }

    override fun setHighlights(state : EpicPvpGameState) {
        if(!state.gameObjects.contains(button1)) {
            startProcess(state);
        }
        button1.highlight = true;
        button2.highlight = true;
    }

    fun endAction(state : EpicPvpGameState) {
        state.getCardProcesses(this.owner).remove(this);
        state.gameObjects.remove(message);
        state.gameObjects.remove(button1);
        state.gameObjects.remove(button2);
        state.handleClick(-1f, -1f);
    }

    override fun beforeMovePhase(state : EpicPvpGameState) {
        // don't bother asking unless there are moves against me
        if(state.countMoves(!this.owner) > 0) {
            state.getCardProcesses(this.owner).add(this);
        }
    }

    override fun afterMovePhase(state : EpicPvpGameState) {
        if(blockAll && state.movesPlayed > 0) {
            for(card in state.moveArea.moves.keys) {
                if(card.owner != this.owner) {
                    state.discardMove(card, this as EpicPvpCard);
                }
            }
        }
    }

    override fun create() : EpicPvpCard {
        return DruidClass(owner);
    }

    override fun clonePropertiesTo(card: EpicPvpCard) {
        super.clonePropertiesTo(card);
        if(card is DruidClass) {
            card.blockAll = blockAll;
        }
    }

    override fun getButton(label: String): EpicPvpButton {
        return when(label) { button1.buttonLabel -> button1; else -> button2; }
    }
}