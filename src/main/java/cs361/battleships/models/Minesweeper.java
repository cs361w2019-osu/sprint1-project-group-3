package cs361.battleships.models;


public class Minesweeper extends Ship {

    public Minesweeper() {
        super("MINESWEEPER");
    }
    
    @Override
    public Result processAttack(int x, char y) {
        return super.processAttack(x, y);
    }
}