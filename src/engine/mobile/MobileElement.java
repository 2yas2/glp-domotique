package engine.mobile;

import engine.map.Block;

/**
 * Base class for moving elements.
 */
public abstract class MobileElement {

	private Block position;

	/**
	 * Builds one moving element.
	 * @param position start block
	 */
	public MobileElement(Block position) {
		this.position = position;
	}

	public Block getPosition() {
		return position;
	}

	public void setPosition(Block position) {
		this.position = position;
	}
}
