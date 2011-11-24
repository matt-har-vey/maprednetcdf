package maprednetcdf.hive;

import java.io.IOException;

import maprednetcdf.io.Metar;
import maprednetcdf.io.MetarSequenceKey;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SequenceFileInputFormat;

/**
 * Old API input format to work with Hive
 */
@SuppressWarnings("deprecation")
public class MetarSequenceFileInputFormatOld implements InputFormat<MetarSequenceKey,Metar> {
	private SequenceFileInputFormat<Text,BytesWritable> delegate;
	
	public MetarSequenceFileInputFormatOld() {
		delegate = new SequenceFileInputFormat<Text,BytesWritable>();
	}
	
	@Override
	public RecordReader<MetarSequenceKey, Metar> getRecordReader(
			InputSplit split, JobConf jc, Reporter reporter) throws IOException {
		return new MetarRecordReaderOld(delegate.getRecordReader(split, jc, reporter));
	}

	@Override
	public InputSplit[] getSplits(JobConf jc, int n) throws IOException {
		return delegate.getSplits(jc, n);
	}

}
