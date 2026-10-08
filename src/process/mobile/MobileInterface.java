package process.mobile;

import engine.item.device.Light;
import engine.item.device.Switch;
import engine.item.furniture.Furniture;
import engine.map.Block;
import engine.mobile.Friend;
import engine.mobile.Master;
import engine.state.MasterState;
import process.routine.SimulationClock;

/**
 * Small interface used by the gui and builders.
 */
public interface MobileInterface {

	void set(Master master);

	void set(Switch interrupter);

	void set(Light light);

	void nextRound();

	boolean isNear(Block position1, Block position2);

	void moveCloser(Block target);

	MasterState getMasterState();

	Master getMaster();

	Switch getSwitch();

	Light getLight();

	SimulationClock getClock();

	String getNotifications();

	String getRoutineStatus();

	String getRoutineSummary();

	String getDayTypeLabel();

	String getImprevuLabel();

	boolean hasImprevu();

	boolean isAtHome();

	Furniture getCurrentFurniture();

	Friend getFriend();
}
