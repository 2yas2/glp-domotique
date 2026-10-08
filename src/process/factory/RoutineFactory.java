package process.factory;

import process.routine.RoutinePlan;

/**
 * Factory interface for one routine plan.
 */
public interface RoutineFactory {

	RoutinePlan create();
}
