package cja.game.cp.action

import cja.game.GameAction
import cja.game.cp.CPGameState
import cja.game.cp.DamageCard
import cja.game.cp.TurnAction

class DrawDamageCard : GameAction {

    var gs: CPGameState

    constructor(gs: CPGameState) {
        this.gs = gs;
    }

    override fun execute(): Boolean {
//        var card = gs.damageDeck.pop() as DamageCard;
//        card.isFront = true;
//        if(!
        if(gs.lastDrawBuildCard != null) {
            when (gs.lastDrawBuildCard!!.damageColumn) {
                1, 2, 3 -> gs.yearMats[0].damageColumn(gs.damageGems[gs.lastDrawBuildCard!!.damageColumn - 1], gs.lastDrawBuildCard!!.damageColumn);
                4, 5, 6 -> gs.yearMats[1].damageColumn(gs.damageGems[gs.lastDrawBuildCard!!.damageColumn - 1], gs.lastDrawBuildCard!!.damageColumn - 3);
                7, 8, 9 -> gs.yearMats[2].damageColumn(gs.damageGems[gs.lastDrawBuildCard!!.damageColumn - 1], gs.lastDrawBuildCard!!.damageColumn - 6);
                10, 11, 12 -> gs.yearMats[3].damageColumn(gs.damageGems[gs.lastDrawBuildCard!!.damageColumn - 1], gs.lastDrawBuildCard!!.damageColumn - 9);
                else -> false;
            }
        }
//        ) {
//            gs.damageDiscard.cards.add(card);
//        }

//        if(gs.damageDeck.cards.isEmpty()) {
//            while(gs.damageDiscard.cards.size() > 0) {
//                var gc = gs.damageDiscard.pop();
//                gc.isFront = false;
//                gs.damageDeck.cards.add(gc);
//            }
//            gs.damageDeck.shuffle();
//        }

        var bonus = 0;
        if(gs.players[gs.playerTurn].scientistName.equals("Geologist")) {
            bonus = 1;
        }
        if(gs.players[gs.playerTurn].hand.size() > gs.maxCards + bonus) {
            gs.turnAction = TurnAction.DiscardBuild;
        } else {
            gs.turnAction = TurnAction.TimeTravel;
        }
        return true;
    }
}
