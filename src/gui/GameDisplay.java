package gui;

import config.SimulationConfiguration;
import engine.map.Map;
import engine.mobile.Friend;
import engine.mobile.Master;
import process.furniture.FurnitureManager;
import process.mobile.MobileInterface;
import process.room.RoomManager;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Zone centrale qui dessine la simulation.
 */
public class GameDisplay extends JPanel {

	private static final long serialVersionUID = 1L;

	private Map map;
	private MobileInterface manager;
	private RoomManager roomManager;
	private FurnitureManager furnitureManager;
	private PaintStrategy paintStrategy = new PaintStrategy();

	/**
	 * Construit l'affichage principal.
	 * @param map carte de jeu
	 * @param manager gestionnaire du personnage
	 * @param roomManager gestionnaire des pieces
	 * @param furnitureManager gestionnaire des meubles
	 */
	public GameDisplay(Map map, MobileInterface manager, RoomManager roomManager, FurnitureManager furnitureManager) {
		this.map = map;
		this.manager = manager;
		this.roomManager = roomManager;
		this.furnitureManager = furnitureManager;
	}

	/**
	 * Remplace le modele courant.
	 * @param map nouvelle carte
	 * @param manager nouveau gestionnaire mobile
	 * @param roomManager nouveau gestionnaire des pieces
	 * @param furnitureManager nouveau gestionnaire des meubles
	 */
	public void setModel(Map map, MobileInterface manager, RoomManager roomManager, FurnitureManager furnitureManager) {
		this.map = map;
		this.manager = manager;
		this.roomManager = roomManager;
		this.furnitureManager = furnitureManager;
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		Graphics2D graphics2D = (Graphics2D) graphics.create();

		int blockSize = SimulationConfiguration.BLOCK_SIZE;
		int naturalWidth = map.getColumnCount() * blockSize;
		int naturalHeight = map.getLineCount() * blockSize;
		double scaleX = getWidth() / (double) naturalWidth;
		double scaleY = getHeight() / (double) naturalHeight;
		double scale = Math.min(scaleX, scaleY);
		graphics2D.scale(scale, scale);

		paintStrategy.paintFloors(map, roomManager, graphics2D);
		paintStrategy.paintWalls(map, roomManager, graphics2D);
		paintStrategy.paintFurnitures(furnitureManager, graphics2D);

		Master master = manager.getMaster();
		if (manager.isAtHome()) {
			paintStrategy.paint(master, manager.getCurrentFurniture(), graphics2D);
		}
		Friend friend = manager.getFriend();
		if (friend != null) {
			paintStrategy.paint(friend, graphics2D);
		}
		paintStrategy.paintDarkRoom(map, roomManager, graphics2D);

		graphics2D.dispose();
	}
}
