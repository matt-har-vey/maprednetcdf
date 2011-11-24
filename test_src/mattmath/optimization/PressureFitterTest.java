package mattmath.optimization;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

import maprednetcdf.io.Metar;
import mattmath.matrix.ColumnVector;
import mattmath.optimization.PressureFitter;

import org.junit.Test;

public class PressureFitterTest {
	@Test
	public void testFit() throws IOException {
		final PressureFitter fitter = new PressureFitter();
		fitter.initialize(loadMetars());
		fitter.train();
		
		final ColumnVector theta = fitter.getTheta();
		assertTrue(theta.normSquared() > 0);
	}

	private Iterable<Metar> loadMetars() throws IOException {
		final LinkedList<Metar> metars = new LinkedList<Metar>();
		
		final BufferedReader br = new BufferedReader(new InputStreamReader(
				getClass().getResourceAsStream("1600.csv")));
		
		String line;
		while ((line = br.readLine()) != null) {
			final StringTokenizer st = new StringTokenizer(line, "\t");
			
			final float longitude = Float.valueOf(st.nextToken());
			final float latitude = Float.valueOf(st.nextToken());
			final float pressure = Float.valueOf(st.nextToken());
			
			final Metar metar = new Metar();
			metar.getLatitude().set(latitude);
			metar.getLongitude().set(longitude);
			metar.getSeaLevelPress().set(pressure);
			
			metars.add(metar);
		}
		
		return metars;
	}
}
