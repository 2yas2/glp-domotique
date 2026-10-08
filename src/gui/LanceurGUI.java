package gui;

import log.LoggerUtility;
import org.apache.log4j.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Petit lanceur du projet.
 */
public class LanceurGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerUtility.getLogger(LanceurGUI.class, "html");

	private JButton play = new JButton(" Play ");
	private JButton credits = new JButton(" Cr\u00E9dits ");
	private JButton quitter = new JButton(" Quitter ");
	private Color backgroundColor = new Color(20, 41, 73);
	private static final Font FONT_BOUTON = new Font(Font.MONOSPACED, Font.BOLD, 18);
	private static final Font FONT_TITRE = new Font(Font.SANS_SERIF, Font.BOLD, 24);

	/**
	 * Ouvre la fenetre d'accueil.
	 */
	public LanceurGUI() {
		super("Simulation Domotique");
		init();
	}

	public static void main(String[] args) {
		new LanceurGUI();
	}

	/**
	 * Construit la vue du menu.
	 */
	public void init() {

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(backgroundColor);
		mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

		JLabel titre = new JLabel("Simulation Domotique", JLabel.CENTER);
		titre.setFont(FONT_TITRE);
		titre.setForeground(Color.WHITE);
		titre.setBorder(new EmptyBorder(0, 0, 20, 0));
		mainPanel.add(titre, BorderLayout.NORTH);

		JPanel boutonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
		boutonPanel.setBackground(backgroundColor);

		styleButton(play);
		styleButton(credits);
		styleButton(quitter);

		play.addActionListener(new LancerAction());
		credits.addActionListener(new CreditsAction());
		quitter.addActionListener(new QuitAction());

		boutonPanel.add(play);
		boutonPanel.add(credits);
		boutonPanel.add(quitter);

		mainPanel.add(boutonPanel, BorderLayout.CENTER);

		add(mainPanel);

		setBackground(backgroundColor);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setSize(800, 500);
		setLocationRelativeTo(null);
		setVisible(true);
		logger.info("Launcher opened");
	}

	private void styleButton(JButton button) {
		button.setForeground(Color.WHITE);
		button.setBackground(new Color(45, 75, 115));
		button.setFont(FONT_BOUTON);
		button.setFocusPainted(false);

		button.setContentAreaFilled(true);
		button.setOpaque(true);
		button.setBorderPainted(false);
	}

	private class LancerAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			MainGUI gameMainGUI = new MainGUI("Simulation Domotique");
			Thread gameThread = new Thread(gameMainGUI);
			gameThread.start();
			logger.info("Simulation window started");
			dispose();
		}
	}

	private class CreditsAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			logger.info("Open credits");
			new CreditClass();
		}
	}

	private class QuitAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			logger.info("Launcher closed");
			dispose();
		}
	}
}
