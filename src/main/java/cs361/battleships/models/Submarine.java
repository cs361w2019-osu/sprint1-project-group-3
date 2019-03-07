package cs361.battleships.models;

public class Submarine extends Ship {
    public Submarine() {
        super("SUBMARINE");
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

        for(Square sq : this.occupiedSquares)
            sq.setSubmerged(this.submerged);
    }


    @Override
    public Result processAttack(int x, char y) {
        if(isSubmerged()) {
            return null;
        } else {
            return super.processAttack(x, y);
        }
    }


}
