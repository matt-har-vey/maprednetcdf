package maprednetcdf.hive;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import maprednetcdf.io.Metar;
import maprednetcdf.io.MetarSequenceKey;
import maprednetcdf.io.NetcdfMetarUtility;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.RecordReader;
/**
 * Old API record reader to work with Hive.
 */
public class MetarRecordReaderOld implements
		RecordReader<MetarSequenceKey, Metar> {
	
	private RecordReader<Text,BytesWritable> delegate;
	
	private Iterator<Metar> iterator;
	private long pos;
	private long index;
	
	private Text name;
	private BytesWritable bytes;

	public MetarRecordReaderOld(RecordReader<Text,BytesWritable> recordReader) {
		this.delegate = recordReader;
		pos = 0;
		
		final List<Metar> empty = Collections.emptyList();
		iterator = empty.iterator();
		
		name = new Text();
		bytes = new BytesWritable();
	}

	@Override
	public MetarSequenceKey createKey() {
		return new MetarSequenceKey();
	}

	@Override
	public Metar createValue() {
		return new Metar();
	}

	@Override
	public long getPos() throws IOException {
		return pos;
	}

	@Override
	public boolean next(MetarSequenceKey key, Metar metar) throws IOException {
		if (iterator.hasNext()) {
			final Metar iterMetar = iterator.next();
			
			index += 1;
			pos += 1;
			
			key.setName(name);
			key.setIndex(index);
			
			metar.set(iterMetar);
			
			return true;
		} else {
			if (!delegate.next(name, bytes)) {
				return false;
			} else {
				final List<Metar> metars = NetcdfMetarUtility.read(
					name.toString(),
					bytes.getBytes());
				
				iterator = metars.iterator();
				index = 0;
				
				return next(key, metar);
			}
		}
	}

	@Override
	public float getProgress() throws IOException {
		return delegate.getProgress();
	}

	@Override
	public void close() throws IOException {
		delegate.close();
	}

}
