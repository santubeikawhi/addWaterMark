package com.converter.jwatermark.demo.image;

import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageInputStreamUtils {

    private static final int ROTATE =150;

    private static final String WATER_MARK_PATH = "watermark/watermark.png";
    
    
    public static void main(String[] args) throws IOException {
        watermark(new FileInputStream("D:/爱莎.jpg"),new FileOutputStream("D:/test111.jpg"));
    }

    private static void watermark(InputStream fileInputStream, OutputStream outputStream) throws IOException {
        BufferedImage original = ImageIO.read(fileInputStream);
        ClassPathResource classPathResource = new ClassPathResource(WATER_MARK_PATH);
        //水印图片
        BufferedImage markImage = ImageIO.read(classPathResource.getFile());
        // determine image type and handle correct transparency
        Graphics2D g2d = original.createGraphics();
        //透明度
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.7f));
        //角度
        g2d.rotate(ROTATE);
        int canvasHeight = original.getHeight();
        int canvasWidth = original.getWidth();
        int markHeight = markImage.getHeight();
        int markWidth = markImage.getWidth();
        for(int i=-canvasHeight;i<canvasWidth+canvasHeight;i=i+markWidth){
            for(int j=-canvasWidth;j<canvasHeight+canvasWidth;j=j+markHeight+100){
                g2d.drawImage(markImage,i,j,markImage.getWidth(),markImage.getHeight(),null);
            }
        }
        g2d.dispose();
        ImageIO.write(original, "jpg", outputStream);
    }
    
}
