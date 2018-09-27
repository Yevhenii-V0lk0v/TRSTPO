package trstpo.lab1.modifier.expand.thread;

import java.awt.image.BufferedImage;

public class ImageExpandRunnable implements Runnable {

	private BufferedImage src;
	private BufferedImage dst;
	private int extension;
	private int startHeight;
	private int endHeight;

	public ImageExpandRunnable(BufferedImage src, BufferedImage dst, int extension, int startHeight, int endHeight) {
		this.src = src;
		this.dst = dst;
		this.extension = extension;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
	}

	@Override
	public void run() {
		for (int i = startHeight; i < endHeight; i++) {
			if (i < extension) {
				expandTop(i);
			} else if (i >= extension + src.getHeight()) {
				expandBottom(i);
			} else {
				int j = 0;
				for (; j < extension; j++) {
					dst.setRGB(j, i, src.getRGB(0, i - extension));
				}
				for (; j < extension + src.getWidth(); j++) {
					dst.setRGB(j, i, src.getRGB(j - extension, i - extension));
				}
				for (; j < dst.getWidth(); j++) {
					dst.setRGB(j, i, src.getRGB(src.getWidth() - 1, i - extension));
				}
			}
		}
	}

	private void expandBottom(int i) {
		expandHorizontal(i, src.getHeight() - 1);
	}

	private void expandTop(int i) {
		expandHorizontal(i, 0);
	}

	private void expandHorizontal(int i, int mirrorHeight) {
		int j;
		for (j = 0; j < extension; j++) {
			dst.setRGB(j, i, src.getRGB(0, mirrorHeight));
		}
		for (; j < src.getWidth() + extension; j++) {
			dst.setRGB(j, i, src.getRGB(j - extension, mirrorHeight));
		}
		for (; j < dst.getWidth(); j++) {
			dst.setRGB(j, i, src.getRGB(src.getWidth() - 1, mirrorHeight));
		}
	}
}
