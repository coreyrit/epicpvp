package cja.game.pvp

import cja.game.GameCard
import cja.game.GameGraphics

abstract class EpicPvpCard : GameCard {
    var id : Int = 0;
    var name : String;
    var text : String = "";
    var cost : Int = 0;
    var owner : Boolean = false;
    var embelish : Boolean = false;

    constructor(name : String, cost : Int) : super(null, null) {
        this.name = name;
        this.cost = cost;
    }

    override fun draw(g : GameGraphics) {
        super.draw(g);
        if(!isFront) {
            g.drawString1("Epic PVP", x + 5f, y + 5f, 12);
        } else {
            g.drawString1(name, x + 5f, y + 25f, 12);
            g.drawString2(text, x + 5f, y + 50f, 90f, 70f, 8);
        }

        if(highlight || embelish) {
            g.drawRectangle3(x-2, y-2, width+4, height+4, "Black", 5);
        }
    }

    open fun beforeAggressionPhase(state : EpicPvpGameState) {

    }

    open fun afterDrawPhase(state : EpicPvpGameState) {

    }

    open fun beforeMovePhase(state : EpicPvpGameState) {

    }

    open fun afterMovePhase(state : EpicPvpGameState) {

    }

    open fun afterEndPhase(state : EpicPvpGameState) {

    }

    abstract fun create() : EpicPvpCard;

    open fun clonePropertiesTo(card : EpicPvpCard) {
        card.id = id;
        card.cost = cost;
        card.embelish = embelish;
        card.highlight = highlight;
        card.owner = owner;
        card.x = x;
        card.y = y;
        card.width = width;
        card.height = height;
        card.isFront = isFront;
        card.isRotated = isRotated;
    }
}