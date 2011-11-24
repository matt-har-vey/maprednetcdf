package mattmath.optimization;

import java.util.Iterator;
import java.util.LinkedList;

import maprednetcdf.io.Metar;
import mattmath.matrix.ColumnVector;
import mattmath.matrix.Matrix;
import mattmath.matrix.MatrixFactory;
import mattmath.statistics.Moments;

public class PressureFitter {
	private int degree = 5;
	private double lambda = 2;
	
	private Matrix featureMatrix;
	private ColumnVector values;
	private ColumnVector theta;
	
	private Moments mlat;
	private Moments mlong;
	private Moments mpress;
	
	public void initialize(Iterable<Metar> metars)
	{
		final LinkedList<Float> lats = new LinkedList<Float>();
		final LinkedList<Float> longs = new LinkedList<Float>();
		final LinkedList<Float> presses = new LinkedList<Float>();	
		
		int n = 0;
		
		for (final Metar metar : metars) {
			final Float latitude = metar.getLatitude().get();
			final Float longitude = metar.getLongitude().get();
			final Float pressure = metar.getSeaLevelPress().get();
			
			if (pressure != null && !pressure.isNaN()
					&& latitude != null && !latitude.isNaN()
					&& longitude != null && !longitude.isNaN()) {
				
				lats.add(latitude);
				longs.add(longitude);
				presses.add(pressure);
				
				n += 1;
			}
		}
		
		initFromComponents(lats, longs, presses, n);
	}

	public void initFromComponents(final Iterable<? extends Number> lats,
			final Iterable<? extends Number> longs, final Iterable<? extends Number> presses,
			int n) {
		mlat = Moments.get(lats);
		mlong = Moments.get(longs);
		mpress = Moments.get(presses);
		
		final LinkedList<ColumnVector> featureRows = new LinkedList<ColumnVector>();
		values = MatrixFactory.zeroVector(n);

		final Iterator<? extends Number> latIter = lats.iterator();
		final Iterator<? extends Number> longIter = longs.iterator();
		final Iterator<? extends Number> pressIter = presses.iterator();
		
		for (int i = 0; i < n; i++) {
			double x = normalize(latIter.next().doubleValue(), mlat);
			double y = normalize(longIter.next().doubleValue(), mlong);
			double z = normalize(pressIter.next().doubleValue(), mpress);
			
			featureRows.add(newFeatureVector(x,y));
			values.set(i + 1, z);
		}
		
		featureMatrix = MatrixFactory.matrixByRows(featureRows);
	}
	
	public void train()
	{	
		final RegularizedLeastSquares ls =
			new RegularizedLeastSquares(featureMatrix, values, lambda);
		
		final ColumnVector initial =
			MatrixFactory.zeroVector(featureMatrix.getColumns());
		
		final GradientOptimizer optimizer = new BFGS();
		theta = optimizer.optimize(ls, initial);
	}
	
	public ColumnVector getTheta() {
		return theta;
	}

	public Moments getMlat() {
		return mlat;
	}

	public Moments getMlong() {
		return mlong;
	}

	public Moments getMpress() {
		return mpress;
	}

	public double interpolate(double latitude, double longitude)
	{
		double x = normalize(latitude, mlat);
		double y = normalize(longitude, mlong);
		double z = theta.dotProduct(newFeatureVector(x, y));
		
		return unnormalize(z, mpress);
	}
	
	/**
	 * Evaluates the fitting function on a 40-by-40 grid bounded on the
	 * specified limits.
	 */
	public double[][] grid(double xmin, double xmax, double ymin,
			double ymax) {
		final int n = 40;
		double[][] points = new double[n * n][];

		double x = xmin;
		double y = ymin;
		double deltaX = (xmax - xmin) / n;
		double deltaY = (ymax - ymin) / n;

		int k = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; i <= n; j++) {
				final double[] point = new double[3];

				point[0] = x;
				point[1] = y;
				point[2] = interpolate(x, y);

				points[k] = point;
				k++;
				y += deltaY;
			}

			x += deltaX;
		}

		return points;
	}

	private ColumnVector newFeatureVector(double x, double y) {
		// terms of complete polynomial
		int dimension = (degree + 1) * (degree + 2) / 2;
		
		final ColumnVector v = MatrixFactory.zeroVector(dimension);
		
		int index = 1;
		for (int i = 0; i <= degree; i++) {
			for(int j = 0; j <= i; j++) {
				v.set(index, Math.pow(x, i) * Math.pow(y, j));
				index++;
			}
		}
		
		return v;
	}
	
	private double normalize(double d, Moments m) {
		return (d - m.mean) / m.stdev;
	}
	
	private double unnormalize(double z, Moments m) {
		return z * m.stdev + m.mean;
	}
}
