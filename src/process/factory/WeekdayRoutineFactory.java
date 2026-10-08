package process.factory;

import config.RoutineConfiguration;
import process.routine.RoutinePlan;
import process.routine.RoutineStep;

/**
 * Builds the routine used on week days.
 */
public class WeekdayRoutineFactory implements RoutineFactory {

	@Override
	public RoutinePlan create() {
		RoutinePlan plan = new RoutinePlan();

		plan.addStep(new RoutineStep(RoutineConfiguration.WAKE_UP, "Wake up", 7, 30, false, 0));
		plan.addStep(new RoutineStep(RoutineConfiguration.SHOWER, "Take a shower", 7, 35, false, 15));
		plan.addStep(new RoutineStep(RoutineConfiguration.WASH_HANDS, "Wash hands", 7, 48, false, 2));
		plan.addStep(new RoutineStep(RoutineConfiguration.EAT, "Breakfast", 7, 50, false, 20));
		plan.addStep(new RoutineStep(RoutineConfiguration.WATCH_TV, "Relax", 8, 10, false, 10));
		plan.addStep(new RoutineStep(RoutineConfiguration.GO_TO_WORK, "Leave for work", 8, 20, true, 0));

		plan.addStep(new RoutineStep(RoutineConfiguration.RETURN_HOME, "Back home", 12, 0, true, 0));
		plan.addStep(new RoutineStep(RoutineConfiguration.WASH_HANDS, "Wash hands", 12, 3, false, 2));
		plan.addStep(new RoutineStep(RoutineConfiguration.EAT, "Lunch", 12, 5, false, 30));
		plan.addStep(new RoutineStep(RoutineConfiguration.WATCH_TV, "Sofa break", 12, 35, false, 25));
		plan.addStep(new RoutineStep(RoutineConfiguration.GO_TO_WORK, "Back to work", 13, 0, true, 0));

		plan.addStep(new RoutineStep(RoutineConfiguration.RETURN_HOME, "Back home", 16, 30, true, 0));
		plan.addStep(new RoutineStep(RoutineConfiguration.SHOWER, "Take a shower", 16, 35, false, 15));
		plan.addStep(new RoutineStep(RoutineConfiguration.WASH_HANDS, "Wash hands", 16, 58, false, 2));
		plan.addStep(new RoutineStep(RoutineConfiguration.EAT, "Dinner", 17, 0, false, 30));
		plan.addStep(new RoutineStep(RoutineConfiguration.WATCH_TV, "Evening relax", 17, 35, false, 120));
		plan.addStep(new RoutineStep(RoutineConfiguration.SLEEP, "Sleep", 22, 30, false, 510));

		return plan;
	}
}
