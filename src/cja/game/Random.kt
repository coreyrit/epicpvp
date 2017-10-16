package cja.game

class Random {
    var ticks : Int = 0;

    constructor(seed : Int) {
        ticks = seed;
    }

    fun next() : Int {
        var nextTicks = Date().getTime();
        while(nextTicks == ticks) {
            nextTicks = Date().getTime();
        }
        ticks = nextTicks;
        return nextTicks;
    }

    fun nextInt(maxValue : Int) : Int {
        return next() % maxValue;
    }
}
