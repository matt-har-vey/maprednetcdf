package maprednetcdf.job;

import maprednetcdf.io.Metar;
import maprednetcdf.io.MetarSequenceFileInputFormat;
import maprednetcdf.io.PressureCollection;
import maprednetcdf.mapreduce.GroupMetarByHourMapper;
import maprednetcdf.mapreduce.PressureCollectionReducer;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class PressureByHourExtract extends Configured implements Tool {
	@Override
	public int run(String[] args) throws Exception {
		final Job job = new Job(getConf(), getClass().getName());
		job.setJarByClass(Metar.class);

		final FileSystem fs = FileSystem.get(getConf());
		
		for (FileStatus stat : fs.globStatus(new Path("/madis/metar/*.seq"))) {
			FileInputFormat.addInputPath(job, stat.getPath());
		}
		
		FileInputFormat.addInputPath(job, new Path("/madis/maritime/2011.seq"));
		FileOutputFormat.setOutputPath(job, new Path("/pressures"));

		job.setInputFormatClass(MetarSequenceFileInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		job.setMapperClass(GroupMetarByHourMapper.class);
		job.setReducerClass(PressureCollectionReducer.class);	

		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Metar.class);
		
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(PressureCollection.class);
		
		job.waitForCompletion(true);
		
		return 0;
	}
	
	public static void main(String[] args) throws Exception {
		System.exit(
			ToolRunner.run(new PressureByHourExtract(), args));
	}
}
