package cja.game.pvp.goblin

import cja.game.GameButton
import cja.game.GameMessage
import cja.game.pvp.*

public class ShadowCloakCard : SpecialCard, CardProcess, ButtonsCard {
    var message : GameMessage = GameMessage("Shadow Cloak", true);
    var button1 : EpicPvpButton = EpicPvpButton(this, "Discard 3 to remove text");
    var button2 : EpicPvpButton = EpicPvpButton(this, "Do NOT discard");
    var choosing : Boolean = false;

    constructor(pth : String) : super("Shadow Cloak") {
        this.text = "Before playing moves: Put in play as a permanent.  " +
                "Before playing moves: You may discard three cards from your deck to remove the text from any move.";
    }

    override fun startProcess(state : EpicPvpGameState) {
        state.placeButtons(message, button1, button2, 200f);
    }

    override fun handleClick(state : EpicPvpGameState, ptx: Float, pty: Float): Boolean {
        var obj = state.getObjectAt(ptx, pty);

        if(obj == button1) {
            choosing = true;
            state.gameObjects.remove(button1);
            state.gameObjects.remove(button2);
            if(state.countMoves(!this.owner) == 0) {
                endAction(state);
            }
        } else if(obj == button2) {
            endAction(state);
        } else if(choosing && obj == state.moveArea) {
            var card = state.moveArea.getCardAt(ptx, pty);
            if(card != null && card.highlight) {

                // discard
                for(i in 1..3) {
                    var discard = state.drawCard(this.owner);
                    discard.isFront = true;
                    state.getPlayerDeck(this.owner).cards.add(discard);
                }
                // swap out with a basic strike
                var blankText = BasicStrikeCard(card.cost, card.attack, card.defend);
                blankText.name = card.name;
                card.copyPropertiesTo(blankText);

                // replace card with blank
                for(target in state.moveArea.moves.keySet()) {
                    if(target == card) {
                        var defend = state.moveArea.moves.remove(target);
                        state.moveArea.assignMove(blankText, defend);
                    } else if(state.moveArea.moves.get(target) == card) {
                        state.moveArea.assignMove(target, blankText);
                    }
                }

                endAction(state);
            }
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
        if(!choosing && !state.gameObjects.contains(button1)) {
            startProcess(state);
        }
        if(!choosing) {
            button1.highlight = true;
            button2.highlight = true;
        } else {
            for(card in state.moveArea.moves.keySet()) {
                if(card.owner != this.owner) {
                    card.highlight = true;
                } else if(state.moveArea.moves.get(card) != null && state.moveArea.moves.get(card)!!.owner != this.owner) {
                    state.moveArea.moves.get(card)!!.highlight = true;
                }
            }
        }
    }

    override fun play(state : EpicPvpGameState) {
        state.getPlayerPermanents(this.owner).add(this);
        state.getPlayerDiscard(this.owner).cards.remove(this);
        state.discardedCards.remove(this);
        state.specials.remove(this);
        if(state.movesPlayed == 0) {
            // no moves played, can take advantage of special now
            this.beforeMovePhase(state);
        }
    }

    override fun beforeMovePhase(state : EpicPvpGameState) {
        choosing = false;
        state.getCardProcesses(this.owner).add(this);
    }

    override fun create() : EpicPvpCard {
        return ShadowCloakCard("");
    }

    override fun clonePropertiesTo(card: EpicPvpCard) {
        super.clonePropertiesTo(card);
        if(card is ShadowCloakCard) {
            card.choosing = choosing;
        }
    }

    override fun getButton(label: String): EpicPvpButton {
        return when(label) { button1.buttonLabel -> button1; else -> button2; }
    }
}
