package maprednetcdf.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class Metar implements Writable {
	private Text stationName;
	private Text locationName;
	private DoubleWritable timeObs;
	private Text presWeather;
	private FloatWritable temperature;
	private FloatWritable seaLevelPress;
	private FloatWritable windDir;
	private FloatWritable windSpeed;
	private FloatWritable windGust;
	private FloatWritable latitude;
	private FloatWritable longitude;
	private FloatWritable elevation;

	public Metar() {
		stationName = new Text();
		locationName = new Text();
		timeObs = new DoubleWritable();
		presWeather = new Text();
		temperature = new FloatWritable();
		seaLevelPress = new FloatWritable();
		windDir = new FloatWritable();
		windSpeed = new FloatWritable();
		windGust = new FloatWritable();
		latitude = new FloatWritable();
		longitude = new FloatWritable();
		elevation = new FloatWritable();
	}

	public Text getStationName() {
		return stationName;
	}

	public Text getLocationName() {
		return locationName;
	}

	public DoubleWritable getTimeObs() {
		return timeObs;
	}

	public Text getPresWeather() {
		return presWeather;
	}

	public FloatWritable getTemperature() {
		return temperature;
	}

	public FloatWritable getSeaLevelPress() {
		return seaLevelPress;
	}

	public FloatWritable getWindDir() {
		return windDir;
	}

	public FloatWritable getWindSpeed() {
		return windSpeed;
	}

	public FloatWritable getWindGust() {
		return windGust;
	}

	public FloatWritable getLatitude() {
		return latitude;
	}

	public FloatWritable getLongitude() {
		return longitude;
	}

	public FloatWritable getElevation() {
		return elevation;
	}

	void setStationName(String stationName) {
		this.stationName.set(stationName);
	}

	void setLocationName(String locationName) {
		this.locationName.set(locationName);
	}

	void setTimeObs(double timeObs) {
		this.timeObs.set(timeObs);
	}

	void setPresWeather(String presWeather) {
		this.presWeather.set(presWeather);
	}

	void setTemperature(float temperature) {
		this.temperature.set(temperature);
	}

	void setSeaLevelPress(float seaLevelPress) {
		this.seaLevelPress.set(seaLevelPress);
	}

	void setWindDir(float windDir) {
		this.windDir.set(windDir);
	}

	void setWindSpeed(float windSpeed) {
		this.windSpeed.set(windSpeed);
	}

	void setWindGust(float windGust) {
		this.windGust.set(windGust);
	}

	void setLatitude(float latitude) {
		this.latitude.set(latitude);
	}

	void setLongitude(float longitude) {
		this.longitude.set(longitude);
	}

	void setElevation(float elevation) {
		this.elevation.set(elevation);
	}

	public void set(Metar metar) {
		stationName.set(metar.stationName);
		locationName.set(metar.locationName);
		timeObs.set(metar.timeObs.get());
		presWeather.set(metar.presWeather);
		temperature.set(metar.temperature.get());
		seaLevelPress.set(metar.seaLevelPress.get());
		windDir.set(metar.windDir.get());
		windSpeed.set(metar.windSpeed.get());
		windGust.set(metar.windGust.get());
		latitude.set(metar.latitude.get());
		longitude.set(metar.longitude.get());
		elevation.set(metar.elevation.get());
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		stationName.readFields(in);
		locationName.readFields(in);
		timeObs.readFields(in);
		presWeather.readFields(in);
		temperature.readFields(in);
		seaLevelPress.readFields(in);
		windDir.readFields(in);
		windSpeed.readFields(in);
		windGust.readFields(in);
		latitude.readFields(in);
		longitude.readFields(in);
		elevation.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		stationName.write(out);
		locationName.write(out);
		timeObs.write(out);
		presWeather.write(out);
		temperature.write(out);
		seaLevelPress.write(out);
		windDir.write(out);
		windSpeed.write(out);
		windGust.write(out);
		latitude.write(out);
		longitude.write(out);
		elevation.write(out);
	}

	@Override
	public String toString() {
		return "Metar [stationName=" + stationName + ", locationName="
				+ locationName + ", timeObs=" + timeObs + ", presWeather="
				+ presWeather + ", temperature=" + temperature
				+ ", seaLevelPress=" + seaLevelPress + ", windDir=" + windDir
				+ ", windSpeed=" + windSpeed + ", windGust=" + windGust
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", elevation=" + elevation + "]";
	}
}
