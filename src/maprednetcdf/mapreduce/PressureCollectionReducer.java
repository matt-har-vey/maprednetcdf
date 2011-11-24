package maprednetcdf.mapreduce;

import java.io.IOException;

import maprednetcdf.io.Metar;
import maprednetcdf.io.PressureCollection;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

public class PressureCollectionReducer
	extends Reducer<LongWritable,Metar,LongWritable,PressureCollection> {

	@Override
	protected void reduce(LongWritable lw, Iterable<Metar> metars,
			Context context) throws IOException, InterruptedException {
		
		final PressureCollection collection = new PressureCollection();
		
		for (final Metar metar : metars) {
			if (ok(metar.getLatitude()) && ok(metar.getLongitude()) &&
					ok(metar.getSeaLevelPress())) {
				
				collection.add(metar.getLatitude().get(),
						metar.getLongitude().get(),
						metar.getSeaLevelPress().get());
				
			}
		}
		
		if (collection.size() > 0) {
			context.write(lw, collection);
		}
	}

	private boolean ok(FloatWritable fw) {
		return fw != null && !Float.valueOf(fw.get()).isNaN();
	}
}
