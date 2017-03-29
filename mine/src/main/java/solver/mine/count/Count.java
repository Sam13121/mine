package solver.mine.count;

import solver.mine.utils.Base;
import solver.mine.screen.Screen;

public class Count {

	public static int countFlagsAround(boolean[][] array, int i, int j) {
		int mines = 0;

		// See if we're on the edge of the board
		boolean oU = false, oD = false, oL = false, oR = false;
		if (i == 0)
			oU = true;
		if (j == 0)
			oL = true;
		if (i == Base.BoardHeight - 1)
			oD = true;
		if (j == Base.BoardWidth - 1)
			oR = true;

		if (!oU && array[i - 1][j])
			mines++;
		if (!oL && array[i][j - 1])
			mines++;
		if (!oD && array[i + 1][j])
			mines++;
		if (!oR && array[i][j + 1])
			mines++;
		if (!oU && !oL && array[i - 1][j - 1])
			mines++;
		if (!oU && !oR && array[i - 1][j + 1])
			mines++;
		if (!oD && !oL && array[i + 1][j - 1])
			mines++;
		if (!oD && !oR && array[i + 1][j + 1])
			mines++;

		return mines;
	}

	public static int countFreeSquaresAround(int[][] board, int i, int j) {
		int freeSquares = 0;

		if (Screen.onScreen(i - 1, j) == -1)
			freeSquares++;
		if (Screen.onScreen(i + 1, j) == -1)
			freeSquares++;
		if (Screen.onScreen(i, j - 1) == -1)
			freeSquares++;
		if (Screen.onScreen(i, j + 1) == -1)
			freeSquares++;
		if (Screen.onScreen(i - 1, j - 1) == -1)
			freeSquares++;
		if (Screen.onScreen(i - 1, j + 1) == -1)
			freeSquares++;
		if (Screen.onScreen(i + 1, j - 1) == -1)
			freeSquares++;
		if (Screen.onScreen(i + 1, j + 1) == -1)
			freeSquares++;

		return freeSquares;
	}
}
