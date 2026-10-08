package process.service;

import engine.item.furniture.Furniture;
import engine.map.Block;
import engine.map.Map;
import engine.map.Room;
import engine.mobile.Master;
import process.furniture.FurnitureManager;
import process.room.RoomManager;

/**
 * Small service for path and distance helpers.
 * It keeps movement code out of the main manager.
 */
public class MasterNavigationService {

	private Map map;
	private RoomManager roomManager;
	private FurnitureManager furnitureManager;

	public MasterNavigationService(Map map, RoomManager roomManager, FurnitureManager furnitureManager) {
		this.map = map;
		this.roomManager = roomManager;
		this.furnitureManager = furnitureManager;
	}

	/**
	 * Moves the master one step closer to the target.
	 * @param master the moving master
	 * @param target the target block
	 */
	public void moveCloser(Master master, Block target) {
		Block current = master.getPosition();

		if (current.getColumn() != target.getColumn()) {
			int nextColumn = current.getColumn() + (current.getColumn() < target.getColumn() ? 1 : -1);
			Block next = map.getBlock(current.getLine(), nextColumn);
			if (canStepOn(next)) {
				master.setPosition(next);
				return;
			}
		}

		if (current.getLine() != target.getLine()) {
			int nextLine = current.getLine() + (current.getLine() < target.getLine() ? 1 : -1);
			Block next = map.getBlock(nextLine, current.getColumn());
			if (canStepOn(next)) {
				master.setPosition(next);
			}
		}
	}

	private boolean canStepOn(Block block) {
		if (roomManager.isWall(block)) {
			return false;
		}
		if (furnitureManager != null && furnitureManager.isOccupied(block)) {
			return false;
		}
		return true;
	}

	public Block getNextWaypoint(Master master, Block target) {
		Block current = master.getPosition();
		Room masterRoom = getRoomOf(current);
		Room targetRoom = getRoomOf(target);

		if (masterRoom != null) {
			if (masterRoom == targetRoom) {
				return target;
			}
			if (roomManager.isDoor(current)) {
				int corridorColumn = roomManager.getCorridorColStart();
				return map.getBlock(current.getLine(), corridorColumn);
			}
			return masterRoom.getDoor();
		}

		if (targetRoom != null) {
			Block door = targetRoom.getDoor();
			if (current.getLine() == door.getLine()) {
				return door;
			}
			return map.getBlock(door.getLine(), current.getColumn());
		}

		return target;
	}

	public boolean isNearFurniture(Master master, Furniture furniture) {
		Block masterPosition = master.getPosition();
		for (Block block : furniture.getBlocks()) {
			if (isNear(masterPosition, block)) {
				return true;
			}
		}
		return false;
	}

	public Block getNearestFurnitureBlock(Master master, Furniture furniture) {
		Block masterPosition = master.getPosition();
		Block closest = furniture.getBlocks().get(0);
		int minimumDistance = distance(masterPosition, closest);

		for (Block block : furniture.getBlocks()) {
			int currentDistance = distance(masterPosition, block);
			if (currentDistance < minimumDistance) {
				minimumDistance = currentDistance;
				closest = block;
			}
		}
		return closest;
	}

	public boolean isNear(Block first, Block second) {
		int absoluteColumn = Math.abs(first.getColumn() - second.getColumn());
		int absoluteLine = Math.abs(first.getLine() - second.getLine());
		return absoluteColumn <= 1 && absoluteLine <= 1;
	}

	public boolean sameBlock(Block first, Block second) {
		return first.getLine() == second.getLine() && first.getColumn() == second.getColumn();
	}

	public int distance(Block first, Block second) {
		return Math.abs(first.getLine() - second.getLine())
				+ Math.abs(first.getColumn() - second.getColumn());
	}

	private Room getRoomOf(Block block) {
		for (Room room : roomManager.getRooms()) {
			if (room.isRoom(block)) {
				return room;
			}
		}
		return null;
	}
}
