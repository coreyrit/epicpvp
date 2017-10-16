package cja.game.cp.action

import cja.game.GameAction
import cja.game.PlayerHand
import cja.game.cp.*

class TradeCards : GameAction {

    var gs: CPGameState;
    var srcHand : PlayerHand;
    var ptx : Float;
    var pty : Float;

    constructor(gs: CPGameState, srcHand : PlayerHand, ptx : Float, pty : Float) {
        this.gs = gs;
        this.srcHand = srcHand;
        this.ptx = ptx;
        this.pty = pty;
    }

    override fun execute(): Boolean {
        var destHand : PlayerHand? = null;
        var handnum = 0;
        var handi = 0;
        for(hand in gs.players) {
            if(hand != srcHand && hand.highlight) {
                destHand = hand;
                handnum = handi;
            }
            handi++;
        }
        var card = srcHand.getCardAt(ptx, pty);
        var bonus = 0;
        if(gs.players[handnum].scientistName.equals("Geologist")) {
            bonus = 1;
        }
        if(card != null && destHand != null && destHand.hand.size < gs.maxCards+1+bonus
                && srcHand.highlight && destHand.highlight) {
            srcHand.hand.remove(card);
            destHand.hand.add(card);
            return true;
        }
        return false;
    }
}
