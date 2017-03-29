package solver.mine.first;

import solver.mine.utils.Base;
import solver.mine.screen.Screen;
import solver.mine.mouse.Movement;

public class First {
	public static void firstSquare() throws Throwable {
		// Check that it's indeed the first square
		Base.robot.mouseMove(0, 0);
		Thread.sleep(20);
		Screen.updateOnScreen();
		Base.robot.mouseMove(Base.mouseLocX, Base.mouseLocY);
		boolean isUntouched = true;
		for (int i = 0; i < Base.BoardHeight; i++) {
			for (int j = 0; j < Base.BoardWidth; j++) {
				if (Screen.onScreen(i, j) != -1)
					isUntouched = false;
			}
		}
		if (!isUntouched) {
			return;
		}

		// Click the middle
		Movement.clickOn(Base.BoardHeight / 2 - 1, Base.BoardWidth / 2 - 1);
		Movement.clickOn(Base.BoardHeight / 2 - 1, Base.BoardWidth / 2 - 1);
		Thread.sleep(200);

	}
}
