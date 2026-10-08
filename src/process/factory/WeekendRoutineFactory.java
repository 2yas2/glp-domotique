package process.factory;

import config.RoutineConfiguration;
import process.routine.RoutinePlan;
import process.routine.RoutineStep;

/**
 * Builds the routine used on week-ends.
 */
public class WeekendRoutineFactory implements RoutineFactory {

	@Override
	public RoutinePlan create() {
		RoutinePlan plan = new RoutinePlan();

		plan.addStep(new RoutineStep(RoutineConfiguration.WAKE_UP, "Wake up", 9, 0, false, 0));
		plan.addStep(new RoutineStep(RoutineConfiguration.SHOWER, "Take a shower", 9, 10, false, 20));
		plan.addStep(new RoutineStep(RoutineConfiguration.WASH_HANDS, "Wash hands", 9, 32, false, 2));
		plan.addStep(new RoutineStep(RoutineConfiguration.EAT, "Breakfast", 9, 35, false, 25));
		plan.addStep(new RoutineStep(RoutineConfiguration.WATCH_TV, "Watch TV", 10, 5, false, 80));
		plan.addStep(new RoutineStep(RoutineConfiguration.WASH_HANDS, "Wash hands", 12, 27, false, 2));
		plan.addStep(new RoutineStep(RoutineConfiguration.EAT, "Lunch", 12, 30, false, 35));
		plan.addStep(new RoutineStep(RoutineConfiguration.WATCH_TV, "Living-room break", 14, 0, false, 120));
		plan.addStep(new RoutineStep(RoutineConfiguration.SHOWER, "Take a shower", 18, 0, false, 20));
		plan.addStep(new RoutineStep(RoutineConfiguration.WASH_HANDS, "Wash hands", 18, 57, false, 2));
		plan.addStep(new RoutineStep(RoutineConfiguration.EAT, "Dinner", 19, 0, false, 30));
		plan.addStep(new RoutineStep(RoutineConfiguration.SLEEP, "Sleep", 23, 0, false, 600));

		return plan;
	}
}
