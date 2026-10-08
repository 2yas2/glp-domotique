package process.factory;

import process.imprevu.ImprevuManager;
import process.routine.RoutineManager;

/**
 * Small factory used to build a ready routine manager.
 */
public class RoutineManagerFactory {

	private RoutineFactory weekdayFactory;
	private RoutineFactory weekendFactory;

	public RoutineManagerFactory(RoutineFactory weekdayFactory, RoutineFactory weekendFactory) {
		this.weekdayFactory = weekdayFactory;
		this.weekendFactory = weekendFactory;
	}

	public static RoutineManagerFactory createDefault() {
		return new RoutineManagerFactory(new WeekdayRoutineFactory(), new WeekendRoutineFactory());
	}

	public RoutineManager build() {
		return build(null);
	}

	public RoutineManager build(ImprevuManager imprevuManager) {
		return new RoutineManager(weekdayFactory.create(), weekendFactory.create(), imprevuManager);
	}
}
