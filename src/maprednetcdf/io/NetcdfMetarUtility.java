package maprednetcdf.io;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ucar.ma2.ArrayChar;
import ucar.ma2.ArrayChar.StringIterator;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayFloat;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

public class NetcdfMetarUtility {
	public static List<Metar> read(String name, byte[] bytes) throws IOException {
		final NetcdfFile netcdfFile = NetcdfFile.openInMemory(name, bytes);
		final NetcdfDataset dataset = new NetcdfDataset(netcdfFile);
		
		final Variable varStation = dataset.findVariable("stationName");
		final Variable varLocation = dataset.findVariable("locationName");	
		final Variable varTimeObs = dataset.findVariable("timeObs");
		final Variable varPresent = dataset.findVariable("presWeather");
		final Variable varTemperature = dataset.findVariable("temperature");
		final Variable varPress = dataset.findVariable("seaLevelPress");
		final Variable varWindDir = dataset.findVariable("windDir");
		final Variable varWindSpeed = dataset.findVariable("windSpeed");
		final Variable varWindGust = dataset.findVariable("windGust");
		final Variable varLatitude = dataset.findVariable("latitude");
		final Variable varLongitude = dataset.findVariable("longitude");
		final Variable varElevation = dataset.findVariable("elevation");
		
		final StringIterator iStation = ((ArrayChar)varStation.read()).getStringIterator();		
		StringIterator iLocation = null;
		if (varLocation != null)
			iLocation = ((ArrayChar)varLocation.read()).getStringIterator();		
		final StringIterator iPresent = ((ArrayChar)varPresent.read()).getStringIterator();
		final ArrayDouble.D1 aTimeObs = (ArrayDouble.D1)varTimeObs.read();
		final ArrayFloat.D1 aTemperature = (ArrayFloat.D1)varTemperature.read();
		final ArrayFloat.D1 aPress = (ArrayFloat.D1)varPress.read();
		final ArrayFloat.D1 aWindDir = (ArrayFloat.D1)varWindDir.read();
		final ArrayFloat.D1 aWindSpeed = (ArrayFloat.D1)varWindSpeed.read();
		final ArrayFloat.D1 aWindGust = (ArrayFloat.D1)varWindGust.read();
		final ArrayFloat.D1 aLatitude = (ArrayFloat.D1)varLatitude.read();
		final ArrayFloat.D1 aLongitude = (ArrayFloat.D1)varLongitude.read();
		final ArrayFloat.D1 aElevation = (ArrayFloat.D1)varElevation.read();
		
		final List<Metar> metars = new LinkedList<Metar>();
		
		int i = 0;
		while (iStation.hasNext()) {			
			final Metar metar = new Metar();
			metar.setStationName(iStation.next());		
			if (iLocation != null)
				metar.setLocationName(iLocation.next());
			metar.setTimeObs(aTimeObs.get(i));
			metar.setPresWeather(iPresent.next());
			metar.setTemperature(aTemperature.get(i));
			metar.setSeaLevelPress(aPress.get(i));
			metar.setWindDir(aWindDir.get(i));
			metar.setWindSpeed(aWindSpeed.get(i));
			metar.setWindGust(aWindGust.get(i));
			metar.setLatitude(aLatitude.get(i));
			metar.setLongitude(aLongitude.get(i));
			metar.setElevation(aElevation.get(i));
			
			metars.add(metar);
			i++;
		}
		
		return metars;
	}
	
	private NetcdfMetarUtility() {
	}
}
