package com.shiv.utilities;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class ScreenshotUtility {
	
	public static String failedEvidence() throws IOException, AWTException {
		
        // Get current timestamp for uniqueness
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        
        // Create screenshot directory (adjust the path as necessary)
        String screenshotDir = System.getProperty("user.dir") + File.separator + "src\\test\\resources\\com\\shiv\\screenshots";
        File directory = new File(screenshotDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Capture the screen
        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenImage = robot.createScreenCapture(screenRect);

        // Set destination path for the screenshot
        String destination = screenshotDir + File.separator + "Screen_" + timestamp + ".png";
        ImageIO.write(screenImage, "png", new File(destination));

        return destination;
    }

}
