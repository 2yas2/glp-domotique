package engine.item.furniture;

import java.util.ArrayList;

import engine.item.visitor.FurnitureVisitor;
import engine.map.Block;
import engine.state.Energy;
import engine.state.MasterState;

/**
 * Bed used to restore energy.
 */
public class Bed extends Furniture {

	private static final int RECOVERY = 70;
	private static final int DURATION = 15;

	private int currentRounds = 0;
	private int totalSleptRounds = 0;

	public Bed(ArrayList<Block> blocks) {
		super("Bed", blocks);
	}

	@Override
	public void tick() {
		currentRounds++;
		totalSleptRounds++;
	}

	public void applyEffects(Energy energy) {
		energy.increase(RECOVERY);
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
	}

	@Override
	public String getStatus() {
		return "Sleeping... " + currentRounds + "/" + DURATION;
	}

	public int getTotalSleptRounds() {
		return totalSleptRounds;
	}

	@Override
	public <T> T accept(FurnitureVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
