package trstpo.lab1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Lab1Main {

	/**
	 * @param args 1. Файл картинки
	 *             2. Куда сохранять
	 *             3. Радиус
	 *             4. Коэфф. ?
	 *             5. Кол-во потоков
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		File src = new File(args[0]);
		BufferedImage image = ImageIO.read(src);
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		long matrixRadius = Long.parseLong(args[2]);
		double dispersion = Double.parseDouble(args[3]);
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				newImage.setRGB(i, j, image.getRGB(i, j));
			}
		}
		File dst = new File(args[1]);
		if (!dst.exists()) {
			dst.createNewFile();
		}
		ImageIO.write(newImage, "png", dst);
	}
}
