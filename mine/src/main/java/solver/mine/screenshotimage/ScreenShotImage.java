package solver.mine.screenshotimage;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import solver.mine.utils.Base;

public class ScreenShotImage {

	public static BufferedImage screenShotImage() {
		try {
			Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			Base.ScreenWidth = captureSize.width;
			Base.ScreenHeight = captureSize.height;
			BufferedImage bufferedImage = Base.robot.createScreenCapture(captureSize);
			return bufferedImage;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
