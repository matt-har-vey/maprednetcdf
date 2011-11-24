package mattmath.statistics;

import java.util.ArrayList;

public class Moments {
	public double mean;
	public double stdev;

	private Moments() {
	}

	public static Moments get(double[] data) {
		final ArrayList<Double> list = new ArrayList<Double>(data.length);
		for (double d : data)
			list.add(d);
		return Moments.get(list);
	}

	public static Moments get(Iterable<? extends Number> data) {
		long n = 0;

		double sum = 0;
		double sumsq = 0;

		for (Number d : data) {
			sum += d.doubleValue();
			n += 1;
		}

		double mean = sum / n;

		for (Number d : data) {
			double diff = d.doubleValue() - mean;
			sumsq += diff * diff;
		}

		double stdev = Math.sqrt(sumsq / n);

		final Moments moments = new Moments();
		moments.mean = mean;
		moments.stdev = stdev;

		return moments;
	}
}
