package mattmath.matrix;

/**
 * A two-dimensional matrix of doubles. Methods with void returns modify the
 * matrix in place to avoid copying memory. Methods with Matrix or ColumnVector
 * returns allocate new memory and leave the original untouched.
 */
public interface Matrix {
	public int getRows();
	public int getColumns();
	
	public double get(int row, int column);
	public void set(int row, int column, double entry);
	
	public Matrix copy();
	public Matrix transpose();
	public Matrix timesScalar(double scalar);
	public Matrix rightMultiplyBy(Matrix right);
	
	public ColumnVector rightMultiplyBy(ColumnVector right);	

	public void add(Matrix m);
	public void subtract(Matrix m);
	public void scalarMultiplyBy(double scalar);
	public void scalarDivideBy(double x);
}
