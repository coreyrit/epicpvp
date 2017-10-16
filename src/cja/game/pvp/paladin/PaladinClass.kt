package cja.game.pvp.paladin

import cja.game.GameButton
import cja.game.GameMessage
import cja.game.pvp.*

public class PaladinClass : ClassCard, CardProcess, ButtonsCard {
    var message : GameMessage = GameMessage("Paladin Ability", true);
    var button1 : EpicPvpButton = EpicPvpButton(this, "Draw a card");
    var button2 : EpicPvpButton = EpicPvpButton(this, "Do NOT draw a card");
    var discarding : Boolean = false;

    constructor(owner : Boolean) : super(owner, "Paladin", 3) {
        this.text = "After you take damage: You may draw a card from your deck.  If you do, discard a card from either your hand or your aggression pile.";
    }

    override fun startProcess(state : EpicPvpGameState) {
        discarding = false;
        state.placeButtons(message, button1, button2, 200f);
    }

    override fun handleClick(state : EpicPvpGameState, ptx: Float, pty: Float): Boolean {
        var obj = state.getObjectAt(ptx, pty);

        var hand = state.getPlayerHand(this.owner);
        var aggression = state.getPlayerAggression(this.owner);
        if(obj == button1) {
            discarding = true;
            state.gameObjects.remove(message);
            state.gameObjects.remove(button1);
            state.gameObjects.remove(button2);
            var card = state.drawCard(this.owner);
            card.isFront = true;
            hand.hand.add(card);
        } else if(obj == button2) {
            endAction(state);
        } else if(discarding && obj == hand) {
            var card = hand.getCardAt(ptx, pty);
            if(card != null) {
                state.discardCard(this.owner, card as EpicPvpCard);
                hand.hand.remove(card);
                endAction(state);
            }
        } else if(discarding && obj == aggression && aggression.highlight) {
            var card = aggression.aggression.remove(0);
            state.discardCard(this.owner, card);
            endAction(state);
        }

        return true;
    }

    override fun setHighlights(state : EpicPvpGameState) {
        if (!discarding && !state.gameObjects.contains(button1)) {
            startProcess(state);
        }
        if (!discarding) {
            button1.highlight = true;
            button2.highlight = true;
        } else {
            for(card in state.getPlayerHand(this.owner).hand) {
                card.highlight = true;
            }
            if(state.getPlayerAggression(this.owner).aggression.size() > 0) {
                state.getPlayerAggression(this.owner).highlight = true;
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

    override fun afterEndPhase(state : EpicPvpGameState) {
        discarding = false;
        if(state.damageTaken > 0) {
            state.getCardProcesses(this.owner).add(this);
        }
    }

    override fun create() : EpicPvpCard {
        return PaladinClass(owner);
    }

    override fun clonePropertiesTo(card: EpicPvpCard) {
        super.clonePropertiesTo(card)
        if(card is PaladinClass) {
            card.discarding = discarding;
        }
    }

    override fun getButton(label: String): EpicPvpButton {
        return when(label) { button1.buttonLabel -> button1; else -> button2; }
    }
}