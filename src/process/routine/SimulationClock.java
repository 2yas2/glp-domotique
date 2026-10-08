package process.routine;

/**
 * Small clock used by the simulation.
 * One round means one minute in game.
 */
public class SimulationClock {

	private int totalRounds = 0;
	private int hour = 7;
	private int minute = 30;
	private int dayIndex = 0;

	/**
	 * Moves the clock by one round.
	 */
	public void nextRound() {
		totalRounds++;
		minute++;

		if (minute >= 60) {
			minute = 0;
			hour++;
		}

		if (hour >= 24) {
			hour = 0;
			dayIndex++;
		}
	}

	public int getTotalRounds() {
		return totalRounds;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public String getFormattedTime() {
		return String.format("%02d:%02d", hour, minute);
	}

	/**
	 * Puts the clock back to the start.
	 */
	public void reset() {
		totalRounds = 0;
		hour = 7;
		minute = 30;
		dayIndex = 0;
	}

	public int getDayIndex() {
		return dayIndex;
	}
}
