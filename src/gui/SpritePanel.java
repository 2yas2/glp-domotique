package gui;

import log.LoggerUtility;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Petit helper pour lire une image ou un bout d'image.
 */
public class SpritePanel {

	private static Logger logger = LoggerUtility.getLogger(SpritePanel.class, "html");

	private String image;

	/**
	 * Garde le chemin d'une image.
	 * @param image chemin dans les ressources
	 */
	public SpritePanel(String image) {
		this.image = image;
	}

	/**
	 * Charge l'image complete.
	 * @return image lue, ou null si elle manque
	 */
	public BufferedImage getImage() {
		try {
			return ImageIO.read(getClass().getResource(image));
		} catch (IOException exception) {
			logger.warn("Sprite read error: " + image);
			return null;
		} catch (IllegalArgumentException exception) {
			logger.warn("Sprite not found: " + image);
			return null;
		}
	}

	/**
	 * Decoupe un morceau dans l'image.
	 * @param x coin gauche x
	 * @param y coin haut y
	 * @param width largeur
	 * @param height hauteur
	 * @return sous-image, ou null si l'image manque
	 */
	public BufferedImage getSubImage(int x, int y, int width, int height) {
		BufferedImage full = getImage();
		if (full == null) {
			return null;
		}
		return full.getSubimage(x, y, width, height);
	}
}
