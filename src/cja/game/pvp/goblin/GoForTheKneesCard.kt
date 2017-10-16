package cja.game.pvp.goblin

import cja.game.GameButton
import cja.game.GameMessage
import cja.game.pvp.*

public class GoForTheKneesCard : MoveCard, CardProcess, ButtonsCard {
    var permanent : Boolean = false;
    var message : GameMessage = GameMessage("Go for the Knees", true);
    var button1 : EpicPvpButton = EpicPvpButton(this, "Discard for +1 defend");
    var button2 : EpicPvpButton = EpicPvpButton(this, "Do NOT discard");
    var choosing : Boolean = false;

    constructor(img : String) : super("Go for the Knees", 2, 2, 2) {
        this.text = "After blocking: If this attack was blocked put in play as permanent.  " +
                "After playing moves: Once per turn, you may discard a card from your deck to give one of your moves a +1 defense counter.";
    }

    override fun startProcess(state : EpicPvpGameState) {
        state.placeButtons(message, button1, button2, 200f);
    }

    override fun handleClick(state : EpicPvpGameState, ptx: Float, pty: Float): Boolean {
        var obj = state.getObjectAt(ptx, pty);

        if(obj == button1) {
            choosing = true;
            state.gameObjects.remove(button1);
            state.gameObjects.remove(button2);
            if(state.countMoves(this.owner) == 0) {
                endAction(state);
            }
        } else if(obj == button2) {
            endAction(state);
        } else if(choosing && obj == state.moveArea) {
            var card = state.moveArea.getCardAt(ptx, pty);
            if(card != null && card.highlight) {
                var discard = state.drawCard(this.owner);
                state.discardCard(this.owner, discard);
                card.defend++;
                endAction(state);
            }
        }

        return true;
    }

    fun endAction(state : EpicPvpGameState) {
        state.getCardProcesses(this.owner).remove(this);
        state.gameObjects.remove(message);
        state.gameObjects.remove(button1);
        state.gameObjects.remove(button2);
        state.handleClick(-1f, -1f);
    }

    override fun setHighlights(state : EpicPvpGameState) {
        if(!choosing && !state.gameObjects.contains(button1)) {
            startProcess(state);
        }
        if(!choosing) {
            button1.highlight = true;
            button2.highlight = true;
        } else {
            for(card in state.moveArea.moves.keySet()) {
                if(card.owner == this.owner) {
                    card.highlight = true;
                } else if(state.moveArea.moves.get(card) != null && state.moveArea.moves.get(card)!!.owner == this.owner) {
                    state.moveArea.moves.get(card)!!.highlight = true;
                }
            }
        }
    }

    override fun create() : EpicPvpCard {
        // a clone of this has nothing special about it because it can't be a permanent
        var card = BasicStrikeCard(2, 2, 2);
        card.name = "Go for the Knees";
        return card;
    }

    override fun afterEndPhase(state : EpicPvpGameState) {
        if(blockedBy != null && !permanent) {
            state.getPlayerPermanents(this.owner).add(this);
            permanent = true;
            state.getPlayerDiscard(this.owner).cards.remove(this);
        }
    }

    override fun afterMovePhase(state : EpicPvpGameState) {
        if(permanent) {
            choosing = false;
            state.getCardProcesses(this.owner).add(this);
        }
    }

    override fun clonePropertiesTo(card : EpicPvpCard) {
        super.clonePropertiesTo(card);
        if(card is GoForTheKneesCard) {
            card.permanent = permanent;
            card.choosing = choosing;
        }
    }

    override fun getButton(label: String): EpicPvpButton {
        return when(label) { button1.buttonLabel -> button1; else -> button2; }
    }
}
