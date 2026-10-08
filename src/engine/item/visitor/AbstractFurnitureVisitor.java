package engine.item.visitor;

import engine.item.furniture.Bathtub;
import engine.item.furniture.Bed;
import engine.item.furniture.Decor;
import engine.item.furniture.Oven;
import engine.item.furniture.Sink;
import engine.item.furniture.Sofa;
import engine.item.furniture.Television;
import engine.item.furniture.WaterBottle;

/**
 * Base visitor with one default value.
 * A subclass only overrides what it needs.
 */
public abstract class AbstractFurnitureVisitor<T> implements FurnitureVisitor<T> {

	private T defaultValue;

	protected AbstractFurnitureVisitor(T defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public T visit(Bed bed) {
		return defaultValue;
	}

	@Override
	public T visit(Bathtub bathtub) {
		return defaultValue;
	}

	@Override
	public T visit(Oven oven) {
		return defaultValue;
	}

	@Override
	public T visit(Sofa sofa) {
		return defaultValue;
	}

	@Override
	public T visit(Television television) {
		return defaultValue;
	}

	@Override
	public T visit(WaterBottle waterBottle) {
		return defaultValue;
	}

	@Override
	public T visit(Sink sink) {
		return defaultValue;
	}

	@Override
	public T visit(Decor decor) {
		return defaultValue;
	}
}
