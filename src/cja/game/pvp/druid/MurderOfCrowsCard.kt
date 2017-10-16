package cja.game.pvp.druid

import cja.game.GameButton
import cja.game.GameMessage
import cja.game.PlayerHand
import cja.game.pvp.*

public class MurderOfCrowsCard : SpecialCard, CardProcess, ButtonsCard {

    var chooseCard : PlayerHand = PlayerHand("black");
    var message : GameMessage = GameMessage("Murder of Crows", true);
    var button1 : EpicPvpButton = EpicPvpButton(this, "Control enemy permanent");
    var button2 : EpicPvpButton = EpicPvpButton(this, "Draw non-move from discard");
    var choosing : Boolean = false;

    constructor(pth : String) : super("Murder of Crows") {
        this.text = "Before playing moves: Choose - Take control of an enemy's permanent or add a non-move card from your discard pile to your hand.";
    }

    override fun play(state : EpicPvpGameState) {
        state.getPlayerPermanents(this.owner).add(this);
        state.getPlayerDiscard(this.owner).cards.remove(this);
        state.discardedCards.remove(this);
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
            if(chooseCard.hand.size() == 0) {
                endAction(state);
            } else {
                chooseCard.setPosition(state.moveArea.x+50f, state.moveArea.y-75f, state.moveArea.width-100f, state.moveArea.height+150f);
                state.gameObjects.add(0, chooseCard);
            }
        } else if(obj == button2) {
            choosing = true;
            for(card in state.getPlayerDiscard(this.owner).cards) {
                if(card != this && !(card is MoveCard)) {
                    chooseCard.hand.add(card);
                }
            }
            if(chooseCard.hand.size() == 0) {
                endAction(state);
            } else {
                chooseCard.setPosition(state.moveArea.x+50f, state.moveArea.y-75f, state.moveArea.width-100f, state.moveArea.height+150f);
                state.gameObjects.add(0, chooseCard);
            }
        } else if(choosing && obj == chooseCard) {
            var card = chooseCard.getCardAt(ptx, pty) as EpicPvpCard?;
            if(card != null) {
                if(card.owner == this.owner) {
                    // from discard
                    state.getPlayerDiscard(card.owner).cards.remove(card);
                    state.getPlayerHand(this.owner).hand.add(card);
                } else {
                    // permanent
                    state.getPlayerPermanents(card.owner).remove(card);
                    card.owner = this.owner;
                    state.getPlayerPermanents(this.owner).add(card);
                }
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

    override fun create() : EpicPvpCard {
        return MurderOfCrowsCard("");
    }

    override fun clonePropertiesTo(card: EpicPvpCard) {
        super.clonePropertiesTo(card)
        if(card is MurderOfCrowsCard) {
            card.choosing = choosing;
        }
    }

    override fun getButton(label: String): EpicPvpButton {
        return when(label) { button1.buttonLabel -> button1; else -> button2; }
    }
}
