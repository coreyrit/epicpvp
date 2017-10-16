package cja.game.cp.action

import cja.game.GameAction
import cja.game.cp.CPGameState
import cja.game.cp.TurnAction

class ShuffleInMovement : GameAction {
    var gs : CPGameState;

    constructor(gs : CPGameState) {
        this.gs = gs;
    }

    override fun execute(): Boolean {
        //var card = gs.discardDeck.cards.remove(gs.discardDeck.cards.size()-1);
        for(card in gs.moveCards) {
            gs.discardDeck.cards.remove(card);
            card.isFront = false;
            gs.buildDeck.cards.add(card);
            gs.buildDeck.shuffle();
        }
        gs.moveCards.clear();

        // Damage now comes from drawn build rail cards!

//        if(gs.players[gs.playerTurn].scientistName.equals("Chemist")) {
            gs.turnAction = TurnAction.TimeTravel;
//        } else {
//            gs.turnAction = TurnAction.DrawDamage;
//        }
        return true;
    }
}