package cja.game.pvp.human

import cja.game.pvp.EpicPvpCard
import cja.game.pvp.EpicPvpGameState
import cja.game.pvp.MoveCard

public class ViciousElbowCard : MoveCard {

    constructor(cost : Int, attack : Int, defend : Int) : super("Vicious Elbow", cost, attack, defend) {
        this.text = "After playing moves: All of your other moves gain a +1 attack counter.";
    }

    override fun afterMovePhase(state : EpicPvpGameState) {
        for(card in state.moveArea.moves.keys) {
            if(card != this && card.owner == this.owner) {
                card.attack++;
            } else if(state.moveArea.moves.get(card) != null && state.moveArea.moves.get(card)!!.owner == this.owner && state.moveArea.moves.get(card) != this) {
                state.moveArea.moves.get(card)!!.attack++;
            }
        }
    }

    override fun create() : EpicPvpCard {
        return ViciousElbowCard(cost, attack, defend);
    }
}