package engine.item.furniture;

import java.util.ArrayList;

import engine.item.visitor.FurnitureVisitor;
import engine.map.Block;
import engine.state.Energy;
import engine.state.MasterState;

/**
 * Television used on its own or through the sofa.
 */
public class Television extends Furniture {

	private static final int RECOVERY = 20;
	private static final int DURATION = 4;
	private static final String DEFAULT_SHOW = "Programme libre";

	private int currentRounds = 0;
	private String currentShow = null;

	public Television(ArrayList<Block> blocks) {
		super("TV", blocks);
	}

	@Override
	public void tick() {
		currentRounds++;
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
		currentShow = null;
	}

	@Override
	public String getStatus() {
		if (!isActive()) {
			return "TV off";
		}
		String show = currentShow == null ? DEFAULT_SHOW : currentShow;
		return "Watching: " + show + " (" + currentRounds + "/" + DURATION + ")";
	}

	public String getCurrentShow() {
		return currentShow;
	}

	public void setCurrentShow(String show) {
		if (show == null || show.isEmpty()) {
			return;
		}
		this.currentShow = show;
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
