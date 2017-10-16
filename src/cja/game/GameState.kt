package cja.game

abstract class GameState {
    var width : Float = 800f;
    var height : Float = 600f;
    var gameObjects : MutableList<GameObject> = ArrayList<GameObject>();

    fun getObjectAt(x : Float, y : Float) : GameObject? {
        for(obj in gameObjects) {
            if(x in obj.x..obj.x+obj.width && y in obj.y..obj.y+obj.height) {
                return obj;
            }
        }
        return null;
    }

    fun getScore() : Int {
        return 0;
    }

    open fun handleClick(ptx : Float, pty : Float) : Boolean {
        return false;
    }
}
