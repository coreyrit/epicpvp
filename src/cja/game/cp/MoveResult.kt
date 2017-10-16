package cja.game.cp

enum class MoveResult {
    Safe,
    DamagedRail,
    OffMap,
    MissingRail,
    Obstacle,
    MismatchedRail,
    Win
}
