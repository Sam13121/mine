package solver.mine.boundry;

import solver.mine.utils.Base;

public class Boundary {
	public static boolean isBoundry(int[][] board, int i, int j) {
		if (board[i][j] != -1)
			return false;

		boolean oU = false, oD = false, oL = false, oR = false;
		if (i == 0)
			oU = true;
		if (j == 0)
			oL = true;
		if (i == Base.BoardHeight - 1)
			oD = true;
		if (j == Base.BoardWidth - 1)
			oR = true;
		boolean isBoundry = false;

		if (!oU && board[i - 1][j] >= 0)
			isBoundry = true;
		if (!oL && board[i][j - 1] >= 0)
			isBoundry = true;
		if (!oD && board[i + 1][j] >= 0)
			isBoundry = true;
		if (!oR && board[i][j + 1] >= 0)
			isBoundry = true;
		if (!oU && !oL && board[i - 1][j - 1] >= 0)
			isBoundry = true;
		if (!oU && !oR && board[i - 1][j + 1] >= 0)
			isBoundry = true;
		if (!oD && !oL && board[i + 1][j - 1] >= 0)
			isBoundry = true;
		if (!oD && !oR && board[i + 1][j + 1] >= 0)
			isBoundry = true;

		return isBoundry;
	}
}
