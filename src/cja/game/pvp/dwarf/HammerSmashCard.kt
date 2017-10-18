package cja.game.pvp.dwarf

import cja.game.GameMessage
import cja.game.PlayerHand
import cja.game.pvp.*
import cja.game.pvp.druid.MurderOfCrowsCard

public class HammerSmashCard : MoveCard, CardProcess, ButtonsCard {
    var chooseCard : PlayerHand = PlayerHand("black");
    var message : GameMessage = GameMessage("Hammer Smash", true);
    var button1 : EpicPvpButton = EpicPvpButton(this, "Discard a permanent");
    var button2 : EpicPvpButton = EpicPvpButton(this, "Do NOT discard");
    var choosing : Boolean = false;

    constructor() : super("Hammer Smash", 4, 4, 4) {
        this.text = "After blocking: If this attack was blocked, you may discard one permanent in play.";
    }

    override fun startProcess(state : EpicPvpGameState) {
        state.placeButtons(message, button1, button2, 200f);
    }

    override fun handleClick(state : EpicPvpGameState, ptx: Float, pty: Float): Boolean {
        var obj = state.getObjectAt(ptx, pty);

        if(obj == button1) {
            choosing = true;
            for(card in state.getPlayerPermanents(!this.owner)) {
                chooseCard.hand.add(card);
            }
            /*for(card in state.getPlayerPermanents(this.owner)) {
                chooseCard.hand.add(card);
            }*/
            if(chooseCard.hand.size == 0) {
                endAction(state);
            } else {
                chooseCard.setPosition(state.moveArea.x+50f, state.moveArea.y-75f, state.moveArea.width-100f, state.moveArea.height+150f);
                state.gameObjects.add(0, chooseCard);
            }
        } else if(obj == button2) {
            endAction(state);
        } else if(choosing && obj == chooseCard) {
            var card = chooseCard.getCardAt(ptx, pty) as EpicPvpCard?;
            if(card != null) {
                state.getPlayerPermanents(card.owner).remove(card);
                state.getPlayerDiscard(card.owner).cards.add(card);
                endAction(state);
            }
        }

        return true;
    }

    override fun setHighlights(state : EpicPvpGameState) {
        if(!choosing && !state.gameObjects.contains(button1)) {
            startProcess(state);
        }
        if(!choosing) {
            button1.highlight = true;
            button2.highlight = true;
        } else {
            chooseCard.highlight = true;
        }
    }

    fun endAction(state : EpicPvpGameState) {
        state.getCardProcesses(this.owner).remove(this);
        state.gameObjects.remove(message)
        state.gameObjects.remove(button1);
        state.gameObjects.remove(button2);
        state.gameObjects.remove(chooseCard);
        state.handleClick(-1f, -1f);
    }

    override fun afterEndPhase(state : EpicPvpGameState) {
        if(this.blockedBy != null) {
            state.getCardProcesses(this.owner).add(this);
        }
    }

    override fun create(): EpicPvpCard {
        return HammerSmashCard();
    }

    override fun clonePropertiesTo(card: EpicPvpCard) {
        super.clonePropertiesTo(card)
        if(card is HammerSmashCard) {
            card.choosing = choosing;
        }
    }

    override fun getButton(label: String): EpicPvpButton {
        return when(label) { button1.buttonLabel -> button1; else -> button2; }
    }
}
