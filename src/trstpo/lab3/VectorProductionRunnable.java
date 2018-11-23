package trstpo.lab3;

public class VectorProductionRunnable implements Runnable {

	private int start;
	private int end;
	private int col;
	private int resultCol;
	private double[][] resultMatrix;
	private double[][] srcMatrix;
	private double[][] vectorSrcMatrix;

	public VectorProductionRunnable(int start, int end, int col, int resultCol, double[][] resultMatrix, double[][] srcMatrix, double[][] vectorSrcMatrix) {
		this.start = start;
		this.end = end;
		this.col = col;
		this.resultCol = resultCol;
		this.resultMatrix = resultMatrix;
		this.srcMatrix = srcMatrix;
		this.vectorSrcMatrix = vectorSrcMatrix;
	}

	@Override
	public void run() {
		for (int k = start; k < end; k++) {
			double prod = vectorProd(srcMatrix[k], getCol(col, vectorSrcMatrix));
			resultMatrix[k][resultCol] = prod;
		}
	}

	private double vectorProd(double[] f, double[] s) {
		double result = 0;
		for (int i = 0; i < f.length; i++) {
			result += f[i] * s[i];
		}
		return result;
	}

	private double[] getCol(int col, double[][] matrix) {
		double[] result = new double[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			result[i] = matrix[i][col];
		}
		return result;
	}
}
