package main;

public class MatricesGenerator {

	private boolean quiet;
	private int numberOfThreads;
	private int maxValue;
	private double[][] A, B;

	public MatricesGenerator(double[][] a, double b[][], int maxValue,
			int numberOfThreads, boolean quiet) {

		this.maxValue = maxValue;
		this.numberOfThreads = numberOfThreads;
		this.quiet = quiet;
		A = a;
		B = b;
	}

	public void generateMatrices() {

		generateMatrix(A);
		generateMatrix(B);

	}

	protected void generateMatrix(double[][] a) {
		int m = a.length;
		int n = a[0].length;
		int numberOfRows = m / numberOfThreads;
		int leftOver = m % numberOfThreads;

		MatrixGeneratingThread[] threads = new MatrixGeneratingThread[numberOfThreads];

		if (numberOfThreads > 1) {

			for (int i = 0; i < numberOfThreads - 1; i++) {

				threads[i] = new MatrixGeneratingThread(i * numberOfRows, a,
						numberOfRows, n, maxValue, quiet);
				threads[i].start();

			}
			// Adding the leftover rows to the last thread
			threads[numberOfThreads - 1] = new MatrixGeneratingThread(
					(numberOfThreads - 1) * numberOfRows, a, numberOfRows
							+ leftOver, n, maxValue, quiet);
			threads[numberOfThreads - 1].start();

		}
		// When only one thread is used
		else {

			threads[0] = new MatrixGeneratingThread(0, a, numberOfRows, n,
					maxValue, quiet);
			threads[0].start();
		}
		// Join threads so that the program waits for them to finish in order to
		// proceed.
		for (MatrixGeneratingThread thread : threads) {
			try {
				thread.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!quiet) {
			System.out.println("Matrix generated!");
		}
	}
}
