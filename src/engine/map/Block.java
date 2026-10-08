package engine.map;

/**
 * One cell of the map grid.
 */
public class Block {
	private int line;
	private int column;

	/**
	 * Builds a block with its grid coordinates.
	 * @param line block line
	 * @param column block column
	 */
	public Block(int line, int column) {
		this.line = line;
		this.column = column;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Block)) return false;
		Block other = (Block) obj;
		return this.line == other.line && this.column == other.column;
	}

	@Override
	public int hashCode() {
		return 31 * line + column;
	}

	@Override
	public String toString() {
		return "Block [line=" + line + ", column=" + column + "]";
	}
}
