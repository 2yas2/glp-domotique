package process.routine;

import java.util.Random;

import config.RoutineConfiguration;
import config.SimulationConfiguration;
import log.LoggerUtility;
import org.apache.log4j.Logger;
import process.factory.WeekdayRoutineFactory;
import process.factory.WeekendRoutineFactory;
import process.imprevu.ImprevuManager;

/**
 * Keeps the current daily routine.
 * It also applies the daily imprevu when needed.
 */
public class RoutineManager {

	private static Logger logger = LoggerUtility.getLogger(RoutineManager.class, "html");

	private RoutinePlan weekdayTemplate;
	private RoutinePlan weekendTemplate;

	private RoutinePlan weekdayPlan;
	private RoutinePlan weekendPlan;

	private int currentDayType;
	private int currentStepIndex;
	private int routineDayIndex;

	private ImprevuManager imprevuManager;
	private int lastEvaluatedDay;
	private int lastEvaluatedRoutineDay;

	public RoutineManager() {
		this(null, null, null);
	}

	public RoutineManager(ImprevuManager imprevuManager) {
		this(null, null, imprevuManager);
	}

	public RoutineManager(RoutinePlan weekdayTemplate, RoutinePlan weekendTemplate, ImprevuManager imprevuManager) {
		if (weekdayTemplate == null) {
			weekdayTemplate = new WeekdayRoutineFactory().create();
		}
		if (weekendTemplate == null) {
			weekendTemplate = new WeekendRoutineFactory().create();
		}

		this.weekdayTemplate = weekdayTemplate;
		this.weekendTemplate = weekendTemplate;
		this.weekdayPlan = weekdayTemplate.copy();
		this.weekendPlan = weekendTemplate.copy();
		this.currentDayType = RoutineConfiguration.WEEKDAY;
		this.currentStepIndex = 0;
		this.routineDayIndex = 0;
		this.imprevuManager = imprevuManager;
		this.lastEvaluatedDay = -1;
		this.lastEvaluatedRoutineDay = -1;
		logger.info("Routine manager ready");
	}

	/**
	 * Replaces the imprevu manager.
	 * @param imprevuManager the new imprevu manager
	 */
	public void setImprevuManager(ImprevuManager imprevuManager) {
		this.imprevuManager = imprevuManager;
	}

	public ImprevuManager getImprevuManager() {
		return imprevuManager;
	}

	public boolean evaluateDailyImprevu(int dayIndex) {
		// We key on routineDayIndex so plans are not reset in the middle of a sleep
		// step that crosses real midnight (which used to wipe imprevu durations and
		// wake the master at random hours like 02:15).
		if (routineDayIndex == lastEvaluatedRoutineDay) {
			return imprevuManager != null && imprevuManager.hasImprevu();
		}

		lastEvaluatedDay = dayIndex;
		lastEvaluatedRoutineDay = routineDayIndex;
		weekdayPlan = weekdayTemplate.copy();
		weekendPlan = weekendTemplate.copy();

		if (imprevuManager == null) {
			return false;
		}

		if (imprevuManager.getLastEvaluatedDay() != routineDayIndex) {
			imprevuManager.clearToday();
		}

		boolean has = imprevuManager.rollForImprevu(routineDayIndex);
		if (has) {
			logger.info("Imprevu applied for routine day " + routineDayIndex);
			imprevuManager.applyToRoutine(getCurrentPlan());
		}
		return has;
	}


	public RoutinePlan getCurrentPlan() {
		if (currentDayType == RoutineConfiguration.WEEKEND) {
			return weekendPlan;
		}
		return weekdayPlan;
	}

	public RoutineStep getCurrentStep() {
		return getCurrentPlan().getStep(currentStepIndex);
	}

	public int getCurrentStepIndex() {
		return currentStepIndex;
	}

	public void goToNextStep() {
		RoutinePlan plan = getCurrentPlan();
		if (plan.isEmpty()) {
			return;
		}

		currentStepIndex++;
		if (currentStepIndex >= plan.size()) {
			currentStepIndex = 0;
			routineDayIndex++;
			currentDayType = dayTypeFor(routineDayIndex);
			logger.info("New routine day: " + routineDayIndex);
		}
	}

	private int dayTypeFor(int dayIndex) {
		int cycleLength = SimulationConfiguration.DAYS_PER_WEEK + SimulationConfiguration.DAYS_PER_WEEKEND;
		if (cycleLength <= 0) {
			return RoutineConfiguration.WEEKDAY;
		}
		int position = dayIndex % cycleLength;
		if (position < SimulationConfiguration.DAYS_PER_WEEK) {
			return RoutineConfiguration.WEEKDAY;
		}
		return RoutineConfiguration.WEEKEND;
	}

	public void reset() {
		currentDayType = RoutineConfiguration.WEEKDAY;
		currentStepIndex = 0;
		routineDayIndex = 0;
		lastEvaluatedDay = -1;
		lastEvaluatedRoutineDay = -1;
		weekdayPlan = weekdayTemplate.copy();
		weekendPlan = weekendTemplate.copy();
		if (imprevuManager != null) {
			imprevuManager.clearToday();
		}
		logger.info("Routine reset");
	}

	public void setCurrentDayType(int currentDayType) {
		this.currentDayType = currentDayType;
		this.currentStepIndex = 0;
	}

	public int getCurrentDayType() {
		return currentDayType;
	}

	public int getRoutineDayIndex() {
		return routineDayIndex;
	}

	public boolean shouldStartCurrentStep(int dayIndex, int hour, int minute) {
		RoutineStep step = getCurrentStep();
		if (step == null) {
			return false;
		}

		int currentTotalMinutes = dayIndex * 24 * 60 + hour * 60 + minute;
		int stepTotalMinutes = routineDayIndex * 24 * 60 + step.getHour() * 60 + step.getMinute();
		return currentTotalMinutes >= stepTotalMinutes;
	}

	public RoutineStep getNextStep() {
		RoutinePlan plan = getCurrentPlan();
		if (plan.isEmpty()) {
			return null;
		}

		int nextIndex = currentStepIndex + 1;
		if (nextIndex >= plan.size()) {
			nextIndex = 0;
		}
		return plan.getStep(nextIndex);
	}
}
