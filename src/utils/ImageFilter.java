package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageFilter {

	public static BufferedImage applyTexture(BufferedImage texture, BufferedImage image) {
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();
		texture = ImageResizer.resizeImage(texture, texture.getType(), imageWidth, imageHeight);
		for(int x = 0; x < imageWidth; x++) {
			for(int y = 0; y < imageHeight; y++) {
				Color c = new Color(texture.getRGB(x, y));
				if(!GrayScaleConverter.isTransparent(image, x,y) && c.getBlue() < 25 && c.getGreen() < 25 && c.getRed() < 25) {
					double d = Math.random();
					image.setRGB(x,y,darken(new Color(image.getRGB(x, y)),d));
				}
			}
		}
		return image;
	}
	
	public static int darken(Color color, double fraction) {

        int red = (int) Math.round(Math.max(0, color.getRed() - 255 * fraction));
        int green = (int) Math.round(Math.max(0, color.getGreen() - 255 * fraction));
        int blue = (int) Math.round(Math.max(0, color.getBlue() - 255 * fraction));
        int alpha = color.getAlpha();
        return new Color(red, green, blue, alpha).getRGB();

    }
	
}
