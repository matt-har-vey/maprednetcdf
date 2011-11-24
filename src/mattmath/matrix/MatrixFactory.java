package mattmath.matrix;

import java.util.List;

public class MatrixFactory {
	private MatrixFactory() {}
	
	public static Matrix zeroMatrix(int rows, int columns) {
		return new MatrixImpl(rows, columns);
	}
	
	public static Matrix identity(int n) {
		final Matrix m = new MatrixImpl(n,n);
		for (int i = 1; i <= n; i++)
			m.set(i,i,1);
		return m;
	}
	
	public static ColumnVector zeroVector(int dimension) {
		return new ColumnVectorImpl(dimension);
	}

	public static Matrix matrixByRows(List<ColumnVector> rows) {
		return new MatrixImpl(rows);
	}
}
