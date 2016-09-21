package pccontrol;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScreenSnapshot {
	private String path = "d:/desktop.jpg";
	   public String snapshot() {  
	        try {  
	            int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();  //要截取的宽度
	            int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();  //要截取的高度
	            Robot robot = new Robot();  
	            BufferedImage image = robot.createScreenCapture(new Rectangle(width,height));  
	            image = image.getSubimage(0, 0, width, height);
	            ImageIO.write (image, "jpg" , new File(path));   //保存在D盘 图片名为1.jpg
	           
	        } catch (AWTException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	        return path;
	    }  
	  
}
