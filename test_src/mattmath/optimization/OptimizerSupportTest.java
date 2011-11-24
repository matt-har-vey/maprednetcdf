package mattmath.optimization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OptimizerSupportTest {
	@Test
	public void testEstimateMinCubic() {
		OptimizerSupport s = new OptimizerSupport();
		double min = s.estimateMinCubic(0, 0, -1, 1, 0, 2);
		assertEquals(Double.valueOf(Math.sqrt(1.0 / 3.0)), Double.valueOf(min));
	}
	
	@Test
	public void testEstimateMinCubicYDiffers() {
		OptimizerSupport s = new OptimizerSupport();
		double min = s.estimateMinCubic(-1,4,-1,2,1,8);
		assertEquals(Double.valueOf(Math.sqrt(4.0/3.0)), Double.valueOf(min));
	}
	
	@Test
	public void testEstimateMinCubicDegen() {
		OptimizerSupport s = new OptimizerSupport();
		double min = s.estimateMinCubic(0, 0, -1, 1, 0, 1);
		assertEquals(Double.valueOf(0.5), Double.valueOf(min));
	}
}
