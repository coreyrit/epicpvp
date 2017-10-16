package cja.game.loopinc

import cja.game.GameCard

public class TripCard : GameCard {
    var leftYear : Int;
    var rightYear : Int;
    var leftRequirements : List<ChitIcon>;
    var rightRequirements : List<ChitIcon>;

    constructor(leftYear : Int, rightYear : Int,
                leftRequirements : List<ChitIcon>, rightRequirements : List<ChitIcon>,
                frontImg : String?, backImg : String?) : super(frontImg, backImg) {
        this.leftYear = leftYear;
        this.rightYear = rightYear;
        this.leftRequirements = leftRequirements;
        this.rightRequirements = rightRequirements;
    }
}
