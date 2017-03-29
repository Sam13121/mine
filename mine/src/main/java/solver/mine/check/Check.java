package solver.mine.check;

import solver.mine.utils.Base;
import solver.mine.count.Count;
import solver.mine.screen.Screen;

public class Check {
	public static boolean checkConsistency() {
		for (int i = 0; i < Base.BoardHeight; i++) {
			for (int j = 0; j < Base.BoardWidth; j++) {

				int freeSquares = Count.countFreeSquaresAround(Base.onScreen, i, j);
				int numFlags = Count.countFlagsAround(Base.flags, i, j);

				if (Screen.onScreen(i, j) == 0 && freeSquares > 0) {
					return false;
				}
				if ((Screen.onScreen(i, j) - numFlags) > 0 && freeSquares == 0) {
					return false;
				}

			}
		}

		return true;
	}
}
