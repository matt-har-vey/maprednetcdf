package mattmath.matrix;

import java.util.Arrays;
import java.util.List;

class MatrixImpl implements Matrix {
	private int rows;
	private int columns;
	private double[][] data;
	
	MatrixImpl(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		
		data = new double[rows][];
		for (int i = 0; i < rows; i++) {
			final double[] row = new double[columns];
			Arrays.fill(row, 0);
			data[i] = row;
		}
	}
	
	MatrixImpl(List<ColumnVector> rows) {
		this.rows = rows.size();
		this.columns = rows.get(0).getDimension();
		
		data = new double[this.rows][];
		
		int r = 0;
		for (final ColumnVector row : rows) {
			final double[] rowArray = new double[columns];
			
			for (int i = 0; i < columns; i++) {
				rowArray[i] = row.get(i+1);
			}
			
			data[r] = rowArray;
			r++;
		}
	}
	
	private MatrixImpl(MatrixImpl copy) {
		rows = copy.rows;
		columns = copy.columns;
		data = new double[rows][];
		
		for (int i = 0; i < rows; i++) {
			data[i] = Arrays.copyOf(copy.data[i], columns);
		}
	}

	@Override
	public int getRows() {
		return rows;
	}

	@Override
	public int getColumns() {
		return columns;
	}

	@Override
	public Matrix rightMultiplyBy(Matrix right) {
		if (right.getRows() != getColumns())
			throw new IllegalArgumentException(getColumns() + " columns on left, " + right.getRows() + " rows on right");
			
		final Matrix product = new MatrixImpl(getRows(), right.getColumns());
		multiplyInternal(right, product);	
		return product;
	}

	private void multiplyInternal(Matrix right, Matrix product) {
		
		int d = right.getRows();
		int r = getRows();
		int c = product.getColumns();
		
		for (int i = 1; i <= r; i++) {
			for (int j = 1; j <= c; j++) {
				double entry = 0;
				
				for (int k = 1; k <= d; k++) {
					entry += get(i,k) * right.get(k,j);
				}
				
				product.set(i, j, entry);
			}
		}
		
	}

	@Override
	public double get(int row, int column) {
		return data[row - 1][column - 1];
	}

	@Override
	public void set(int row, int column, double entry) {
		data[row - 1][column - 1] = entry;
	}

	@Override
	public Matrix transpose() {
		int c = getColumns();
		int r = getRows();
		
		final MatrixImpl transpose = new MatrixImpl(c, r);
		
		for (int i = 1; i <= r; i++) {
			for (int j = 1; j <= c; j++) {
				transpose.set(j, i, get(i,j));
			}
		}
		
		return transpose;
	}

	@Override
	public Matrix copy() {
		return new MatrixImpl(this);
	}

	@Override
	public Matrix timesScalar(double scalar) {
		int r = getRows();
		int c = getColumns();
		
		final Matrix m = new MatrixImpl(r, c);
		
		for (int i = 1; i <= r; i++)
			for (int j = 1; j <= c; j++)
				m.set(i,j, scalar * get(i,j));
		
		return m;
	}

	@Override
	public ColumnVector rightMultiplyBy(ColumnVector right) {
		int r = getRows();
		int c = getColumns();
		int d = right.getDimension();
		
		if (c != d)
			throw new IllegalArgumentException(c + " column matrix times " + d + "-vector");
		
		final ColumnVector w = new ColumnVectorImpl(r);
		multiplyInternal(right, w);
		return w;
	}

	@Override
	public void add(Matrix m) {
		int r = getRows();
		int c = getColumns();
		
		if (r != m.getRows() || c != m.getColumns())
			throw new IllegalArgumentException("Dimension mismatch");
		
		for (int i = 1; i <= r; i++)
			for (int j = 1; j <= c; j++)
				set(i,j, get(i,j) + m.get(i,j));
	}

	@Override
	public void subtract(Matrix m) {
		int r = getRows();
		int c = getColumns();
		
		if (r != m.getRows() || c != m.getColumns())
			throw new IllegalArgumentException("Dimension mismatch");
		
		for (int i = 1; i <= r; i++)
			for (int j = 1; j <= c; j++)
				set(i,j, get(i,j) - m.get(i,j));
	}
	
	@Override
	public void scalarMultiplyBy(double x) {
		int r = getRows();
		int c = getColumns();
		
		for (int i = 1; i <= r; i++)
			for (int j = 1; j <= c; j++)
				set(i,j, get(i,j) * x);
	}

	@Override
	public void scalarDivideBy(double x) {
		int r = getRows();
		int c = getColumns();
		
		for (int i = 1; i <= r; i++)
			for (int j = 1; j <= c; j++)
				set(i,j, get(i,j) / x);
	}

	@Override
	public String toString() {
		if (data != null) {
			final StringBuilder sb = new StringBuilder();
			
			for (double[] row : data) {
				if (row != null) {
					for (double d : row) {
						sb.append(d);
						sb.append("\t");
					}
					
					sb.append("\n");
				}
			}
			
			return sb.toString();
		} else {
			return super.toString();
		}
	}
}
