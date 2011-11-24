package mattmath.matrix;

public interface ColumnVector extends Matrix {
	public int getDimension();
	
	public double get(int i);
	public void set(int i, double x);
	
	public ColumnVector copy();
	public ColumnVector timesScalar(double scalar);
	
	public double dotProduct(ColumnVector right);
	public double normSquared();
	public double maxNorm();
}
