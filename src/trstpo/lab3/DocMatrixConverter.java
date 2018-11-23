package trstpo.lab3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DocMatrixConverter {
	private static DocMatrixConverter instance;

	public static DocMatrixConverter getInstance() {
		if (instance == null) {
			instance = new DocMatrixConverter();
		}
		return instance;
	}

	public double[][] parseDocument(String docName) throws FileNotFoundException, InvalidObjectException {
		double[][] result;
		try (Scanner scanner = new Scanner(new File(docName))) {
			result = new double[scanner.nextInt()][scanner.nextInt()];
			for (int i = 0; i < result.length; i++) {
				scanner.nextLine();
				for (int j = 0; j < result[i].length; j++) {
					result[i][j] = scanner.nextDouble();
				}
			}
		} catch (NoSuchElementException e) {
			throw new InvalidObjectException("Matrix description doesn't correspond to the given dimensions");
		}
		return result;
	}

	public boolean saveToDocument(String docName, double[][] matrix) {
		File dst = new File(docName);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(dst))) {
			writer.write(matrix.length + " " + matrix[0].length + "\n");
			StringBuilder builder = new StringBuilder();
			for (double[] row : matrix) {
				for (double cell : row) {
					builder.append(cell).append(" ");
				}
				builder.append("\n");
			}
			writer.write(builder.toString());
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
