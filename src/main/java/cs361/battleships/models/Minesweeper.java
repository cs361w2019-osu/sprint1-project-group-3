package cs361.battleships.models;


public class Minesweeper extends Ship {

    public Minesweeper() {
        super("MINESWEEPER");
    }
    
    @Override
    public Result processAttack(int x, char y, Weapon ws) {
        Result res = new Result();
        res.setLocation(new Square(x, y));
        res.setShip(this);

        // check if the ship was hit
        for(Square s : this.occupiedSquares) {
            if(s.getRow() == x && s.getColumn() == y) {
                // check for captains quarters

                if(s.getHit()) {
                    res.setResult(AtackStatus.INVALID);
                    return res;
                }
                    

                res.setResult(AtackStatus.HIT);
                s.setHit(true);

                Square cq = getCaptainsQuarters();
                if(s.getRow() == cq.getRow() && s.getColumn() == cq.getColumn()) {
                    res.setResult(AtackStatus.SUNK);
                    for(Square sq : occupiedSquares) 
                        sq.setHit(true);
                    this.health = 0;
                    return res;
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
}