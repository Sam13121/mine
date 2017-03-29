package solver.mine.difference;

public class ColorDifference {
	public static int colorDifference(int r1, int g1, int b1, int r2, int g2, int b2) {
		return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
	}
}
