package maprednetcdf.job;

import maprednetcdf.io.Metar;
import maprednetcdf.mapreduce.PressureFitMapper;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class PressureFits extends Configured implements Tool {
	@Override
	public int run(String[] args) throws Exception {
		final Job job = new Job(getConf(), getClass().getName());
		job.setJarByClass(Metar.class);
			
		FileInputFormat.addInputPath(job, new Path("/pressures"));
		FileOutputFormat.setOutputPath(job, new Path("/fits"));

		job.setInputFormatClass(SequenceFileInputFormat.class);

		job.setMapperClass(PressureFitMapper.class);
		
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		
		job.waitForCompletion(true);
		
		return 0;
	}
	
	public static void main(String[] args) throws Exception {
		System.exit(
			ToolRunner.run(new PressureFits(), args));
	}
}
