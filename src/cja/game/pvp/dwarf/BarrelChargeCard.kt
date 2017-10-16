package cja.game.pvp.dwarf

import cja.game.GameMessage
import cja.game.PlayerHand
import cja.game.pvp.*

public class BarrelChargeCard : SpecialCard, CardProcess, ButtonsCard {
    var chooseCard : PlayerHand = PlayerHand("black");
    var message : GameMessage = GameMessage("Barrel Charge", true);
    var button1 : EpicPvpButton = EpicPvpButton(this, "+2 attack per enemy move");
    var button2 : EpicPvpButton = EpicPvpButton(this, "Discard 2 cards");
    var choosing : Boolean = false;

    constructor(img : String) : super("Barrel Charge") {
        this.text = "After playing moves: Your enemy chooses - Each of your moves gains two +1 attack counters or they discard two cards.";
    }

    override fun startProcess(state : EpicPvpGameState) {
        state.placeButtons(message, button1, button2, 200f);
    }

    override fun handleClick(state : EpicPvpGameState, ptx: Float, pty: Float): Boolean {
        var obj = state.getObjectAt(ptx, pty);

        if(obj == button1) {
            for(card in state.moveArea.moves.keySet()) {
                if(card.owner == this.owner) {
                    card.attack += 2;
                } else if(state.moveArea.moves.get(card) != null && state.moveArea.moves.get(card)!!.owner == this.owner) {
                    state.moveArea.moves.get(card)!!.attack += 2;
                }
            }
            endAction(state);
        } else if(obj == button2) {
            choosing = true;
            for(card in state.getPlayerHand(!this.owner)) {
                chooseCard.hand.add(card);
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
                state.getPlayerHand(card.owner).hand.remove(card);
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
        state.getCardProcesses(!this.owner).remove(this);
        state.gameObjects.remove(message)
        state.gameObjects.remove(button1);
        state.gameObjects.remove(button2);
        state.gameObjects.remove(chooseCard);
        state.handleClick(-1f, -1f);
    }

    override fun afterMovePhase(state : EpicPvpGameState) {
        state.getCardProcesses(!this.owner).add(this);
    }

    override fun create(): EpicPvpCard {
        return BarrelChargeCard("");
    }

    override fun clonePropertiesTo(card: EpicPvpCard) {
        super.clonePropertiesTo(card)
        if(card is BarrelChargeCard) {
            card.choosing = choosing;
        }
    }

    override fun getButton(label: String): EpicPvpButton {
        return when(label) { button1.buttonLabel -> button1; else -> button2; }
    }
}