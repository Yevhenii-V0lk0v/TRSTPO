package trstpo.lab1;

import java.awt.image.BufferedImage;

public class ImageBlurThread extends Thread {
	private long radius;
	private double dispersion;
	private BufferedImage src;
	private BufferedImage dst;
	private PixelColor editColor;

	@Override
	public void run() {

	}

	public ImageBlurThread(long radius, double dispersion, BufferedImage src, BufferedImage dst, PixelColor editColor) {
		this.radius = radius;
		this.dispersion = dispersion;
		this.src = src;
		this.dst = dst;
		this.editColor = editColor;
	}
}
