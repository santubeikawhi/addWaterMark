package com.converter.jwatermark.util.impl;

import com.converter.jwatermark.constant.MimeTypeFileType;
import com.converter.jwatermark.util.JRayWaterMarker;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class ImageWaterMarker implements JRayWaterMarker {
    
    private static final int HEIGHT_STEP = 100;

    private static final int WIDTH_STEP = 80;
    
    private static final int ROTATE =150;

    private static final String WATER_MARK_PATH = "watermark/watermark.png";

    @Override
    public String getFileType(String mimeType) {
        return MimeTypeFileType.IMAGE.findFileType(mimeType);
    }
    
    @Override
    public Boolean watermark(InputStream inputStream, OutputStream outputStream, String mimeType) {
        try {
            BufferedImage original = ImageIO.read(inputStream);
            String fileType = getFileType(mimeType);
            ClassPathResource classPathResource = new ClassPathResource(WATER_MARK_PATH);
            //水印图片
            BufferedImage markImage = ImageIO.read(classPathResource.getInputStream());
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
            for(int i=-canvasHeight;i<canvasWidth+canvasHeight;i=i+markWidth+WIDTH_STEP){
                for(int j=-canvasWidth;j<canvasHeight+canvasWidth;j=j+markHeight+HEIGHT_STEP){
                    g2d.drawImage(markImage,i,j,markImage.getWidth(),markImage.getHeight(),null);
                }
            }
            g2d.dispose();
            ImageIO.write(original, fileType, outputStream);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
