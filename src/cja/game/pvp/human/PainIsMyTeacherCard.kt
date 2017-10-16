package cja.game.pvp.human

import cja.game.GameMessage
import cja.game.pvp.*

public class PainIsMyTeacherCard : SpecialCard, CardProcess, ButtonsCard {
    var message : GameMessage = GameMessage("Pain is my Teacher", true);
    var button1 : EpicPvpButton = EpicPvpButton(this, "Draw a card");
    var button2 : EpicPvpButton = EpicPvpButton(this, "Do NOT draw a card");

    constructor(pth : String) : super("Pain Teaches Me") {
        this.text = "Before playing moves: Put in play as a permanent.  After you take damage: You may draw a card.";
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

    override fun play(state : EpicPvpGameState) {
        state.getPlayerPermanents(this.owner).add(this);
        state.getPlayerDiscard(this.owner).cards.remove(this);
        state.discardedCards.remove(this as MoveCard);
        state.specials.remove(this);
    }

    override fun afterEndPhase(state : EpicPvpGameState) {
        if(state.damageTaken > 0) {
            state.getCardProcesses(this.owner).add(this);
        }
    }

    override fun create() : EpicPvpCard {
        return PainIsMyTeacherCard("");
    }

    override fun getButton(label: String): EpicPvpButton {
        return when(label) { button1.buttonLabel -> button1; else -> button2; }
    }
}