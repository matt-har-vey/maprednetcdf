package maprednetcdf.download;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class MADISDownloadRun implements Runnable {

	@Override
	public void run() {
		runMaritime(new GregorianCalendar(2011,0,1,0,0),
				new GregorianCalendar(2011,9,22,23,59));
	}

	private void runYear(int year) {
		runMetar(new GregorianCalendar(year, 0, 1, 0, 0),
				new GregorianCalendar(year, 11, 31, 23, 59));
	}
	
	private void runMaritime(final Calendar start, final Calendar end) {
		runRange(start, end, "point/maritime/netcdf", "/media/madis/maritime/", 4);
	}

	private void runMetar(final Calendar start, final Calendar end) {
		runRange(start, end, "point/metar/netcdf", "/media/madis/metar/", 6);
	}

	private void runRange(final Calendar start, final Calendar end,
			String dataset, String outPath, int prefixLength) {
		final Logger logger = Logger.getLogger(getClass().getName());
		final MADISDownloader downloader = new MADISDownloader(start, end,
				dataset);

		try {
			final PrintWriter errors = new PrintWriter(new FileWriter(
					"madiserrors.log"));
			final BlobSequenceFileGenerator sfg = new BlobSequenceFileGenerator();

			String currentPrefix = "";
			while (downloader.hasNext()) {
				MADISEntry entry = null;
				try {
					entry = downloader.next();
				} catch (MADISIteratorException e) {
					errors.println(e.getMessage());
					errors.flush();
				}

				if (entry != null) {
					final String prefix = entry.getName().substring(0,
							prefixLength);
					if (!currentPrefix.equals(prefix)) {
						sfg.close();
						sfg.open(outPath + prefix + ".seq");
						currentPrefix = prefix;
					}

					sfg.addRecord(entry.getName(), entry.getData());

					logger.info("wrote " + entry.getName());
				}
			}

			sfg.close();
			errors.close();

		} catch (IOException e) {
			logger.log(Level.SEVERE, "IOException", e);
		}
	}

	public static void main(String[] args) {
		new MADISDownloadRun().run();
	}
}
