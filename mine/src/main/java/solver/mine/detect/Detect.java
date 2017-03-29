package solver.mine.detect;

import java.awt.image.BufferedImage;

import solver.mine.utils.Base;
import solver.mine.difference.ColorDifference;;

public class Detect {
	public static int detect_3_7(int[] areapix) {
		boolean redx[][] = new boolean[15][15];
		for (int k = 0; k < 225; k++) {
			int i = k % 15;
			int j = k / 15;
			int rgb = areapix[k];
			int red = (rgb >> 16) & 0xFF;
			int green = (rgb >> 8) & 0xFF;
			int blue = rgb & 0xFF;

			if (ColorDifference.colorDifference(red, green, blue, 170, 0, 0) < 100)
				redx[i][j] = true;
		}

		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				if (!redx[i][j] && !redx[i][j + 1] && !redx[i][j + 2] && redx[i + 1][j + 1])
					return 3;
			}
		}
		return 7;
	}

	public static int detect(BufferedImage bi, int i, int j) {
		int mouseX = Base.BoardTopW + (int) (j * Base.BoardPix);
		int mouseY = Base.BoardTopH + (int) (i * Base.BoardPix);

		// Don't take one pixel, take a 15x15 area of pixels
		int areapix[] = new int[225];
		int count = 0;
		for (int ii = mouseX - 7; ii <= mouseX + 7; ii++)
			for (int jj = mouseY - 7; jj <= mouseY + 7; jj++) {
				areapix[count] = bi.getRGB(ii, jj);
				count++;
			}

		boolean hasColorOfOneSquare = false;
		boolean hasColorOfBlank = false;
		boolean isRelativelyHomogenous = true;

		for (int rgb : areapix) {
			int red = (rgb >> 16) & 0xFF;
			int green = (rgb >> 8) & 0xFF;
			int blue = rgb & 0xFF;

			// Detect death
			if (ColorDifference.colorDifference(red, green, blue, 110, 110, 110) < 20)
				return -10;

			// Detect flagging of any sort
			if (ColorDifference.colorDifference(red, green, blue, 255, 0, 0) < 30)
				return -3;

			if (ColorDifference.colorDifference(red, green, blue, 65, 79, 188) < 10) {
				hasColorOfOneSquare = true;
			}
			if (blue > red && blue > green && ColorDifference.colorDifference(red, green, blue, 220, 220, 255) < 200) {
				hasColorOfBlank = true;
			}
			if (ColorDifference.colorDifference(red, green, blue, 167, 3, 5) < 20)
				return detect_3_7(areapix);
			if (ColorDifference.colorDifference(red, green, blue, 29, 103, 4) < 20)
				return 2;
			if (ColorDifference.colorDifference(red, green, blue, 0, 0, 138) < 20)
				return 4;
			if (ColorDifference.colorDifference(red, green, blue, 124, 1, 3) < 20)
				return 5;
			if (ColorDifference.colorDifference(red, green, blue, 7, 122, 131) < 20)
				return 6;
		}

		// Determine how 'same' the area is.
		// This is to separate the empty areas which are relatively the same
		// from
		// the unexplored areas which have a gradient of some sort.
		{
			int rgb00 = areapix[0];
			int red00 = (rgb00 >> 16) & 0xFF;
			int green00 = (rgb00 >> 8) & 0xFF;
			int blue00 = rgb00 & 0xFF;
			for (int rgb : areapix) {
				int red = (rgb >> 16) & 0xFF;
				int green = (rgb >> 8) & 0xFF;
				int blue = rgb & 0xFF;
				if (ColorDifference.colorDifference(red, green, blue, red00, green00, blue00) > 60) {
					isRelativelyHomogenous = false;
					break;
				}
			}
		}

		if (hasColorOfOneSquare && hasColorOfBlank)
			return 1;

		if (hasColorOfBlank && isRelativelyHomogenous)
			return 0;

		return -1;
	}
}
