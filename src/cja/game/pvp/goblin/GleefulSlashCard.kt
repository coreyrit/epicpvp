package cja.game.pvp.goblin

import cja.game.GameButton
import cja.game.GameMessage
import cja.game.pvp.*

public class GleefulSlashCard : MoveCard, CardProcess, ButtonsCard {
    var message : GameMessage = GameMessage("Gleeful Slash", true);
    var button1 : EpicPvpButton = EpicPvpButton(this, "Gain Aggression");
    var button2 : EpicPvpButton = EpicPvpButton(this, "Do NOT Gain Aggression");

    constructor(cost : Int, attack : Int, defend : Int) : super("Gleeful Slash", cost, attack, defend) {
        this.text = "If your enemy took damage from at least one attack, you may gain one aggression.";
    }

    override fun startProcess(state : EpicPvpGameState) {
        state.placeButtons(message, button1, button2, 200f);
    }

    override fun handleClick(state : EpicPvpGameState, ptx: Float, pty: Float): Boolean {
        var obj = state.getObjectAt(ptx, pty);

        if(obj == button1) {
            state.getPlayerAggression(this.owner).aggression.add(state.drawCard(this.owner));
            endAction(state);
        } else if(obj == button2) {
            endAction(state);
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
        if(!state.gameObjects.contains(button1)) {
            startProcess(state);
        }
        button1.highlight = true;
        button2.highlight = true;
    }

    override fun afterEndPhase(state : EpicPvpGameState) {
        if(state.damageTaken > 0) {
            state.getCardProcesses(this.owner).add(this);
        }
    }

    override fun create() : EpicPvpCard {
        return GleefulSlashCard(cost, attack, defend);
    }

    override fun getButton(label: String): EpicPvpButton {
        return when(label) { button1.buttonLabel -> button1; else -> button2; }
    }
}
