package maprednetcdf.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class MetarSequenceKey implements WritableComparable<MetarSequenceKey> {
	private Text name;
	private LongWritable index;
	
	public MetarSequenceKey() {
		set(new Text(), new LongWritable());
	}
	
	public void set(Text name, LongWritable index) {
		this.name = name;
		this.index = index;
	}
	
	public void setName(Text name) {
		this.name.set(name);
	}
	
	public void setIndex(long index) {
		this.index.set(index);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		name.readFields(in);
		index.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		name.write(out);
		index.write(out);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MetarSequenceKey) {
			final MetarSequenceKey other = (MetarSequenceKey)obj;
			return name.equals(other.name) && index.equals(other.index);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return 61 * name.hashCode() + index.hashCode();
	}

	@Override
	public String toString() {
		return "MetarSequenceKey [name=" + name + ", index=" + index + "]";
	}

	@Override
	public int compareTo(MetarSequenceKey other) {
		int c = name.compareTo(other.name);
		if (c != 0)
			return c;
		else
			return index.compareTo(other.index);
	}
}
