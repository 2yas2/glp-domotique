package engine.item.furniture;

import java.util.ArrayList;

import engine.item.visitor.FurnitureVisitor;
import engine.map.Block;
import engine.state.MasterState;

/**
 * Base class for all furniture objects.
 */
public abstract class Furniture {

	private String name;
	private ArrayList<Block> blocks;
	private boolean active = false;

	public Furniture(String name, ArrayList<Block> blocks) {
		this.name = name;
		this.blocks = blocks;
	}

	public boolean isActive() {
		return active;
	}

	public void activate() {
		this.active = true;
	}

	public void standby() {
		this.active = false;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public boolean occupied(Block block) {
		return blocks.contains(block);
	}

	public abstract String getStatus();

	public abstract void tick();

	public abstract boolean isDone();

	public abstract void reset();

	public abstract void applyEffects(MasterState state);

	public abstract <T> T accept(FurnitureVisitor<T> visitor);
}
