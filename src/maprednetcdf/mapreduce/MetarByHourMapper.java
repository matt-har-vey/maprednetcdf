package maprednetcdf.mapreduce;

import java.io.IOException;

import maprednetcdf.io.Metar;
import maprednetcdf.io.MetarSequenceKey;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;

public abstract class MetarByHourMapper<T extends Writable> extends
		Mapper<MetarSequenceKey, Metar, LongWritable, T> {
	
	@Override
	protected void map(MetarSequenceKey key, Metar metar, Context context)
			throws IOException, InterruptedException {

		float lat = metar.getLatitude().get();
		float longi = metar.getLongitude().get();

		if (lat > 25 && lat < 50 && longi > -170 && longi < -114) {
			long hour = (long) (metar.getTimeObs().get() / 3600);
			final LongWritable lw = new LongWritable(hour);
			context.write(lw, value(metar));
		}
	}
	
	protected abstract T value(Metar metar);
}
