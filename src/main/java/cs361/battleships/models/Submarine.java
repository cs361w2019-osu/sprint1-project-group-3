package cs361.battleships.models;

public class Submarine extends Ship {
    public Submarine() {
        super("SUBMARINE");
    }




    public Result processAttack(int x, char y, Weapon weapon) {
        Result res = new Result();
        res.setLocation(new Square(x, y));
        res.setShip(this);

        // check if the ship was hit
        for(Square s : this.occupiedSquares) {
            if(s.getRow() == x && s.getColumn() == y) {

                if(weapon != Weapon.LASER && isSubmerged()) {
                    return null;
                }

                if(s.getHit()) {
                    res.setResult(AtackStatus.INVALID);
                    return res;
                }

                // if the weapon is a space laser, a hit counts as death
                if(weapon == Weapon.LASER) {
                    res.setResult(AtackStatus.SUNK);
                    this.health = 0;
                    for(Square sq : occupiedSquares)
                        sq.setHit(true);
                    return res;
                }


                res.setResult(AtackStatus.HIT);
                s.setHit(true);

                Square cq = getCaptainsQuarters();
                if(s.getRow() == cq.getRow() && s.getColumn() == cq.getColumn()) {
                    if(!cqHit) {
                        res.setResult(AtackStatus.MISS);
                        s.setHit(false);
                        this.cqHit = true;
                        return res;
                    } else {
                        res.setResult(AtackStatus.SUNK);
                        this.health = 0;
                        for(Square sq : occupiedSquares)
                            sq.setHit(true);
                        return res;
                    }
                }

                if(!s.getHit())
                    this.health--;
                if(this.health <= 0) {
                    res.setResult(AtackStatus.SUNK);
                }

                return res;
            }
        }

        return null;
    }

    @Override
    public void setOccupiedSquaresByOrientation(int row, char col, boolean vertical) {
        this.occupiedSquares.clear();
        // submarines are four units long
        for(int i = 0; i < 4; i++) {
            Square s;
            if(vertical) {
                // rows increase going down the page
                s = new Square(row + i, col);
            } else {
                // columns increase going to the right
                s = new Square(row, (char)(col + i));
            }
            this.occupiedSquares.add(s);
        }
        // add the extra bit
        Square s;
        if(vertical) {
            s = new Square(row + 2, (char)(col + 1));
        } else {
            s = new Square(row - 1, (char)(col + 2));
        }
        this.occupiedSquares.add(s);

        if(occupiedSquares.size() > 0) {
            this.captainsQuarters = new Square(
                    occupiedSquares.get(occupiedSquares.size() - 2).getRow(),
                    occupiedSquares.get(occupiedSquares.size() - 2).getColumn());
        }
    }


}
