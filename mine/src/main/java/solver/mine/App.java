package solver.mine;

import java.awt.Robot;
import solver.mine.utils.Base;
import solver.mine.attempt.Attempt;
import solver.mine.calibrate.Calibrate;
import solver.mine.check.Check;
import solver.mine.first.First;
import solver.mine.screen.Screen;

public class App {
	public static void main(String[] args) throws Throwable {
		Thread.sleep(2000);
		Base.robot = new Robot();

		Calibrate.calibrate();
		if (Base.BoardWidth < 9 || Base.BoardHeight < 9 || Base.BoardWidth > 30 || Base.BoardWidth > 30) {
			System.out.println("Calibration Failed.");
			return;
		}
		// Initialize internal constructs

		Base.onScreen = new int[Base.BoardHeight][Base.BoardWidth];
		Base.flags = new boolean[Base.BoardHeight][Base.BoardWidth];
		for (int i = 0; i < Base.BoardHeight; i++)
			for (int j = 0; j < Base.BoardWidth; j++)
				Base.flags[i][j] = false;

		First.firstSquare();
		for (int c = 0; c < 1000000; c++) {
			int status = Screen.updateOnScreen();
			if (!Check.checkConsistency()) {
				Base.robot.mouseMove(0, 0);
				status = Screen.updateOnScreen();
				Base.robot.mouseMove(Base.mouseLocX, Base.mouseLocY);
				if (status == -10)
					exit();
				continue;
			}
			// Exit on death
			if (status == -10)
				exit();
			Attempt.attemptFlagMine();
			Attempt.attemptMove();
		}

	}

	static void exit() {
		System.exit(0);
	}

	// Debugging: for whatever reason, dump the board
	static void dumpPosition() {
		for (int i = 0; i < Base.BoardHeight; i++) {
			for (int j = 0; j < Base.BoardWidth; j++) {
				int d = Screen.onScreen(i, j);
				if (Base.flags[i][j])
					System.out.print(".");
				else if (d >= 1)
					System.out.print(d);
				else if (d == 0)
					System.out.print(" ");
				else
					System.out.print("#");

			}
			System.out.println();
		}
		System.out.println();
	}
}
