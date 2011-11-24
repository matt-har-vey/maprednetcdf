package maprednetcdf.mapreduce;

import maprednetcdf.io.Metar;

public class GroupMetarByHourMapper extends MetarByHourMapper<Metar> {

	@Override
	protected Metar value(Metar metar) {
		return metar;
	}

}
