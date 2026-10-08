package process.game;

import config.SimulationConfiguration;
import engine.item.device.Light;
import engine.item.device.Switch;
import engine.map.Block;
import engine.map.Map;
import engine.mobile.Master;
import log.LoggerUtility;
import org.apache.log4j.Logger;
import process.factory.DefaultFurnitureFactory;
import process.factory.FurnitureFactory;
import process.factory.MapFactory;
import process.furniture.FurnitureManager;
import process.mobile.MobileElementManager;
import process.mobile.MobileInterface;
import process.room.RoomManager;

/**
 * Builds the main objects of the game.
 * It keeps the setup code in one place.
 */
public class GameBuilder {

	private static Logger logger = LoggerUtility.getLogger(GameBuilder.class, "html");

	private static final MapFactory MAP_FACTORY = new MapFactory();
	private static final FurnitureFactory FURNITURE_FACTORY = new DefaultFurnitureFactory();

	/**
	 * Builds the map.
	 * @return the game map
	 */
	public static Map buildMap() {
		logger.info("Build map");
		return MAP_FACTORY.createDefault();
	}

	/**
	 * Builds the room manager.
	 * @param map the map used by the rooms
	 * @return the room manager
	 */
	public static RoomManager buildRoomManager(Map map) {
		logger.info("Build room manager");
		return new RoomManager(map);
	}

	/**
	 * Builds the mobile manager and the first mobile objects.
	 * @param map the game map
	 * @param roomManager the room manager
	 * @param furnitureManager the furniture manager
	 * @return the mobile manager
	 */
	public static MobileInterface buildInitMobile(Map map, RoomManager roomManager, FurnitureManager furnitureManager) {
		MobileInterface manager = new MobileElementManager(map, roomManager, furnitureManager);
		initializeMaster(map, manager);
		initializeSwitch(map, manager);
		return manager;
	}

	/**
	 * Places the master on the map.
	 * @param map the game map
	 * @param manager the mobile manager
	 */
	public static void initializeMaster(Map map, MobileInterface manager) {
		Block block = map.getBlock(5, 20);
		Master master = new Master(block);
		logger.info("Init master");
		manager.set(master);
	}

	/**
	 * Places the switch on the map.
	 * @param map the game map
	 * @param manager the mobile manager
	 */
	public static void initializeSwitch(Map map, MobileInterface manager) {
		Block block = map.getBlock((SimulationConfiguration.LINE_COUNT - 1) / 2 + 2,
				(SimulationConfiguration.COLUMN_COUNT - 1) - 2);
		Light light = FURNITURE_FACTORY.createLight();
		Switch interrupter = FURNITURE_FACTORY.createSwitch(map, block.getLine(), block.getColumn(), light);
		logger.info("Init switch");
		manager.set(interrupter);
	}

	/**
	 * Builds all furniture of the default game.
	 * @param map the game map
	 * @return the furniture manager
	 */
	public static FurnitureManager buildFurnitureManager(Map map) {
		logger.info("Build furniture manager");
		return FURNITURE_FACTORY.buildFurnitureManager(map);
	}
}
