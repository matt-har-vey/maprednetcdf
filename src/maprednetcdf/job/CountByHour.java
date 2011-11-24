package maprednetcdf.job;

import java.io.IOException;
import java.util.Iterator;

import maprednetcdf.io.Metar;
import maprednetcdf.io.MetarSequenceFileInputFormat;
import maprednetcdf.mapreduce.MetarByHourMapper;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class CountByHour {
	public static class CountByHourMapper extends
			MetarByHourMapper<IntWritable> {
		private IntWritable iw = new IntWritable(1);

		protected IntWritable value(Metar metar) {
			return iw;
		}
	}

	public static class CountByHourReducer extends
			Reducer<LongWritable, IntWritable, LongWritable, IntWritable> {

		@Override
		protected void reduce(LongWritable hour, Iterable<IntWritable> counts,
				Context context) throws IOException, InterruptedException {

			int count = 0;
			final Iterator<IntWritable> iter = counts.iterator();
			while (iter.hasNext())
				count += iter.next().get();

			context.write(hour, new IntWritable(count));
		}
	}

	public static void main(String[] args) throws IOException,
			InterruptedException, ClassNotFoundException {
		final Job job = new Job();
		job.setJarByClass(Metar.class);

		FileInputFormat.addInputPath(job, new Path("/madis/maritime"));
		FileInputFormat.addInputPath(job, new Path("/madis/metar"));
		FileOutputFormat.setOutputPath(job, new Path("/hourcounts"));

		job.setInputFormatClass(MetarSequenceFileInputFormat.class);

		job.setMapperClass(CountByHourMapper.class);
		job.setReducerClass(CountByHourReducer.class);
		job.setCombinerClass(CountByHourReducer.class);

		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(IntWritable.class);
		
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.waitForCompletion(true);
	}
}
