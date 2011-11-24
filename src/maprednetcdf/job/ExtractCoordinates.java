package maprednetcdf.job;

import java.io.IOException;
import java.util.GregorianCalendar;

import maprednetcdf.io.Coordinates;
import maprednetcdf.io.Metar;
import maprednetcdf.io.MetarSequenceFileInputFormat;
import maprednetcdf.io.MetarSequenceKey;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class ExtractCoordinates {
	public static class CoordinatesMapper extends
			Mapper<MetarSequenceKey, Metar, Coordinates, Coordinates> {

		private double start;
		private double end;

		public CoordinatesMapper() {
			start = (double) (new GregorianCalendar(2011, 0, 1, 0, 0)
					.getTimeInMillis() / 1000);
			end = (double) (new GregorianCalendar(2011, 0, 31, 23, 59)
					.getTimeInMillis() / 1000);
		}

		int n = 0;

		@Override
		protected void map(MetarSequenceKey key, Metar metar, Context context)
				throws IOException, InterruptedException {

			float lat = metar.getLatitude().get();
			float longi = metar.getLongitude().get();

			if (lat > 25 && lat < 50 && longi > -170 && longi < -114) {
				if (n % 10 == 0) {
					final double t = metar.getTimeObs().get();
					if (start < t && t < end) {
						final Coordinates coords = new Coordinates(
								metar.getLatitude(), metar.getLongitude());
						context.write(coords, coords);
					}
				}
			}
			n++;
		}
	}

	public static class CoordinatesReducer extends
			Reducer<Coordinates, Coordinates, Coordinates, NullWritable> {
		private NullWritable nw = NullWritable.get();
		private int n = 0;

		@Override
		protected void reduce(Coordinates key, Iterable<Coordinates> values,
				Context context) throws IOException, InterruptedException {
			context.write(key, nw);
			n++;
		}
	}

	public static void main(String[] args) throws IOException,
			InterruptedException, ClassNotFoundException {
		final Job job = new Job();
		job.setJarByClass(Metar.class);

		FileInputFormat.addInputPath(job, new Path("/maritime/2011.seq"));
		FileOutputFormat.setOutputPath(job, new Path("/coordinates/maritime"));

		job.setInputFormatClass(MetarSequenceFileInputFormat.class);

		job.setMapperClass(CoordinatesMapper.class);
		job.setReducerClass(CoordinatesReducer.class);

		job.setOutputKeyClass(Coordinates.class);
		job.setOutputValueClass(Coordinates.class);

		job.waitForCompletion(true);
	}
}
