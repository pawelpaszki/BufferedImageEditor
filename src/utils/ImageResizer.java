package utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageResizer {

	public static BufferedImage resizeImage(BufferedImage originalImage, int type, Integer img_width,
			Integer img_height) {
		BufferedImage resizedImage = new BufferedImage(img_width, img_height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, img_width, img_height, null);
		g.dispose();
		return resizedImage;
	}
}
