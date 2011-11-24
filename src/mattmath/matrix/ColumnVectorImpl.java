package mattmath.matrix;

class ColumnVectorImpl extends MatrixImpl implements ColumnVector {

	ColumnVectorImpl(int rows) {
		super(rows, 1);
	}

	@Override
	public int getDimension() {
		return getRows();
	}

	@Override
	public double get(int i) {
		return get(i, 1);
	}

	@Override
	public void set(int i, double x) {
		set(i, 1, x);
	}

	@Override
	public double dotProduct(ColumnVector right) {
		final Matrix m = transpose().rightMultiplyBy(right);
		return m.get(1, 1);
	}

	@Override
	public double normSquared() {
		return dotProduct(this);
	}

	@Override
	public double maxNorm() {
		double norm = 0;
		
		for (int i = 1; i <= getDimension(); i++) {
			final double e = get(i);
			if (e > norm)
				norm = e;
		}
		
		return norm;
	}

	@Override
	public ColumnVector copy() {
		int d = getDimension();
		
		final ColumnVector copy = new ColumnVectorImpl(d);
		for (int i = 1; i <= d; i++)
			copy.set(i,get(i));
		
		return copy;
	}
	
	@Override
	public ColumnVector timesScalar(double scalar) {
		int d = getDimension();
		
		final ColumnVector copy = new ColumnVectorImpl(d);
		for (int i = 1; i <= d; i++)
			copy.set(i,get(i) * scalar);
		
		return copy;
	}
}
