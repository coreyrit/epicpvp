package cja.game.cp

import cja.game.*
import cja.game.cp.action.*
import cja.game.cp.highlight.*
import kotlin.js.Date

class CPGameState : GameState {
    var numPlayers : Int = 2;
    var maxCards : Int = 6;

    var yearMats : MutableList<YearMat> = ArrayList<YearMat>();

    var playerTurn : Int = 0;
    var turnAction : TurnAction = TurnAction.DrawBuild;
    var players : MutableList<Scientist> = ArrayList<Scientist>();
    var pawns : MutableList<GamePawn> = ArrayList<GamePawn>();

    var buildDeck : GameDeck = GameDeck(Random(Date().getTime()));
    var discardDeck : GameDeck = GameDeck(Random(Date().getTime()));
    var message : GameMessage = GameMessage("", true);
//    var damageDeck : GameDeck = GameDeck(Random(Date().getTime()));
    var damageDiscard : GameDeck = GameDeck(Random(Date().getTime()));
    var tradeButton : GameButton = GameButton("Trade");
    var buildButton : GameButton = GameButton("Build");
    var repairButton : GameButton = GameButton("Repair");
    var railDecks : MutableList<GameDeck> = ArrayList<GameDeck>();
    var scrapCards : MutableList<GameCard> = ArrayList<GameCard>();
    var movements : Int = 0;

    var moveYear : Int = 0;
    var moveResult : MoveResult = MoveResult.Safe;

    var allowedMovement : MutableMap<Int, MutableList<Int>> = HashMap<Int, MutableList<Int>>();

    var flipButton : GameButton = GameButton("Rotate\nRails");
    var moveButton : GameButton = GameButton("Move");
    var moveAllButton : GameButton = GameButton("Discard to\nMove All");

    var cardsFlipped : Boolean = false;

    var playerNames : Array<String> = arrayOf("Chemist", "Physicist", "Astronomer", "Geologist");
    var playerColors : Array<String> = arrayOf("Red", "Blue", "Yellow", "Green");
    var pawnColors : Array<String> = arrayOf("red", "blue", "yellow", "green");
    var pawnMats : Array<Int> = arrayOf(2, 3, 1, 0);
    var selectedScientists : List<String> = ArrayList<String>();
    var scientists : MutableList<Scientist> = ArrayList<Scientist>();
    var path : String = "";

    var moveCards : MutableList<MoveCard> = ArrayList<MoveCard>();
    var damageGems : MutableList<DamageGem> = ArrayList<DamageGem>();

    var lastDrawBuildCard : BuildRailCard? = null;

    constructor(pth : String, sc : Array<String>) {
        this.path = pth;
        selectedScientists = sc.asList();
        initialize();
    }

    fun initialize() {
        for(j in 0..selectedScientists.size-1) {
            for(i in 0..playerNames.size-1) {
                if(playerNames[i].equals(selectedScientists[j])) {
                    scientists.add(Scientist(playerNames[i], playerColors[i], pawnColors[i], pawnMats[i]));
                }
            }
        }

        numPlayers = scientists.size;
        maxCards = when(numPlayers) {
            2 -> 6;
            3 -> 4;
            4 -> 3;
            else -> 0;
        }

        width = 1050f;
        height = 800f;

        for(i in 1..15) {
            allowedMovement.put(i,  ArrayList<Int>());
        }
        var column1 = arrayOf(1, 4, 7, 10, 13);
        var column3 = arrayOf(3, 6, 9, 12, 15);

        for(i in 1..15) {
            if(!column1.contains(i)) {
                allowedMovement[i]!!.add(i-1);
            }
            if(!column3.contains(i)) {
                allowedMovement[i]!!.add(i+1);
            }
            if(i > 3) {
                allowedMovement[i]!!.add(i-3);
            }
            if(i < 13) {
                allowedMovement[i]!!.add(i+3);
            }
        }

        var x = 100f;
        var y = 20f;
        var years = arrayOf(1930, 1957, 1984, 2011);
        for(i in 1..years.size) {
            var yearMat = YearMat(years[i-1], path + "/mats/terrain" + i + ".png", null);
            yearMat.setPosition(x, y, 155f, 390f);
            gameObjects.add(yearMat);
            x += 165;
            yearMats.add(yearMat);
        }

        var letters = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P");
        x = 770f;
        y = 20f;

        for(i in 1..letters.size) {
            var railDeck = GameDeck(Random(Date().getTime()));
            railDecks.add(railDeck);
            railDeck.setPosition(x, y, 50f, 70f);

            for(j in 0..3) {
                var railCard = RailCard(letters[i-1], path + "/rails/rail" + i + ".png", path + "/rails/unavailable.png");
                addRoutes(railCard, letters[i-1], cardsFlipped);
                railDeck.cards.add(railCard);
            }
            gameObjects.add(railDeck);
            x += 60;

            if(i % 4 == 0) {
                x = 770f;
                y += 80f;
            }
        }

        x = 20f;
        y = 20f;

        for(i in 0..4) {
            var scraps = ScrapsCard(path + "/rails/scraps.png", path + "/rails/rail-scraps.png");
            scraps.setPosition(x, y, 50f, 70f);
            gameObjects.add(scraps);
            y += 80;
            scrapCards.add(scraps);
        }

        var buildX = 810f;
        buildDeck.setPosition(buildX, 440f, 100f, 135f);

        var numLetters = 3;
        var numWilds = 4;

        var railCol = 1;
        var yearCounts = arrayOf(2, 3, 3, 4);
        for(j in 0..numLetters-1) {
            for(i in 1..letters.size) {
                var card = BuildRailCard(letters[i - 1], path + "/build/build" + i + ".png", path + "/build/crommelin_periapsis.png", railCol);
                addRoutes(card, letters[i-1], cardsFlipped);
                card.isFront = false;
                buildDeck.cards.add(card);

                railCol++;
                if(railCol > 12) {
                    railCol = 1;
                }
            }
        }

        for(i in 0..numWilds-1) {
            var card = WildCard(path + "/build/build_wild.png", path + "/build/crommelin_periapsis.png");
            card.isFront = false;
            buildDeck.cards.add(card);
        }

        for(i in 0..years.size-1) {
            for(j in 0..yearCounts[i]-1) {
                var card = MoveCard(years[i], path + "/build/move_" + years[i] + ".png", path + "/build/crommelin_periapsis.png");
                card.isFront = false;
                buildDeck.cards.add(card);
            }
        }

        gameObjects.add(buildDeck);
        buildDeck.shuffle();

        discardDeck.setPosition(buildX, 580f, 100f, 135f);
        gameObjects.add(discardDeck);

        x = 20f;
        y = 440f;

        var buildSize = when(numPlayers) {
            2 -> Point(675f, 135f);
            3 -> Point(480f, 135f);
            4 -> Point(385f, 135f);
            else -> Point(0f, 0f);
        }

        for(i in 0..numPlayers-1) {
            var player = scientists[i];
            var pawn = ScientistPawn(path + "/pawns/pawn_" + scientists[i].pawnColor + ".png", scientists[i].playerColor);
            yearMats[scientists[i].pawnMat].setPawnSquare(14, pawn);

            if(numPlayers == 3 && i == 1) {
                player.setPosition(x, y, 290f, 275f);
                x = 20f;
                y += buildSize.y + 5;
            } else {
                var pad = 0;
                if(player.scientistName.equals("Geologist")) {
                    pad = 100;
                }

                player.setPosition(x, y, buildSize.x + pad, buildSize.y);
                x += buildSize.x + pad + 5;

                if(x > 600) {
                    x = 20f;
                    y += buildSize.y + 5;
                }
            }

            players.add(player);
            pawns.add(pawn);
        }

        for(i in 0..maxCards-2) {
            for(j in 0..players.size-1) {
                var card = buildDeck.pop();
                while(card is MoveCard) {
                    buildDeck.cards.add(card);
                    buildDeck.shuffle();
                    card = buildDeck.pop();
                }

                card.width = 90f;
                card.height = 125f;
                card.isFront = true;
                players[j].hand.add(card);
            }
        }

        println("adding " + players.size + " players");
        gameObjects.addAll(players);

        var buttonsY = 440f;
        for(bttn in arrayOf(tradeButton, buildButton, repairButton, flipButton, moveAllButton)) {
            bttn.setPosition(930f, buttonsY, 100f, 50f);
            gameObjects.add(bttn);
            buttonsY += 55;
        }

        var r = Random(Date().getTime());
        var num1 = r.nextInt(7) + 1;
        var num2 = r.nextInt(7) + 7;

        yearMats[1].setCardSquare(num1, ObstacleCard(path + "/rails/rail_barrier.png", null));
        yearMats[2].setCardSquare(num1, ObstacleCard(path + "/rails/rail_barrier.png", null));
        yearMats[3].setCardSquare(num1, ObstacleCard(path + "/rails/rail_barrier.png", null));
        yearMats[3].setCardSquare(num2, ObstacleCard(path + "/rails/rail_barrier.png", null));

//        for(i in 1..12) {
//            for(j in 0..1) {
//                var card = DamageCard(i, path + "/damage/rail_broken" + i + ".png", path + "/damage/rail-damage.png");
//                card.isFront = false;
//                damageDeck.cards.add(card);
//            }
//        }
//        for(i in 0..7) {
//            var card = DamageCard(0, path + "/damage/no-damage.png", path + "/damage/rail-damage.png");
//            card.isFront = false;
//            damageDeck.cards.add(card);
//        }
//        damageDeck.shuffle();
//        damageDeck.setPosition(890f, 340f, 50f, 70f);
//        gameObjects.add(damageDeck);

//        damageDiscard.setPosition(950f, 340f, 50f, 70f);
//        gameObjects.add(damageDiscard);

        message.message = getGameMessage();
        message.setPosition(20f, 730f, 1000f, 40f);
        gameObjects.add(message);

        for(pawn in pawns) {
            gameObjects.add(pawn);
        }

        var greenCube = CargoPawn(path + "/pawns/cube_green.png", "Green");
        greenCube.setPosition(yearMats[0].x + 10, yearMats[0].y+yearMats[0].height+5, 15f, 15f);
        yearMats[0].cargo = greenCube;

        var yellowCube = CargoPawn(path + "/pawns/cube_yellow.png", "Yellow");
        yellowCube.setPosition(yearMats[1].x + 60, yearMats[1].y+yearMats[1].height+5, 15f, 15f);
        yearMats[1].cargo = yellowCube;

        var redCube = CargoPawn(path + "/pawns/cube_red.png", "Red");
        redCube.setPosition(yearMats[2].x + 80, yearMats[2].y+yearMats[2].height+5, 15f, 15f);
        yearMats[2].cargo = redCube;

        var blueCube = CargoPawn(path + "/pawns/cube_blue.png", "Blue");
        blueCube.setPosition(yearMats[3].x + 130, yearMats[3].y+yearMats[3].height+5, 15f, 15f);
        yearMats[3].cargo = blueCube;

        discardDeck.drawFirst = false;
//        damageDiscard.drawFirst = false;

        for(i in 1..12) {
            var redGem = DamageGem(path + "/pawns/gem_red.png");
            redGem.setPosition(770f, 340f, 25f, 25f);
            gameObjects.add(redGem);
            damageGems.add(redGem);
        }

        setHighlights();
    }

    fun addRoutes(card : GameCard, letter : String, flipped : Boolean) {
        var routes = when(letter) {
            "A" -> arrayOf(Route(RailLocation.TopLeft, RailLocation.BottomLeft), Route(RailLocation.TopRight, RailLocation.BottomRight));
            "B" -> arrayOf(Route(RailLocation.TopLeft, RailLocation.BottomRight), Route(RailLocation.TopRight, RailLocation.BottomLeft));
            "C" -> arrayOf(Route(RailLocation.TopRight, RailLocation.CenterLeft), Route(RailLocation.BottomLeft, RailLocation.CenterRight));
            "D" -> arrayOf(Route(RailLocation.TopLeft, RailLocation.CenterRight), Route(RailLocation.BottomRight, RailLocation.CenterLeft));
            "E" -> arrayOf(Route(RailLocation.TopLeft, RailLocation.CenterLeft), Route(RailLocation.BottomRight, RailLocation.CenterRight));
            "F" -> arrayOf(Route(RailLocation.TopRight, RailLocation.CenterRight), Route(RailLocation.BottomLeft, RailLocation.CenterLeft));
            "G" -> if(!flipped) arrayOf(Route(RailLocation.BottomLeft, RailLocation.CenterLeft), Route(RailLocation.BottomRight, RailLocation.CenterRight))
                else arrayOf(Route(RailLocation.TopLeft, RailLocation.CenterLeft), Route(RailLocation.TopRight, RailLocation.CenterRight));
            "H" -> if(!flipped) arrayOf(Route(RailLocation.BottomLeft, RailLocation.CenterRight), Route(RailLocation.BottomRight, RailLocation.CenterLeft))
                else arrayOf(Route(RailLocation.TopLeft, RailLocation.CenterRight), Route(RailLocation.TopRight, RailLocation.CenterLeft));
            "I" -> if(!flipped) arrayOf(Route(RailLocation.TopLeft, RailLocation.BottomLeft), Route(RailLocation.BottomRight, RailLocation.CenterRight))
                else arrayOf(Route(RailLocation.TopRight, RailLocation.BottomRight), Route(RailLocation.TopLeft, RailLocation.CenterLeft));
            "J" -> if(!flipped) arrayOf(Route(RailLocation.TopRight, RailLocation.BottomRight), Route(RailLocation.BottomLeft, RailLocation.CenterLeft))
                else arrayOf(Route(RailLocation.TopLeft, RailLocation.BottomLeft), Route(RailLocation.TopRight, RailLocation.CenterRight));
            "K" -> if(!flipped) arrayOf(Route(RailLocation.BottomLeft, RailLocation.TopRight), Route(RailLocation.BottomRight, RailLocation.CenterLeft))
                else arrayOf(Route(RailLocation.BottomLeft, RailLocation.TopRight), Route(RailLocation.TopLeft, RailLocation.CenterRight));
            "L" -> if(!flipped) arrayOf(Route(RailLocation.BottomRight, RailLocation.TopLeft), Route(RailLocation.BottomLeft, RailLocation.CenterRight))
                else arrayOf(Route(RailLocation.BottomRight, RailLocation.TopLeft), Route(RailLocation.TopRight, RailLocation.CenterLeft));
            "M" -> if(!flipped) arrayOf(Route(RailLocation.TopLeft, RailLocation.BottomRight), Route(RailLocation.BottomLeft, RailLocation.CenterLeft))
                else arrayOf(Route(RailLocation.TopLeft, RailLocation.BottomRight), Route(RailLocation.TopRight, RailLocation.CenterRight));
            "N" -> if(!flipped) arrayOf(Route(RailLocation.TopRight, RailLocation.BottomLeft), Route(RailLocation.BottomRight, RailLocation.CenterRight))
                else arrayOf(Route(RailLocation.TopRight, RailLocation.BottomLeft), Route(RailLocation.TopLeft, RailLocation.CenterLeft));
            "O" -> if(!flipped) arrayOf(Route(RailLocation.TopLeft, RailLocation.BottomLeft), Route(RailLocation.BottomRight, RailLocation.CenterLeft))
                else arrayOf(Route(RailLocation.TopRight, RailLocation.BottomRight), Route(RailLocation.TopLeft, RailLocation.CenterRight));
            "P" -> if(!flipped) arrayOf(Route(RailLocation.TopRight, RailLocation.BottomRight), Route(RailLocation.BottomLeft, RailLocation.CenterRight))
                else arrayOf(Route(RailLocation.TopLeft, RailLocation.BottomLeft), Route(RailLocation.TopRight, RailLocation.CenterLeft));
            else -> emptyArray<Route>()
        }
        for(route in routes) {
            addRoute(card, route.a, route.b);
        }

    }

    fun addRoute(card : GameCard, a : RailLocation, b : RailLocation) {
        if(card is BuildRailCard) {
            card.addRoute(a, b);
        } else if(card is RailCard) {
            card.addRoute(a, b);
        }
    }

    fun getGameMessage() : String {
        var message : String;
        if(turnAction == TurnAction.GameOver) {
            message = "Game over.  ";
            message += when(moveResult) {
                MoveResult.DamagedRail -> "The train in " + moveYear + " hit a damaged rail.";
                MoveResult.MismatchedRail -> "The train in " + moveYear + " moved onto a mismatched rail.";
                MoveResult.MissingRail -> "The train in " + moveYear + " ran out of rail.";
                MoveResult.Obstacle -> "The train in " + moveYear + " moved into an obstacle.";
                MoveResult.OffMap -> "The train in " + moveYear + " moved off the map.";
                MoveResult.Win -> "You destroyed the Crommelin Comet.  Congratulations!";
                else -> "";
            }
        } else {
            message = players[playerTurn].scientistName + "'s turn (";
            message += players[playerTurn].pawnColor + ").  ";
            var bonus = 0;
            if(players[playerTurn].scientistName.equals("Geologist")) {
                bonus = 1;
            }
            message += when(turnAction) {
                TurnAction.DrawBuild -> "1) Draw a Build card. Check for damage.";
                TurnAction.DiscardBuild -> "Discard down to " + (maxCards+bonus) + " cards.";
                TurnAction.ShuffleInMovement -> "Shuffle the move card back into the draw deck.";
//                TurnAction.RollDamage -> "2) Roll for Damage.";
                TurnAction.DrawDamage -> "Apply damage.";
                TurnAction.TimeTravel -> "2) Travel through time.";
                TurnAction.Move -> "3) Move.";
                TurnAction.Trade -> "4) Trade cards with ONE player.";
                TurnAction.BuildRepair -> "5) Build or Repair.";
                TurnAction.ScrapsBuild -> "Choose an unavailable rail to build.";
                TurnAction.ScrapsDiscard -> "Discard a card.";
                else -> "";
            }
        }
        message += "  [" + getScore() + "] (" + moveCards.size + ")";
        return message;
    }

    override fun handleClick(ptx : Float, pty : Float) : Boolean {
        println(ptx.toString() + "," + pty.toString());

        var obj = getObjectAt(ptx, pty);
        var result = false;

        if(obj == flipButton) {
            result = FlipCards(this).execute();
        } else if(obj != null) {
            if(turnAction == TurnAction.DrawBuild && obj == buildDeck) {
                result = DrawBuildCard(this).execute();
            } else if(turnAction == TurnAction.ShuffleInMovement && obj == discardDeck) {
                result = ShuffleInMovement(this).execute();
            } else if(turnAction == TurnAction.DiscardBuild && obj == players[playerTurn]) {
                result = DiscardBuildCard(this, ptx, pty).execute();
            } else if(turnAction == TurnAction.DrawDamage && ptx in 770..795 && pty in 340..365) {
                result = DrawDamageCard(this).execute();
            } else if(turnAction == TurnAction.TimeTravel && obj is YearMat) {
                result = TravelThroughTime(this, obj, ptx, pty).execute();
            } else if(turnAction == TurnAction.Move && obj is YearMat) {
                result = MovePawn(this, obj, ptx, pty).execute();
            } else if(turnAction == TurnAction.Trade && obj is PlayerHand) {
                result = TradeCards(this, obj, ptx, pty).execute();
            } else if(turnAction == TurnAction.Trade && obj == tradeButton && tradeButton.highlight) {
                result = FinishTrade(this).execute();
            } else if(turnAction == TurnAction.BuildRepair && railDecks.contains(obj) && obj.highlight) {
                result = BuildRail(this, obj as GameDeck).execute();
            } else if(turnAction == TurnAction.BuildRepair && obj == buildButton) {
                result = FinishBuild(this).execute();
            } else if(turnAction == TurnAction.BuildRepair && obj == repairButton && repairButton.highlight) {
                result = RepairRail(this).execute();
            } else if(turnAction == TurnAction.BuildRepair && obj is ScrapsCard && obj.highlight) {
                result = BuildScraps(this, obj).execute();
            } else if(turnAction == TurnAction.ScrapsBuild && railDecks.contains(obj) && obj.highlight) {
                result = UseScraps(this, obj as GameDeck).execute();
            } else if(turnAction == TurnAction.BuildRepair && obj == moveAllButton && moveAllButton.highlight) {
                result = MoveAllCargo(this).execute();
            } else if(turnAction == TurnAction.ScrapsDiscard && obj == players[playerTurn]) {
                result = DiscardBuildCard(this, ptx, pty).execute();
            }
        }

        setHighlights();
        message.message = getGameMessage();

        return result;
    }

    fun nextPlayerTurn() {
        turnAction = TurnAction.DrawBuild;
        playerTurn++;
        if(playerTurn >= players.size) {
            playerTurn = 0;
        }
        movements = 0;
    }

    fun clearHighlights() {
        for(obj in gameObjects) {
            obj.highlight = false;
        }
        for(mat in yearMats) {
            mat.clearHighlights();
        }
    }

    fun setHighlights() {
        clearHighlights();

        flipButton.highlight = true;

        if(turnAction == TurnAction.BuildRepair && players[playerTurn].hand.size > 0) {
            moveAllButton.highlight = true;
        }

        var myMat = getPawnMat(pawns[playerTurn]);

        var pawnNumber = myMat!!.getPawnNumber(pawns[playerTurn]);

        when(turnAction) {
            TurnAction.DrawBuild -> buildDeck.highlight = true;
            TurnAction.ShuffleInMovement -> discardDeck.highlight = true;
            TurnAction.DiscardBuild -> players[playerTurn].highlight = true;
            TurnAction.DrawDamage -> for(gem in damageGems) if(gem.x>700) gem.highlight = true;
            TurnAction.TimeTravel -> TimeTravelHighlights(this, myMat).highlight();
            TurnAction.Move -> MoveHighlights(this, myMat, pawnNumber).highlight();
            TurnAction.Trade -> TradeHighlights(this, myMat).highlight();
            TurnAction.ScrapsBuild -> ScrapsHighlights(this).highlight();
            TurnAction.ScrapsDiscard -> players[playerTurn].highlight = true;
            TurnAction.BuildRepair -> BuildRepairHighlights(this, myMat, pawnNumber).highlight();
            TurnAction.GameOver -> println();
            else -> throw RuntimeException("Invalid turn action: " + turnAction);
        }
    }

    fun getPawnMat(pawn : GamePawn) : YearMat? {
        for(mat in yearMats) {
            var number = mat.getPawnNumber(pawn);
            if(number > 0) {
                return mat;
            }
        }
        return null;
    }
}
