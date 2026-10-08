package process.furniture;

import java.util.ArrayList;
import java.util.List;

import engine.item.furniture.Bathtub;
import engine.item.furniture.Bed;
import engine.item.furniture.Decor;
import engine.item.furniture.Furniture;
import engine.item.furniture.Oven;
import engine.item.furniture.Sink;
import engine.item.furniture.Sofa;
import engine.item.furniture.Television;
import engine.item.furniture.WaterBottle;
import engine.map.Block;

/**
 * Regroupe tous les meubles utiles du jeu.
 */
public class FurnitureManager {

	private Bed bed;
	private Bathtub bathtub;
	private Oven oven;
	private Sofa sofa;
	private Television television;
	private WaterBottle waterBottle;
	private Sink sink;
	private List<Decor> decors;

	private ArrayList<Furniture> all;

	/**
	 * Construit le gestionnaire des meubles.
	 * @param bed lit principal
	 * @param bathtub baignoire
	 * @param oven four
	 * @param sofa canape
	 * @param television television
	 * @param waterBottle bouteille d'eau
	 * @param sink lavabo
	 * @param decors decors de la carte
	 */
	public FurnitureManager(Bed bed, Bathtub bathtub, Oven oven, Sofa sofa, Television television,
			WaterBottle waterBottle, Sink sink, List<Decor> decors) {
		this.bed = bed;
		this.bathtub = bathtub;
		this.oven = oven;
		this.sofa = sofa;
		this.television = television;
		this.waterBottle = waterBottle;
		this.sink = sink;
		this.decors = decors;
		this.all = new ArrayList<Furniture>();
		all.add(bed);
		all.add(bathtub);
		all.add(oven);
		all.add(sofa);
		all.add(television);
		all.add(waterBottle);
		all.add(sink);
		for (Decor decor : decors) {
			all.add(decor);
		}
	}

	public Bed getBed() {
		return bed;
	}

	public Bathtub getBathtub() {
		return bathtub;
	}

	public Oven getOven() {
		return oven;
	}

	public Sofa getSofa() {
		return sofa;
	}

	public Television getTelevision() {
		return television;
	}

	public WaterBottle getWaterBottle() {
		return waterBottle;
	}

	public Sink getSink() {
		return sink;
	}

	public List<Decor> getDecors() {
		return decors;
	}

	public ArrayList<Furniture> getAll() {
		return all;
	}

	/**
	 * Remet tous les meubles en veille.
	 */
	public void setStandby() {
		for (Furniture furniture : all) {
			furniture.standby();
		}
	}

	/**
	 * Dit si un bloc est pris par un meuble.
	 * @param block bloc teste
	 * @return true si occupe
	 */
	public boolean isOccupied(Block block) {
		for (Furniture furniture : all) {
			if (furniture.occupied(block)) {
				return true;
			}
		}
		return false;
	}
}
