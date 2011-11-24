package maprednetcdf.hive;

import org.apache.hadoop.hive.serde2.objectinspector.primitive.AbstractPrimitiveWritableObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.SettableDoubleObjectInspector;
import org.apache.hadoop.io.DoubleWritable;

/**
 * Copied from Hive source to use the Hadoop DoubleWritable
 */
public class WritableDoubleObjectInspector extends
		AbstractPrimitiveWritableObjectInspector implements
		SettableDoubleObjectInspector {

	private static WritableDoubleObjectInspector instance;
	
	public static WritableDoubleObjectInspector get() {
		if (instance == null)
			instance = new WritableDoubleObjectInspector();
		return instance;
	}
	
	private WritableDoubleObjectInspector() {
		super(PrimitiveObjectInspectorUtils.doubleTypeEntry);
	}

	@Override
	public double get(Object o) {
		return ((DoubleWritable) o).get();
	}

	@Override
	public Object copyObject(Object o) {
		return o == null ? null
				: new DoubleWritable(((DoubleWritable) o).get());
	}

	@Override
	public Object getPrimitiveJavaObject(Object o) {
		return o == null ? null : Double.valueOf(((DoubleWritable) o).get());
	}

	@Override
	public Object create(double value) {
		return new DoubleWritable(value);
	}

	@Override
	public Object set(Object o, double value) {
		((DoubleWritable) o).set(value);
		return o;
	}
}
