package cja.game.loopinc

import cja.game.GameMat
import cja.game.GamePawn

public class PlayerBoard : GameMat {
    var mark1day1 : GamePawn = GamePawn("");
    var mark1day2 : GamePawn = GamePawn("");
    var mark1day3 : GamePawn = GamePawn("");
    var mark2day1 : GamePawn = GamePawn("");
    var mark2day2 : GamePawn = GamePawn("");
    var mark3day3 : GamePawn = GamePawn("");
    var ads : MutableList<GamePawn> = ArrayList<GamePawn>();

    constructor() : super(null, null) {
        for(i in 1..3) {
            ads.add(GamePawn(""));
        }
    }
}
