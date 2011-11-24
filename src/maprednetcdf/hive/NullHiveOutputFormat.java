package maprednetcdf.hive;

import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.FileSinkOperator.RecordWriter;
import org.apache.hadoop.hive.ql.io.HiveOutputFormat;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Progressable;

/**
 * HiveOutputFormat that simply ignores writes. This is used when the
 * INPUTFORMAT is MetarSequenceFileInputFormatOld because Hive wants
 * OUTPUTFORMAT specified whenever INPUTFORMAT is, however our interface to
 * METAR data is read-only. Hive 0.7.1 will not accept the standard Hadoop
 * NullOutputFormat.
 */
@SuppressWarnings("deprecation")
public class NullHiveOutputFormat implements
		HiveOutputFormat<WritableComparable<?>, Writable> {

	@Override
	public RecordWriter getHiveRecordWriter(JobConf jc, Path finalOutPath,
			Class<? extends Writable> valueClass, boolean isCompressed,
			Properties tableProperties, Progressable progress)
			throws IOException {
		return new RecordWriter() {

			@Override
			public void write(Writable w) throws IOException {

			}

			@Override
			public void close(boolean abort) throws IOException {

			}

		};
	}
}
