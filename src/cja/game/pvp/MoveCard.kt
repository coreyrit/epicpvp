package cja.game.pvp

import cja.game.GameGraphics

abstract class MoveCard : EpicPvpCard {
    var attack : Int;
    var defend : Int;
    var damage : Int = 1;

    var blockedBy : EpicPvpCard? = null;
    var delta : Int = 0;

    constructor(name : String, cost : Int, attack : Int, defend : Int) : super(name, cost) {
        this.attack = attack;
        this.defend = defend;
    }

    fun copyPropertiesTo(card : MoveCard) {
        card.attack = attack;
        card.defend = defend;
        card.damage = damage;
        card.cost = cost;
        card.x = x;
        card.y = y;
        card.width = width;
        card.height = height;
        card.isFront = isFront;
        card.isRotated = isRotated;
        card.owner = owner;
    }

    override fun clonePropertiesTo(card : EpicPvpCard) {
        super.clonePropertiesTo(card);
        copyPropertiesTo(card as MoveCard);
        card.blockedBy = blockedBy; // should this be a clone?
        card.delta = delta;
    }

    open fun canBlock(card : MoveCard) : Boolean {
        return this.defend >= card.attack;
    }

    open fun validBlock(card : MoveCard) : Boolean {
        return true;
    }

    override fun draw(g : GameGraphics) {
        super.draw(g);
        if(isFront) {
            if(!isRotated) {
                g.drawString1(cost.toString(), x + 5f, y + 5f, 12);
                g.drawString1(defend.toString(), x + width / 2 - 3f, y + 5f, 12);
                g.drawRectangle3(x + width / 2 - 5f, y + 2f, 10f, 18f, "yellow", 2);
                g.drawString1(attack.toString(), x + width / 2 - 3f, y + height - 17f, 12);
                g.drawRectangle3(x + width / 2 - 5f, y + height - 19f, 10f, 18f, "red", 2);
                g.drawString1(damage.toString(), x + width - 15f, y + height - 17f, 12);
            } else {
                g.drawString1(cost.toString(), x + 5f, y + height - 17f, 12);
                g.drawString1(attack.toString(), x + width / 2 - 3f, y + 5f, 12);
                g.drawRectangle3(x + width / 2 - 5f, y + 2f, 10f, 18f, "red", 2);
                g.drawString1(defend.toString(), x + width / 2 - 3f, y + height - 17f, 12);
                g.drawRectangle3(x + width / 2 - 5f, y + height - 19f, 10f, 18f, "yellow", 2);
                g.drawString1(damage.toString(), x + width - 15f, y + 5f, 12);
            }
        }
    }
}
