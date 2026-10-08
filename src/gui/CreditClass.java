package gui;

import log.LoggerUtility;
import org.apache.log4j.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Fenetre simple pour afficher les credits du projet.
 */
public class CreditClass extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerUtility.getLogger(CreditClass.class, "html");

	private static final Color BACKGROUND = new Color(18, 33, 58);
	private static final Color TITLE_COLOR = new Color(255, 214, 102);
	private static final Color SUBTITLE_COLOR = new Color(190, 210, 240);
	private static final Color NAME_COLOR = new Color(255, 255, 255);
	private static final Color BUTTON_COLOR = new Color(56, 88, 138);

	private static final Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 30);
	private static final Font SUBTITLE_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
	private static final Font NAME_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 22);
	private static final Font BUTTON_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);

	/**
	 * Ouvre la fenetre des credits.
	 */
	public CreditClass() {
		super("Credits");
		init();
	}

	/**
	 * Construit une page de credits tres simple.
	 */
	public void init() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(BACKGROUND);
		mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

		JPanel contentPanel = new JPanel();
		contentPanel.setOpaque(false);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		JLabel title = createLabel("Simulation Domotique", TITLE_FONT, TITLE_COLOR);
		JLabel subtitle = createLabel("Projet de L2 Informatique", SUBTITLE_FONT, SUBTITLE_COLOR);
		JLabel nameOne = createLabel("Yassine Ait Talb", NAME_FONT, NAME_COLOR);
		JLabel nameTwo = createLabel("Agnies Sadli", NAME_FONT, NAME_COLOR);
		JLabel nameThree = createLabel("Mariam Traore", NAME_FONT, NAME_COLOR);

		contentPanel.add(Box.createVerticalGlue());
		contentPanel.add(title);
		contentPanel.add(Box.createVerticalStrut(12));
		contentPanel.add(subtitle);
		contentPanel.add(Box.createVerticalStrut(36));
		contentPanel.add(nameOne);
		contentPanel.add(Box.createVerticalStrut(14));
		contentPanel.add(nameTwo);
		contentPanel.add(Box.createVerticalStrut(14));
		contentPanel.add(nameThree);
		contentPanel.add(Box.createVerticalGlue());

		JButton closeButton = new JButton("Fermer");
		closeButton.setFont(BUTTON_FONT);
		closeButton.setForeground(Color.WHITE);
		closeButton.setBackground(BUTTON_COLOR);
		closeButton.setFocusPainted(false);
		closeButton.addActionListener(new QuitAction());

		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.add(closeButton);

		mainPanel.add(contentPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		setContentPane(mainPanel);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(620, 420);
		setLocationRelativeTo(null);
		setVisible(true);
		logger.info("Credits window opened");
	}

	private JLabel createLabel(String text, Font font, Color color) {
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		label.setAlignmentX(CENTER_ALIGNMENT);
		label.setFont(font);
		label.setForeground(color);
		return label;
	}

	private class QuitAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			logger.info("Launcher closed");
			dispose();
		}
	}
}
