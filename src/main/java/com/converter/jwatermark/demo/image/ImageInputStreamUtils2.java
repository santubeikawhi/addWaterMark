package com.converter.jwatermark.demo.image;

import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageInputStreamUtils2 {

    private static final int ROTATE =150;

    private static final String WATER_MARK_PATH = "D:\\watermark.png";
    
    
    public static void main(String[] args) throws IOException {
   /*     File file = new File("D:\\test.png");
        *//*if (!file.exists()) {
            file.createNewFile();
        }*//*
        watermark(null,new FileOutputStream("D:/watermark-rotate.png"));*/

        ClassPathResource classPathResource = new ClassPathResource(WATER_MARK_PATH);
        BufferedImage bufferedImage = rotateImage(ImageIO.read(new File(WATER_MARK_PATH)), -30);

        ImageIO.write(bufferedImage, "png", new FileOutputStream("D:/watermark-rotate.png"));
    }

    private static void watermark(InputStream fileInputStream, OutputStream outputStream) throws IOException {
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(new FileOutputStream("D:\\test.png"));
        BufferedImage original = ImageIO.read(imageOutputStream);
        ClassPathResource classPathResource = new ClassPathResource(WATER_MARK_PATH);
        //水印图片
        BufferedImage markImage = ImageIO.read(classPathResource.getFile());
        // determine image type and handle correct transparency
        Graphics2D g2d = markImage.createGraphics();
        //透明度
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.7f));
        //角度
        g2d.rotate(ROTATE);
        g2d.drawImage(markImage,0,0,markImage.getWidth(),markImage.getHeight(),null);
        g2d.dispose();
        ImageIO.write(markImage, "png", outputStream);
    }

    public static BufferedImage rotateImage( BufferedImage bufferedimage,
                                            final int degree) {
        int w = bufferedimage.getWidth();
        int h = bufferedimage.getHeight();
        int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(w,w, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(Math.toRadians(degree));
        graphics2d.drawImage(bufferedimage,0,0,bufferedimage.getWidth(),bufferedimage.getHeight(),null);
        graphics2d.dispose();
        return img;
    }
    
}
