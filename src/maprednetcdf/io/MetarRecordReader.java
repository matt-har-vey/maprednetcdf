package maprednetcdf.io;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class MetarRecordReader extends RecordReader<MetarSequenceKey,Metar> {
	
	private RecordReader<Text,BytesWritable> delegate;

	private Iterator<Metar> iterator;
	
	private MetarSequenceKey key;
	private Metar value;
	
	private long index;
	
	public MetarRecordReader(
			RecordReader<Text, BytesWritable> delegate) {
		
		this.delegate = delegate;
		
		final List<Metar> empty = Collections.emptyList();
		iterator = empty.iterator();
		index = 0;
		
		key = new MetarSequenceKey();
		value = new Metar();
	}

	@Override
	public void initialize(InputSplit split, TaskAttemptContext context)
			throws IOException, InterruptedException {
		delegate.initialize(split, context);
	}

	@Override
	public MetarSequenceKey getCurrentKey() throws IOException,
			InterruptedException {
		return key;
	}

	@Override
	public Metar getCurrentValue() throws IOException, InterruptedException {
		return value;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		return delegate.getProgress();
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		if (iterator.hasNext()) {
			final Metar iterMetar = iterator.next();
			
			index += 1;
			
			key.setName(delegate.getCurrentKey());
			key.setIndex(index);
			
			value.set(iterMetar);
			return true;
		} else {
			if (!delegate.nextKeyValue()) {
				return false;
			} else {
				final List<Metar> metars = NetcdfMetarUtility.read(
					delegate.getCurrentKey().toString(),
					delegate.getCurrentValue().getBytes());
				
				iterator = metars.iterator();
				index = 0;
				
				return nextKeyValue();
			}
		}
	}

	@Override
	public void close() throws IOException {
		delegate.close();
	}
}
