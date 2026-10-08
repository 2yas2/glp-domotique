package engine.state;

import config.SimulationConfiguration;

/**
 * Thirst bar of the master.
 */
public class Thirst {

	private static final int DECREASE = 10;
	private static final int CEILING = 100;
	private int value = 80;
	private int counter = 0;

	public void updateState() {
		counter++;
		if (counter >= SimulationConfiguration.UPDATE_STATE_SPEED) {
			tick();
			counter = 0;
		}
	}

	public void tick() {
		value = value - DECREASE;
		if (value < 0) {
			value = 0;
		}
	}

	public void increase(int amount) {
		value = value + amount;
		if (value > CEILING) {
			value = CEILING;
		}
	}

	public boolean isCritical() {
		return value < 20;
	}

	public boolean isUrgent() {
		return value < 35 && value >= 20;
	}

	public int getValue() {
		return value;
	}

	public String getLabel() {
		return "Thirst";
	}
}
