package trstpo.lab1.modifier.blur.thread;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GaussianBlurRunnable implements Runnable {
	private double[][] weightMatrix;
	private int startHeight;
	private int endHeight;
	private BufferedImage src;
	private BufferedImage dst;

	public GaussianBlurRunnable(BufferedImage src, BufferedImage dst, double[][] weightMatrix, int startHeight, int endHeight) {
		this.src = src;
		this.dst = dst;
		this.weightMatrix = weightMatrix;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
	}

	@Override
	public void run() {
		for (int x = 0; x < dst.getWidth(); x++) {
			for (int y = startHeight; y < endHeight; y++) {
				double[][] distributedColorRed = new double[weightMatrix.length][weightMatrix[0].length];
				double[][] distributedColorGreen = new double[weightMatrix.length][weightMatrix[0].length];
				double[][] distributedColorBlue = new double[weightMatrix.length][weightMatrix[0].length];
				double[][] distributedColorAlpha = new double[weightMatrix.length][weightMatrix[0].length];
				for (int weightX = 0; weightX < weightMatrix.length; weightX++) {
					for (int weightY = 0; weightY < weightMatrix[weightX].length; weightY++) {
						int sampleX = x + weightX;
						int sampleY = y + weightY;

						double currentWeight = weightMatrix[weightX][weightY];

						Color sampledColor = new Color(src.getRGB(sampleX, sampleY));

						distributedColorRed[weightX][weightY] = currentWeight * sampledColor.getRed();
						distributedColorGreen[weightX][weightY] = currentWeight * sampledColor.getGreen();
						distributedColorBlue[weightX][weightY] = currentWeight * sampledColor.getBlue();
						distributedColorAlpha[weightX][weightY] = currentWeight * sampledColor.getAlpha();

					}
				}
				dst.setRGB(x, y, new Color(
								getWeightedColorValue(distributedColorRed),
								getWeightedColorValue(distributedColorGreen),
								getWeightedColorValue(distributedColorBlue),
								getWeightedColorValue(distributedColorAlpha)
						).getRGB()
				);
			}
		}
	}

	private int getWeightedColorValue(double[][] weightedColor) {
		double summation = 0;
		for (double[] colorsRow : weightedColor) {
			for (double color : colorsRow) {
				summation += color;
			}
		}
		return (int) summation;
	}
}
