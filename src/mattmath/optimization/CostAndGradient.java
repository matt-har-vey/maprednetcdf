package mattmath.optimization;

import mattmath.matrix.ColumnVector;

public interface CostAndGradient {
	public double getCost(ColumnVector theta);
	public ColumnVector getGradient(ColumnVector theta);
}
