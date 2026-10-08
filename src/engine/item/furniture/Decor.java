package engine.item.furniture;

import java.util.ArrayList;

import engine.item.visitor.FurnitureVisitor;
import engine.map.Block;
import engine.state.MasterState;

/**
 * Decorative object placed in the rooms.
 */
public class Decor extends Furniture {

	private boolean walkable;

	public Decor(String name, ArrayList<Block> blocks) {
		this(name, blocks, false);
	}

	public Decor(String name, ArrayList<Block> blocks, boolean walkable) {
		super(name, blocks);
		this.walkable = walkable;
	}

	@Override
	public boolean occupied(Block block) {
		if (walkable) {
			return false;
		}
		return super.occupied(block);
	}

	@Override
	public void tick() {
	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public void reset() {
	}

	@Override
	public void applyEffects(MasterState state) {
	}

	@Override
	public String getStatus() {
		return getName();
	}

	@Override
	public <T> T accept(FurnitureVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
