package cja.game.pvp.paladin

import cja.game.GameButton
import cja.game.GameMessage
import cja.game.pvp.*

public class HolyStrikeCard : MoveCard, CardProcess, ButtonsCard {
    var message : GameMessage = GameMessage("Holy Strike", true);
    var button1 : EpicPvpButton = EpicPvpButton(this, "+1 Attack");
    var button2 : EpicPvpButton = EpicPvpButton(this, "+1 Defend");

    constructor(cost : Int, attack : Int, defend : Int) : super("Holy Strike", cost, attack, defend) {
        this.text = "After playing moves: You may add a +1 attack or a +1 defense counter to this move.";
    }

    override fun startProcess(state : EpicPvpGameState) {
        state.placeButtons(message, button1, button2, 150f);
    }

    override fun handleClick(state : EpicPvpGameState, ptx: Float, pty: Float): Boolean {
        var obj = state.getObjectAt(ptx, pty);
        if(obj == button1) {
            this.attack++;
            endAction(state);
        } else if(obj == button2) {
            this.defend++;
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

    override fun afterMovePhase(state : EpicPvpGameState) {
        state.getCardProcesses(this.owner).add(this);
    }

    override fun create() : EpicPvpCard {
        return HolyStrikeCard(cost, attack, defend);;
    }

    override fun getButton(label: String): EpicPvpButton {
        return when(label) { button1.buttonLabel -> button1; else -> button2; }
    }
}
