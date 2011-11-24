package maprednetcdf.hive;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import maprednetcdf.io.Metar;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.serde2.SerDe;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.io.DoubleWritable;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Writable;

public class MetarSerDe implements SerDe {
	
	private ObjectInspector objectInspector;
	
	private ArrayList<Object> row;
	private DoubleWritable timeObs;

	@Override
	public void initialize(Configuration conf, Properties tbl)
			throws SerDeException {
		final int n = 12;
		
		final List<String> columnNames = new ArrayList<String>(n);
		columnNames.add("stationName");
		columnNames.add("locationName");
		columnNames.add("timeObs");
		columnNames.add("presWeather");
		columnNames.add("temperature");
		columnNames.add("seaLevelPress");
		columnNames.add("windDir");
		columnNames.add("windSpeed");
		columnNames.add("windGust");
		columnNames.add("latitude");
		columnNames.add("longitude");
		columnNames.add("elevation");
		
		final List<ObjectInspector> columnOIs = new ArrayList<ObjectInspector>(n);
		columnOIs.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);
		columnOIs.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);
		columnOIs.add(PrimitiveObjectInspectorFactory.writableDoubleObjectInspector);
		columnOIs.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);
		columnOIs.add(PrimitiveObjectInspectorFactory.writableFloatObjectInspector);
		columnOIs.add(PrimitiveObjectInspectorFactory.writableFloatObjectInspector);
		columnOIs.add(PrimitiveObjectInspectorFactory.writableFloatObjectInspector);
		columnOIs.add(PrimitiveObjectInspectorFactory.writableFloatObjectInspector);
		columnOIs.add(PrimitiveObjectInspectorFactory.writableFloatObjectInspector);
		columnOIs.add(PrimitiveObjectInspectorFactory.writableFloatObjectInspector);
		columnOIs.add(PrimitiveObjectInspectorFactory.writableFloatObjectInspector);
		columnOIs.add(PrimitiveObjectInspectorFactory.writableFloatObjectInspector);
		
		objectInspector = ObjectInspectorFactory.
			getStandardStructObjectInspector(columnNames, columnOIs);
		
		row = new ArrayList<Object>(n);
		for (int i = 0; i < n; i++)
			row.add(null);
		
		timeObs = new DoubleWritable();
	}

	@Override
	public ObjectInspector getObjectInspector() throws SerDeException {
		return objectInspector;
	}

	@Override
	public Writable serialize(Object o, ObjectInspector objectInspector)
			throws SerDeException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object deserialize(Writable writable) throws SerDeException {
		final Metar metar = (Metar)writable;
		
		// Need this conversion because Hive has its own DoubleWritable, as it
		// is tracking to about Hadoop 0.17 as of Oct 2011.
		timeObs.set(metar.getTimeObs().get());
		
		row.set(0, metar.getStationName());
		row.set(1, metar.getLocationName());
		row.set(2, timeObs);
		row.set(3, metar.getPresWeather());
		row.set(4, metar.getTemperature());
		row.set(5, metar.getSeaLevelPress());
		row.set(6, metar.getWindDir());
		row.set(7, metar.getWindSpeed());
		row.set(8, metar.getWindGust());
		row.set(9, metar.getLatitude());
		row.set(10, metar.getLongitude());
		row.set(11, metar.getElevation());
		
		return row;
	}

	@Override
	public Class<? extends Writable> getSerializedClass() {
		return Metar.class;
	}
}
