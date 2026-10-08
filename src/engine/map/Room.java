package engine.map;

import engine.item.device.Light;

/**
 * Base class for the rooms of the house.
 */
public abstract class Room {

	private int startLine;
	private int startColumn;
	private int endLine;
	private int endColumn;
	private Light light;
	private Block door;

	/**
	 * Builds a room rectangle and its door.
	 * @param startLine first line
	 * @param startColumn first column
	 * @param endLine last line
	 * @param endColumn last column
	 * @param door room door
	 */
	public Room(int startLine, int startColumn, int endLine, int endColumn, Block door) {
		this.startLine = startLine;
		this.startColumn = startColumn;
		this.endLine = endLine;
		this.endColumn = endColumn;
		this.door = door;
	}

	public int getStartLine() {
		return startLine;
	}

	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	public int getStartColumn() {
		return startColumn;
	}

	public void setStartColumn(int startColumn) {
		this.startColumn = startColumn;
	}

	public int getEndLine() {
		return endLine;
	}

	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	public int getEndColumn() {
		return endColumn;
	}

	public void setEndColumn(int endColumn) {
		this.endColumn = endColumn;
	}

	public Block getDoor() {
		return door;
	}

	public void setDoor(Block door) {
		this.door = door;
	}

	public Light getLight() {
		return light;
	}

	public void setLight(Light light) {
		this.light = light;
	}

	/**
	 * Tells if a block is inside the room area.
	 * @param block tested block
	 * @return true if inside the room
	 */
	public boolean isRoom(Block block) {
		return block.getLine() >= startLine && block.getLine() <= endLine
				&& block.getColumn() >= startColumn && block.getColumn() <= endColumn;
	}

	public boolean isDoor(Block block) {
		return block.equals(door);
	}

	public boolean isWall(Block block) {
		if (!isRoom(block)) {
			return false;
		}

		boolean onBorder = block.getLine() == startLine
				|| block.getLine() == endLine
				|| block.getColumn() == startColumn
				|| block.getColumn() == endColumn;

		if (!onBorder) return false;

		return !block.equals(door);
	}

	/**
	 * Tells if a block is on a thick top wall of the room.
	 * Top wall is given thickness blocks, other sides stay 1 block.
	 * @param block tested block
	 * @param thickness wall thickness for the top side
	 * @return true if it is a thick wall block
	 */
	public boolean isWallWithThickness(Block block, int thickness) {
		if (!isRoom(block)) {
			return false;
		}

		boolean onBorder = block.getLine() < startLine + thickness
				|| block.getLine() == endLine
				|| block.getColumn() == startColumn
				|| block.getColumn() == endColumn;

		return onBorder && !block.equals(door);
	}
}
