package gui;

import engine.item.furniture.Furniture;
import process.furniture.FurnitureManager;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Affiche l'etat rapide des meubles utiles.
 */
public class FurnitureStatusPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private FurnitureManager furnitureManager;

	/**
	 * Construit le panneau des meubles.
	 * @param furnitureManager gestionnaire des meubles
	 */
	public FurnitureStatusPanel(FurnitureManager furnitureManager) {
		this.furnitureManager = furnitureManager;
		setPreferredSize(new Dimension(230, 210));
		setBackground(Color.WHITE);
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		drawFurnitureStatus(graphics, furnitureManager.getBed(), 15, 30);
		drawFurnitureStatus(graphics, furnitureManager.getOven(), 15, 60);
		drawFurnitureStatus(graphics, furnitureManager.getBathtub(), 15, 90);
		drawFurnitureStatus(graphics, furnitureManager.getTelevision(), 15, 120);
		drawFurnitureStatus(graphics, furnitureManager.getSofa(), 15, 150);
		drawFurnitureStatus(graphics, furnitureManager.getWaterBottle(), 15, 180);
	}

	/**
	 * Dessine une ligne d'etat pour un meuble.
	 * @param graphics contexte graphique
	 * @param furniture meuble a afficher
	 * @param x position x
	 * @param y position y
	 */
	private void drawFurnitureStatus(Graphics graphics, Furniture furniture, int x, int y) {
		boolean active = furniture.isActive();

		graphics.setColor(Color.BLACK);
		graphics.drawString(furniture.getName(), x, y);

		graphics.setColor(active ? new Color(50, 180, 70) : new Color(210, 60, 60));
		graphics.fillRoundRect(x + 95, y - 14, 90, 20, 10, 10);

		graphics.setColor(Color.WHITE);
		graphics.drawString(active ? "Activ\u00E9" : "Veille", x + 112, y);
	}
}
