package process.factory;

import config.SimulationConfiguration;
import engine.map.Map;

/**
 * Small factory for the map.
 */
public class MapFactory {

	/**
	 * Builds a map with custom dimensions.
	 * @param lineCount number of lines
	 * @param columnCount number of columns
	 * @return a new map
	 */
	public Map create(int lineCount, int columnCount) {
		return new Map(lineCount, columnCount);
	}

	public Map createDefault() {
		return new Map(SimulationConfiguration.LINE_COUNT, SimulationConfiguration.COLUMN_COUNT);
	}
}
