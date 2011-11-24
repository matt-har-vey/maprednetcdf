package maprednetcdf.io;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;

public class MetarSequenceFileInputFormat extends
		InputFormat<MetarSequenceKey, Metar> {

	private SequenceFileInputFormat<Text,BytesWritable> delegate;
	
	public MetarSequenceFileInputFormat() {
		delegate = new SequenceFileInputFormat<Text,BytesWritable>();
	}
	
	@Override
	public RecordReader<MetarSequenceKey, Metar> createRecordReader(
			InputSplit split, TaskAttemptContext context) throws IOException,
			InterruptedException {
		return new MetarRecordReader(delegate.createRecordReader(split, context));
	}

	@Override
	public List<InputSplit> getSplits(JobContext context) throws IOException,
			InterruptedException {
		return delegate.getSplits(context);
	}
}
