package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class GrayScaleConverter {
	
	public static BufferedImage convertToGrayScale(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (!isTransparent(image,x, y)) {
					Color c = new Color(image.getRGB(x, y));
					int red = (int) (c.getRed() * 0.21);
					int green = (int) (c.getGreen() * 0.72);
					int blue = (int) (c.getBlue() * 0.07);
					int sum = red + green + blue;
					Color newColor = new Color(sum, sum, sum);
					image.setRGB(x, y, newColor.getRGB());
				}
			}
		}
		return image;
	}
	
	public static boolean isTransparent(BufferedImage image, int x, int y) {
		int pixel = image.getRGB(x, y);
		if ((pixel >> 24) == 0x00) {
			return true;
		} else {
			return false;
		}
	}
}
