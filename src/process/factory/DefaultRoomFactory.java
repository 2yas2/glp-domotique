package process.factory;

import engine.item.device.Light;
import engine.map.BathRoom;
import engine.map.BedRoom;
import engine.map.Block;
import engine.map.Kitchen;
import engine.map.LivingRoom;

/**
 * Default factory used to build the rooms with lights.
 */
public class DefaultRoomFactory implements RoomFactory {

	@Override
	public BedRoom createBedRoom(int startLine, int startColumn, int endLine, int endColumn, Block door) {
		BedRoom room = new BedRoom(startLine, startColumn, endLine, endColumn, door);
		room.setLight(new Light());
		return room;
	}

	@Override
	public BathRoom createBathRoom(int startLine, int startColumn, int endLine, int endColumn, Block door) {
		BathRoom room = new BathRoom(startLine, startColumn, endLine, endColumn, door);
		room.setLight(new Light());
		return room;
	}

	@Override
	public LivingRoom createLivingRoom(int startLine, int startColumn, int endLine, int endColumn, Block door) {
		LivingRoom room = new LivingRoom(startLine, startColumn, endLine, endColumn, door);
		room.setLight(new Light());
		return room;
	}

	@Override
	public Kitchen createKitchen(int startLine, int startColumn, int endLine, int endColumn, Block door) {
		Kitchen room = new Kitchen(startLine, startColumn, endLine, endColumn, door);
		room.setLight(new Light());
		return room;
	}
}
