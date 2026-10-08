package gui;

import config.SimulationConfiguration;
import engine.map.Map;
import log.LoggerUtility;
import org.apache.log4j.Logger;
import process.furniture.FurnitureManager;
import process.game.GameBuilder;
import process.mobile.MobileInterface;
import process.room.RoomManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

/**
 * Fenetre principale du jeu.
 */
public class MainGUI extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerUtility.getLogger(MainGUI.class, "html");

	private static final int SIDEBAR_WIDTH = 260;
	private static final int BOTTOM_PANEL_HEIGHT = 200;

	private static final Font font = new Font(Font.MONOSPACED, Font.BOLD, 18);

	private Map map;
	private RoomManager roomManager;
	private FurnitureManager furnitureManager;
	private MobileInterface manager;

	private GameDisplay dashboard;
	private StatsPanel statsPanel;
	private JPanel furnitureCard = new JPanel();
	private FurnitureStatusPanel furnitureStatusPanel;

	private boolean stop = true;

	private JPanel rootCenterPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	private JPanel clockCard = new JPanel();
	private JPanel statsCard = new JPanel();
	private JPanel controlCard = new JPanel();
	private JPanel bottomBar = new JPanel();
	private JPanel routineCard = new JPanel();
	private JPanel notificationCard = new JPanel();
	private JPanel imprevuCard = new JPanel();
	private JPanel spacerPanel = new JPanel();

	private JLabel clockLabel = new JLabel();
	private JLabel dayTypeLabel = new JLabel();
	private JLabel imprevuLabel = new JLabel();
	private JTextArea notificationsArea = new JTextArea();
	private JTextArea routineArea = new JTextArea();
	private JPanel speedCard = new JPanel();
	private JTextArea actualSpeed = new JTextArea();

	private int hour = 7;
	private int minute = 30;

	private JButton startButton = new JButton(" Start ");
	private JButton clearButton = new JButton(" Clear ");
	private JButton speedButton = new JButton(" Speed Up ");

	private int speedMultiplier = 1;

	/**
	 * Construit la fenetre principale.
	 * @param title titre de la fenetre
	 */
	public MainGUI(String title) {
		super(title);
		init();
	}

	@Override
	public void run() {
		long lastLogicTime = System.currentTimeMillis();
		long lastRendTime = System.currentTimeMillis();
		while (!stop) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastLogicTime >= SimulationConfiguration.GAME_SPEED / speedMultiplier) {
				manager.nextRound();
				lastLogicTime = currentTime;
			}
			if (currentTime - lastRendTime >= SimulationConfiguration.FRAME_DURATION) {
				refreshDisplay();
				lastRendTime = currentTime;
			}
			try {
				Thread.sleep(SimulationConfiguration.FRAME_DURATION);
			} catch (InterruptedException exception) {
				System.out.println(exception.getMessage());
			}
		}
	}

	/**
	 * Met a jour les infos affichees.
	 */
	private void refreshDisplay() {
		clockLabel.setText(manager.getClock().getFormattedTime());
		dayTypeLabel.setText(manager.getDayTypeLabel());
		actualSpeed.setText("Speed : x" + Integer.toString(speedMultiplier));
		routineArea.setText(manager.getRoutineSummary());
		notificationsArea.setText(manager.getNotifications());
		refreshImprevuCard();
		dashboard.repaint();
		statsPanel.repaint();
		furnitureStatusPanel.repaint();
	}

	private void refreshImprevuCard() {
		boolean active = manager.hasImprevu();
		imprevuLabel.setText(manager.getImprevuLabel());
		if (active) {
			imprevuCard.setBackground(new Color(220, 60, 60));
			imprevuLabel.setForeground(Color.WHITE);
		} else {
			imprevuCard.setBackground(Color.WHITE);
			imprevuLabel.setForeground(Color.DARK_GRAY);
		}
		imprevuCard.repaint();
	}

	/**
	 * Cree une carte simple dans la barre laterale.
	 * @param title titre de la carte
	 * @param width largeur voulue
	 * @param height hauteur voulue
	 * @return panneau pret a etre rempli
	 */
	private JPanel createCard(String title, int width, int height) {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setPreferredSize(new Dimension(width, height));
		panel.setMaximumSize(new Dimension(width, height));
		panel.setMinimumSize(new Dimension(width, height));
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GRAY, 2),
				title,
				TitledBorder.LEFT,
				TitledBorder.TOP,
				new Font(Font.SANS_SERIF, Font.BOLD, 13),
				Color.DARK_GRAY
		));
		return panel;
	}

	/**
	 * Recharge une nouvelle partie propre.
	 */
	private void updateValues() {
		map = GameBuilder.buildMap();
		roomManager = GameBuilder.buildRoomManager(map);
		furnitureManager = GameBuilder.buildFurnitureManager(map);
		roomManager.setFurnitureManager(furnitureManager);
		manager = GameBuilder.buildInitMobile(map, roomManager, furnitureManager);

		furnitureStatusPanel = new FurnitureStatusPanel(furnitureManager);
		furnitureCard.removeAll();
		furnitureCard.setLayout(new BorderLayout());
		furnitureCard.add(furnitureStatusPanel, BorderLayout.CENTER);
		furnitureCard.revalidate();
		furnitureCard.repaint();

		dashboard.setModel(map, manager, roomManager, furnitureManager);
		statsPanel.setMasterStats(manager.getMasterState());

		furnitureStatusPanel.repaint();
		dashboard.repaint();
		statsPanel.repaint();

		hour = 7;
		minute = 30;
		updateClock();
		logger.info("Simulation reset");
	}

	/**
	 * Construit toute l'interface.
	 */
	public void init() {
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		map = GameBuilder.buildMap();

		int simulationWidth = map.getColumnCount() * SimulationConfiguration.BLOCK_SIZE;
		int simulationHeight = map.getLineCount() * SimulationConfiguration.BLOCK_SIZE;

		roomManager = GameBuilder.buildRoomManager(map);
		furnitureManager = GameBuilder.buildFurnitureManager(map);
		roomManager.setFurnitureManager(furnitureManager);
		manager = GameBuilder.buildInitMobile(map, roomManager, furnitureManager);

		statsPanel = new StatsPanel(manager.getMasterState());
		statsPanel.setOpaque(true);
		statsPanel.setBackground(Color.WHITE);
		furnitureStatusPanel = new FurnitureStatusPanel(furnitureManager);
		furnitureCard = createCard("\u00C9tat des objets", SIDEBAR_WIDTH, 240);
		furnitureCard.setLayout(new BorderLayout());
		furnitureCard.add(furnitureStatusPanel, BorderLayout.CENTER);

		rootCenterPanel.setLayout(new BorderLayout());
		rootCenterPanel.setPreferredSize(new Dimension(simulationWidth, simulationHeight + BOTTOM_PANEL_HEIGHT));

		dashboard = new GameDisplay(map, manager, roomManager, furnitureManager);
		dashboard.setPreferredSize(new Dimension(simulationWidth, simulationHeight));
		rootCenterPanel.add(dashboard, BorderLayout.CENTER);

		int halfWidth = simulationWidth / 2;
		routineCard = createCard("Routine", halfWidth, BOTTOM_PANEL_HEIGHT);
		routineCard.setLayout(new BorderLayout());
		notificationCard = createCard("Notifications", simulationWidth - halfWidth, BOTTOM_PANEL_HEIGHT);
		notificationCard.setLayout(new BorderLayout());

		routineArea.setEditable(false);
		routineArea.setFont(new Font("Monospaced", Font.BOLD, 12));
		routineArea.setBackground(Color.WHITE);
		routineArea.setForeground(Color.BLACK);
		routineArea.setMargin(new Insets(8, 8, 8, 8));

		notificationsArea.setEditable(false);
		notificationsArea.setFont(new Font("Monospaced", Font.ITALIC, 12));
		notificationsArea.setBackground(Color.WHITE);
		notificationsArea.setForeground(Color.BLACK);
		notificationsArea.setMargin(new Insets(8, 8, 8, 8));

		JScrollPane routineScroll = new JScrollPane(routineArea);
		routineScroll.setBorder(BorderFactory.createEmptyBorder());

		JScrollPane notificationsScroll = new JScrollPane(notificationsArea);
		notificationsScroll.setBorder(BorderFactory.createEmptyBorder());

		routineCard.add(routineScroll, BorderLayout.CENTER);
		notificationCard.add(notificationsScroll, BorderLayout.CENTER);

		bottomBar.setLayout(new GridLayout(1, 2, 8, 0));
		bottomBar.setPreferredSize(new Dimension(simulationWidth, BOTTOM_PANEL_HEIGHT));
		bottomBar.setOpaque(false);
		bottomBar.add(routineCard);
		bottomBar.add(notificationCard);

		rootCenterPanel.add(bottomBar, BorderLayout.SOUTH);
		contentPane.add(rootCenterPanel, BorderLayout.CENTER);

		statsPanel = new StatsPanel(manager.getMasterState());
		statsPanel.setOpaque(true);
		statsPanel.setBackground(Color.WHITE);

		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setPreferredSize(new Dimension(SIDEBAR_WIDTH,
				SimulationConfiguration.WINDOW_HEIGHT + BOTTOM_PANEL_HEIGHT));
		rightPanel.setBackground(new Color(235, 235, 235));

		clockCard = createCard("Heure", SIDEBAR_WIDTH, 110);
		clockCard.setLayout(new BorderLayout());
		dayTypeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		dayTypeLabel.setForeground(new Color(80, 80, 80));
		dayTypeLabel.setHorizontalAlignment(JLabel.CENTER);
		dayTypeLabel.setText(manager.getDayTypeLabel());
		clockLabel.setFont(font);
		clockLabel.setHorizontalAlignment(JLabel.CENTER);
		clockCard.add(dayTypeLabel, BorderLayout.NORTH);
		clockCard.add(clockLabel, BorderLayout.CENTER);

		statsCard = createCard("Barre d'\u00E9tat", SIDEBAR_WIDTH, 185);
		statsCard.setLayout(new BorderLayout());
		statsCard.add(statsPanel, BorderLayout.CENTER);

		spacerPanel.setOpaque(false);
		spacerPanel.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 10));
		spacerPanel.setMaximumSize(new Dimension(SIDEBAR_WIDTH, 10));
		spacerPanel.setMinimumSize(new Dimension(SIDEBAR_WIDTH, 10));

		controlCard = createCard("Contr\u00F4les", SIDEBAR_WIDTH, 160);
		controlCard.setLayout(new BorderLayout());

		JPanel buttonRow = new JPanel();
		buttonRow.setOpaque(false);
		startButton.setFont(font);
		startButton.addActionListener(new StartStopAction());
		buttonRow.add(startButton, BorderLayout.NORTH);

		clearButton.setFont(font);
		clearButton.addActionListener(new ClearAction());
		buttonRow.add(clearButton, BorderLayout.CENTER);

		speedButton.setFont(font);
		speedButton.addActionListener(new SpeedUpAction());
		buttonRow.add(speedButton, BorderLayout.SOUTH);

		controlCard.add(buttonRow, BorderLayout.CENTER);

		imprevuCard = createCard("Imprévu", SIDEBAR_WIDTH, 80);
		imprevuCard.setLayout(new BorderLayout());
		imprevuLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		imprevuLabel.setHorizontalAlignment(JLabel.CENTER);
		imprevuLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
		imprevuCard.add(imprevuLabel, BorderLayout.CENTER);
		refreshImprevuCard();

		speedCard = createCard("Actual Speed", SIDEBAR_WIDTH, 70);
		speedCard.setLayout(new BorderLayout());
		actualSpeed.setFont(new Font("Monospaced", Font.ITALIC, 30));
		actualSpeed.setText("Speed : x" + Integer.toString(speedMultiplier));
		actualSpeed.setForeground(new Color(50, 50, 50));
		actualSpeed.setEditable(false);
		actualSpeed.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		speedCard.add(actualSpeed);

		rightPanel.add(clockCard);
		rightPanel.add(Box.createVerticalStrut(10));
		rightPanel.add(speedCard);
		rightPanel.add(Box.createVerticalStrut(10));
		rightPanel.add(statsCard);
		rightPanel.add(Box.createVerticalStrut(10));
		rightPanel.add(furnitureCard);
		rightPanel.add(Box.createVerticalStrut(10));
		rightPanel.add(controlCard);
		rightPanel.add(Box.createVerticalStrut(8));
		rightPanel.add(imprevuCard);

		contentPane.add(rightPanel, BorderLayout.EAST);

		updateClock();

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		logger.info("Main window ready");
	}

	private void updateClock() {
		String time = String.format("%02d:%02d", hour, minute);
		clockLabel.setText(time);
	}

	private class StartStopAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if (!stop) {
				stop = true;
				startButton.setText(" Start ");
				logger.info("Simulation paused");
			} else {
				stop = false;
				startButton.setText(" Pause ");
				Thread simThread = new Thread(MainGUI.this);
				simThread.start();
				logger.info("Simulation started");
			}
		}
	}

	private class ClearAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			stop = true;
			startButton.setText(" Start ");
			updateValues();
		}
	}

	private class SpeedUpAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if (speedMultiplier == 1) {
				speedMultiplier = 2;
			} else if (speedMultiplier == 2) {
				speedMultiplier = 3;
			} else if (speedMultiplier == 3) {
				speedMultiplier = 10;
			} else {
				speedMultiplier = 1;
			}
			logger.info("Speed changed to x" + speedMultiplier);
		}
	}
}
