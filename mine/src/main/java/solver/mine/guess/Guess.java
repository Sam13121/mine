package solver.mine.guess;

import solver.mine.utils.Base;
import solver.mine.screen.Screen;
import solver.mine.mouse.Movement;

public class Guess {
	public static void guessRandomly() throws Throwable {
		System.out.println("Attempting to guess randomly");
		while (true) {
			int k = Base.rand.nextInt(Base.BoardHeight * Base.BoardWidth);
			int i = k / Base.BoardWidth;
			int j = k % Base.BoardWidth;

			if (Screen.onScreen(i, j) == -1 && !Base.flags[i][j]) {
				Movement.clickOn(i, j);
				return;
			}
		}
	}
}
