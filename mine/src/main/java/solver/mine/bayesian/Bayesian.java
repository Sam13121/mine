package solver.mine.bayesian;

import java.util.ArrayList;
import java.util.LinkedList;

import solver.mine.boundry.Boundary;
import solver.mine.check.Check;
import solver.mine.count.Count;
import solver.mine.mouse.Movement;
import solver.mine.pair.Pair;
import solver.mine.screen.Screen;
import solver.mine.utils.Base;

public class Bayesian {

	public static void bayesianSolver() throws Throwable {
		// Be extra sure it's consistent
		Thread.sleep(100);
		Base.robot.mouseMove(0, 0);
		Thread.sleep(20);
		Screen.updateOnScreen();
		Base.robot.mouseMove(Base.mouseLocX, Base.mouseLocY);
		// dumpPosition();
		if (!Check.checkConsistency())
			return;

		// Timing
		long bayesianTime = System.currentTimeMillis();

		ArrayList<Pair> borderTiles = new ArrayList<Pair>();
		ArrayList<Pair> allEmptyTiles = new ArrayList<Pair>();

		// Endgame case: if there are few enough tiles, don't bother with border
		// tiles.
		Base.borderOptimization = false;
		for (int i = 0; i < Base.BoardHeight; i++)
			for (int j = 0; j < Base.BoardWidth; j++)
				if (Screen.onScreen(i, j) == -1 && !Base.flags[i][j])
					allEmptyTiles.add(new Pair(i, j));

		// Determine all border tiles
		for (int i = 0; i < Base.BoardHeight; i++)
			for (int j = 0; j < Base.BoardWidth; j++)
				if (Boundary.isBoundry(Base.onScreen, i, j) && !Base.flags[i][j])
					borderTiles.add(new Pair(i, j));

		// Count how many squares outside the knowable range
		int numOutSquares = allEmptyTiles.size() - borderTiles.size();
		if (numOutSquares > Base.BF_LIMIT) {
			Base.borderOptimization = true;
		} else
			borderTiles = allEmptyTiles;

		// Something went wrong
		if (borderTiles.size() == 0)
			return;

		// Run the segregation routine before recursing one by one
		// Don't bother if it's endgame as doing so might make it miss some
		// cases
		ArrayList<ArrayList<Pair>> segregated;
		if (!Base.borderOptimization) {
			segregated = new ArrayList<ArrayList<Pair>>();
			segregated.add(borderTiles);
		} else
			segregated = bayesianSegregate(borderTiles);

		int totalMultCases = 1;
		boolean success = false;
		double prob_best = 0; // Store information about the best probability
		int prob_besttile = -1;
		int prob_best_s = -1;
		for (int s = 0; s < segregated.size(); s++) {

			// Copy everything into temporary constructs
			Base.bayesian_solutions = new ArrayList<boolean[]>();
			Base.bayesian_board = Base.onScreen.clone();
			Base.knownMine = Base.flags.clone();

			Base.knownEmpty = new boolean[Base.BoardHeight][Base.BoardWidth];
			for (int i = 0; i < Base.BoardHeight; i++)
				for (int j = 0; j < Base.BoardWidth; j++)
					if (Base.bayesian_board[i][j] >= 0)
						Base.knownEmpty[i][j] = true;
					else
						Base.knownEmpty[i][j] = false;

			// Compute solutions -- here's the time consuming step
			bayesianRecurse(segregated.get(s), 0);

			// Something screwed up
			if (Base.bayesian_solutions.size() == 0)
				return;

			// Check for solved squares
			for (int i = 0; i < segregated.get(s).size(); i++) {
				boolean allMine = true;
				boolean allEmpty = true;
				for (boolean[] sln : Base.bayesian_solutions) {
					if (!sln[i])
						allMine = false;
					if (sln[i])
						allEmpty = false;
				}

				Pair<Integer, Integer> q = segregated.get(s).get(i);
				int qi = q.getFirst();
				int qj = q.getSecond();

				// Muahaha
				if (allMine) {
					Base.flags[qi][qj] = true;
					Movement.flagOn(qi, qj);
				}
				if (allEmpty) {
					success = true;
					Movement.clickOn(qi, qj);
				}
			}

			totalMultCases *= Base.bayesian_solutions.size();

			// Calculate probabilities, in case we need it
			if (success)
				continue;
			int maxEmpty = -10000;
			int iEmpty = -1;
			for (int i = 0; i < segregated.get(s).size(); i++) {
				int nEmpty = 0;
				for (boolean[] sln : Base.bayesian_solutions) {
					if (!sln[i])
						nEmpty++;
				}
				if (nEmpty > maxEmpty) {
					maxEmpty = nEmpty;
					iEmpty = i;
				}
			}
			double probability = (double) maxEmpty / (double) Base.bayesian_solutions.size();

			if (probability > prob_best) {
				prob_best = probability;
				prob_besttile = iEmpty;
				prob_best_s = s;
			}

		}

		// But wait! If there's any hope, bruteforce harder (by a factor of
		// 32x)!
		if (Base.BF_LIMIT == 8 && numOutSquares > 8 && numOutSquares <= 13) {
			System.out.println("Extending bruteforce horizon...");
			Base.BF_LIMIT = 13;
			bayesianSolver();
			Base.BF_LIMIT = 8;
			return;
		}

		bayesianTime = System.currentTimeMillis() - bayesianTime;
		if (success) {
			System.out.printf("TANK Solver successfully invoked at step %d (%dms, %d cases)%s\n", Base.numMines,
					bayesianTime, totalMultCases, (Base.borderOptimization ? "" : "*"));
			return;
		}

		// Take the guess, since we can't deduce anything useful
		System.out.printf("TANK Solver guessing with probability %1.2f at step %d (%dms, %d cases)%s\n", prob_best,
				Base.numMines, bayesianTime, totalMultCases, (Base.borderOptimization ? "" : "*"));
		@SuppressWarnings("unchecked")
		Pair<Integer, Integer> q = segregated.get(prob_best_s).get(prob_besttile);
		int qi = q.getFirst();
		int qj = q.getSecond();
		Movement.clickOn(qi, qj);

	}

	public static ArrayList<ArrayList<Pair>> bayesianSegregate(ArrayList<Pair> borderTiles) {

		ArrayList<ArrayList<Pair>> allRegions = new ArrayList<ArrayList<Pair>>();
		ArrayList<Pair> covered = new ArrayList<Pair>();

		while (true) {

			LinkedList<Pair> queue = new LinkedList<Pair>();
			ArrayList<Pair> finishedRegion = new ArrayList<Pair>();

			// Find a suitable starting point
			for (Pair firstT : borderTiles) {
				if (!covered.contains(firstT)) {
					queue.add(firstT);
					break;
				}
			}

			if (queue.isEmpty())
				break;

			while (!queue.isEmpty()) {

				Pair<Integer, Integer> curTile = queue.poll();
				int ci = curTile.getFirst();
				int cj = curTile.getSecond();

				finishedRegion.add(curTile);
				covered.add(curTile);

				// Find all connecting tiles
				for (Pair<Integer, Integer> tile : borderTiles) {
					int ti = tile.getFirst();
					int tj = tile.getSecond();

					boolean isConnected = false;

					if (finishedRegion.contains(tile))
						continue;

					if (Math.abs(ci - ti) > 2 || Math.abs(cj - tj) > 2)
						isConnected = false;

					else {
						// Perform a search on all the tiles
						tilesearch: for (int i = 0; i < Base.BoardHeight; i++) {
							for (int j = 0; j < Base.BoardWidth; j++) {
								if (Screen.onScreen(i, j) > 0) {
									if (Math.abs(ci - i) <= 1 && Math.abs(cj - j) <= 1 && Math.abs(ti - i) <= 1
											&& Math.abs(tj - j) <= 1) {
										isConnected = true;
										break tilesearch;
									}
								}
							}
						}
					}

					if (!isConnected)
						continue;

					if (!queue.contains(tile))
						queue.add(tile);

				}
			}

			allRegions.add(finishedRegion);

		}

		return allRegions;

	}

	

	// Recurse from depth k (0 is root)
	// Assumes the bayesian variables are already set; puts solutions in
	// the static arraylist.
	public static void bayesianRecurse(ArrayList<Pair> borderTiles, int k) {

		// Return if at this point, it's already inconsistent
		int flagCount = 0;
		for (int i = 0; i < Base.BoardHeight; i++)
			for (int j = 0; j < Base.BoardWidth; j++) {

				// Count flags for endgame cases
				if (Base.knownMine[i][j])
					flagCount++;

				int num = Base.bayesian_board[i][j];
				if (num < 0)
					continue;

				// Total bordering squares
				int surround = 0;
				if ((i == 0 && j == 0) || (i == Base.BoardHeight - 1 && j == Base.BoardWidth - 1))
					surround = 3;
				else if (i == 0 || j == 0 || i == Base.BoardHeight - 1 || j == Base.BoardWidth - 1)
					surround = 5;
				else
					surround = 8;

				int numFlags = Count.countFlagsAround(Base.knownMine, i, j);
				int numFree = Count.countFlagsAround(Base.knownEmpty, i, j);

				// Scenario 1: too many mines
				if (numFlags > num)
					return;

				// Scenario 2: too many empty
				if (surround - numFree < num)
					return;
			}

		// We have too many flags
		if (flagCount > Base.TOT_MINES)
			return;

		// Solution found!
		if (k == borderTiles.size()) {

			// We don't have the exact mine count, so no
			if (!Base.borderOptimization && flagCount < Base.TOT_MINES)
				return;

			boolean[] solution = new boolean[borderTiles.size()];
			for (int i = 0; i < borderTiles.size(); i++) {
				Pair<Integer, Integer> s = borderTiles.get(i);
				int si = s.getFirst();
				int sj = s.getSecond();
				solution[i] = Base.knownMine[si][sj];
			}
			Base.bayesian_solutions.add(solution);
			return;
		}

		Pair<Integer, Integer> q = borderTiles.get(k);
		int qi = q.getFirst();
		int qj = q.getSecond();

		// Recurse two positions: mine and no mine
		Base.knownMine[qi][qj] = true;
		bayesianRecurse(borderTiles, k + 1);
		Base.knownMine[qi][qj] = false;

		Base.knownEmpty[qi][qj] = true;
		bayesianRecurse(borderTiles, k + 1);
		Base.knownEmpty[qi][qj] = false;

	}

}
