package cja.game

abstract class GameRenderer {
    fun render(state : GameState) : GameDisplay {
        var bmp = createDisplay(state.width, state.height);
        var g = bmp.getGraphics();
        g.fillRectangle1(0f, 0f, state.width, state.height, "White");
        for(obj in state.gameObjects.reversed()) { // draw in reverse order
            obj.draw(g);
            if(obj.highlight) {
                //println("highlighting " + obj);
                g.drawRectangle3(obj.x-2, obj.y-2, obj.width+4, obj.height+4, "Black", 5);
            }
        }
        return bmp;
    }

    abstract fun createDisplay(w : Float, h : Float) : GameDisplay;
}