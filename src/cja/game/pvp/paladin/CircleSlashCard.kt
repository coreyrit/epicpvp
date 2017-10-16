package cja.game.pvp.paladin

import cja.game.pvp.EpicPvpCard
import cja.game.pvp.EpicPvpGameState
import cja.game.pvp.MoveCard

public class CircleSlashCard : MoveCard {

    constructor(img : String) : super("Circle Slash", 9, 8, 0) {
        this.text = "After playing moves: Block up to 9 attack worth of enemy attack.";
    }

    // eventually let the player choose?
    override fun afterMovePhase(state : EpicPvpGameState) {
        // minimize the damage

        // get a list of unblocked attacks
        var unblocked = ArrayList<MoveCard>();
        for(card in state.moveArea.moves.keys) {
            if(card.owner != this.owner && state.moveArea.moves.get(card) == null) {
                unblocked.add(card);
            }
        }

        // find every combination of unblocked attacks
        var combinations = ArrayList<ArrayList<MoveCard>>();
        for(i in 0..unblocked.size-1) {
            for(j in 0..combinations.size-1) {
                var temp = ArrayList(combinations.get(j));
                temp.add(unblocked.get(i));
                combinations.add(temp);
            }
            var sub = ArrayList<MoveCard>();
            sub.add(unblocked.get(i));
            combinations.add(sub);
        }

        // find every combo that is even blockable by this card
        var blockable = ArrayList<ArrayList<MoveCard>>();
        for(combo in combinations) {
            var defense = 9;
            for(card in combo) {
                defense -= card.attack;
            }
            if(defense >= 0) {
                blockable.add(combo);
            }
        }

        // now find the combo that deals the most damage
        var maxDamage = 0;
        var target = ArrayList<MoveCard>();
        for(combo in blockable) {
            var damage = 0;
            for(card in combo) {
                damage += card.damage;
            }
            if(damage > maxDamage) {
                maxDamage = damage;
                target = combo;
            }
        }

        // now wipe out those cards
        for(card in target) {
            state.discardMove(card, this);
        }
    }

    override fun create() : EpicPvpCard {
        return CircleSlashCard("");
    }
}
