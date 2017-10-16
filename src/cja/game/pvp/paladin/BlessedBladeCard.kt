package cja.game.pvp.paladin

import cja.game.GameButton
import cja.game.GameMessage
import cja.game.pvp.*

public class BlessedBladeCard : MoveCard, CardProcess, ButtonsCard {
    var message : GameMessage = GameMessage("Blessed Blade", true);
    var button1 : EpicPvpButton = EpicPvpButton(this, "Discard Blessed Blade");
    var button2 : EpicPvpButton = EpicPvpButton(this, "Do NOT discard");
    var discarding : Boolean = false;

    constructor(img : String) : super("Blessed Blade", 1, 1, 1) {
        this.text = "After playing moves: You may discard this move from play to give one of your other moves a +1 attack, a +1 defense, and a +1 damage counter.";
    }

    override fun startProcess(state : EpicPvpGameState) {
        state.placeButtons(message, button1, button2, 150f);
    }

    override fun handleClick(state : EpicPvpGameState, ptx: Float, pty: Float): Boolean {
        var obj = state.getObjectAt(ptx, pty);

        if(obj == button1) {
            for(card in state.moveArea.moves.keys) {
                if(card == this) {
                    state.moveArea.moves.remove(card);
                } else if(state.moveArea.moves.get(card) == this) {
                    state.moveArea.moves.put(card, null);
                }
                state.discardCard(this.owner, this);
            }
            state.gameObjects.remove(button1);
            state.gameObjects.remove(button2);
            discarding = true;

            if(state.countMoves(this.owner) == 0) {
                endAction(state);
            }
        } else if(obj == button2) {
            endAction(state);
        } else if(obj == state.moveArea && discarding) {
            var card = state.moveArea.getCardAt(ptx, pty);
            if(card != null && card.highlight) {
                card.attack++;
                card.defend++;
                card.damage++;
                endAction(state);
            }
        }

        return true;
    }

    override fun setHighlights(state : EpicPvpGameState) {
        if(!state.gameObjects.contains(button1) && !discarding) {
            startProcess(state);
        }
        if(!discarding) {
            button1.highlight = true;
            button2.highlight = true;
        } else {
            for(card in state.moveArea.moves.keys) {
                if(card.owner == this.owner) {
                    card.highlight = true;
                } else if(state.moveArea.moves.get(card) != null && state.moveArea.moves.get(card)!!.owner == this.owner) {
                    state.moveArea.moves.get(card)!!.highlight = true;
                }
            }
        }
    }

    fun endAction(state : EpicPvpGameState) {
        state.getCardProcesses(this.owner).remove(this);
        state.gameObjects.remove(message);
        state.gameObjects.remove(button1);
        state.gameObjects.remove(button2);
        state.handleClick(-1f, -1f);
    }

    override fun afterMovePhase(state : EpicPvpGameState) {
        state.getCardProcesses(this.owner).add(this);
    }

    override fun create() : EpicPvpCard {
        return BlessedBladeCard("");
    }

    override fun clonePropertiesTo(card: EpicPvpCard) {
        super.clonePropertiesTo(card)
        if(card is BlessedBladeCard) {
            card.discarding = discarding;
        }
    }

    override fun getButton(label: String): EpicPvpButton {
        return when(label) { button1.buttonLabel -> button1; else -> button2; }
    }
}
