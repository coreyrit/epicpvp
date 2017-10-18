package cja.game.pvp.human

import cja.game.GameGraphics
import cja.game.pvp.*

public class HumanRace : RaceCard, CardProcess, ButtonsCard {
    var button1 : EpicPvpButton = EpicPvpButton(this, "", false);
    var button2 : EpicPvpButton = EpicPvpButton(this, "Do NOT use Insight");
    var discarding : Boolean = false;
    var discarded : Int = 0;
    var insight : Int = 3;
    var player : Boolean;

    constructor(owner : Boolean) : super(owner, "Human", 5, 2) {
        this.text = "Setup: Put three Insight counters on this card.  Before playing moves: Once per turn, you may remove an Insight counter " +
                "from this card.  If you do, discard any number of cards from your hand, then draw that many cards.";
        this.player = owner;
    }

    override fun startProcess(state : EpicPvpGameState) {
        button1.setPosition(this.x, this.y, this.width, this.height);
        state.gameObjects.add(0, button1);
        if(player) {
            button2.setPosition(state.player1area.playerHand.x, state.player1area.playerHand.y, state.player1area.playerHand.width, state.player1area.playerHand.height);
        } else {
            button2.setPosition(state.player2area.playerHand.x, state.player2area.playerHand.y, state.player2area.playerHand.width, state.player2area.playerHand.height);
        }
        state.gameObjects.add(0, button2);
    }

    override fun handleClick(state : EpicPvpGameState, ptx: Float, pty: Float): Boolean {
        var obj = state.getObjectAt(ptx, pty);

        if(obj == button1) {
            discarding = true;
            state.gameObjects.remove(button1);
            state.gameObjects.remove(button2);
            insight--;
        } else if(obj == button2) {
            endAction(state);
        } else if(discarding && obj == state.getPlayerHand(this.owner)) {
            var card = state.getPlayerHand(this.owner).getCardAt(ptx, pty);
            if(card != null && card.highlight) {
                state.getPlayerHand(this.owner).hand.remove(card);
                state.discardCard(this.owner, card as EpicPvpCard);
                discarded++;
            } else {
                endAction(state);
            }
        }

        return true;
    }

    fun endAction(state : EpicPvpGameState) {
        for(i in 1..discarded) {
            var card = state.drawCard(this.owner);
            card.isFront = true;
            state.getPlayerHand(this.owner).hand.add(card);
        }

        state.getCardProcesses(this.owner).remove(this);
        state.gameObjects.remove(button1);
        state.gameObjects.remove(button2);
        state.handleClick(-1f, -1f);
    }

    override fun setHighlights(state : EpicPvpGameState) {
        if(!discarding && !state.gameObjects.contains(button1)) {
            startProcess(state);
        }
        if(!discarding) {
            button1.highlight = true;
            button2.highlight = true;
        } else {
            state.getPlayerHand(this.owner).highlight = true;
            for(card in state.getPlayerHand(this.owner).hand) {
                card.highlight = true;
            }
        }
    }

    override fun beforeMovePhase(state : EpicPvpGameState) {
        discarding = false;
        discarded = 0;
        if(insight > 0) {
            state.getCardProcesses(this.owner).add(this);
        }
    }

    override fun draw(g : GameGraphics) {
        super.draw(g);
        g.drawString1(insight.toString(), x + width - 15f, y + height - 17f, 12);
    }

    override fun create() : EpicPvpCard {
        return HumanRace(owner);
    }

    override fun clonePropertiesTo(card: EpicPvpCard) {
        super.clonePropertiesTo(card)
        if(card is HumanRace) {
            card.discarded = discarded;
            card.discarding = discarding;
            card.insight = insight;
        }
    }

    override fun getButton(label: String): EpicPvpButton {
        return when(label) { button1.buttonLabel -> button1; else -> button2; }
    }
}
