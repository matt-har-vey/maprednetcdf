package mattmath.optimization;

import mattmath.matrix.ColumnVector;

public interface GradientOptimizer {
	public ColumnVector optimize(CostAndGradient cg, ColumnVector initial);
}
