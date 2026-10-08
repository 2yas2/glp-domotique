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
 * Visitor used on the different furniture types.
 * @param <T> returned value type
 */
public interface FurnitureVisitor<T> {

	T visit(Bed bed);

	T visit(Bathtub bathtub);

	T visit(Oven oven);

	T visit(Sofa sofa);

	T visit(Television television);

	T visit(WaterBottle waterBottle);

	T visit(Sink sink);

	T visit(Decor decor);
}
