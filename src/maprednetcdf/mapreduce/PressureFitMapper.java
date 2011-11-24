package maprednetcdf.mapreduce;

import java.io.IOException;

import maprednetcdf.io.PressureCollection;
import mattmath.matrix.ColumnVector;
import mattmath.optimization.PressureFitter;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PressureFitMapper extends
		Mapper<LongWritable, PressureCollection, LongWritable, Text> {

	private Text text;
	
	public PressureFitMapper() {
		text = new Text();
	}

	@Override
	protected void map(LongWritable lw, PressureCollection coll,
			Context context) throws IOException, InterruptedException {
		
		if (coll.size() > 0) {
			final PressureFitter fitter = new PressureFitter();
			fitter.initFromComponents(coll.getLatitudes(), coll.getLongitudes(),
					coll.getPressures(), coll.size());
			fitter.train();

			final ColumnVector theta = fitter.getTheta();
			
			final StringBuilder sb = new StringBuilder();
			int d = theta.getDimension();
			for (int i = 1; i <= d; i++) {
				sb.append(theta.get(i));
				if (i < d)
					sb.append("\t");
			}
				
			text.set(sb.toString());

			context.write(lw, text);
		}
	}
}