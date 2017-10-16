package cja.game.pvp.goblin

import cja.game.GameButton
import cja.game.GameMessage
import cja.game.pvp.*

public class GoblinRace : RaceCard, CardProcess, ButtonsCard {
    var message : GameMessage = GameMessage("Goblin Ability", true);
    var button1 : EpicPvpButton = EpicPvpButton(this, "Draw a card");
    var button2 : EpicPvpButton = EpicPvpButton(this, "Do NOT draw a card");

    constructor(owner : Boolean) : super(owner, "Goblin", 4, 4) {
        this.text = "After you block: If at least one of your moves blocked a move with the same printed cost, you may draw a card.";
    }

    override fun startProcess(state : EpicPvpGameState) {
        state.placeButtons(message, button1, button2, 200f);
    }

    override fun handleClick(state : EpicPvpGameState, ptx: Float, pty: Float): Boolean {
        var obj = state.getObjectAt(ptx, pty);

        if(obj == button1) {
            var card = state.drawCard(this.owner);
            card.isFront = true;
            state.getPlayerHand(this.owner).hand.add(card);
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

    override fun afterEndPhase(state : EpicPvpGameState) {
        for(card in state.discardedCards) {
            if(card.blockedBy != null && card.blockedBy is MoveCard && card.blockedBy!!.cost == card.cost) {
                state.getCardProcesses(this.owner).add(this);
            }
        }
    }

    override fun create(): EpicPvpCard {
        return GoblinRace(owner);
    }

    override fun getButton(label: String): EpicPvpButton {
        return when(label) { button1.buttonLabel -> button1; else -> button2; }
    }
}