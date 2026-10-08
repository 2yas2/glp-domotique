package engine.item.furniture;

import java.util.ArrayList;

import engine.item.visitor.FurnitureVisitor;
import engine.map.Block;
import engine.state.Energy;
import engine.state.MasterState;

/**
 * Sofa used with the television.
 */
public class Sofa extends Furniture {

	private static final int DURATION = 5;

	private int currentRounds = 0;
	private Television tv;

	public Sofa(ArrayList<Block> blocks, Television tv) {
		super("Sofa", blocks);
		this.tv = tv;
	}

	@Override
	public void activate() {
		super.activate();
		tv.activate();
	}

	@Override
	public void standby() {
		super.standby();
		tv.standby();
	}

	@Override
	public void tick() {
		tv.activate();
		tv.tick();
		currentRounds++;
	}

	public void applyEffects(Energy energy) {
		tv.applyEffects(energy);
	}

	@Override
	public void applyEffects(MasterState state) {
		applyEffects(state.getEnergy());
	}

	@Override
	public boolean isDone() {
		return currentRounds >= DURATION;
	}

	@Override
	public void reset() {
		currentRounds = 0;
		tv.reset();
		tv.standby();
	}

	@Override
	public String getStatus() {
		String show = tv.getCurrentShow();
		if (show != null) {
			return "Watching \"" + show + "\" " + currentRounds + "/" + DURATION;
		}
		return "Watching TV... " + currentRounds + "/" + DURATION;
	}

	public Television getTv() {
		return tv;
	}

	public int getCurrentRounds() {
		return currentRounds;
	}

	public int getDuration() {
		return DURATION;
	}

	@Override
	public <T> T accept(FurnitureVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
