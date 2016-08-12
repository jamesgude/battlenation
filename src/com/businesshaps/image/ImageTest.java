package com.businesshaps.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageTest {
	public ImageTest() {
		BufferedImage image = null;
	    try {
	
	        File url = new File("C:\\testimage\\land-rover_range-rover_evoque_5-door_11_03.jpg");
	        image = ImageIO.read(url);
	        int iw = image.getWidth();
	        int ih = image.getHeight();
	        BufferedImage image2 = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
	        
	        Graphics2D g = image.createGraphics();
	        g.setColor(new Color(255, 255, 255));
	        g.drawRect(550,550,0,0);
	        g.dispose();
	        
	        int argb = image2.getRGB(1, 1);
	        int rgb[] = new int[] {
	        	    (argb >> 16) & 0xff, //red
	        	    (argb >>  8) & 0xff, //green
	        	    (argb      ) & 0xff  //blue
	        	};
	        System.out.println(rgb[0] + " - " + rgb[1] + " - " + rgb[2]);

	        ImageIO.write(image2, "jpg",new File("C:\\testimage\\out.jpg"));
	        ImageIO.write(image2, "gif",new File("C:\\testimage\\out.gif"));
	        ImageIO.write(image2, "png",new File("C:\\testimage\\out.png"));
	
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    System.out.println("Done");
	}
	public static void main(String[] args) {
		new ImageTest();
	}
}
