package engine.map;

/**
 * Grid map of the house.
 * It stores all blocks and some border helpers.
 */
public class Map {

	private Block[][] blocks;
	private int lineCount;
	private int columnCount;

	/**
	 * Builds a map filled with blocks.
	 * @param lineCount number of lines
	 * @param columnCount number of columns
	 */
	public Map(int lineCount, int columnCount) {
		this.lineCount = lineCount;
		this.columnCount = columnCount;
		this.blocks = new Block[lineCount][columnCount];

		for (int line = 0; line < lineCount; line++) {
			for (int column = 0; column < columnCount; column++) {
				blocks[line][column] = new Block(line, column);
			}
		}
	}

	public Block[][] getBlocks() {
		return blocks;
	}

	public int getLineCount() {
		return lineCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public Block getBlock(int line, int column) {
		return blocks[line][column];
	}

	public boolean isOnTop(Block block) {
		return block.getLine() == 0;
	}

	public boolean isOnBottom(Block block) {
		return block.getLine() == lineCount - 1;
	}

	public boolean isOnLeftBorder(Block block) {
		return block.getColumn() == 0;
	}

	public boolean isOnRightBorder(Block block) {
		return block.getColumn() == columnCount - 1;
	}

	/**
	 * Tells if the block is on any border.
	 * @param block the tested block
	 * @return true if the block touches a border
	 */
	public boolean isOnBorder(Block block) {
		return isOnTop(block) || isOnBottom(block) || isOnLeftBorder(block) || isOnRightBorder(block);
	}
}
