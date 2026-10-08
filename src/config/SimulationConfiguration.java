package config;

/**
 * Main constants of the simulation.
 * Sizes, speed and a few direction values are stored here.
 */
public class SimulationConfiguration {

	public static final int BLOCK_SIZE = 32;

	public static final int COLUMN_COUNT = 38;
	public static final int LINE_COUNT = 22;

	public static final int WINDOW_WIDTH = COLUMN_COUNT * BLOCK_SIZE;
	public static final int WINDOW_HEIGHT = LINE_COUNT * BLOCK_SIZE;

	public static final int SIDEBAR_WIDTH = 260;
	public static final int BOTTOM_PANEL_HEIGHT = 180;


	public static final int GAME_SPEED = 500;
	public static final int UPDATE_STATE_SPEED = 30;

	public static final int FPS = 144;
	public static final int FRAME_DURATION = 1000 / FPS;

	public static final int WALK_ANIMATION_FRAME_MS = 120;

	public static final int DAYS_PER_WEEK = 1;
	public static final int DAYS_PER_WEEKEND = 1;

	public static final int DIRECTION_DOWN = 2;
	public static final int DIRECTION_UP = -2;
	public static final int DIRECTION_LEFT = -1;
	public static final int DIRECTION_RIGHT = 1;

	public static final int WALL_THICKNESS = 3;
}
