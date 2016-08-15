package main;

public interface MatricesMultiplierInterface {

	public void multiplyMatrix(int m, int n, int k, int threads, boolean quiet);

	public void printProductMatrix(double[][] c);

	public void importMatrices(String fileName, int numberOfThreads,
			boolean quiet);

	public void ExportMatrices(int m, int n, int k, String filename);

	public void ExportProductMatrix(String filename);

	public void compute();

}
