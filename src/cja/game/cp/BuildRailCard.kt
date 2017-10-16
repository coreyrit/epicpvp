package cja.game.cp

import cja.game.GameGraphics
import cja.game.Point
import java.util.*

class BuildRailCard : BuildCard {
    var buildLetter : String = "";
    var routes : MutableMap<RailLocation, RailLocation> = HashMap<RailLocation, RailLocation>();
    var damageColumn : Int = 0;

    constructor(letter : String, front: String, back : String, col : Int) : super(front, back) {
        this.buildLetter = letter;
        this.damageColumn = col;
    }

    fun addRoute(a : RailLocation, b : RailLocation) {
        routes.put(a, b);
        routes.put(b, a);
    }

    override fun draw(g : GameGraphics) {
        if(isFront) {
            g.drawImage2(frontImage, x, y, width, height, isRotated);
        } else {
            g.drawImage2(backImage, x, y, width, height, false);
        }
        if(!g.lastImageSuccess()) {
            g.drawRectangle1(x, y, width, height);
        }
        if(!g.lastImageSuccess()) {
            if (isFront) {
                g.drawString1(buildLetter, x + 5, y + 5, 12);
                for (src in routes.keySet()) {
                    var a = getLocationPoint(src);
                    var b = getLocationPoint(routes[src]);
                    g.drawLine1(x + a.x, y + a.y, x + b.x, y + b.y);
                }
            }
        }
        if(isFront) {
            g.drawString1("#" + damageColumn, x + width - 25, y + 5, 12);
        } else {
            g.drawString1("?", x + 5, y + 5, 12);
        }
    }

    fun getLocationPoint(loc : RailLocation?) : Point {
        return when(loc) {
            RailLocation.BottomLeft -> Point(width / 3, height);
            RailLocation.BottomRight -> Point(width*2/3, height);
            RailLocation.CenterLeft -> Point(0f, height/2);
            RailLocation.CenterRight -> Point(width, height/2);
            RailLocation.TopLeft -> Point(width/3, 0f);
            RailLocation.TopRight -> Point(width*2/3, 0f);
            else -> Point(-10f, -10f);
        }
    }
}
