package engine.item.furniture;

import java.util.ArrayList;

import engine.item.visitor.FurnitureVisitor;
import engine.map.Block;
import engine.state.Hygiene;
import engine.state.MasterState;

/**
 * Bathtub used to restore hygiene.
 */
public class Bathtub extends Furniture {

	private static final int RECOVERY = 60;
	private static final int DURATION = 2;

	private int currentRounds = 0;

	public Bathtub(ArrayList<Block> blocks) {
		super("Bathtub", blocks);
	}

	@Override
	public void tick() {
		currentRounds++;
	}

	public void applyEffects(Hygiene hygiene) {
		hygiene.increase(RECOVERY);
	}

	@Override
	public void applyEffects(MasterState state) {
		applyEffects(state.getHygiene());
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
		return "Washing... " + currentRounds + "/" + DURATION;
	}

	@Override
	public <T> T accept(FurnitureVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
