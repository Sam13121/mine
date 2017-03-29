package solver.mine.mouse;

import solver.mine.utils.Base;

public class Movement {
	public static void moveMouse(int mouseX, int mouseY) throws Throwable {
		Base.robot.mouseMove(mouseX, mouseY);
		Base.mouseLocX = mouseX;
		Base.mouseLocY = mouseY;
	}

	public static void clickOn(int i, int j) throws Throwable {
		int mouseX = Base.BoardTopW + (int) (j * Base.BoardPix);
		int mouseY = Base.BoardTopH + (int) (i * Base.BoardPix);
		moveMouse(mouseX, mouseY);

		Base.robot.mousePress(16);
		Thread.sleep(5);
		Base.robot.mouseRelease(16);
		Thread.sleep(10);
	}

	public static void flagOn(int i, int j) throws Throwable {
		int mouseX = Base.BoardTopW + (int) (j * Base.BoardPix);
		int mouseY = Base.BoardTopH + (int) (i * Base.BoardPix);
		moveMouse(mouseX, mouseY);

		Base.robot.mousePress(4);
		Thread.sleep(5);
		Base.robot.mouseRelease(4);
		Thread.sleep(10);
	}

	public static void chordOn(int i, int j) throws Throwable {
		int mouseX = Base.BoardTopW + (int) (j * Base.BoardPix);
		int mouseY = Base.BoardTopH + (int) (i * Base.BoardPix);
		moveMouse(mouseX, mouseY);

		Base.robot.mousePress(4);
		Base.robot.mousePress(16);
		Thread.sleep(5);
		Base.robot.mouseRelease(4);
		Base.robot.mouseRelease(16);
		Thread.sleep(10);
	}
}
