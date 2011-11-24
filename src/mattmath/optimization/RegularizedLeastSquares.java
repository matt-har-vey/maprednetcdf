package mattmath.optimization;

import mattmath.matrix.ColumnVector;
import mattmath.matrix.Matrix;

public class RegularizedLeastSquares implements CostAndGradient {
	private Matrix x;
	private Matrix xTr;
	private ColumnVector y;
	private int m;
	
	private double lambda;
	
	public RegularizedLeastSquares(Matrix x, ColumnVector y, double lambda) {	
		this.x = x;
		this.y = y;
		
		m = y.getDimension();
		xTr = x.transpose();
		
		this.lambda = lambda;
	}
	
	
	@Override
	public double getCost(ColumnVector theta) {
		final ColumnVector thetaReg = theta.copy();
		thetaReg.set(1, 0);
		
		final ColumnVector w = x.rightMultiplyBy(theta);
		w.subtract(y);

		return (w.normSquared() + lambda * thetaReg.normSquared())
			/ (2 * m);
	}

	@Override
	public ColumnVector getGradient(ColumnVector theta) {
		final ColumnVector thetaReg = theta.copy();
		thetaReg.set(1, 0);
		
		final ColumnVector w = x.rightMultiplyBy(theta);
		w.subtract(y);
		
		final ColumnVector u = xTr.rightMultiplyBy(w);
		u.add(thetaReg.timesScalar(lambda));
		u.scalarDivideBy(m);
		
		return u;
	}
}
