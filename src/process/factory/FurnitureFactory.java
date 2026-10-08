package process.factory;

import engine.item.device.Light;
import engine.item.device.Switch;
import engine.map.Map;
import process.furniture.FurnitureManager;

/**
 * Factory interface for the furniture setup.
 */
public interface FurnitureFactory {

	Light createLight();

	Switch createSwitch(Map map, int line, int column, Light light);

	FurnitureManager buildFurnitureManager(Map map);
}
