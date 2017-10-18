package cja.game.cp

import cja.game.*

class YearMat : GameMat {
    var matYear : Int = 0;

    var squares : Array<List<MatCard>> = Array<List<MatCard>>(15, {ArrayList<MatCard>()});
    var pawns : Array<List<GamePawn>> = Array<List<GamePawn>>(15, {ArrayList<GamePawn>()});
    var highlights : Array<Boolean> = Array<Boolean>(15, {false});

    var cargo : GamePawn = GamePawn("");
    var cargoSquare : Int = 0;
    var cargoLocation : RailLocation = RailLocation.None;

    constructor(year : Int, front : String?, back : String?) : super(front, back) {
        this.matYear = year;
    }

    fun moveCargo() : MoveResult {
        if(cargoLocation == RailLocation.None) {
            cargoLocation = RailLocation.Start;
            cargo.setPosition(cargo.x, cargo.y-20, cargo.width, cargo.height);
        } else if(cargoLocation == RailLocation.Finish) {
            return MoveResult.Safe;
        } else if(cargoLocation == RailLocation.Start) {
            var nl = when(matYear) {
                1930 -> NumberLocation(13, RailLocation.BottomLeft);
                1957 -> NumberLocation(14, RailLocation.BottomLeft);
                1984 -> NumberLocation(14, RailLocation.BottomRight);
                2011 -> NumberLocation(15, RailLocation.BottomRight);
                else -> NumberLocation(-1, RailLocation.None);
            }
            if(isBlocked(nl.number)) return MoveResult.Obstacle;
            if(isEmpty(nl.number)) return MoveResult.MismatchedRail;
            if(isDamaged(nl.number)) return MoveResult.DamagedRail;

            var rails = squares[nl.number-1];
            var rail = rails[rails.size-1] as RailCard;

            if(!rail.routes.containsKey(nl.location)) {
                return MoveResult.MissingRail;
            }

            cargoLocation = rail.routes[nl.location] as RailLocation;
            var pt = rail.getLocationPoint(cargoLocation);
            cargo.setPosition(rail.x+pt.x-7, rail.y+pt.y, 15f, 15f);
            cargoSquare = nl.number;
        } else {
            var nl = NumberLocation(0, RailLocation.None);

            var column1 = arrayOf(1, 4, 7, 10, 13);
            var column3 = arrayOf(3, 6, 9, 12, 15);
            if(cargoLocation == RailLocation.CenterLeft) {
                if(column1.contains(cargoSquare)) {
                    return MoveResult.OffMap;
                } else {
                    nl.number = cargoSquare-1;
                }
            } else if(cargoLocation == RailLocation.TopLeft || cargoLocation == RailLocation.TopRight) {
                if(cargoSquare <= 3) {
                    cargoLocation = RailLocation.Finish;
                    cargo.setPosition(cargo.x, cargo.y-20, cargo.width, cargo.height);
                    return MoveResult.Safe;
                } else {
                    nl.number = cargoSquare-3;
                }
            } else if(cargoLocation == RailLocation.BottomLeft || cargoLocation == RailLocation.BottomRight) {
                if(cargoSquare >= 13) {
                    return MoveResult.OffMap;
                } else {
                    nl.number = cargoSquare+3;
                }
            } else if(cargoLocation == RailLocation.CenterRight) {
                if(column3.contains(cargoSquare)) {
                    return MoveResult.OffMap;
                } else {
                    nl.number = cargoSquare+1;
                }
            }

            if(isBlocked(nl.number)) return MoveResult.Obstacle;
            if(isEmpty(nl.number)) return MoveResult.MissingRail;
            if(isDamaged(nl.number)) return MoveResult.DamagedRail;

            nl.location = when(cargoLocation) {
                RailLocation.BottomLeft -> RailLocation.TopLeft;
                RailLocation.BottomRight -> RailLocation.TopRight;
                RailLocation.TopLeft -> RailLocation.BottomLeft;
                RailLocation.TopRight -> RailLocation.BottomRight;
                RailLocation.CenterLeft -> RailLocation.CenterRight;
                RailLocation.CenterRight -> RailLocation.CenterLeft;
                else -> RailLocation.None;
            }

            var rails = squares[nl.number-1];
            var rail = rails[rails.size-1] as RailCard;
            if(!rail.routes.containsKey(nl.location)) {
                return MoveResult.MismatchedRail;
            }

            cargoLocation = rail.routes[nl.location] as RailLocation;
            var pt = rail.getLocationPoint(cargoLocation);
            cargo.setPosition(rail.x+pt.x-7, rail.y+pt.y, 15f, 15f);
            cargoSquare = nl.number;
        }
        return MoveResult.Safe;
    }

    fun setCardSquare(number : Int, card : MatCard) {
        (squares[number-1] as MutableList).add(card);
    }

    fun removePawn(pawn : GamePawn) {
        for(pwns in pawns) {
            if(pwns.contains(pawn)) {
                (pwns as MutableList).remove(pawn);
            }
        }
    }

    fun setPawnSquare(number : Int, pawn : GamePawn) {
        (pawns[number-1] as MutableList).add(pawn);
    }

    fun highlightSquare(number : Int) {
        highlights[number-1] = true;
    }

    fun isEmpty(number : Int) : Boolean {
        return squares[number-1].size == 0;
    }

    fun isDamaged(number : Int) : Boolean {
        for(card in squares[number-1]) {
            if(card is DamageGem) {
                return true;
            }
        }
        return false;
    }

    fun cardAt(number : Int) : MatCard? {
        var count = squares[number-1].size;
        if(count > 0) {
            return squares[number-1][count-1];
        } else {
            return null;
        }
    }

    fun isBlocked(number : Int) : Boolean {
        for(card in squares[number-1]) {
            if(card is ObstacleCard) {
                return true;
            }
        }
        return false;
    }

    fun isRail(number : Int) : Boolean {
        var count = squares[number-1].size;
        if(count > 0) {
            if(squares[number-1][count-1] is RailCard) {
                return true;
            }
        }
        return false;
    }

    fun clearHighlights() {
        for(i in 0..highlights.size-1) {
            highlights[i] = false;
        }
    }

    fun checkForDamage(column : Int) : Boolean {
        var column1 = arrayOf(1, 4, 7, 10, 13);
        var column2 = arrayOf(2, 5, 8, 11, 14);
        var column3 = arrayOf(3, 6, 9, 12, 15);

        var col = when(column) {
            1 -> column1;
            2 -> column2;
            else -> column3;
        }

        for(i in 0..col.size-1) {
            var number = col[i];
            if(isDamaged(number)) {
                return false;
            }
            if(isRail(number)) {
                if(number == cargoSquare) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    fun damageColumn(card : DamageGem, column : Int) : Boolean {
        var column1 = arrayOf(1, 4, 7, 10, 13);
        var column2 = arrayOf(2, 5, 8, 11, 14);
        var column3 = arrayOf(3, 6, 9, 12, 15);

        var col = when(column) {
            1 -> column1;
            2 -> column2;
            else -> column3;
        }

        for(i in 0..col.size-1) {
            var number = col[i];
            if(isDamaged(number)) {
                return false;
            }
            if(isRail(number)) {
                if(number == cargoSquare) {
                    return false;
                } else {
                    setCardSquare(number, card);
                    return true;
                }
            }
        }
        return false;
    }

    fun repairSquare(number : Int) {
        for(i in 0..squares[number-1].size-1) {
            if(squares[number-1][i] is DamageGem) {
                //deck.cards.add((squares[number-1] as MutableList).remove(i));
                var gem = (squares[number-1] as MutableList).removeAt(i);
                gem.setPosition(770f, 340f, 25f, 25f);
                return;
            }
        }
    }

    fun getSquareNumberPosition(number : Int) : Point {
        var i = 0;
        var yy = y+36;

        for(row in 0..4) {
            var xx = x + 2;
            for(col in 0..2) {
                i++;
                if(i == number) {
                    return Point(xx+23, yy+66);
                }
                xx += 46;
            }
            yy += 66;
        }
        return Point(0f, 0f);
    }

    fun getSquareNumberAt(ptx : Float, pty : Float) : Int {
        var i = 0;
        var yy = y+36;

        for(row in 0..4) {
            var xx = x + 2;
            for(col in 0..2) {
                var bounds = Rectangle(xx, yy, 46f, 66f);
                if(bounds.contains(ptx, pty)) {
                    return i+1;
                }
                i++;
                xx += 46;
            }
            yy += 66;
        }
        return 0;
    }

    fun getPawnNumber(pawn : GamePawn) : Int {
        for(i in 0..pawns.size-1) {
            if(pawns[i].contains(pawn)) {
                return i+1;
            }
        }
        return 0;
    }

    override fun draw(g : GameGraphics) {
        super.draw(g);
        var drawGrid = !g.lastImageSuccess();

        var i = 0;
        var yy = y+36;
        var sqh = 66f;
        var sqw = 46f;

        for(row in 0..4) {
            var xx = x + 8;
            for(col in 0..2) {
                for(card in squares[i]) {
                    card.setPosition(xx, yy,
                            when (card.width) {0f -> sqw; else -> card.width;},
                            when (card.height) {0f -> sqh; else -> card.height;});
                    card.draw(g);
                }
//                if(squares[i].size() > 0) {
//                    squares[i][squares[i].size()-1].draw(g);
//                }
                if(drawGrid) {
                    g.drawRectangle2(xx, yy, sqw, sqh, "Gray");
                }

                if(highlights[i]) {
                    g.fillRectangle2(xx, yy, sqw, sqh, "rgba(255, 255, 255, 0.33)", 100f);
                    //g.drawRectangle3(xx-2, yy-2, sqw+4, sqh+4, "Black", 5);
                }

                for(p in 0..pawns[i].size-1) {
                    var pawn = pawns[i][p];
                    when(p) {
                        0 -> pawn.setPosition(xx, yy+35, 30f, 30f);
                        1 -> pawn.setPosition(xx+25, yy+35, 30f, 30f);
                        2 -> pawn.setPosition(xx, yy+5, 30f, 30f);
                        3 -> pawn.setPosition(xx+25, yy+35, 30f, 30f);
                    }
                    pawn.draw(g);
                }
                i++;
                xx += sqw;
            }
            yy += sqh;
        }
        cargo.draw(g);
    }
}
