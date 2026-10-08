package process.routine;

/**
 * One step of the daily routine.
 * It keeps the time, label and action code.
 */
public class RoutineStep {

	private int actionType;
	private String label;
	private int hour;
	private int minute;
	private boolean timeSkip;
	private int durationInMinutes;

	/**
	 * Builds one routine step.
	 * @param actionType the action id
	 * @param label the small label shown to the user
	 * @param hour start hour
	 * @param minute start minute
	 * @param timeSkip true if the action jumps in time
	 * @param durationInMinutes duration of the step
	 */
	public RoutineStep(int actionType, String label, int hour, int minute, boolean timeSkip, int durationInMinutes) {
		this.actionType = actionType;
		this.label = label;
		this.hour = hour;
		this.minute = minute;
		this.timeSkip = timeSkip;
		this.durationInMinutes = durationInMinutes;
	}

	public int getActionType() {
		return actionType;
	}

	public String getLabel() {
		return label;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public boolean isTimeSkip() {
		return timeSkip;
	}

	public int getDurationInMinutes() {
		return durationInMinutes;
	}

	public boolean matchesTime(int currentHour, int currentMinute) {
		return hour == currentHour && minute == currentMinute;
	}

	/**
	 * Returns a copy with another duration.
	 * @param newDuration new duration in minutes
	 * @return a copied step
	 */
	public RoutineStep withDuration(int newDuration) {
		int safe = newDuration < 0 ? 0 : newDuration;
		return new RoutineStep(actionType, label, hour, minute, timeSkip, safe);
	}

	public RoutineStep withTime(int newHour, int newMinute) {
		return new RoutineStep(actionType, label, newHour, newMinute, timeSkip, durationInMinutes);
	}

	public int getTotalMinutes() {
		return hour * 60 + minute;
	}
}
