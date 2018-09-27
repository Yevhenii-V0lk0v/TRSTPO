package trstpo.lab1;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageComparator {
	private static ImageComparator ourInstance = new ImageComparator();

	public static ImageComparator getInstance() {
		return ourInstance;
	}

	private ImageComparator() {
	}

	public BufferedImage compareImages(BufferedImage image1, BufferedImage image2) {
		BufferedImage bufferedImage = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_ARGB);
		if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
			return null;
		} else {
			for (int i = 0; i < image1.getWidth(); i++) {
				for (int j = 0; j < image1.getHeight(); j++) {
					if (image1.getRGB(i, j) != image2.getRGB(i, j)) {
						Color diff = new Color(
								getColorDiffRGB(
										new Color(image1.getRGB(i, j)),
										new Color(image2.getRGB(i, j))
								)
						);
						bufferedImage.setRGB(i, j, diff.getRGB());
					}
				}
			}
			return bufferedImage;
		}
	}

	private int getColorDiffRGB(Color color1, Color color2) {
		return ((color1.getAlpha() - color2.getAlpha()) << 24) |
				((color1.getRed() - color2.getRed()) << 16) |
				((color1.getGreen() - color2.getGreen()) << 8) |
				(color1.getBlue() - color2.getBlue());
	}
}
