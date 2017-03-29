package solver.mine.calibrate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import solver.mine.screenshotimage.ScreenShotImage;
import solver.mine.utils.Base;
import solver.mine.dark.IsDark;

public class Calibrate {
	public static void calibrate(){
		System.out.println("Calibrete Screen...");
		BufferedImage bi = ScreenShotImage.screenShotImage();
		bi.createGraphics();
		Graphics2D g = (Graphics2D)bi.getGraphics();
		
		int hh = 0;
		int firh = 0;
		int firw = 0;
		int lash = 0;
		int lasw = 0;
		int tot = 0;
		int and = 11111111;
		
		for(int w=0; w<Base.ScreenWidth; w++){
			for(int h=0; h<Base.ScreenHeight; h++){
				int rgb = bi.getRGB(w, h);
				
				if(IsDark.isDark(rgb)){
					if(w<10 || h<10 || w>Base.ScreenWidth-10 || h>Base.ScreenHeight-10)
						continue;
					if(IsDark.isDark(bi.getRGB(w+7,h)))
				    if(IsDark.isDark(bi.getRGB(w-7,h)))
				    if(IsDark.isDark(bi.getRGB(w,h+7)))
				    if(IsDark.isDark(bi.getRGB(w,h-7)))
				    if(IsDark.isDark(bi.getRGB(w+3,h)))
				    if(IsDark.isDark(bi.getRGB(w-3,h)))
				    if(IsDark.isDark(bi.getRGB(w,h+3)))
				    if(IsDark.isDark(bi.getRGB(w,h-3)))
				    if(!IsDark.isDark(bi.getRGB(w-7,h-7)))
				    if(!IsDark.isDark(bi.getRGB(w+7,h-7)))
				    if(!IsDark.isDark(bi.getRGB(w-7,h+7)))
				    if(!IsDark.isDark(bi.getRGB(w+7,h+7)))
				    if(!IsDark.isDark(bi.getRGB(w-3,h-3)))
				    if(!IsDark.isDark(bi.getRGB(w+3,h-3)))
				    if(!IsDark.isDark(bi.getRGB(w-3,h+3)))
				    if(!IsDark.isDark(bi.getRGB(w+3,h+3))){
				    	 g.setColor(Color.YELLOW);
				    	 g.fillRect(w-3, h-3, 7, 7);
				    	 tot++;
				    	 Base.BoardHeight++;
				            //System.out.printf("%d %d\n",h,w);
				    	 if(h+w < and){
				    		 firh = h;
				    		 firw = w;
				    		 and = h + w;
				    	 }
				    	 
				    	 lash = h;
				    	 lasw = w;
				    }
				}
			}
			if(Base.BoardHeight >1){
				hh = Base.BoardHeight;
				Base.BoardHeight = 1;
			}
		}	
		
		try {
			ImageIO.write(bi, "jpeg", new File("2.jpeg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Base.BoardHeight = hh;
	    if(tot % (Base.BoardHeight-1) == 0)
	    	Base.BoardWidth = tot / (Base.BoardHeight-1) + 1;
	    else Base.BoardWidth = 0;

	    // Determine BoardPix by taking an average
	    Base.BoardPix = 0.5*((double)(lasw - firw) / (double)(Base.BoardWidth-2))
	             + 0.5*((double)(lash - firh) / (double)(Base.BoardHeight-2));
	    
	    // Determine first cell position (where to click)
	    int halfsiz = (int)Base.BoardPix / 2;
	    Base.BoardTopW = firw - halfsiz + 3;
	    Base.BoardTopH = firh - halfsiz + 3;
	    
	    System.out.printf("firh=%d, firw=%d, lash=%d, lasw=%d, tot=%d\n",firh,firw,lash,lasw,tot);
	    System.out.printf("BoardWidth=%d, BoardHeight=%d, BoardPix=%f\n", Base.BoardWidth, Base.BoardHeight, Base.BoardPix);
	    System.out.printf("BoardTopW=%d, BoardTopH=%d\n",Base.BoardTopW, Base.BoardTopH);
	}
}
