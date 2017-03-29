package solver.mine.dark;

public class IsDark {
	public static boolean isDark(int rgb) {
		int red = (rgb >> 16) & 0xFF;
		int green = (rgb >> 8) & 0xFF;
		int blue = rgb & 0xFF;
		return (red + green + blue) < 120;
	}
}
