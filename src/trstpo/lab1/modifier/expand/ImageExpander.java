package trstpo.lab1.modifier.expand;

import trstpo.lab1.modifier.expand.thread.ImageExpandRunnable;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageExpander {

	private ImageExpander() {

	}

	private static ImageExpander instance;

	public static ImageExpander getInstance() {
		if (instance == null) {
			instance = new ImageExpander();
		}
		return instance;
	}

	public BufferedImage expandImage(BufferedImage src, int expansion, int procNum) throws InterruptedException {
		int expansionSize = expansion * 2;
		BufferedImage dst = new BufferedImage(src.getWidth() + expansionSize, src.getHeight() + expansionSize, src.getType());
		dst.getHeight();
		int step = dst.getHeight() / procNum;
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < dst.getHeight(); i += step) {
			if (i + step > dst.getHeight()) {
				executorService.submit(new ImageExpandRunnable(src, dst, expansion, i, dst.getHeight()));
			} else {
				executorService.submit(new ImageExpandRunnable(src, dst, expansion, i, i + step));
			}
		}
		executorService.shutdown();
		executorService.awaitTermination(10, TimeUnit.MINUTES);
		return dst;
	}
}
