package maprednetcdf.download;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Text;

public class BlobSequenceFileGenerator {
	private Writer writer;
	
	public void open(String filename) throws IOException {
		final Configuration conf = new Configuration();
		final FileSystem fs = FileSystem.getLocal(conf);
		final Path path = new Path(filename);
		
		writer = SequenceFile.createWriter(fs, conf, path,
			Text.class, BytesWritable.class, CompressionType.BLOCK);
	}
	
	public void addRecord(String name, byte[] bytes) throws IOException {
		final Text text = new Text(name);
		final BytesWritable bw = new BytesWritable(bytes);
		writer.append(text, bw);
	}
	
	public void close() throws IOException {
		if (writer != null)
			writer.close();
	}
}
