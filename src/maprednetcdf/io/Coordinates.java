package maprednetcdf.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.WritableComparable;

public class Coordinates implements WritableComparable<Coordinates> {
	private FloatWritable latitude;
	private FloatWritable longitude;

	public Coordinates() {
		latitude = new FloatWritable();
		longitude = new FloatWritable();
	}
	
	public Coordinates(FloatWritable latitude, FloatWritable longitude) {
		this();
		this.latitude.set(latitude.get());
		this.longitude.set(longitude.get());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Coordinates))
			return false;

		final Coordinates other = (Coordinates) obj;
		return latitude.equals(other.latitude)
				&& longitude.equals(other.longitude);
	}

	@Override
	public int hashCode() {
		return latitude.hashCode() + 67 * longitude.hashCode();
	}

	@Override
	public String toString() {
		return latitude + "\t" + longitude;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		latitude.readFields(in);
		longitude.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		latitude.write(out);
		longitude.write(out);
	}

	@Override
	public int compareTo(Coordinates other) {
		int c = latitude.compareTo(other.latitude);
		if (c != 0)
			return c;
		return longitude.compareTo(other.longitude);
	}

}
