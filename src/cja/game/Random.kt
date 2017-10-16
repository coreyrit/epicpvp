package cja.game

import kotlin.js.Date
import kotlin.js.Math

class Random {
    var ticks : Double = 0.0;

    constructor(seed : Double) {
        ticks = seed;
    }

    fun next() : Double {
        var nextTicks = Date().getTime();
        while(nextTicks == ticks) {
            nextTicks = Date().getTime();
        }
        ticks = nextTicks;
        return nextTicks;
    }

    fun nextInt(maxValue : Int) : Int {
        return Math.round(next() % maxValue);
    }
}
