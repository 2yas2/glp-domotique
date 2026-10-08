package process.imprevu;

import java.util.ArrayList;
import java.util.Random;

import config.ImprevuConfiguration;
import config.RoutineConfiguration;
import process.routine.RoutinePlan;
import process.routine.RoutineStep;

/**
 * Picks and applies daily imprevus.
 */
public class ImprevuManager {

	private ArrayList<Imprevu> available;
	private Random random;

	private Imprevu todayImprevu;
	private int lastEvaluatedDay;
	private boolean appliedToday;

	public ImprevuManager() {
		this(new Random());
	}

	public ImprevuManager(Random random) {
		this.random = random;
		this.available = buildCatalog();
		this.todayImprevu = null;
		this.lastEvaluatedDay = -1;
		this.appliedToday = false;
	}

	private ArrayList<Imprevu> buildCatalog() {
		ArrayList<Imprevu> list = new ArrayList<Imprevu>();
		list.add(new Imprevu(
				ImprevuConfiguration.OVERSLEEP,
				"Grasse matinée",
				"Le maître se réveille en retard, toute la routine du matin est décalée."));
		list.add(new Imprevu(
				ImprevuConfiguration.SURPRISE_VISIT,
				"Visite surprise",
				"Un ami passe à l'improviste, la pause télé du soir est rallongée."));
		list.add(new Imprevu(
				ImprevuConfiguration.POWER_OUTAGE,
				"Coupure de courant",
				"Plus d'électricité, les écrans sont coupés pour la journée."));
		list.add(new Imprevu(
				ImprevuConfiguration.SICK_DAY,
				"Journée malade",
				"Le maître ne va pas travailler aujourd'hui."));
		list.add(new Imprevu(
				ImprevuConfiguration.RUSH_DAY,
				"Journée pressée",
				"Toutes les activités sont écourtées."));
		return list;
	}

	/**
	 * Rolls the daily imprevu once for the given day.
	 * @param dayIndex current simulated day
	 * @return true if an imprevu exists for this day
	 */
	public boolean rollForImprevu(int dayIndex) {
		if (dayIndex == lastEvaluatedDay) {
			return todayImprevu != null;
		}
		lastEvaluatedDay = dayIndex;
		appliedToday = false;
		int x = random.nextInt(ImprevuConfiguration.PROBABILITY_DENOMINATOR) + 1;
		if (x == ImprevuConfiguration.TRIGGER_VALUE) {
			todayImprevu = available.get(random.nextInt(available.size()));
			return true;
		}
		todayImprevu = null;
		return false;
	}

	/**
	 * Forces one imprevu by its code.
	 * @param code the imprevu code
	 */
	public void selectImprevu(int code) {
		for (Imprevu imprevu : available) {
			if (imprevu.getCode() == code) {
				todayImprevu = imprevu;
				appliedToday = false;
				return;
			}
		}
	}

	/**
	 * Applies the current imprevu to the routine plan.
	 * @param plan the plan to change
	 * @return true if something changed
	 */
	public boolean applyToRoutine(RoutinePlan plan) {
		if (todayImprevu == null || plan == null || plan.isEmpty() || appliedToday) {
			return false;
		}

		switch (todayImprevu.getCode()) {
			case ImprevuConfiguration.OVERSLEEP:
				applyOversleep(plan);
				break;
			case ImprevuConfiguration.SURPRISE_VISIT:
				applySurpriseVisit(plan);
				break;
			case ImprevuConfiguration.POWER_OUTAGE:
				applyPowerOutage(plan);
				break;
			case ImprevuConfiguration.SICK_DAY:
				applySickDay(plan);
				break;
			case ImprevuConfiguration.RUSH_DAY:
				applyRushDay(plan);
				break;
			default:
				return false;
		}
		appliedToday = true;
		return true;
	}

	private void applyOversleep(RoutinePlan plan) {
		plan.shiftAllSteps(ImprevuConfiguration.OVERSLEEP_DELAY_MIN);
	}

	private void applySurpriseVisit(RoutinePlan plan) {
		// L'ami reste toute la journée, le maître se couche tard (vers minuit).
		for (int i = plan.size() - 1; i >= 0; i--) {
			RoutineStep step = plan.getStep(i);
			if (step != null && step.getActionType() == RoutineConfiguration.SLEEP) {
				plan.replaceStep(i, step.withTime(
						ImprevuConfiguration.SURPRISE_VISIT_SLEEP_HOUR,
						ImprevuConfiguration.SURPRISE_VISIT_SLEEP_MIN));
				return;
			}
		}
	}

	private void applyPowerOutage(RoutinePlan plan) {
		plan.removeStepsByAction(RoutineConfiguration.WATCH_TV);
	}

	private void applySickDay(RoutinePlan plan) {
		plan.removeStepsByAction(RoutineConfiguration.GO_TO_WORK);
		plan.removeStepsByAction(RoutineConfiguration.RETURN_HOME);
	}

	private void applyRushDay(RoutinePlan plan) {
		// First, halve all durations.
		for (int i = 0; i < plan.size(); i++) {
			RoutineStep step = plan.getStep(i);
			if (step == null) {
				continue;
			}
			int reduced = step.getDurationInMinutes()
					* ImprevuConfiguration.RUSH_DAY_DURATION_FACTOR_PERCENT / 100;
			plan.replaceStep(i, step.withDuration(reduced));
		}

		// Then ensure each step has enough room after the previous one.
		// Without this, a 1 minute wash-hands at 7:48 followed by an eat at 7:50
		// makes the master arrive late because he still has to walk.
		int buffer = ImprevuConfiguration.RUSH_DAY_WALK_BUFFER_MIN;
		for (int i = 1; i < plan.size(); i++) {
			RoutineStep previous = plan.getStep(i - 1);
			RoutineStep current = plan.getStep(i);
			if (previous == null || current == null) {
				continue;
			}
			int earliest = previous.getTotalMinutes() + previous.getDurationInMinutes() + buffer;
			if (earliest >= 24 * 60) {
				earliest = 24 * 60 - 1;
			}
			if (current.getTotalMinutes() < earliest) {
				plan.replaceStep(i, current.withTime(earliest / 60, earliest % 60));
			}
		}
	}

	public Imprevu getTodayImprevu() {
		return todayImprevu;
	}

	public boolean hasImprevu() {
		return todayImprevu != null;
	}

	public ArrayList<Imprevu> getAvailable() {
		return available;
	}

	public int getLastEvaluatedDay() {
		return lastEvaluatedDay;
	}

	/**
	 * Clears the current daily imprevu.
	 */
	public void clearToday() {
		todayImprevu = null;
		appliedToday = false;
	}

	public boolean isAppliedToday() {
		return appliedToday;
	}
}
