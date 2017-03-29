package solver.mine.attempt;

import solver.mine.utils.Base;
import solver.mine.screen.Screen;
import solver.mine.bayesian.Bayesian;
import solver.mine.count.Count;
import solver.mine.mouse.Movement;

public class Attempt {

	public static void attemptFlagMine() throws Throwable {

		for (int i = 0; i < Base.BoardHeight; i++) {
			for (int j = 0; j < Base.BoardWidth; j++) {

				if (Screen.onScreen(i, j) >= 1) {
					int curNum = Screen.onScreen(i, j);

					// Flag necessary squares
					if (curNum == Count.countFreeSquaresAround(Base.onScreen, i, j)) {
						for (int ii = 0; ii < Base.BoardHeight; ii++) {
							for (int jj = 0; jj < Base.BoardWidth; jj++) {
								if (Math.abs(ii - i) <= 1 && Math.abs(jj - j) <= 1) {
									if (Screen.onScreen(ii, jj) == -1 && !Base.flags[ii][jj]) {
										Base.flags[ii][jj] = true;
										Movement.flagOn(ii, jj);
									}
								}
							}
						}
					}

				}
			}
		}

	}

	public static void attemptMove() throws Throwable {

		boolean success = false;
		for (int i = 0; i < Base.BoardHeight; i++) {
			for (int j = 0; j < Base.BoardWidth; j++) {

				if (Screen.onScreen(i, j) >= 1) {

					// Count how many mines around it
					int curNum = Base.onScreen[i][j];
					int mines = Count.countFlagsAround(Base.flags, i, j);
					int freeSquares = Count.countFreeSquaresAround(Base.onScreen, i, j);

					// Click on the deduced non-mine squares
					if (curNum == mines && freeSquares > mines) {
						success = true;

						// Use the chord or the classical algorithm
						if (freeSquares - mines > 1) {
							Movement.chordOn(i, j);
							Base.onScreen[i][j] = 0; // hack to make it not
														// overclick a square
							continue;
						}

						// Old algorithm: don't chord
						for (int ii = 0; ii < Base.BoardHeight; ii++) {
							for (int jj = 0; jj < Base.BoardWidth; jj++) {
								if (Math.abs(ii - i) <= 1 && Math.abs(jj - j) <= 1) {
									if (Screen.onScreen(ii, jj) == -1 && !Base.flags[ii][jj]) {
										Movement.clickOn(ii, jj);
										Base.onScreen[ii][jj] = 0;
									}
								}
							}
						}

					}
				}
			}
		}

		if(success)	return;

		// Bring in the big guns
		Bayesian.bayesianSolver();
	}
}
