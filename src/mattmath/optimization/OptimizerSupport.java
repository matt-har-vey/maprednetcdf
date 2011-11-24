package mattmath.optimization;

import mattmath.matrix.ColumnVector;

class OptimizerSupport {
	private static final double wolfeInitialAlpha = 0.001;
	private static final double wolfeMinAlpha = 1e-8;
	private static final double wolfeTau = 0.5;
	private static final double wolfeC1 = 1e-4;
	private static final double wolfeC2 = 0.9;
	
	private static final double decInitialAlpha = 10;
	private static final double decTau = 0.5;
	private static final double decMinAlpha = 1e-13;
	
	class LineSearchResult {
		double alpha;
		ColumnVector xnew;
	}

	/**
	 * Backtracking line search on Wolfe conditions
	 * (http://en.wikipedia.org/wiki/Wolfe_conditions). This tries to find a
	 * good alpha_k for the update x_{k+1} = x_k + alpha_k * p_k.
	 * 
	 * @param cg
	 *            used to re-evaluate while backtracking alpha
	 * @param gradK
	 *            to avoid re-evaluating at xK
	 * @param costK
	 *            to avoid re-evaluating at xK
	 * @param xK
	 *            where the step begins
	 * @param pK
	 *            the search direction, which is allowed to be arbitrary
	 * @return the vector x_{k+1}, stopping on the Wolfe conditions and the
	 *         alpha used to obtain it
	 */
	 LineSearchResult backtrackWolfe(
			CostAndGradient cg, ColumnVector gradK,
			double costK, ColumnVector xK, ColumnVector pK) {
		
		double alpha = wolfeInitialAlpha;		
		
		ColumnVector newX, newGrad;
		double newCost;
		
		newX = pK.timesScalar(alpha);
		newX.add(xK);
		
		newCost = cg.getCost(newX);
		newGrad = cg.getGradient(newX);
		
		while (!(
				
			(newCost <= costK + wolfeC1 * alpha * pK.dotProduct(gradK)) &&
			(pK.dotProduct(newGrad) >= wolfeC2 * pK.dotProduct(gradK))
			
		)) {
			
			alpha *= wolfeTau;
			
			newX = pK.timesScalar(alpha);
			newX.add(xK);
			
			newCost = cg.getCost(newX);
			newGrad = cg.getGradient(newX);

			if (alpha < wolfeMinAlpha)
				break;
		}
		
		final LineSearchResult res = new LineSearchResult();
		res.xnew = newX;
		res.alpha = alpha;
		
		return res;
	}
	
	/**
	 * Simple backtracking line search that halves the step size until the
	 * function decreases.
	 */
	LineSearchResult backtrackDecrease(CostAndGradient cg,
			ColumnVector xK, ColumnVector pK) {

		double alpha = decInitialAlpha;
		double costK = cg.getCost(xK);

		ColumnVector newX = xK.copy();
		newX.add(pK.timesScalar(alpha));
		double newCost = cg.getCost(newX);

		while (newCost > costK && alpha > decMinAlpha) {
			alpha *= decTau;

			newX = xK.copy();
			newX.add(pK.timesScalar(alpha));
			newCost = cg.getCost(newX);
		}

		final LineSearchResult res = new LineSearchResult();
		res.alpha = alpha;
		res.xnew = newX;

		return res;
	}
	
	LineSearchResult cubicLineSearch(CostAndGradient cg, ColumnVector xK, ColumnVector pK) {
		
		double alpha = 1;
		
		ColumnVector d = pK.copy();
		d.scalarDivideBy(Math.sqrt(d.normSquared()));
		
		double oldCost = cg.getCost(xK);
		double oldSlope = cg.getGradient(xK).dotProduct(d);
		
		ColumnVector newX = xK.copy();
		xK.add(pK.timesScalar(alpha));		
		
		double newSlope = cg.getGradient(newX).dotProduct(d);
		double newCost = cg.getCost(newX);
		double qTsK = alpha * (newSlope - oldSlope);
		
		double alphaC = estimateMinCubic(
			0, oldCost, oldSlope, alpha, newCost, newSlope);
		
		// from Matlab help - no idea where the magic numbers come from
		if (newSlope >= 0) {
			if (newCost > oldCost) {
				if (alpha < 0.1) {
					alpha = alphaC / 2;
				} else {
					alpha = alphaC;
				}
			} else {
				if (qTsK >= 0) {
					alpha = Math.min(1, alphaC);
				} else {
					alpha = 0.9*alphaC;
				}
			}
		} else {
			if (newCost < oldCost) {
				if (qTsK >= 0) {
					double p = 1 + qTsK - newSlope + Math.min(0, alpha);
					alpha = Math.min(2, 1.2*alphaC);
					alpha = Math.min(alpha, p);
				} else {
					alpha = Math.min(2, Math.max(1.5, alpha));
					alpha = Math.min(alpha, alphaC);
				}
			} else {
				alpha = Math.min(alphaC, alpha / 2);
			}
		}
		
		newX = xK.copy();
		newX.add(pK.timesScalar(alpha));
		
		final LineSearchResult res = new LineSearchResult();
		res.alpha = alpha;
		res.xnew = newX;
		
		return res;
	}
	
	/**
	 * Estimates where the minimum occurs in an interval based on function value
	 * and derivatives at the endpoints.
	 */
	double estimateMinCubic(double x1, double y1, double yp1,
			double x2, double y2, double yp2) {
		
		// Evaluate expansion around (x-a) at both points to get linear system
		// Solve on paper
		double u = x2 - x1;

		double A = (yp2 - yp1) / (u*u) - 2 * (y2 - y1) / u + 2 * yp1;
		double B = 3 * ((y2 - y1)/(u*u) - yp1 / u) - (yp2 - yp1) / u;
		double C = yp1;
		// D is irrelevant
		
		if (A == 0) {
			if (B == 0) {
				if (y1 < y2)
					return x1;
				else
					return x2;
			} else {
				return x1 - C / (2*B);
			}
		} else {
			double discrim = B*B-3*A*C;
			if (discrim < 0) {
				if (y1 < y2)
					return x1;
				else
					return x2;
			} else {
				return x1 + ((-1)*B + Math.sqrt(discrim)) / (3*A);
			}
		}
	}
}
