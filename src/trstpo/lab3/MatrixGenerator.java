package trstpo.lab3;

import java.util.Random;

public class MatrixGenerator {
	public static void main(String[] args) {
		int rows = Integer.parseInt(args[0]);
		int cols = Integer.parseInt(args[1]);
		double[][] result = new double[rows][cols];
		Random random = new Random();
		for (int i = 0; i < result.length; i++) {
			result[i] = random.doubles(cols, 0, 10).toArray();
		}
		DocMatrixConverter.getInstance().saveToDocument(args[2], result);
	}
}
