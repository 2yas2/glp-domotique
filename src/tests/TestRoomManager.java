package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import config.SimulationConfiguration;
import engine.map.Block;
import engine.map.Map;
import process.room.RoomManager;

/**
 * Tests sur le RoomManager.
 * Vérifie les portes, la sortie, le couloir et les pièces.
 */
public class TestRoomManager {

	private Map map;
	private RoomManager roomManager;

	@Before
	public void prepareRoomManager() {
		map = new Map(SimulationConfiguration.LINE_COUNT, SimulationConfiguration.COLUMN_COUNT);
		roomManager = new RoomManager(map);
	}

	@Test
	public void testRoomManagerIsCreated() {
		assertNotNull(roomManager);
	}

	@Test
	public void testAllFourRoomsExist() {
		assertNotNull(roomManager.getBedroom());
		assertNotNull(roomManager.getBathroom());
		assertNotNull(roomManager.getLivingroom());
		assertNotNull(roomManager.getKitchen());
	}

	@Test
	public void testDoorIsNotAWall() {
		Block bedroomDoor = roomManager.getBedroom().getDoor();
		Block bathroomDoor = roomManager.getBathroom().getDoor();
		Block livingRoomDoor = roomManager.getLivingroom().getDoor();
		Block kitchenDoor = roomManager.getKitchen().getDoor();

		assertFalse(roomManager.isWall(bedroomDoor));
		assertFalse(roomManager.isWall(bathroomDoor));
		assertFalse(roomManager.isWall(livingRoomDoor));
		assertFalse(roomManager.isWall(kitchenDoor));
	}

	@Test
	public void testDoorIsRecognisedAsDoor() {
		Block bedroomDoor = roomManager.getBedroom().getDoor();
		assertTrue(roomManager.isDoor(bedroomDoor));
	}

	@Test
	public void testExitIsRecognised() {
		Block exit = roomManager.getExit();
		assertNotNull(exit);
		assertTrue(roomManager.isExit(exit));
	}

	@Test
	public void testExitIsNotAWall() {
		Block exit = roomManager.getExit();
		assertFalse(roomManager.isWall(exit));
	}

	@Test
	public void testCorridorIsNotAWall() {
		int corridorCol = roomManager.getCorridorColStart();
		int midLine = SimulationConfiguration.LINE_COUNT / 2;
		Block corridor = map.getBlock(midLine, corridorCol);

		assertTrue(roomManager.isCorridor(corridor));
		assertFalse(roomManager.isWall(corridor));
	}

	@Test
	public void testInsideRoomIsWalkable() {
		Block insideBedroom = map.getBlock(5, 5);
		assertFalse(roomManager.isWall(insideBedroom));
	}
}
