package cja.game.pvp

import cja.game.GameButton

public class EpicPvpButton : GameButton {
    var card : EpicPvpCard;

    constructor(card : EpicPvpCard, label : String) : super(label) {
        this.card = card;
    }
}
