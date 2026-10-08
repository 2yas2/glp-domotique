package process.factory;

import engine.map.BathRoom;
import engine.map.BedRoom;
import engine.map.Block;
import engine.map.Kitchen;
import engine.map.LivingRoom;

/**
 * Factory interface for the rooms.
 */
public interface RoomFactory {

	BedRoom createBedRoom(int startLine, int startColumn, int endLine, int endColumn, Block door);

	BathRoom createBathRoom(int startLine, int startColumn, int endLine, int endColumn, Block door);

	LivingRoom createLivingRoom(int startLine, int startColumn, int endLine, int endColumn, Block door);

	Kitchen createKitchen(int startLine, int startColumn, int endLine, int endColumn, Block door);
}
