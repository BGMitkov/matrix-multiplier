package main;

import java.util.Calendar;

public class MultiplyMatrixThread extends Thread {
	
	private int startingRow;
    private int numberOfRows;
    private int m;
    private int n;
    private int k;	
    private boolean q;
    private double [][] A;
    private double [][] tB;
    private double [][] C;
    private long t1,t2;
	
    public MultiplyMatrixThread(int startingRow, int numberOfRows, int m, int n, int k
    		, double[][] a, double[][] tb, double[][] c, boolean q) {
		this.startingRow = startingRow;
		this.numberOfRows = numberOfRows;
		this.m = m;
		this.n = n;
		this.k = k;
		this.q = q;
		A = a;
		tB = tb;
		C = c;
		
	}
    
    public double rowByColumn(double[] row, double[] column) {
    	double cell = 0.0;
    	for (int i = 0; i < column.length; i++) {
			cell += row[i]*column[i];
		}
		return cell;
	}
    
	@Override
	public void run() {
		if(!q){
			System.out.println(Thread.currentThread() + " started");
			t1 = Calendar.getInstance().getTimeInMillis();
		}
		for(int i = startingRow; i < startingRow + numberOfRows; i++){
			for(int j = 0; j < k; j++){
				C[i][j] = rowByColumn(A[i], tB[j]) ;
			}
		}
		if(!q){
			t2 = Calendar.getInstance().getTimeInMillis();
			System.out.println(Thread.currentThread() + " stopped");
			System.out.println(Thread.currentThread() + " execution time was (milis): " + (t2 - t1));	
		}
	}
}
