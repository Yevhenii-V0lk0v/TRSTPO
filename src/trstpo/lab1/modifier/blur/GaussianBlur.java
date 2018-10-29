package trstpo.lab1.modifier.blur;

import trstpo.lab1.modifier.blur.thread.GaussianBlurRunnable;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class GaussianBlur {

	private final Logger logger = Logger.getLogger(this.getClass().getName());

	private GaussianBlur() {
	}

	private static GaussianBlur blur;

	public static GaussianBlur getInstance() {
		if (blur == null) {
			blur = new GaussianBlur();
		}
		return blur;
	}

	public BufferedImage blurImage(BufferedImage src, int blurRadius, int procNum) throws InterruptedException {
		logger.info("Blurring started");
		int borderSize = 2 * blurRadius;
		BufferedImage dst = new BufferedImage(src.getWidth() - borderSize, src.getHeight() - borderSize, src.getType());
		double[][] weightMatrix = getWeightMatrix(blurRadius);
		printWeightedMatrixToFile(weightMatrix);
		int step = dst.getHeight() / procNum;
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < dst.getHeight(); i += step) {
			if (i + step > dst.getHeight()) {
				executorService.submit(new GaussianBlurRunnable(src, dst, weightMatrix, i, dst.getHeight()));
			} else {
				executorService.submit(new GaussianBlurRunnable(src, dst, weightMatrix, i, i + step));
			}
		}
		long start = new Date().getTime();
		executorService.shutdown();
		if (executorService.awaitTermination(10, TimeUnit.MINUTES)) {
			long end = new Date().getTime();
			String msg = String.format("Blur took: %d ms", end - start);
			logger.info(msg);
		}
		return dst;
	}

	private double[][] getWeightMatrix(int radius) {
		double[][] weights = new double[2 * radius + 1][2 * radius + 1];
		double summation = 0;
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				weights[i][j] = getGaussianCoefficient(i - (double) radius, j - (double) radius, radius / 4.0);
				summation += weights[i][j];
			}
		}
		if (summation > 0) {
			for (int i = 0; i < weights.length; i++) {
				for (int j = 0; j < weights[i].length; j++) {
					weights[i][j] /= summation;
				}
			}
		}
		return weights;
	}

	private void printWeightedMatrixToFile(double[][] weightMatrix) {
		BufferedImage img = new BufferedImage(weightMatrix.length, weightMatrix.length, BufferedImage.TYPE_INT_RGB);

		double max = Double.MIN_VALUE;
		for (double[] weightsRow : weightMatrix) {
			for (double weight : weightsRow) {
				max = Math.max(max, weight);
			}
		}

		for (int i = 0; i < weightMatrix.length; i++) {
			for (int j = 0; j < weightMatrix.length; j++) {
				int grayScaleValue = (int) ((weightMatrix[i][j] / max) * 255D);
				img.setRGB(i, j, new Color(grayScaleValue, grayScaleValue, grayScaleValue).getRGB());
			}
		}
		try {
			ImageIO.write(img, "JPEG", new File("weightMatrix.png"));
		} catch (IOException e) {
			logger.severe(e.toString());
		}
	}

	private double getGaussianCoefficient(double x, double y, double variance) {
		return (1 / (2 * Math.PI * Math.pow(variance, 2))
				* Math.exp(-(Math.pow(x, 2) + Math.pow(y, 2)) / (2 * Math.pow(variance, 2))));
	}
}
