package mattmath.matrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;

import mattmath.matrix.ColumnVector;
import mattmath.matrix.Matrix;
import mattmath.matrix.MatrixFactory;

import org.junit.Test;

public class MatrixTest {
	@Test
	public void testZeroMatrix() {
		final Matrix m = MatrixFactory.zeroMatrix(7, 5);
		int r = m.getRows();
		int c = m.getColumns();
		
		assertTrue(r == 7);
		assertTrue(c == 5);
		
		for (int i = 1; i <= r; i++)
			for (int j = 1; j <= c; j++)
				if (!(0 == m.get(i,j)))
					fail("Matrix was not zeros");
	}
	
	@Test
	public void testZeroVector() {
		final ColumnVector c = MatrixFactory.zeroVector(6);
		
		assertTrue(6 == c.getDimension());
		
		for (int i = 1; i <= 6; i++)
			if (!(0 == c.get(i)))
				fail("Vector was not zeros");
	}
	
	@Test
	public void testMultiplyTwoTwo() {
		final Matrix m = MatrixFactory.zeroMatrix(2, 2);
		final Matrix n = MatrixFactory.zeroMatrix(2, 2);
		
		m.set(1,1,1);
		m.set(1,2,2);
		m.set(2,1,3);
		m.set(2,2,4);
		
		n.set(1,1,5);
		n.set(1,2,6);
		n.set(2,1,7);
		n.set(2,2,8);
		
		final Matrix p = m.rightMultiplyBy(n);
		
		assertTrue(19 == p.get(1,1));
		assertTrue(22 == p.get(1,2));
		assertTrue(43 == p.get(2,1));
		assertTrue(50 == p.get(2,2));
	}
	
	@Test
	public void testMatrixTimesVector() {
		final Matrix m = MatrixFactory.zeroMatrix(2, 3);
		m.set(1,1,1);
		m.set(1,2,1);
		m.set(1,3,1);
		m.set(2,1,3);
		m.set(2,2,2);
		m.set(2,3,1);
		
		final ColumnVector v = MatrixFactory.zeroVector(3);
		v.set(1,5);
		v.set(2,7);
		v.set(3,11);
		
		final ColumnVector w = m.rightMultiplyBy(v);
		assertEquals(2, w.getDimension());
		assertTrue(23 == w.get(1));
		assertTrue(40 == w.get(2));
	}
	
	@Test
	public void testScalarMultiply() {
		final Matrix m = randomMatrix(3,3);
		final double s = Math.random();
		
		final Matrix n = m.copy();
		n.scalarMultiplyBy(s);
		
		for (int i = 1; i <= 3; i++)
			for (int j = 1; j <= 3; j++)
				assertTrue(n.get(i,j) == s * m.get(i,j));
	}
	
	@Test
	public void testScalarDivide() {
		final Matrix m = MatrixFactory.zeroMatrix(2, 3);
		
		m.set(1, 1, 3);
		m.set(2, 3, 2);
		
		m.scalarDivideBy(2);
		
		assertTrue(1.5 == m.get(1,1));
		assertTrue(1 == m.get(2,3));
	}
	
	@Test
	public void testMatrixCopy() {
		int r = 5;
		int c = 4;
		
		final Matrix m = randomMatrix(r, c);	
		final Matrix n = m.copy();
		
		assertTrue(m != n);
		for (int i = 1; i <= r; i++)
			for (int j = 1; j <= c; j++)
				assertTrue(m.get(i,j) == n.get(i,j));
	}

	@Test
	public void testVectorCopy() {
		int d = 11;
		
		final ColumnVector c = MatrixFactory.zeroVector(d);
		
		for (int i = 1; i <= d; i++)
			c.set(i, Math.random());
		
		final ColumnVector copy = c.copy();
		
		for (int i = 1; i <= d; i++)
			assertTrue(c.get(i) == copy.get(i));
	}
	
	@Test
	public void testTranspose() {
		int r = 5;
		int c = 4;
		
		final Matrix m = randomMatrix(r, c);
		final Matrix t = m.transpose();
		
		assertEquals(c, t.getRows());
		assertEquals(r, t.getColumns());
		
		for (int i = 1; i <= r; i++)
			for (int j = 1; j <= c; j++)
				assertTrue(m.get(i,j) == t.get(j,i));
	}
	
	@Test
	public void testAdd() {
		final Matrix m = MatrixFactory.zeroMatrix(2, 2);
		
		final Matrix n = MatrixFactory.zeroMatrix(2, 2);
		n.set(1,1,1);
		n.set(1,2,2);
		n.set(2,1,3);
		n.set(2,2,4);
		
		m.add(n);
		
		assertTrue(1 == m.get(1,1));
		assertTrue(2 == m.get(1,2));
		assertTrue(3 == m.get(2,1));
		assertTrue(4 == m.get(2,2));
	}
	
	@Test
	public void testVectorNorm() {
		final ColumnVector v = primesVector();
		
		assertTrue(38 == v.normSquared());
	}
	
	@Test
	public void testDotProduct() {
		final ColumnVector v = primesVector();
		
		final ColumnVector w = MatrixFactory.zeroVector(3);
		w.set(1,1);
		w.set(2,1);
		w.set(3,1);
		
		double dot = v.dotProduct(w);
		assertTrue(10 == dot);
	}

	@Test
	public void testMatrixTimesScalar() {
		double s = Math.random();
		
		final Matrix m = randomMatrix(3,5);
		final Matrix n = m.timesScalar(s);
		
		assertTrue(m.getRows() == n.getRows());
		assertTrue(m.getColumns() == n.getColumns());
		
		for (int i = 1; i <= m.getRows(); i++)
			for (int j = 1; j <= m.getColumns(); j++)
				assertTrue(m.get(i,j) * s == n.get(i,j));
	}
	
	@Test
	public void testVectorTimesScalar() {
		double s = Math.random();
		
		final ColumnVector v = primesVector();
		final ColumnVector w = v.timesScalar(s);
		
		assertTrue(v.getDimension() == w.getDimension());
		
		for (int i = 1; i <= v.getDimension(); i++)
			assertTrue(v.get(i) * s == w.get(i));
	}
	
	@Test
	public void testMatrixSubtract() {
		final Matrix m = randomMatrix(6,2);
		final Matrix s = m.copy();
		final Matrix n = randomMatrix(6,2);
		s.subtract(n);
		
		for (int i = 1; i <= m.getRows(); i++)
			for (int j = 1; j <= m.getColumns(); j++)
				assertTrue(m.get(i,j) - n.get(i,j) == s.get(i,j));
	}
	
	@Test
	public void testMatrixFromRows() {
		int r = 4;
		
		final List<ColumnVector> rows = new LinkedList<ColumnVector>();
		for (int c = 0; c < r; c++)
			rows.add(randomVector(3));
		
		final Matrix m = MatrixFactory.matrixByRows(rows);
		
		assertTrue(r == m.getRows());
		assertTrue(3 == m.getColumns());
		
		int row = 1;
		for (final ColumnVector v : rows) {
			for (int col = 1; col <= 3; col++) {
				assertTrue(m.get(row,col) == v.get(col));
			}
			row++;
		}
	}

	private ColumnVector primesVector() {
		final ColumnVector v = MatrixFactory.zeroVector(3);
		v.set(1,2);
		v.set(2,3);
		v.set(3,5);
		
		return v;
	}

	private Matrix randomMatrix(int r, int c) {
		final Matrix m = MatrixFactory.zeroMatrix(r, c);
		for (int i = 1; i <= r; i++)
			for (int j = 1; j <= c; j++)
				m.set(i,j,Math.random());
		return m;
	}
	
	private ColumnVector randomVector(int d) {
		final ColumnVector v = MatrixFactory.zeroVector(d);
		for (int i = 1; i <= d; i++)
			v.set(i, Math.random());
		return v;
	}
}
