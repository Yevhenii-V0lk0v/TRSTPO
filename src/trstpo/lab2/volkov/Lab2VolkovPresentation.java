package trstpo.lab2.volkov;

import trstpo.lab2.volkov.container.ShowerRoom;
import trstpo.lab2.volkov.user.Gal;
import trstpo.lab2.volkov.user.Guy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Lab2VolkovPresentation {
	private static List<Thread> threads = new ArrayList<>();

	public static void presentSolution() {
		ShowerRoom<String> testRoom = new ShowerRoom<>(5000, "'Resource'");
		boolean gal = true;
		for (int i = 0; i < 20; i++) {
			if (gal) {
				threads.add(new Gal(testRoom));
			} else {
				threads.add(new Guy(testRoom));
			}
			gal = !gal;
		}
		for (Thread thread : threads) {
			thread.start();
		}
		Scanner scanner = new Scanner(System.in);
		scanner.next();
		testRoom.interrupt();
	}
}
