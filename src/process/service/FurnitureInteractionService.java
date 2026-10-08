package process.service;

import engine.item.furniture.Furniture;
import engine.state.MasterState;

/**
 * Small service used during furniture interactions.
 * It keeps the calls grouped in one place.
 */
public class FurnitureInteractionService {

	/**
	 * Advances the current furniture.
	 * @param furniture the active furniture
	 */
	public void tick(Furniture furniture) {
		if (furniture == null) {
			return;
		}

		furniture.tick();
	}

	public boolean isDone(Furniture furniture) {
		if (furniture == null) {
			return true;
		}

		return furniture.isDone();
	}

	public void reset(Furniture furniture) {
		if (furniture == null) {
			return;
		}

		furniture.reset();
	}

	public void applyEffects(Furniture furniture, MasterState masterState) {
		if (furniture == null || masterState == null) {
			return;
		}

		furniture.applyEffects(masterState);
	}
}
