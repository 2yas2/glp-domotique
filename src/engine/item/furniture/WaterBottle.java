package engine.item.furniture;

import java.util.ArrayList;

import engine.item.visitor.FurnitureVisitor;
import engine.map.Block;
import engine.state.MasterState;
import engine.state.Thirst;

/**
 * Water bottle used to restore thirst.
 */
public class WaterBottle extends Furniture {

	private static final int RECOVERY = 90;
	private static final int DURATION = 2;

	private int currentRounds = 0;

	public WaterBottle(ArrayList<Block> blocks) {
		super("Water bottle", blocks);
	}

	@Override
	public void tick() {
		currentRounds++;
	}

	public void applyEffects(Thirst thirst) {
		thirst.increase(RECOVERY);
	}

	@Override
	public void applyEffects(MasterState state) {
		applyEffects(state.getThirst());
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
		return "Drinking... " + currentRounds + "/" + DURATION;
	}

	@Override
	public <T> T accept(FurnitureVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
