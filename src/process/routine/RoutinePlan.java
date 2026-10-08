package process.routine;

import java.util.ArrayList;

/**
 * Stores the list of routine steps for one day.
 */
public class RoutinePlan {

	private ArrayList<RoutineStep> steps;

	public RoutinePlan() {
		this.steps = new ArrayList<RoutineStep>();
	}

	/**
	 * Adds one step at the end of the plan.
	 * @param step the step to add
	 */
	public void addStep(RoutineStep step) {
		steps.add(step);
	}

	public RoutineStep getStep(int index) {
		if (index < 0 || index >= steps.size()) {
			return null;
		}
		return steps.get(index);
	}

	public boolean isEmpty() {
		return steps.isEmpty();
	}

	public int size() {
		return steps.size();
	}

	public void replaceStep(int index, RoutineStep step) {
		if (index < 0 || index >= steps.size() || step == null) {
			return;
		}
		steps.set(index, step);
	}

	/**
	 * Removes all steps of the same action type.
	 * @param actionType the action to remove
	 * @return number of removed steps
	 */
	public int removeStepsByAction(int actionType) {
		int removed = 0;
		ArrayList<RoutineStep> remaining = new ArrayList<RoutineStep>();
		for (RoutineStep step : steps) {
			if (step.getActionType() == actionType) {
				removed++;
			} else {
				remaining.add(step);
			}
		}
		steps = remaining;
		return removed;
	}

	public void shiftAllSteps(int minutes) {
		for (int i = 0; i < steps.size(); i++) {
			RoutineStep step = steps.get(i);
			int total = step.getTotalMinutes() + minutes;
			if (total < 0) {
				total = 0;
			}
			if (total >= 24 * 60) {
				total = 24 * 60 - 1;
			}
			steps.set(i, step.withTime(total / 60, total % 60));
		}
	}

	public ArrayList<RoutineStep> getSteps() {
		return new ArrayList<RoutineStep>(steps);
	}

	public RoutinePlan copy() {
		RoutinePlan clone = new RoutinePlan();
		for (RoutineStep step : steps) {
			clone.addStep(step);
		}
		return clone;
	}
}
