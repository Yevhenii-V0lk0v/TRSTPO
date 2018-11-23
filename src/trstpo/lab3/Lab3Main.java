package trstpo.lab3;

import mpi.MPI;

import java.io.FileNotFoundException;
import java.io.InvalidObjectException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Lab3Main {
	private static final int ARG_OFFSET = 3;

	public static void main(String[] args) throws FileNotFoundException, InvalidObjectException, MatrixProductException, InterruptedException {
		MPI.Init(args);
		DocMatrixConverter converter = DocMatrixConverter.getInstance();
		double[][] firstMatrix = converter.parseDocument(args[ARG_OFFSET]);
		double[][] secondMatrix = converter.parseDocument(args[ARG_OFFSET + 1]);
		int tAmount = Integer.parseInt(args[ARG_OFFSET + 3]);
		if (firstMatrix[0].length != secondMatrix.length) {
			throw new MatrixProductException("Matrixes dimensions must align");
		}
		int me = MPI.COMM_WORLD.Rank();
		int pAmount = MPI.COMM_WORLD.Size();
		StringBuilder report = new StringBuilder("Report from ").append(me).append(":\n");
		double pStep = ((double) secondMatrix[0].length) / pAmount;
		double tStep = ((double) firstMatrix.length) / tAmount;
		int colRange = ((int) ((me + 1) * pStep)) - ((int) (me * pStep));
		double[][] result = new double[firstMatrix.length][colRange];
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = (int) (me * pStep); i < secondMatrix[0].length && i < (me + 1) * pStep; i++) {
			report.append("for column ").append(i).append(":\n");
			for (double j = 0; j < firstMatrix.length; j += tStep) {
				report.append("from ").append((int) j).append(" to ").append((int) (j + tStep)).append("\n");
				VectorProductionRunnable task = new VectorProductionRunnable(
						(int) j,
						(int) (j + tStep),
						i,
						i - (int) (me * pStep),
						result,
						firstMatrix,
						secondMatrix
				);
				executorService.submit(task);
			}
		}
		executorService.shutdown();
		executorService.awaitTermination(10, TimeUnit.MINUTES);
		report.append("Calculated:\n").append(matrixToString(result));
		if (me != pAmount - 1) {
			Object[] resBuffer = new Object[1];
			MPI.COMM_WORLD.Recv(
					resBuffer,
					0,
					resBuffer.length,
					MPI.OBJECT,
					me + 1,
					MPI.ANY_TAG
			);
			result = mergeMatricesByCol(result, (double[][]) resBuffer[0]);
		}
		if (me > 0) {
			MPI.COMM_WORLD.Send(
					new Object[]{result},
					0,
					1,
					MPI.OBJECT,
					me - 1,
					MPI.ANY_TAG
			);
		} else {
			converter.saveToDocument(args[ARG_OFFSET + 2], result);
		}
		System.out.println(report.toString());
		MPI.Finalize();
	}

	private static double[][] mergeMatricesByCol(double[][] first, double[][] second) {
		double[][] result = new double[first.length][first[0].length + second[0].length];
		for (int i = 0; i < first.length; i++) {
			System.arraycopy(first[i], 0, result[i], 0, first[i].length);
			System.arraycopy(second[i], 0, result[i], first[i].length, second[i].length);
		}
		return result;
	}

	private static String matrixToString(double[][] matrix) {
		StringBuilder builder = new StringBuilder();
		for (double[] doubles : matrix) {
			for (double v : doubles) {
				builder.append(v).append("\t");
			}
			builder.append("\n");
		}
		return builder.toString();
	}
}
