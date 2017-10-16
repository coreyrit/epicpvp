package cja.game.pvp.paladin

import cja.game.GameButton
import cja.game.GameMessage
import cja.game.pvp.*

public class CallLightningCard : SpecialCard, CardProcess, ButtonsCard {
    var message : GameMessage = GameMessage("Call Lightning", true);
    var button1 : EpicPvpButton = EpicPvpButton(this, "Take 1 damage");
    var button2 : EpicPvpButton = EpicPvpButton(this, "Discard 2 aggression");

    override fun startProcess(state : EpicPvpGameState) {
        state.placeButtons(message, button1, button2, 150f);
    }

    override fun handleClick(state : EpicPvpGameState, ptx: Float, pty: Float): Boolean {
        var obj = state.getObjectAt(ptx, pty);

        if(obj == button1) {
            state.dealDamage(!this.owner);
            endAction(state);
        } else if(obj == button2) {
            var aggression = state.getPlayerAggression(!this.owner);
            for(i in 1..2) {
                if(aggression.aggression.size() > 0) {
                    var card = aggression.aggression.remove(0);
                    card.isFront = true;
                    state.discardCard(!this.owner, card);
                }
            }
            endAction(state);
        }

        return true;
    }

    fun endAction(state : EpicPvpGameState) {
        state.getCardProcesses(!this.owner).remove(this);
        state.gameObjects.remove(message);
        state.gameObjects.remove(button1);
        state.gameObjects.remove(button2);
        state.handleClick(-1f, -1f);
    }

    override fun setHighlights(state : EpicPvpGameState) {
        if(!state.gameObjects.contains(button1)) {
            startProcess(state);
        }
        button1.highlight = true;
        button2.highlight = true;
    }

    constructor(pth : String) : super("Call Lightning") {
        this.text = "After playing moves: Your enemy chooses - They take 1 damage or discard two cards from their aggression pile.";
    }

    override fun afterMovePhase(state : EpicPvpGameState) {
        state.getCardProcesses(!this.owner).add(this);
    }

    override fun create() : EpicPvpCard {
        return CallLightningCard("");
    }

    override fun getButton(label: String): EpicPvpButton {
        return when(label) { button1.buttonLabel -> button1; else -> button2; }
    }
}
