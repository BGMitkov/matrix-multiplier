package main;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A Thread class for generating a matrix by given dimensions and a number of
 * rows for the current thread to fill from a starting row.
 * 
 * @author bgmitkov
 *
 */
public class MatrixGeneratingThread extends Thread {

	private boolean quiet;
	private int startingRow;
	private int rowsToFill;
	private int numberOfColumns;
	private int maxCellValue;
	private double[][] A;
	private long t1, t2;

	public MatrixGeneratingThread(int startingRow, double[][] a,
			int rowsToFill, int numberOfColumns, int maxCellValue, boolean quiet) {

		this.startingRow = startingRow;
		A = a;
		this.rowsToFill = rowsToFill;
		this.numberOfColumns = numberOfColumns;
		this.maxCellValue = maxCellValue;
		this.quiet = quiet;

	}

	@Override
	public void run() {
		if (!quiet) {

			System.out.println(Thread.currentThread() + " started");
			t1 = Calendar.getInstance().getTimeInMillis();

		}

		for (int i = startingRow; i < startingRow + rowsToFill; i++) {

			for (int j = 0; j < numberOfColumns; j++) {

				A[i][j] = ThreadLocalRandom.current().nextInt(maxCellValue);

			}
		}

		if (!quiet) {

			t2 = Calendar.getInstance().getTimeInMillis();
			System.out.println(Thread.currentThread() + " stopped");
			System.out.println(Thread.currentThread()
					+ " execution time was (milis): " + (t2 - t1));

		}
	}
}
