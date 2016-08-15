package main;

public interface MatricesMultiplierInterface {

	public void multiplyMatrix(int m, int n, int k, int threads, boolean quiet);

	public void printProductMatrix(double[][] c);

	public void importMatrices(String fileName, int numberOfThreads,
			boolean quiet);

	public void ExportMatrices(int m, int n, int k, String filename);

	public void ExportProductMatrix(String filename);

	public void compute();

	/*
	 * public void MatrixGeneratorWithMathRandom(int m, int n, double[][] a, int
	 * numberOfThreads, int numberRange, boolean quiet);
	 * 
	 * public void MatricesGeneratorWithMathRandom(int m, int n, int k, int
	 * numberOfThreads, int numberRange, boolean quiet);
	 * 
	 * public double[] toDoubleArray(String line);
	 * 
	 * public void transpose(int n, int k);
	 */

}
