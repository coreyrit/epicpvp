package cja.game.cp

import cja.game.GameCard
import cja.game.PlayerHand

class Scientist : PlayerHand {
    var scientistName : String = "";
    var pawnColor : String = "";
    var pawnMat : Int = 0;

    constructor(name : String, color : String, pcolor : String, pmat : Int) : super(color) {
        this.scientistName = name;
        this.pawnColor = pcolor;
        this.pawnMat = pmat;
    }

    fun remove(card : GameCard) {
        hand.remove(card);
    }
}
