package cja.game.cp.action

import cja.game.GameAction
import cja.game.GameDeck
import cja.game.cp.CPGameState
import cja.game.cp.RailCard
import cja.game.cp.TurnAction

class UseScraps : GameAction {
    var gs: CPGameState;
    var railDeck : GameDeck;

    constructor(gs: CPGameState, railDeck : GameDeck) {
        this.gs = gs;
        this.railDeck = railDeck;
    }

    override fun execute(): Boolean {
        var myMat = gs.getPawnMat(gs.pawns[gs.playerTurn]);
        var pawnNumber = myMat!!.getPawnNumber(gs.pawns[gs.playerTurn]);
        myMat.repairSquare(pawnNumber);
        var rc = railDeck.pop() as RailCard;
        rc.isFront = true;
        myMat.setCardSquare(pawnNumber, rc);
        gs.turnAction = TurnAction.ScrapsDiscard;
//        gs.nextPlayerTurn();
        return true;
    }
}

