package decision;

import engine.item.furniture.Furniture;

/**
 * Small result object returned by the decision maker.
 */
public class Decision {

	private boolean fromRoutine;
	private Furniture targetFurniture;

	public Decision(boolean fromRoutine, Furniture targetFurniture) {
		this.fromRoutine = fromRoutine;
		this.targetFurniture = targetFurniture;
	}

	public boolean isFromRoutine() {
		return fromRoutine;
	}

	public Furniture getTargetFurniture() {
		return targetFurniture;
	}
}
