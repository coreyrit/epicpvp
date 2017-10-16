package cja.game

import java.util.*

class GameDie : GameObject {
    var rand : Random = Random(Date().getTime());
    var index : Int = 0;
    var dieSides : List<String> = ArrayList<String>();

    constructor(sides : Array<String>) {
        var temp = ArrayList<String>();
        for(side in sides) {
            temp.add(side);
        }
        dieSides = temp;
    }

    fun roll() {
        index = rand.nextInt(dieSides.size());
    }

    fun getValue() : Int {
        return index+1;
    }

    override fun draw(g : GameGraphics) {
        g.drawImage1(dieSides[index], x, y, width, height);
        g.drawString1(getValue().toString(), x+5, y+5, 12);
    }
}
