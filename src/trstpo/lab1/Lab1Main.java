package trstpo.lab1;

import trstpo.lab1.modifier.ImagePanel;
import trstpo.lab1.modifier.blur.GaussianBlur;
import trstpo.lab1.modifier.expand.ImageExpander;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Lab1Main {

	/**
	 * @param args 1. Файл картинки
	 *             2. Куда сохранять
	 *             3. Радиус
	 *             4. Кол-во потоков
	 * @throws IOException Если возникнут проблемы при сохранении картинок
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		if (args.length > 0) {
			File src = new File(args[0]);
			File dst = new File(args[1]);
			BufferedImage sourceImage = ImageIO.read(src);
			int matrixRadius = Integer.parseInt(args[2]);
			int procNum = Integer.parseInt(args[3]);
			if (dst.exists() || dst.createNewFile()) {
				BufferedImage expandedImage = ImageExpander.getInstance().expandImage(sourceImage, matrixRadius, procNum);
				BufferedImage result = GaussianBlur.getInstance().blurImage(
						expandedImage,
						matrixRadius,
						procNum);
				ImageIO.write(result, "png", dst);

				JFrame compareFrame = new JFrame();
				compareFrame.setTitle("Before / After");
				compareFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				compareFrame.setSize(sourceImage.getWidth() * 2 + 50, sourceImage.getHeight() + 100);
				JPanel comparePanel = new JPanel(new GridLayout(1, 2));
				comparePanel.add(new ImagePanel(sourceImage));
				comparePanel.add(new ImagePanel(result));
				compareFrame.setContentPane(comparePanel);
				compareFrame.setVisible(true);
			}
		}
	}
}
