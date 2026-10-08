package process.room;

import config.SimulationConfiguration;
import engine.item.device.Light;
import engine.map.BathRoom;
import engine.map.BedRoom;
import engine.map.Block;
import engine.map.Kitchen;
import engine.map.LivingRoom;
import engine.map.Map;
import engine.map.Room;
import log.LoggerUtility;
import org.apache.log4j.Logger;
import process.factory.DefaultRoomFactory;
import process.factory.RoomFactory;
import process.furniture.FurnitureManager;

/**
 * Stores the rooms, corridor and exit.
 * It also knows what is walkable or blocked.
 */
public class RoomManager {

	private static Logger logger = LoggerUtility.getLogger(RoomManager.class, "html");

	private Map map;

	private BedRoom bedroom;
	private BathRoom bathroom;
	private LivingRoom livingRoom;
	private Kitchen kitchen;
	private Block exit;

	private int corridorColStart;
	private int corridorColEnd;
	private Light corridorLight;
	private FurnitureManager furnitureManager;
	private RoomFactory roomFactory;

	public RoomManager(Map map) {
		this(map, new DefaultRoomFactory());
	}

	public RoomManager(Map map, RoomFactory roomFactory) {
		this.map = map;
		this.roomFactory = roomFactory;
		build();
	}

	/**
	 * Gives the furniture manager to the room manager.
	 * @param furnitureManager the furniture manager
	 */
	public void setFurnitureManager(FurnitureManager furnitureManager) {
		this.furnitureManager = furnitureManager;
	}

	private void build() {
		int lines = map.getLineCount();
		int columns = map.getColumnCount();
		int midLine = lines / 2;

		corridorColStart = columns / 2;
		corridorColEnd = columns / 2;
		corridorLight = new Light();

		int topMidLine = (midLine - 1) / 2;
		int bottomMidLine = (midLine + lines - 1) / 2;

		Block bedroomDoor = map.getBlock(topMidLine, corridorColStart - 1);
		Block bathroomDoor = map.getBlock(topMidLine, corridorColEnd + 1);
		Block livingRoomDoor = map.getBlock(bottomMidLine, corridorColStart - 1);
		Block kitchenDoor = map.getBlock(bottomMidLine, corridorColEnd + 1);

		bedroom = roomFactory.createBedRoom(0, 0, midLine, corridorColStart - 1, bedroomDoor);
		bathroom = roomFactory.createBathRoom(0, corridorColEnd + 1, midLine, columns - 1, bathroomDoor);
		livingRoom = roomFactory.createLivingRoom(midLine, 0, lines - 1, corridorColStart - 1, livingRoomDoor);
		kitchen = roomFactory.createKitchen(midLine, corridorColEnd + 1, lines - 1, columns - 1, kitchenDoor);

		int exitColumn = (corridorColStart + corridorColEnd) / 2;
		exit = map.getBlock(lines - 1, exitColumn);
		logger.info("Rooms built");
	}

	/**
	 * Tells if a block is blocked by a wall, border or furniture.
	 * @param block the tested block
	 * @return true if blocked
	 */
	public boolean isWall(Block block) {
		// Murs extérieurs (bord de map) → épaisseur 1, inchangé
		if (map.isOnBorder(block) && !block.equals(exit)) return true;

		// Murs intérieurs → épaisseur configurable
		for (Room room : getRooms()) {
			if (room.isWallWithThickness(block, SimulationConfiguration.WALL_THICKNESS)) {
				if (!map.isOnBorder(block)) return true; // on n'épaissit pas vers l'extérieur
			}
		}

		return false;


	}

	public boolean isWalkable(Block block) {
		return !isWall(block);
	}

	public boolean isCorridor(Block block) {
		return block.getColumn() >= corridorColStart && block.getColumn() <= corridorColEnd;
	}

	public boolean isDoor(Block block) {
		for (Room room : getRooms()) {
			if (room.isDoor(block)) {
				return true;
			}
		}
		return false;
	}

	public boolean isExit(Block block) {
		return block.equals(exit);
	}

	public Block getExit() {
		return exit;
	}

	public BedRoom getBedroom() {
		return bedroom;
	}

	public BathRoom getBathroom() {
		return bathroom;
	}

	public LivingRoom getLivingroom() {
		return livingRoom;
	}

	public Kitchen getKitchen() {
		return kitchen;
	}

	public int getCorridorColStart() {
		return corridorColStart;
	}

	public int getCorridorColEnd() {
		return corridorColEnd;
	}

	public Light getCorridorLight() {
		return corridorLight;
	}

	/**
	 * Returns all rooms.
	 * @return the four rooms
	 */
	public Room[] getRooms() {
		return new Room[] { bedroom, bathroom, livingRoom, kitchen };
	}

	/**
	 * Finds the room that contains the block.
	 * @param block the tested block
	 * @return the room or null
	 */
	public Room getRoomAt(Block block) {
		for (Room room : getRooms()) {
			if (room.isRoom(block)) {
				return room;
			}
		}
		return null;
	}
}
