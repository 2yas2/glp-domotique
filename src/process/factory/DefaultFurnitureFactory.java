package process.factory;

import java.util.ArrayList;
import java.util.List;

import engine.item.furniture.Bathtub;
import engine.item.furniture.Bed;
import engine.item.furniture.Decor;
import engine.item.device.Light;
import engine.item.furniture.Oven;
import engine.item.furniture.Sink;
import engine.item.furniture.Sofa;
import engine.item.device.Switch;
import engine.item.furniture.Television;
import engine.item.furniture.WaterBottle;
import engine.map.Block;
import engine.map.Map;
import process.furniture.FurnitureManager;

/**
 * Default factory used to place the base furniture and decor.
 */
public class DefaultFurnitureFactory implements FurnitureFactory {

	@Override
	public Light createLight() {
		return new Light();
	}

	@Override
	public Switch createSwitch(Map map, int line, int column, Light light) {
		Block block = map.getBlock(line, column);
		return new Switch(light, block);
	}

	@Override
	public FurnitureManager buildFurnitureManager(Map map) {
		Bed bed = new Bed(createBlocks(map, new int[][] {
				{ 2, 6 }, { 2, 7 }, { 3, 6 }, { 3, 7 }, { 4, 6 }, { 4, 7 }
		}));

		Bathtub bathtub = new Bathtub(createBlocks(map, new int[][] {
				{ 2, 22 }, { 2, 23 }, { 3, 22 }, { 3, 23 }
		}));

		Sink sink = new Sink(createBlocks(map, new int[][] {
				{ 2, 30 }
		}));

		Oven oven = new Oven(createBlocks(map, new int[][] {
				{ 13, 28 }, { 14, 28 }
		}));

		Television television = new Television(createBlocks(map, new int[][] {
				{ 18, 6 }
		}));

		Sofa sofa = new Sofa(createBlocks(map, new int[][] {
				{ 13, 6 }, { 13, 7 }, { 14, 6 }, { 14, 7 }
		}), television);

		WaterBottle waterBottle = new WaterBottle(createBlocks(map, new int[][] {
				{ 12, 35 }, { 13, 35 }, { 14, 35 }
		}));

		List<Decor> decors = new ArrayList<Decor>();
		decors.add(createDecor(map, "Wardrobe", new int[][] {
				{ 1, 14 }, { 1, 15 }, { 2, 14 }, { 2, 15 }, { 3, 14 }, { 3, 15 }
		}));
		decors.add(createDecor(map, "Dresser", new int[][] {
				{ 8, 2 }, { 8, 3 }, { 8, 4 }, { 9, 2 }, { 9, 3 }, { 9, 4 }
		}));
		decors.add(createDecor(map, "Dresserchair", new int[][] {
				{ 10, 3 }
		}));
		decors.add(createDecor(map, "Lamp", new int[][] {
				{ 2, 4 }
		}));
		decors.add(createDecor(map, "Lamp2", new int[][] {
				{ 2, 9 }
		}));
		decors.add(createDecor(map, "tabledechevetgauche", new int[][] {
				{ 3, 4 }
		}));
		decors.add(createDecor(map, "tabledechevetdroit", new int[][] {
				{ 3, 9 }
		}));
		decors.add(createDecor(map, "cadre", new int[][] {
				{ 1, 6 }, { 1, 7 }
		}));
		decors.add(createDecor(map, "meubleCuisine", new int[][] {
				{ 13, 23 }, { 13, 24 }, { 13, 25 }, { 13, 26 }, { 13, 27 },
				{ 14, 23 }, { 14, 24 }, { 14, 25 }, { 14, 26 }, { 14, 27 }
		}));
		decors.add(createDecor(map, "meubleCuisine2", new int[][] {
				{ 13, 29 }, { 13, 30 }, { 14, 29 }, { 14, 30 }
		}));
		decors.add(createDecor(map, "meubleCuisine3", new int[][] {
				{ 12, 24 }, { 12, 25 }, { 12, 26 }, { 12, 27 }, { 12, 28 }, { 12, 29 }
		}));
		decors.add(createDecor(map, "tabledecuisine", new int[][] {
				{ 16, 27 }, { 16, 28 }, { 17, 27 }, { 17, 28 }, { 18, 27 }, { 18, 28 }
		}));
		decors.add(createDecor(map, "tabouretgauche", new int[][] {
				{ 17, 25 }
		}));
		decors.add(createDecor(map, "tabouretdroit", new int[][] {
				{ 17, 30 }
		}));
		decors.add(createDecor(map, "Cabinet", new int[][] {
				{ 1, 29 }, { 1, 30 }
		}));
		decors.add(createDecor(map, "Toilet", new int[][] {
				{ 2, 35 }, { 3, 35 }
		}));
		decors.add(createDecor(map, "Bookshelf", new int[][] {
				{ 12, 12 }, { 13, 12 }, { 12, 13 }, { 13, 14 }, { 14, 14 }
		}));
		decors.add(createDecor(map, "canape", new int[][] {
				{ 15, 4 }
		}));
		decors.add(createDecor(map, "CoffeeTable", new int[][] {
				{ 16, 6 }, { 16, 7 }
		}));
		decors.add(createDecor(map, "meubletele", new int[][] {
				{ 19, 5 }, { 19, 6 }, { 19, 7 }
		}));
		return new FurnitureManager(bed, bathtub, oven, sofa, television, waterBottle, sink, decors);
	}

	private Decor createDecor(Map map, String name, int[][] coordinates) {
		return new Decor(name, createBlocks(map, coordinates));
	}

	private ArrayList<Block> createBlocks(Map map, int[][] coordinates) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		for (int i = 0; i < coordinates.length; i++) {
			blocks.add(map.getBlock(coordinates[i][0], coordinates[i][1]));
		}
		return blocks;
	}
}
