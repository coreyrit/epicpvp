package cja.game.pvp

public class BasicStrikeCard : MoveCard {

    constructor(cost : Int, attack : Int, defend : Int) : super("Basic Strike", cost, attack, defend) {

    }

    override fun create() : MoveCard {
        return BasicStrikeCard(cost, attack, defend);
    }
}
