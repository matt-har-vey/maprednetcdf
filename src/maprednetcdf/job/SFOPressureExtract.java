package maprednetcdf.job;

import java.io.IOException;

import maprednetcdf.io.Metar;
import maprednetcdf.io.MetarSequenceFileInputFormat;
import maprednetcdf.io.MetarSequenceKey;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SFOPressureExtract extends Configured implements Tool {
	public static class PressureMapper extends
			Mapper<MetarSequenceKey, Metar, DoubleWritable, FloatWritable> {

		@Override
		protected void map(MetarSequenceKey key, Metar value, Context context)
				throws IOException, InterruptedException {
			
			if ("KSFO".equals(value.getStationName().toString())) {
				final FloatWritable wPress = value.getSeaLevelPress();
				final Float pressure = wPress.get();
				if (!pressure.isNaN()) {
					context.write(value.getTimeObs(), wPress);
				}
			}
			
		}
	}

	public static class PressureReducer extends
			Reducer<DoubleWritable, FloatWritable, DoubleWritable, FloatWritable> {

		@Override
		protected void reduce(DoubleWritable time, Iterable<FloatWritable> pressures,
				Context context)
				throws IOException, InterruptedException {
			
			long n = 0;
			float average = 0;
			
			for (final FloatWritable pressure : pressures) {
				final Float p = pressure.get();
				if (!p.isNaN()) {
					n += 1;
					average += p;
				}
			}
			
			if (n > 0) {
				average = average / n;
				context.write(time, new FloatWritable(average));
			}
		}
	}

	@Override
	public int run(String[] arg0) throws Exception {
		final Job job = new Job(getConf());
		job.setJarByClass(Metar.class);
	
		FileInputFormat.addInputPath(job, new Path(
				"/madis/metar"));
		FileOutputFormat.setOutputPath(job, new Path(
				"/sfo"));
	
		job.setInputFormatClass(MetarSequenceFileInputFormat.class);
	
		job.setMapperClass(PressureMapper.class);
		job.setReducerClass(PressureReducer.class);
	
		job.setOutputKeyClass(DoubleWritable.class);
		job.setOutputValueClass(FloatWritable.class);
	
		job.waitForCompletion(true);
		
		return 0;
	}

	public static void main(String[] args) throws Exception {
		System.exit(ToolRunner.run(new SFOPressureExtract(), args));
	}
}
