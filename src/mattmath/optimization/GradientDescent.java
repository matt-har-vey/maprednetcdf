package mattmath.optimization;

import mattmath.matrix.ColumnVector;
import mattmath.matrix.MatrixFactory;

/**
 * Ordinary gradient descent that attempts to calibrate its own learning rate -
 * can be pretty slow.
 */
public class GradientDescent extends OptimizerSupport implements GradientOptimizer {
	private final double tolerance = 1e-3;

	@Override
	public ColumnVector optimize(CostAndGradient cg, ColumnVector initial) {
		ColumnVector v = initial.copy();
			
		while (true) {

			final double cost = cg.getCost(v);
			final ColumnVector grad = cg.getGradient(v);
			
			if (grad.maxNorm() < tolerance)
				break;
			
			// Direction is negative gradient
			final ColumnVector p = MatrixFactory.zeroVector(grad.getDimension());
			p.subtract(grad);
			
			v = backtrackWolfe(cg, grad, cost, v, p).xnew;
		}
		
		return v;
	}
}
