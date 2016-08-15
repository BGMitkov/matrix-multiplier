package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.commons.cli.*;

/**
 * A class used for loading the matrices from a file or generating them and then
 * multiplying them thus producing the result with an option to be written to a
 * file.
 * 
 * @author bgmitkov
 *
 */
public class MatricesMultiplier implements MatricesMultiplierInterface {

	public static double[][] A, B, C, tB;
	private CommandLine parsedOptions;
	private OptionsValidator validator;

	public MatricesMultiplier(String[] args) {

		validator = new OptionsValidator();
		parsedOptions = validator.parseOptions(args);

	}

	public void compute() {

		if (parsedOptions == null) {
			return;
		}
		long t1 = 0;
		long t2 = 0;
		t1 = Calendar.getInstance().getTimeInMillis();

		if (!parsedOptions.hasOption("m") && !parsedOptions.hasOption("n")
				&& !parsedOptions.hasOption("k")
				&& !parsedOptions.hasOption("r")
				&& parsedOptions.hasOption("i")) {

			importMatrices(parsedOptions.getOptionValue("i"),
					Integer.parseInt(parsedOptions.getOptionValue("t")),
					parsedOptions.hasOption("q"));

			multiplyMatrix(A.length, A[0].length, B[0].length,
					Integer.parseInt(parsedOptions.getOptionValue("t")),
					parsedOptions.hasOption("q"));

		} else if (parsedOptions.hasOption("m") && parsedOptions.hasOption("n")
				&& parsedOptions.hasOption("k") && parsedOptions.hasOption("r")
				&& !parsedOptions.hasOption("i")) {

			int m = Integer.parseInt(parsedOptions.getOptionValue("m"));
			int n = Integer.parseInt(parsedOptions.getOptionValue("n"));
			int k = Integer.parseInt(parsedOptions.getOptionValue("k"));
			int r = Integer.parseInt(parsedOptions.getOptionValue("r"));
			int t = Integer.parseInt(parsedOptions.getOptionValue("t"));
			boolean q = parsedOptions.hasOption("q");
			A = new double[m][n];
			B = new double[n][k];

			MatricesGenerator generator = new MatricesGenerator(A, B, r, t, q);
			generator.generateMatrices();

			multiplyMatrix(m, n, k, t, q);

			if (parsedOptions.hasOption("e")) {

				ExportMatrices(m, n, k, parsedOptions.getOptionValue("e"));

			}

		} else {
			System.out.println("Invalid options!");

			new HelpFormatter().printHelp("java "
					+ validator.getClass().getName(), validator.getHeader(),
					validator.getOptions(), "\n", true);
			return;
		}

		if (parsedOptions.hasOption('o')) {

			ExportProductMatrix(parsedOptions.getOptionValue("o"));

		}

		t2 = Calendar.getInstance().getTimeInMillis();

		System.out.println("Threads used in current run: "
				+ parsedOptions.getOptionValue("t"));
		System.out.println("Total execution time for current run (milis): "
				+ (t2 - t1));

	}

	/*
	 * @Override public void MatricesGenerator(int m, int n, int k, int
	 * numberOfThreads, int numberRange, boolean q) {
	 * 
	 * A = new double[m][n]; B = new double[n][k];
	 * 
	 * MatrixGenerator(m, n, A, numberOfThreads, numberRange, q);
	 * MatrixGenerator(n, k, B, numberOfThreads, numberRange, q);
	 * 
	 * multiplyMatrix(m, n, k, numberOfThreads, q); }
	 */

	/*
	 * @Override public void MatrixGenerator(int m, int n, double[][] a, int
	 * numberOfThreads, int numberRange, boolean q) { int numberOfRows = m /
	 * numberOfThreads; int leftOver = m % numberOfThreads;
	 * MatrixGeneratingThread[] threads = new
	 * MatrixGeneratingThread[numberOfThreads]; if (numberOfThreads > 1) { for
	 * (int i = 0; i < numberOfThreads - 1; i++) { threads[i] = new
	 * MatrixGeneratingThread(i * numberOfRows, a, numberOfRows, n, numberRange,
	 * q); threads[i].start(); }
	 * 
	 * threads[numberOfThreads - 1] = new MatrixGeneratingThread(
	 * (numberOfThreads - 1) * numberOfRows, a, numberOfRows + leftOver, n,
	 * numberRange, q); threads[numberOfThreads - 1].start(); } else {
	 * threads[0] = new MatrixGeneratingThread(0, a, numberOfRows, n,
	 * numberRange, q); threads[0].start(); }
	 * 
	 * for (int i = 0; i < threads.length; i++) { try { threads[i].join(); }
	 * catch (Exception e) { e.printStackTrace(); } } if (!q) {
	 * System.out.println("DONE creating matrix"); } }
	 */

	/**
	 * A method for transposing the right B matrix
	 * 
	 * @param n
	 * @param k
	 */
	private void transpose(int n, int k) {
		tB = new double[k][n];
		for (int i = 0; i < k; i++) {
			for (int j = 0; j < n; j++) {
				tB[i][j] = B[j][i];
			}
		}
	}

	@Override
	public void multiplyMatrix(int m, int n, int k, int numberOfThreads,
			boolean q) {
		long t1 = 0;
		long t2 = 0;
		if (!q) {
			System.out.println("Starting multiplying matrices");
		}
		t1 = Calendar.getInstance().getTimeInMillis();

		transpose(n, k);
		C = new double[m][k];

		int numberOfRows = m / numberOfThreads;
		int leftOver = m % numberOfThreads;

		MultiplyMatrixThread[] mthreads = new MultiplyMatrixThread[numberOfThreads];
		if (numberOfThreads > 1) {
			for (int j = 0; j < numberOfThreads - 1; j++) {
				mthreads[j] = new MultiplyMatrixThread(j * numberOfRows,
						numberOfRows, m, n, k, A, tB, C, q);
				mthreads[j].start();
			}

			mthreads[numberOfThreads - 1] = new MultiplyMatrixThread(
					(numberOfThreads - 1) * numberOfRows, numberOfRows
							+ leftOver, m, n, k, A, tB, C, q);
			mthreads[numberOfThreads - 1].start();
		} else {
			mthreads[0] = new MultiplyMatrixThread(0, numberOfRows, m, n, k, A,
					tB, C, q);
			mthreads[0].start();
		}

		for (int i = 0; i < mthreads.length; i++) {
			try {
				mthreads[i].join();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!q) {
			System.out.println("DONE multiplying matrices");
		}
		t2 = Calendar.getInstance().getTimeInMillis();
		System.out.println("Matrix computation time was (milis): " + (t2 - t1));

	}

	@Override
	public void printProductMatrix(double[][] c) {
		for (double[] row : c) {
			for (double cell : row) {
				System.out.print(cell + " ");
			}
			System.out.println();
		}
	}

	/*
	 * @Override public void MatrixGeneratorWithMathRandom(int m, int n,
	 * double[][] a, int numberOfThreads, int numberRange, boolean q) { int
	 * numberOfRows = m / numberOfThreads; int leftOver = m % numberOfThreads;
	 * MatrixGeneratingThreadWithMathRandom[] threads = new
	 * MatrixGeneratingThreadWithMathRandom[numberOfThreads]; for (int i = 0; i
	 * < numberOfThreads - 1; i++) { threads[i] = new
	 * MatrixGeneratingThreadWithMathRandom(i numberOfRows, a, numberOfRows, n,
	 * numberRange, q); System.out.println("rowspre: " + i * numberOfRows);
	 * threads[i].start(); }
	 * 
	 * if (numberOfThreads > 1) { threads[numberOfThreads - 1] = new
	 * MatrixGeneratingThreadWithMathRandom( (numberOfThreads - 1) *
	 * numberOfRows, a, numberOfRows + leftOver, n, numberRange, q);
	 * System.out.println("rowspre: " + (numberOfThreads - 1) numberOfRows);
	 * threads[numberOfThreads - 1].start(); }
	 * 
	 * for (int i = 0; i < threads.length; i++) { try { threads[i].join(); }
	 * catch (Exception e) { e.printStackTrace(); } }
	 * System.out.println("DONE creating matrix");
	 * 
	 * }
	 */

	/*
	 * @Override public void MatricesGeneratorWithMathRandom(int m, int n, int
	 * k, int numberOfThreads, int numberRange, boolean q) { A = new
	 * double[m][n]; B = new double[n][k]; MatrixGeneratorWithMathRandom(m, n,
	 * A, numberOfThreads, numberRange, q); MatrixGeneratorWithMathRandom(n, k,
	 * B, numberOfThreads, numberRange, q); tB = new double[k][n]; transpose(n,
	 * k); C = new double[m][k]; multiplyMatrix(m, n, k, 4, q); }
	 */

	/*
	 * @Override public double[] toDoubleArray(String line) { // TODO
	 * Auto-generated method stub return null; }
	 */

	@Override
	public void importMatrices(String fileName, int numberOfThreads, boolean q) {

		if (!q) {
			System.out.println("STARTTED  IMPORTING OF MATRICES");
		}

		File file = new File(fileName);

		try (Scanner scan = new Scanner(file)) {

			int m = scan.nextInt();
			int n = scan.nextInt();
			int k = scan.nextInt();
			A = new double[m][n];
			B = new double[n][k];
			scan.nextLine();
			String line;

			importMatrix(A, m, n, scan);
			importMatrix(B, n, k, scan);

			if (!q) {
				System.out.println("DONE READING MATRICES FROM FILE");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void importMatrix(double[][] matrix, int rows, int columns,
			Scanner scanner) {

		String line;

		for (int i = 0; i < rows; i++) {

			line = scanner.nextLine();
			String[] splitLine = line.split(" ");

			for (int j = 0; j < columns; j++) {

				matrix[i][j] = Double.parseDouble(splitLine[j]);

			}
		}
	}

	public void ExportMatrices(int m, int n, int k, String fileName) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
			String newLine = System.getProperty("line.separator");
			String sizes = m + " " + n + " " + k + newLine;
			bw.write(sizes);
			StringBuilder sb = new StringBuilder();
			for (double[] row : A) {
				for (double cell : row) {
					sb.append((cell + " "));
				}
				sb.append(newLine);
			}

			for (double[] row : B) {
				for (double cell : row) {
					sb.append((cell + " "));
				}
				sb.append(newLine);
			}

			bw.write(sb.toString());
			bw.flush();

			System.out.println("Matrices Saved");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ExportProductMatrix(String fileName) {

		long t1 = Calendar.getInstance().getTimeInMillis();

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
			int m = C.length;
			int k = C[0].length;
			String newLine = System.getProperty("line.separator");
			String sizes = m + " " + k + newLine;
			bw.write(sizes);
			StringBuilder sb = new StringBuilder();
			for (double[] row : C) {
				for (double cell : row) {
					sb.append(cell + " ");
				}
				sb.append(newLine);
			}

			bw.write(sb.toString());
			bw.flush();

			long t2 = Calendar.getInstance().getTimeInMillis();
			System.out.printf("DONE Saving matrix C to %s for %d ms%n",
					fileName, (t2 - t1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		MatricesMultiplier m3x = new MatricesMultiplier(args);
		m3x.compute();

	}
}
