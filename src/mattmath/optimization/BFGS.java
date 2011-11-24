package mattmath.optimization;

import mattmath.matrix.ColumnVector;
import mattmath.matrix.Matrix;
import mattmath.matrix.MatrixFactory;

public class BFGS extends OptimizerSupport implements GradientOptimizer {
	private static final double tolerance = 1e-5;
	
	@Override
	public ColumnVector optimize(CostAndGradient cg, ColumnVector initial) {
		// Follow pseudocode from Wikipedia: http://en.wikipedia.org/wiki/BFGS_method
		
		final int d = initial.getDimension();
		ColumnVector x = initial.copy();
		
		Matrix b = MatrixFactory.identity(d);
		Matrix binv = MatrixFactory.identity(d);
		
		ColumnVector grad = cg.getGradient(x);
		while (true) {				
			// Step 1
			ColumnVector p = binv.rightMultiplyBy(grad);
			p.scalarMultiplyBy(-1);
			
			// Step 2		
			final LineSearchResult lsRes = cubicLineSearch(
					cg, x, p);
			
			// Step 3
			final ColumnVector xnew = lsRes.xnew;
			final ColumnVector s = p.timesScalar(lsRes.alpha);
			
			// Step 4
			final ColumnVector newGrad = cg.getGradient(xnew);
			if (newGrad.maxNorm() < tolerance)
				break;
			final ColumnVector y = newGrad.copy();
			y.subtract(grad);
			
			// Step 5
			final Matrix yT = y.transpose();
			final Matrix sT = s.transpose();
			
			final Matrix newB = b.copy();
			
			final Matrix firstTerm = y.rightMultiplyBy(yT);
			firstTerm.scalarDivideBy(y.dotProduct(s));		
			
			final Matrix secondTerm = b.rightMultiplyBy(s).
				rightMultiplyBy(sT.rightMultiplyBy(b));
			secondTerm.scalarDivideBy(s.dotProduct(b.rightMultiplyBy(s)));
			
			newB.add(firstTerm);
			newB.subtract(secondTerm);
			
			// B and its inverse for the next step, inverse first
			double sDotY = s.dotProduct(y);
			final Matrix invFirstTerm = s.rightMultiplyBy(sT);
			invFirstTerm.scalarMultiplyBy(
					(sDotY + y.dotProduct(binv.rightMultiplyBy(y))) /
					(sDotY * sDotY)
			);
			
			final Matrix invSecondTerm = binv.rightMultiplyBy(y).rightMultiplyBy(sT);
			invSecondTerm.add(
					s.rightMultiplyBy(yT.rightMultiplyBy(binv)));
			invSecondTerm.scalarDivideBy(sDotY);
			
			binv.add(invFirstTerm);
			binv.subtract(invSecondTerm);
			
			// Update b to the new value now done using the old one in updating binv.
			b = newB;
			
			x = xnew;
			grad = newGrad;
		}
		
		return x;
	}

}
