package maprednetcdf.download;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

public class MADISDownloader implements Iterator<MADISEntry> {
	private Calendar current;
	private Calendar end;
	private String dataset;

	private SimpleDateFormat df;
	private SimpleDateFormat df2;
	
	public MADISDownloader(Calendar start, Calendar end, String dataset) {
		this.current = start;
		this.end = end;
		this.dataset = dataset;
		
		df = new SimpleDateFormat("yyyyMMdd_HHmm");
		df2 = new SimpleDateFormat("yyyy/MM/dd");
	}
	
	@Override
	public boolean hasNext() {
		return end.compareTo(current) > 0;
	}
	
	@Override
	public MADISEntry next() {
		final String name = df.format(current.getTime());
		
		try {
			final URL url = new URL("https://madis-data.noaa.gov/madisPublic/data/archive/" +
					df2.format(current.getTime()) + "/" + dataset + "/" + name + ".gz");
			
			final URLConnection conn = url.openConnection();
			conn.setRequestProperty("Authorization",
				"Basic baseencodedusername:password");	
			final InputStream in = conn.getInputStream();
			
			final GZIPInputStream gin = new GZIPInputStream(in);
			final ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buf = new byte[4096];
			int n;
			while ((n = gin.read(buf, 0, 4096)) > 0)
			{
				bout.write(buf, 0, n);
			}
			
			final MADISEntry entry = new MADISEntry();
			entry.setName(name + ".nc");
			entry.setData(bout.toByteArray());
			
			current.add(Calendar.HOUR, 1);
			
			return entry;
		} catch (Exception e) {
			current.add(Calendar.HOUR, 1);
			throw new MADISIteratorException(e);
		}	
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
