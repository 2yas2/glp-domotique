package engine.state;

import config.NeedConfiguration;
import log.LoggerUtility;
import org.apache.log4j.Logger;

/**
 * Groups all need states of the master.
 */
public class MasterState {

	private static Logger logger = LoggerUtility.getLogger(MasterState.class, "html");

	private Energy energy = new Energy();
	private Hunger hunger = new Hunger();
	private Thirst thirst = new Thirst();
	private Hygiene hygiene = new Hygiene();

	public MasterState() {
	}

	public void updateStates() {
		energy.updateState();
		hunger.updateState();
		thirst.updateState();
		hygiene.updateState();

		if (thirst.isCritical())
			logger.warn("THIRST critical : " + thirst.getValue() + "%");
		if (hunger.isCritical())
			logger.warn("HUNGER critical : " + hunger.getValue() + "%");
		if (energy.isCritical())
			logger.warn("ENERGY critical : " + energy.getValue() + "%");
		if (hygiene.isCritical())
			logger.warn("HYGIENE critical : " + hygiene.getValue() + "%");
	}

	public int getMood() {
		int e = applyPenalty(energy.getValue());
		int f = applyPenalty(hunger.getValue());
		int s = applyPenalty(thirst.getValue());
		int h = applyPenalty(hygiene.getValue());

		return (int) (e * 0.28 + f * 0.25 + s * 0.30 + h * 0.17);
	}

	public String getMoodLabel() {
		int mood = getMood();
		if (mood < 20) return "Very unhappy";
		if (mood < 40) return "Uneasy";
		if (mood < 60) return "Neutral";
		if (mood < 80) return "Happy";
		return "Excellent";
	}

	private int applyPenalty(int value) {
		if (value < 30) {
			return (int) (value * 0.4);
		}
		return value;
	}

	public int getMostCriticalNeed() {
		if (thirst.isCritical()) return NeedConfiguration.THIRST;
		if (hunger.isCritical()) return NeedConfiguration.HUNGER;
		if (hygiene.isCritical()) return NeedConfiguration.HYGIENE;
		if (energy.isCritical()) return NeedConfiguration.ENERGY;
		return -1;
	}

	public Energy getEnergy() {
		return energy;
	}

	public Hunger getHunger() {
		return hunger;
	}

	public Thirst getThirst() {
		return thirst;
	}

	public Hygiene getHygiene() {
		return hygiene;
	}
}
