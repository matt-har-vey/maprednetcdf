package maprednetcdf.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

public class PressureCollection implements Writable {
	private int size;
	private LinkedList<Float> lats;
	private LinkedList<Float> longs;
	private LinkedList<Float> pressures;
	
	public PressureCollection() {
		reset();
	}
	
	public int size() {
		return size;
	}
	
	public void add(float latitude, float longitude, float pressure) {
		lats.add(latitude);
		longs.add(longitude);
		pressures.add(pressure);
		
		size += 1;
	}
	
	public Iterable<Float> getLatitudes() {
		return lats;
	}
	
	public Iterable<Float> getLongitudes() {
		return longs;
	}
	
	public Iterable<Float> getPressures() {
		return pressures;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		final IntWritable sw = new IntWritable();
		sw.readFields(in);
		
		reset();
		size = sw.get();
		
		final FloatWritable fw = new FloatWritable();
		for (int i = 0; i < size; i++) {
			fw.readFields(in);
			lats.add(fw.get());
			fw.readFields(in);
			longs.add(fw.get());
			fw.readFields(in);
			pressures.add(fw.get());
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		final IntWritable sw = new IntWritable();
		sw.set(size);
		sw.write(out);
		
		final FloatWritable fw = new FloatWritable();
		final Iterator<Float> latsIter = lats.iterator();
		final Iterator<Float> longsIter = longs.iterator();
		final Iterator<Float> pressIter = pressures.iterator();
		for (int i = 0; i < size; i++) {
			fw.set(latsIter.next());
			fw.write(out);
			fw.set(longsIter.next());
			fw.write(out);
			fw.set(pressIter.next());
			fw.write(out);
		}
	}
	
	private void reset() {
		size = 0;
		lats = new LinkedList<Float>();
		longs = new LinkedList<Float>();
		pressures = new LinkedList<Float>();
	}
}
