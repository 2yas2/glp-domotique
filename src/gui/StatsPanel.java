package gui;

import engine.state.MasterState;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Affiche les barres d'etat du personnage.
 */
public class StatsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int ROW_HEIGHT = 24;
	private static final int BAR_HEIGHT = 24;
	private static final int MARGIN = 8;
	private static final int LABEL_WIDTH = 70;
	private static final int SPACING = 6;

	private static final Color COLOR_PANEL = new Color(30, 30, 30);
	private static final Color COLOR_BAR_BG = new Color(60, 60, 60);
	private static final Color COLOR_ENERGY = new Color(80, 200, 80);
	private static final Color COLOR_HUNGER = new Color(230, 140, 30);
	private static final Color COLOR_THIRST = new Color(60, 160, 230);
	private static final Color COLOR_HYGIENE = new Color(180, 80, 200);
	private static final Color COLOR_URGENT = new Color(240, 160, 30);
	private static final Color COLOR_CRITICAL = new Color(210, 50, 50);
	private static final Color COLOR_MOOD = new Color(200, 150, 50);
	private static final Font FONT_LABEL = new Font(Font.MONOSPACED, Font.BOLD, 11);

	private MasterState masterState;

	/**
	 * Construit le panneau des stats.
	 * @param masterState etat a afficher
	 */
	public StatsPanel(MasterState masterState) {
		this.masterState = masterState;
		setBackground(COLOR_PANEL);
		int totalHeight = SPACING + 5 * (ROW_HEIGHT + SPACING);
		setPreferredSize(new Dimension(0, totalHeight));
	}

	/**
	 * Change l'etat actuellement affiche.
	 * @param masterState nouvel etat
	 */
	public void setMasterStats(MasterState masterState) {
		this.masterState = masterState;
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		int barWidth = getWidth() - LABEL_WIDTH - MARGIN * 2 - 35;
		int y = SPACING;

		paintBar(graphics, "Energie", masterState.getEnergy().getValue(), COLOR_ENERGY, y, barWidth);
		y += ROW_HEIGHT + SPACING;
		paintBar(graphics, "Faim", masterState.getHunger().getValue(), COLOR_HUNGER, y, barWidth);
		y += ROW_HEIGHT + SPACING;
		paintBar(graphics, "Soif", masterState.getThirst().getValue(), COLOR_THIRST, y, barWidth);
		y += ROW_HEIGHT + SPACING;
		paintBar(graphics, "Hygiene", masterState.getHygiene().getValue(), COLOR_HYGIENE, y, barWidth);
		y += ROW_HEIGHT + SPACING;
		paintBar(graphics, "Humeur", masterState.getMood(), COLOR_MOOD, y, barWidth);
	}

	/**
	 * Dessine une barre simple.
	 * @param graphics contexte graphique
	 * @param label nom affiche
	 * @param value valeur en pourcentage
	 * @param color couleur normale
	 * @param y ligne de dessin
	 * @param barWidth largeur de barre
	 */
	private void paintBar(Graphics graphics, String label, int value, Color color, int y, int barWidth) {
		int xBar = MARGIN + LABEL_WIDTH;
		int yBar = y + (ROW_HEIGHT - BAR_HEIGHT) / 2;
		int filled = (value * barWidth) / 100;

		graphics.setFont(FONT_LABEL);
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.drawString(label, MARGIN, y + ROW_HEIGHT / 2 + 4);

		graphics.setColor(COLOR_BAR_BG);
		graphics.fillRect(xBar, yBar, barWidth, BAR_HEIGHT);

		Color fillColor = color;
		if (value < 20) {
			fillColor = COLOR_CRITICAL;
		} else if (value < 35) {
			fillColor = COLOR_URGENT;
		}

		graphics.setColor(fillColor);
		graphics.fillRect(xBar, yBar, filled, BAR_HEIGHT);

		graphics.setColor(Color.WHITE);
		graphics.drawRect(xBar, yBar, barWidth, BAR_HEIGHT);
		graphics.drawString(value + "%", xBar + barWidth + 6, y + ROW_HEIGHT / 2 + 4);
	}
}
