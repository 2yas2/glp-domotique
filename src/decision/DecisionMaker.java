package decision;

import config.NeedConfiguration;
import config.RoutineConfiguration;
import engine.item.furniture.Furniture;
import engine.state.MasterState;
import log.LoggerUtility;
import org.apache.log4j.Logger;
import process.furniture.FurnitureManager;
import process.routine.RoutineManager;
import process.routine.RoutineStep;
import process.routine.SimulationClock;

/**
 * Chooses the next target of the master.
 * It checks the urgent needs before the routine.
 */
public class DecisionMaker {

	private static Logger logger = LoggerUtility.getLogger(DecisionMaker.class, "html");

	private MasterState masterState;
	private RoutineManager routineManager;
	private FurnitureManager furnitureManager;
	private SimulationClock clock;

	public DecisionMaker(MasterState masterState, RoutineManager routineManager,
			FurnitureManager furnitureManager, SimulationClock clock) {
		this.masterState = masterState;
		this.routineManager = routineManager;
		this.furnitureManager = furnitureManager;
		this.clock = clock;
	}

	public Decision decideNextAction() {
		int criticalNeed = masterState.getMostCriticalNeed();
		if (criticalNeed != -1) {
			logger.info("Decision from need " + criticalNeed);
			return new Decision(false, getFurnitureForNeed(criticalNeed));
		}

		RoutineStep currentStep = routineManager.getCurrentStep();
		boolean stepReady = currentStep != null
				&& routineManager.shouldStartCurrentStep(clock.getDayIndex(), clock.getHour(), clock.getMinute());
		if (stepReady) {
			logger.info("Decision from routine step");
			return new Decision(true, getRoutineTarget(currentStep));
		}

		return new Decision(false, null);
	}

	public Furniture getRoutineTarget(RoutineStep step) {
		if (step == null) {
			return null;
		}

		int action = step.getActionType();
		if (action == RoutineConfiguration.SHOWER) {
			return furnitureManager.getBathtub();
		}
		if (action == RoutineConfiguration.EAT) {
			return furnitureManager.getOven();
		}
		if (action == RoutineConfiguration.WATCH_TV) {
			return furnitureManager.getSofa();
		}
		if (action == RoutineConfiguration.SLEEP) {
			return furnitureManager.getBed();
		}
		if (action == RoutineConfiguration.WASH_HANDS) {
			return furnitureManager.getSink();
		}
		return null;
	}

	public Furniture getFurnitureForNeed(int needCode) {
		if (needCode == NeedConfiguration.HUNGER) {
			return furnitureManager.getOven();
		}
		if (needCode == NeedConfiguration.THIRST) {
			return furnitureManager.getWaterBottle();
		}
		if (needCode == NeedConfiguration.ENERGY) {
			return furnitureManager.getSofa();
		}
		if (needCode == NeedConfiguration.HYGIENE) {
			return furnitureManager.getBathtub();
		}
		return null;
	}
}
