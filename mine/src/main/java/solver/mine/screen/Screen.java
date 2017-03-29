package solver.mine.screen;

import java.awt.image.BufferedImage;

import solver.mine.utils.Base;
import solver.mine.detect.Detect;
import solver.mine.screenshotimage.ScreenShotImage;

public class Screen {
	public static int onScreen(int i, int j) {
		if (i < 0 || j < 0 || i > Base.BoardHeight - 1 || j > Base.BoardWidth - 1)
			return -10;
		return Base.onScreen[i][j];
	}

	public static int updateOnScreen() {
		BufferedImage bi = ScreenShotImage.screenShotImage();
		int numMines_t = 0;
		for (int i = 0; i < Base.BoardHeight; i++) {
			for (int j = 0; j < Base.BoardWidth; j++) {
				int d = Detect.detect(bi, i, j);
				if (d == -10)
					return d; // death
				Base.onScreen[i][j] = d;
				// Special case for flags
				if (d == -3 || Base.flags[i][j]) {
					Base.onScreen[i][j] = -1;
					Base.flags[i][j] = true;
				}
				if (d == -1) {
					Base.flags[i][j] = false;
				}
				// Update mines count
				if (Base.flags[i][j]) {
					numMines_t++;
				}
			}
		}

		// if(numMines_t < numMines - 2) exit();
		Base.numMines = numMines_t;
		return 0;
	}
}
