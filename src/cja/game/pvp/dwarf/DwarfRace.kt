package cja.game.pvp.dwarf

import cja.game.GameMessage
import cja.game.pvp.*
import cja.game.pvp.goblin.GoblinRace

public class DwarfRace : RaceCard, CardProcess, ButtonsCard {
    var message : GameMessage = GameMessage("Dwarf Ability", true);
    var button1 : EpicPvpButton = EpicPvpButton(this, "Gain Aggression");
    var button2 : EpicPvpButton = EpicPvpButton(this, "Do NOT gain aggression");

    constructor(owner : Boolean) : super(owner, "Dwarf", 6, 0) {
        this.text = "After you take damage: If the attack that damaged you was from a Basic Strike, you may gain one aggression.";
    }

    override fun startProcess(state : EpicPvpGameState) {
        state.placeButtons(message, button1, button2, 200f);
    }

    override fun handleClick(state : EpicPvpGameState, ptx: Float, pty: Float): Boolean {
        var obj = state.getObjectAt(ptx, pty);

        if(obj == button1) {
            var card = state.drawCard(this.owner);
            state.getPlayerAggression(this.owner).aggression.add(card);
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
            if(card.blockedBy == null && card is BasicStrikeCard) {
                state.getCardProcesses(this.owner).add(this);
            }
        }
    }

    override fun create(): EpicPvpCard {
        return DwarfRace(owner);
    }

    override fun getButton(label: String): EpicPvpButton {
        return when(label) { button1.buttonLabel -> button1; else -> button2; }
    }
}
