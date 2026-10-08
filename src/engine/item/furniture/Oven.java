package engine.item.furniture;

import java.util.ArrayList;

import engine.item.visitor.FurnitureVisitor;
import engine.map.Block;
import engine.state.Hunger;
import engine.state.MasterState;

/**
 * Oven used to cook and restore hunger.
 */
public class Oven extends Furniture {

	private static final int RECOVERY = 75;
	private static final int DURATION = 4;

	private int currentRounds = 0;
	private String currentDish = null;
	private ArrayList<String> dishes = new ArrayList<String>();
	private boolean preheating = false;

	public Oven(ArrayList<Block> blocks) {
		super("Oven", blocks);
		dishes.add("Pizza Margherita");
		dishes.add("Lasagna");
		dishes.add("Gratin dauphinois");
		dishes.add("Apple pie");
	}

	@Override
	public void tick() {
		if (currentDish == null && !dishes.isEmpty()) {
			currentDish = dishes.remove(0);
		}
		if (currentDish != null) {
			currentRounds++;
		}
	}

	public void applyEffects(Hunger hunger) {
		hunger.increase(RECOVERY);
	}

	@Override
	public void applyEffects(MasterState state) {
		applyEffects(state.getHunger());
	}

	@Override
	public boolean isDone() {
		if (dishes.isEmpty() && currentDish == null) {
			return true;
		}
		return currentRounds >= DURATION;
	}

	@Override
	public void reset() {
		currentRounds = 0;
		currentDish = null;
		preheating = false;
	}

	@Override
	public String getStatus() {
		if (currentDish != null) {
			return "Cooking: " + currentDish + " (" + currentRounds + "/" + DURATION + ")";
		}
		if (dishes.isEmpty()) {
			return "Nothing left to cook!";
		}
		return "Preheating the oven...";
	}

	public void addDish(String dish) {
		if (dish == null || dish.isEmpty()) {
			throw new IllegalArgumentException("Dish name must not be empty");
		}
		dishes.add(dish);
	}

	public ArrayList<String> getDishes() {
		return dishes;
	}

	public String getCurrentDish() {
		return currentDish;
	}

	public void setCurrentDish(String dish) {
		if (dish == null || dish.isEmpty()) {
			return;
		}
		this.currentDish = dish;
		if (currentRounds > 0) {
			currentRounds = 0;
		}
	}

	public int getCurrentRounds() {
		return currentRounds;
	}

	public int getDuration() {
		return DURATION;
	}

	public boolean isPreheating() {
		return preheating;
	}

	public void setPreheating(boolean preheating) {
		this.preheating = preheating;
	}

	@Override
	public <T> T accept(FurnitureVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
