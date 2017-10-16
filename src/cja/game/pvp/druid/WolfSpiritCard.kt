package cja.game.pvp.druid

import cja.game.GameButton
import cja.game.GameMessage
import cja.game.pvp.*

public class WolfSpiritCard : MoveCard, CardProcess, ButtonsCard {
    var message : GameMessage = GameMessage("Wolf Spirit", true);
    var button1 : EpicPvpButton = EpicPvpButton(this, "Choose 1");
    var button2 : EpicPvpButton = EpicPvpButton(this, "Choose 2");

    constructor(img : String) : super("Wolf Spirit", 5, 5, 5) {
        this.text = "End phase: If this move damaged your enemy, choose - 1 or 2.  Add all moves with the printed cost in your discard pile to your hand.";
    }

    override fun startProcess(state : EpicPvpGameState) {
        state.placeButtons(message, button1, button2, 100f);
    }

    override fun handleClick(state : EpicPvpGameState, ptx: Float, pty: Float): Boolean {
        var obj = state.getObjectAt(ptx, pty);

        println("" + obj + ": " + ptx + ", " + pty);

        if(obj == button1) {
            drawCardsFromDiscard(state, 1);
        } else if(obj == button2) {
            drawCardsFromDiscard(state, 2);
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

    fun drawCardsFromDiscard(state : EpicPvpGameState, num : Int) {
        var discard = state.getPlayerDiscard(this.owner);
        var hand = state.getPlayerHand(this.owner);
        for(card in discard.cards) {
            var pvp = card as EpicPvpCard;
            if(pvp.cost == num) {
                discard.cards.remove(pvp);
                hand.hand.add(pvp);
            }
        }

        state.getCardProcesses(this.owner).remove(this);
        state.gameObjects.remove(message);
        state.gameObjects.remove(button1);
        state.gameObjects.remove(button2);
        state.handleClick(-1f, -1f);
    }

    override fun afterEndPhase(state : EpicPvpGameState) {
        if (blockedBy == null) {
            state.getCardProcesses(this.owner).add(this);
        }
    }

    override fun create() : EpicPvpCard {
        return WolfSpiritCard("");
    }

    override fun getButton(label: String): EpicPvpButton {
        return when(label) { button1.buttonLabel -> button1; else -> button2; }
    }
}