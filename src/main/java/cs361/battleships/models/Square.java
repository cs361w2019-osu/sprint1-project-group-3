package cs361.battleships.models;

@SuppressWarnings("unused")
public class Square {

	private int row;
	private char column;
	private boolean hit;

	public Square() {
		this.hit = false;
	}

	public Square(int row, char column) {
		this.row = row;
		this.column = column;
	}

	public char getColumn() {
		return column;
	}

	public void setColumn(char column) {
		this.column = column;
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public boolean getHit() {
		return this.hit;
	}

	public void setHit(boolean b) {
		this.hit = b;
	}

	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Square)) {
			return false;
		}

		Square o = (Square)other;

		return getRow() == o.getRow() && getColumn() == o.getColumn();
	}
}
