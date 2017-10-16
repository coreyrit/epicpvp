package cja.game.pvp

import cja.game.GameGraphics

abstract class ClassCard : EpicPvpCard {
    var initiative : Int;

    constructor(owner : Boolean, name : String, initiative : Int) : super(name, 0) {
        this.owner = owner;
        this.name = name;
        this.initiative = initiative;
    }

    override fun draw(g : GameGraphics) {
        super.draw(g);
        g.drawString1(initiative.toString(), x + 5f, y + 5f, 12);
        g.drawRectangle3(x + 3f, y + 2f, 10f, 18f, "green", 2);

        g.drawString1(name, x + 5f, y + 25f, 12);
        g.drawString2(text, x + 5f, y + 50f, 90f, 70f, 8);
    }
}
