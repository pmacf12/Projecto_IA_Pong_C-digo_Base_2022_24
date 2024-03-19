package breakout;

import java.util.Arrays;


public class Individual {
	private int[] cols;
	private static final int COLUMNS = 8;

	public Individual(int[] cols) {
		this.cols = cols;
	}
	
	public Individual() {
		this.cols = new int[COLUMNS];
		for(int cc = 0; cc < COLUMNS; cc++) 
			this.cols[cc] = (int) (Math.random() * COLUMNS);
	}

	public int[] getCols() {
		return cols;
	}

	public void setCols(int[] cols) {
		this.cols = cols;
	}
	
	public int fitness() {
		int fit = 0;
		for(int ii = 0; ii < COLUMNS; ii++) 
			for(int jj = ii+1; jj < COLUMNS; jj++) 
					if((getCols()[ii] != getCols()[jj]) && (getCols()[jj] != getCols()[ii]+(jj-ii)) && (getCols()[jj] != getCols()[ii]-(jj-ii))) 
						fit ++;
		return fit;
	}
	
	public Individual mutate() {
		int randomcol = (int) Math.random() * (Genetic.DIMPOPULATION-1);
		int randomrow = (int) Math.random() * (Genetic.DIMPOPULATION-1);
		this.getCols()[randomcol] = randomrow;
		return this;
	}

	@Override
	public String toString() {
		return "Individual [cols=" + Arrays.toString(cols) + "]";
	}
}
