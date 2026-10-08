package process.mobile;

import java.util.ArrayList;

import config.ImprevuConfiguration;
import config.RoutineConfiguration;
import decision.Decision;
import decision.DecisionMaker;
import engine.item.furniture.Bathtub;
import engine.item.furniture.Bed;
import engine.item.furniture.Furniture;
import engine.item.device.Light;
import engine.item.furniture.Oven;
import engine.item.furniture.Sink;
import engine.item.furniture.Sofa;
import engine.item.device.Switch;
import engine.item.furniture.Television;
import engine.item.furniture.WaterBottle;
import engine.item.visitor.AbstractFurnitureVisitor;
import engine.item.visitor.HouseInstructionVisitor;
import engine.map.Block;
import engine.map.Map;
import engine.map.Room;
import engine.mobile.Friend;
import engine.mobile.Master;
import engine.state.MasterState;
import log.LoggerUtility;
import org.apache.log4j.Logger;
import process.factory.NormalStateFactory;
import process.factory.RoutineManagerFactory;
import process.furniture.FurnitureManager;
import process.imprevu.Imprevu;
import process.imprevu.ImprevuManager;
import process.room.RoomManager;
import process.routine.RoutineManager;
import process.routine.RoutineStep;
import process.routine.SimulationClock;
import process.service.FurnitureInteractionService;
import process.service.MasterNavigationService;

/**
 * Manage the main loop of the simulation.
 */
public class MobileElementManager implements MobileInterface {

	private static Logger logger = LoggerUtility.getLogger(MobileElementManager.class, "html");

	private RoomManager roomManager;
	private FurnitureManager furnitureManager;
	private MasterState masterState;
	private SimulationClock clock;
	private RoutineManager routineManager;
	private ImprevuManager imprevuManager;

	private Master master;
	private Friend friend;
	private Light light;
	private Switch interrupter;

	private Furniture currentTarget;
	private Furniture currentFurniture;
	private Room currentRoom;
	private boolean inCorridor;

	private boolean inInteraction;
	private boolean routineInteraction;
	private boolean masterAtHome;
	private int currentRoutineStepStartRound;
	private int announcedImprevuDay;

	private ArrayList<String> notifications;

	private ArrayList<String> comfortDishes;
	private ArrayList<String> neutralDishes;
	private ArrayList<String> festiveDishes;
	private ArrayList<String> cheerfulShows;
	private ArrayList<String> calmShows;
	private ArrayList<String> excitingShows;

	private DecisionMaker decisionMaker;
	private FurnitureInteractionService furnitureInteractionService;
	private MasterNavigationService navigationService;

	public MobileElementManager(Map map, RoomManager roomManager, FurnitureManager furnitureManager) {
		this.roomManager = roomManager;
		this.furnitureManager = furnitureManager;
		this.masterState = new NormalStateFactory().create();
		this.clock = new SimulationClock();
		this.imprevuManager = new ImprevuManager();
		this.routineManager = RoutineManagerFactory.createDefault().build(imprevuManager);
		this.notifications = new ArrayList<String>();
		this.masterAtHome = true;
		this.currentRoutineStepStartRound = -1;
		this.announcedImprevuDay = -1;
		this.furnitureInteractionService = new FurnitureInteractionService();
		this.navigationService = new MasterNavigationService(map, roomManager, furnitureManager);
		this.decisionMaker = new DecisionMaker(masterState, routineManager, furnitureManager, clock);

		buildMoodLists();

		logger.info("Simulation started");
		addNotification("La maison démarre la simulation");
		evaluateDailyImprevu();
	}

	private void buildMoodLists() {
		comfortDishes = new ArrayList<String>();
		comfortDishes.add("Lasagne réconfortante");
		comfortDishes.add("Macaroni gratiné");
		comfortDishes.add("Tarte au chocolat");
		comfortDishes.add("Soupe maison");
		comfortDishes.add("Hachis parmentier");

		neutralDishes = new ArrayList<String>();
		neutralDishes.add("Pizza Margherita");
		neutralDishes.add("Pates au beurre");
		neutralDishes.add("Quiche lorraine");
		neutralDishes.add("Riz cantonais");
		neutralDishes.add("Omelette aux champignons");

		festiveDishes = new ArrayList<String>();
		festiveDishes.add("Filet mignon en croéte");
		festiveDishes.add("Risotto aux truffes");
		festiveDishes.add("Magret de canard");
		festiveDishes.add("Saumon en papillote");
		festiveDishes.add("Plateau de sushi");

		cheerfulShows = new ArrayList<String>();
		cheerfulShows.add("Friends");
		cheerfulShows.add("Brooklyn Nine-Nine");
		cheerfulShows.add("Kamelott");
		cheerfulShows.add("The Office");
		cheerfulShows.add("How I Met Your Mother");

		calmShows = new ArrayList<String>();
		calmShows.add("Documentaire animalier");
		calmShows.add("Météo du soir");
		calmShows.add("Top Chef");
		calmShows.add("Des chiffres et des lettres");
		calmShows.add("Reportage Arte");

		excitingShows = new ArrayList<String>();
		excitingShows.add("Stranger Things");
		excitingShows.add("Breaking Bad");
		excitingShows.add("Top Gear");
		excitingShows.add("Coupe du monde - finale");
		excitingShows.add("House of the Dragon");
	}

	@Override
	public void set(Master master) {
		this.master = master;

		Block startPosition = master.getPosition();
		currentRoom = roomManager.getRoomAt(startPosition);
		inCorridor = currentRoom == null && roomManager.isCorridor(startPosition);

		if (currentRoom != null && currentRoom.getLight() != null) {
			currentRoom.getLight().turnOn();
		} else if (inCorridor) {
			roomManager.getCorridorLight().turnOn();
		}
	}

	@Override
	public void set(Light light) {
		this.light = light;
	}

	@Override
	public void set(Switch interrupter) {
		this.interrupter = interrupter;
	}

	@Override
	public void nextRound() {
		clock.nextRound();
		masterState.updateStates();
		evaluateDailyImprevu();
		updateLight();
		preheatOvenIfNextEat();

		if (inInteraction && currentFurniture != null) {
			continueInteraction();
			return;
		}

		RoutineStep currentStep = routineManager.getCurrentStep();
		boolean stepReady = currentStep != null
				&& routineManager.shouldStartCurrentStep(clock.getDayIndex(), clock.getHour(), clock.getMinute());

		if (stepReady && isMandatoryRoutineStep(currentStep)) {
			if (handleWakeUpStep(currentStep) || handleReturnHomeStep(currentStep) || handleGoToWorkStep(currentStep)) {
				return;
			}

			handleFurnitureRoutineStep(currentStep);
			return;
		}

		if (masterAtHome) {
			Decision decision = decisionMaker.decideNextAction();
			Furniture targetFurniture = decision.getTargetFurniture();
			boolean fromRoutine = decision.isFromRoutine();

			if (!fromRoutine && targetFurniture != null) {
				logger.warn("Routine interrupted at " + clock.getFormattedTime()
						+ ", need=" + masterState.getMostCriticalNeed());
			}

			if (targetFurniture != null) {
				if (navigationService.isNearFurniture(master, targetFurniture)) {
					startInteraction(targetFurniture, fromRoutine);
				} else {
					Block nearestBlock = navigationService.getNearestFurnitureBlock(master, targetFurniture);
					Block waypoint = navigationService.getNextWaypoint(master, nearestBlock);
					moveCloser(waypoint);
				}
				return;
			}
		}

		if (!stepReady) {
			return;
		}

		if (handleWakeUpStep(currentStep) || handleReturnHomeStep(currentStep) || handleGoToWorkStep(currentStep)) {
			return;
		}

		handleFurnitureRoutineStep(currentStep);
	}

	private void evaluateDailyImprevu() {
		int dayIndex = clock.getDayIndex();
		boolean prepared = routineManager.evaluateDailyImprevu(dayIndex);

		if (!prepared) {
			updateFriend();
			return;
		}

		int routineDay = routineManager.getRoutineDayIndex();
		if (routineDay == announcedImprevuDay) {
			updateFriend();
			return;
		}

		Imprevu imprevu = imprevuManager.getTodayImprevu();
		if (imprevu != null) {
			announcedImprevuDay = routineDay;
			logger.info("Imprevu of the day: " + imprevu.getName());
			addNotification("Imprévu du jour : " + imprevu.getName() + " - " + imprevu.getDescription());
		}
		updateFriend();
	}

	private void updateFriend() {
		Imprevu imprevu = imprevuManager.getTodayImprevu();
		boolean watchingTv = currentFurniture instanceof Sofa;
		boolean visiting = imprevu != null
				&& imprevu.getCode() == ImprevuConfiguration.SURPRISE_VISIT
				&& watchingTv;

		if (visiting && friend == null) {
			Sofa sofa = furnitureManager.getSofa();
			if (sofa != null && !sofa.getBlocks().isEmpty()) {
				// Bloc bas-droit du canape (ligne max + colonne max)
				Block bottomRight = sofa.getBlocks().get(0);
				for (Block block : sofa.getBlocks()) {
					if (block.getLine() > bottomRight.getLine()
							|| (block.getLine() == bottomRight.getLine()
									&& block.getColumn() > bottomRight.getColumn())) {
						bottomRight = block;
					}
				}
				friend = new Friend(bottomRight);
				logger.info("Friend spawned on the sofa");
			}
		} else if (!visiting && friend != null) {
			friend = null;
			logger.info("Friend left the house");
		}
	}

	private boolean isPowerOutage() {
		Imprevu imprevu = imprevuManager.getTodayImprevu();
		return imprevu != null
				&& imprevu.getCode() == ImprevuConfiguration.POWER_OUTAGE;
	}

	private void startInteraction(Furniture furniture, boolean fromRoutine) {
		logger.info("Interaction start: " + furniture.getName() + " at " + clock.getFormattedTime());

		furnitureManager.setStandby();
		furniture.activate();

		currentFurniture = furniture;
		currentTarget = furniture;
		inInteraction = true;
		routineInteraction = fromRoutine;

		if (fromRoutine) {
			currentRoutineStepStartRound = clock.getTotalRounds();
		}

		applyMoodSelection(furniture);
		addNotification(houseInstructionFor(furniture));
		updateFriend();
	}

	private void applyMoodSelection(Furniture furniture) {
		furniture.accept(new AbstractFurnitureVisitor<Void>(null) {
			@Override
			public Void visit(Oven oven) {
				if (oven.isPreheating()) {
					oven.setPreheating(false);
					addNotification("Le plat est prêt : " + oven.getCurrentDish());
					return null;
				}
				String dish = selectDishForMood(masterState.getMood());
				oven.setCurrentDish(dish);
				addNotification("Aujourd'hui le maitre est " + masterState.getMoodLabel().toLowerCase()
						+ ", je lui cuisine " + dish);
				return null;
			}

			@Override
			public Void visit(Sofa sofa) {
				Television television = sofa.getTv();
				String show = selectShowForMood(masterState.getMood());
				television.setCurrentShow(show);
				addNotification("Le maitre est " + masterState.getMoodLabel().toLowerCase()
						+ ", je lance l'emission " + show);
				return null;
			}

			@Override
			public Void visit(Television television) {
				String show = selectShowForMood(masterState.getMood());
				television.setCurrentShow(show);
				return null;
			}
		});
	}

	private String selectDishForMood(int mood) {
		ArrayList<String> bucket;
		if (mood < 40) {
			bucket = comfortDishes;
		} else if (mood < 70) {
			bucket = neutralDishes;
		} else {
			bucket = festiveDishes;
		}

		int index = Math.abs(mood) % bucket.size();
		return bucket.get(index);
	}

	private String selectShowForMood(int mood) {
		ArrayList<String> bucket;
		if (mood < 40) {
			bucket = cheerfulShows;
		} else if (mood < 70) {
			bucket = calmShows;
		} else {
			bucket = excitingShows;
		}

		int index = Math.abs(mood) % bucket.size();
		return bucket.get(index);
	}

	private void continueInteraction() {
		if (currentFurniture == null) {
			inInteraction = false;
			return;
		}

		if (!routineInteraction) {
			furnitureInteractionService.tick(currentFurniture);
			if (!furnitureInteractionService.isDone(currentFurniture)) {
				if (currentFurniture instanceof Sofa && nextRoutineTargetIsOven()) {
					finishInteraction();
				}
				return;
			}
		} else if (!isRoutineDurationFinished(routineManager.getCurrentStep())) {
			// Si le maitre regarde la tele depuis longtemps et que le prochain
			// step de routine doit le mener au four, il se leve plus tot.
			if (currentFurniture instanceof Sofa && nextRoutineTargetIsOven()) {
				finishInteraction();
			}
			return;
		}

		finishInteraction();
	}

	private boolean nextRoutineTargetIsOven() {
		RoutineStep next = routineManager.getNextStep();
		return next != null && next.getActionType() == RoutineConfiguration.EAT;
	}

	private void finishInteraction() {
		logger.info("Interaction end: " + currentFurniture.getName() + " at " + clock.getFormattedTime());

		furnitureInteractionService.applyEffects(currentFurniture, masterState);
		currentFurniture.standby();
		furnitureInteractionService.reset(currentFurniture);
		currentFurniture = null;
		currentTarget = null;
		inInteraction = false;
		currentRoutineStepStartRound = -1;

		if (routineInteraction) {
			routineManager.goToNextStep();
		}

		routineInteraction = false;
		updateFriend();
	}

	private boolean isMandatoryRoutineStep(RoutineStep step) {
		if (step == null) {
			return false;
		}

		int action = step.getActionType();
		return action == RoutineConfiguration.SLEEP
				|| action == RoutineConfiguration.GO_TO_WORK
				|| action == RoutineConfiguration.RETURN_HOME;
	}

	private void handleFurnitureRoutineStep(RoutineStep step) {
		if (!isFurnitureRoutineStep(step)) {
			return;
		}

		Furniture routineTarget = decisionMaker.getRoutineTarget(step);
		if (routineTarget == null) {
			return;
		}

		currentTarget = routineTarget;

		if (navigationService.isNearFurniture(master, routineTarget)) {
			if (!inInteraction) {
				startInteraction(routineTarget, true);
			}
			return;
		}

		Block nearestBlock = navigationService.getNearestFurnitureBlock(master, routineTarget);
		Block waypoint = navigationService.getNextWaypoint(master, nearestBlock);
		moveCloser(waypoint);
	}

	private boolean handleWakeUpStep(RoutineStep step) {
		if (step == null || step.getActionType() != RoutineConfiguration.WAKE_UP) {
			return false;
		}

		addNotification("Le maitre doit se reveiller");
		routineManager.goToNextStep();
		return true;
	}

	private boolean handleGoToWorkStep(RoutineStep step) {
		if (step == null || step.getActionType() != RoutineConfiguration.GO_TO_WORK) {
			return false;
		}

		if (!masterAtHome) {
			routineManager.goToNextStep();
			return true;
		}

		Block exit = roomManager.getExit();
		if (navigationService.sameBlock(master.getPosition(), exit)) {
			masterAtHome = false;
			logger.info("Master goes to work");
			addNotification("Le maitre doit partir travailler");
			routineManager.goToNextStep();
			return true;
		}

		Block waypoint = navigationService.getNextWaypoint(master, exit);
		moveCloser(waypoint);
		return true;
	}

	private boolean handleReturnHomeStep(RoutineStep step) {
		if (step == null || step.getActionType() != RoutineConfiguration.RETURN_HOME) {
			return false;
		}

		if (masterAtHome) {
			routineManager.goToNextStep();
			return true;
		}

		master.setPosition(roomManager.getExit());
		masterAtHome = true;
		logger.info("Master comes back home");
		addNotification("Le maitre doit rentrer a la maison");
		routineManager.goToNextStep();
		return true;
	}

	private void updateLight() {
		Block position = master.getPosition();
		Room newRoom = roomManager.getRoomAt(position);
		boolean newInCorridor = newRoom == null && roomManager.isCorridor(position);
		boolean outage = isPowerOutage();

		if (outage) {
			// Force every light off for the whole day, even if the master moves around.
			for (Room room : roomManager.getRooms()) {
				if (room.getLight() != null) {
					room.getLight().turnOff();
				}
			}
			roomManager.getCorridorLight().turnOff();
			currentRoom = newRoom;
			inCorridor = newInCorridor;
			return;
		}

		if (newRoom != null && newRoom != currentRoom) {
			if (currentRoom != null) {
				currentRoom.getLight().turnOff();
			} else if (inCorridor) {
				roomManager.getCorridorLight().turnOff();
			}

			newRoom.getLight().turnOn();
			currentRoom = newRoom;
			inCorridor = false;
			return;
		}

		if (newInCorridor != inCorridor) {
			if (currentRoom != null) {
				currentRoom.getLight().turnOff();
			} else if (inCorridor) {
				roomManager.getCorridorLight().turnOff();
			}

			if (newInCorridor) {
				roomManager.getCorridorLight().turnOn();
			}
			currentRoom = null;
			inCorridor = newInCorridor;
		}
	}

	private boolean isRoutineDurationFinished(RoutineStep step) {
		if (step == null || currentRoutineStepStartRound < 0) {
			return false;
		}

		int elapsedRounds = clock.getTotalRounds() - currentRoutineStepStartRound;
		return elapsedRounds >= step.getDurationInMinutes();
	}

	private void preheatOvenIfNextEat() {
		Oven oven = furnitureManager.getOven();
		if (oven == null || oven.isPreheating()) {
			return;
		}
		RoutineStep currentStep = routineManager.getCurrentStep();
		if (currentStep != null && currentStep.getActionType() == RoutineConfiguration.EAT) {
			return;
		}
		RoutineStep nextStep = routineManager.getNextStep();
		if (nextStep == null || nextStep.getActionType() != RoutineConfiguration.EAT) {
			return;
		}
		String dish = selectDishForMood(masterState.getMood());
		oven.setCurrentDish(dish);
		oven.setPreheating(true);
		addNotification("Je prépare " + dish);
	}

	private boolean isFurnitureRoutineStep(RoutineStep step) {
		if (step == null) {
			return false;
		}

		int action = step.getActionType();
		return action == RoutineConfiguration.SHOWER
				|| action == RoutineConfiguration.EAT
				|| action == RoutineConfiguration.WATCH_TV
				|| action == RoutineConfiguration.SLEEP
				|| action == RoutineConfiguration.WASH_HANDS;
	}

	public void addNotification(String message) {
		notifications.add("[ " + clock.getFormattedTime() + " ] " + message);
		if (notifications.size() > 10) {
			notifications.remove(0);
		}
	}

	private String houseInstructionFor(Furniture furniture) {
		return furniture.accept(new HouseInstructionVisitor(furniture));
	}

	private String dayTypeLabel(int dayType) {
		if (dayType == RoutineConfiguration.WEEKEND) {
			return "Week-end";
		}
		return "Semaine";
	}

	public void resetNotifications() {
		notifications = new ArrayList<String>();
	}

	@Override
	public boolean isNear(Block first, Block second) {
		return navigationService.isNear(first, second);
	}

	@Override
	public void moveCloser(Block target) {
		navigationService.moveCloser(master, target);
	}

	@Override
	public MasterState getMasterState() {
		return masterState;
	}

	@Override
	public Master getMaster() {
		return master;
	}

	@Override
	public Switch getSwitch() {
		return interrupter;
	}

	@Override
	public Light getLight() {
		return light;
	}

	@Override
	public SimulationClock getClock() {
		return clock;
	}

	@Override
	public String getNotifications() {
		return String.join("\n", notifications);
	}

	@Override
	public String getRoutineStatus() {
		// kept for backward compatibility / logging; the GUI now uses getRoutineSummary,
		// getDayTypeLabel and getImprevuLabel separately.
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Jour : ").append(getDayTypeLabel()).append("\n");

		Imprevu imprevu = imprevuManager.getTodayImprevu();
		if (imprevu != null) {
			stringBuilder.append("Imprévu : ").append(imprevu.getName()).append("\n");
		} else {
			stringBuilder.append("Imprévu : aucun\n");
		}

		stringBuilder.append(getRoutineSummary());
		return stringBuilder.toString();
	}

	@Override
	public String getRoutineSummary() {
		StringBuilder stringBuilder = new StringBuilder();

		RoutineStep currentStep = routineManager.getCurrentStep();
		if (currentStep != null) {
			stringBuilder.append("Étape actuelle : ")
					.append(currentStep.getLabel())
					.append(" (")
					.append(String.format("%02d:%02d", currentStep.getHour(), currentStep.getMinute()))
					.append(")");
		} else {
			stringBuilder.append("Étape actuelle : aucune");
		}

		RoutineStep nextStep = routineManager.getNextStep();
		if (nextStep != null) {
			stringBuilder.append("\nProchaine étape : ")
					.append(nextStep.getLabel())
					.append(" (")
					.append(String.format("%02d:%02d", nextStep.getHour(), nextStep.getMinute()))
					.append(")");
		} else {
			stringBuilder.append("\nProchaine étape : aucune");
		}

		return stringBuilder.toString();
	}

	@Override
	public String getDayTypeLabel() {
		return dayTypeLabel(routineManager.getCurrentDayType());
	}

	@Override
	public String getImprevuLabel() {
		Imprevu imprevu = imprevuManager.getTodayImprevu();
		if (imprevu == null) {
			return "Aucun imprévu";
		}
		return imprevu.getName() + " : " + imprevu.getDescription();
	}

	@Override
	public boolean hasImprevu() {
		return imprevuManager.getTodayImprevu() != null;
	}

	@Override
	public boolean isAtHome() {
		return masterAtHome;
	}

	public ImprevuManager getImprevuManager() {
		return imprevuManager;
	}

	public RoutineManager getRoutineManager() {
		return routineManager;
	}

	public Furniture getCurrentTarget() {
		return currentTarget;
	}

	@Override
	public Furniture getCurrentFurniture() {
		return currentFurniture;
	}

	@Override
	public Friend getFriend() {
		return friend;
	}
}
