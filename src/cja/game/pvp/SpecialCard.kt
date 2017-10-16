package cja.game.pvp

abstract class SpecialCard : EpicPvpCard {

    constructor(name : String) : super(name, 0) {
        
    }

    open fun play(state : EpicPvpGameState) {

    }

    override fun clonePropertiesTo(card : EpicPvpCard) {
        super.clonePropertiesTo(card);;
    }
}
