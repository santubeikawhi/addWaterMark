package com.converter.jwatermark.demo.image.test;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImageUtils {

    private static final String SPLIT_WORD = "\n";

    public void test() {

        try {
            Iterator<ImageReader> imageReaders = ImageIO.getImageReadersBySuffix("jpg");
            ImageReader imageReader = imageReaders.next();
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(new FileInputStream("D://爱莎.jpg"));
            imageReader.setInput(imageInputStream,true);
            System.out.println(imageReader.getWidth(0));
            System.out.println(imageReader.getHeight(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) throws IOException {
        
        BufferedImage bufferedImage = ImageIO.read(new File("D:/test11.jpg"));
        watermark(bufferedImage,"供应链贸易轨迹凭证 NB20211202 \n NB202112020001");
        File newFile = new File("D:/test111.jpg");
        ImageIO.write(bufferedImage, "jpg", newFile);
    }

    private static void watermark(BufferedImage original, String watermarkText) {
        // create graphics context and enable anti-aliasing
        Graphics2D g2d = original.createGraphics();
        //透明度
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.4f));
        g2d.scale(1, 1);
        //抗锯齿
        g2d.addRenderingHints(
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON));
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // create watermark text shape for rendering
        Font font = new Font(Font.DIALOG_INPUT, Font.ITALIC , 16);
        String[] split = watermarkText.split(SPLIT_WORD);
        List<TextShape> list = new ArrayList<>();
        for (String str : split) {
            list.add(new TextShape(font,g2d,str));
        }

        // use a gradient that repeats 4 times
        g2d.setPaint(new GradientPaint(0, 0,
                Color.GRAY,
                original.getWidth() / 2, original.getHeight() / 2,
                Color.GRAY));
        g2d.setStroke(new BasicStroke(0.3f));


        // step over image rendering watermark text
        for (double x = 0; x < original.getWidth(); ) {
            double totalY = 0;
            double xStep=0;
            for ( double y = 0 ; y < original.getHeight();) {
                for (TextShape textShape : list) {
                    g2d.draw(textShape.getRotatedText());
                    g2d.fill(textShape.getRotatedText());
                    //下移一行
                    g2d.translate(0, textShape.getyStep());
                    totalY += textShape.getyStep();
                    y += textShape.getyStep();
                    xStep =  (textShape.getxStep() > xStep) ? textShape.getxStep() : xStep ;
                }
            }
            x += xStep;
            //左移一列，行复位到0
            g2d.translate(  xStep, -(totalY));
        }

    }

    private static class TextShape{

        private double textWidth;
        private double textHeight;
        private  Shape rotatedText;

        private double xStep ;
        private double yStep;

        public TextShape(Font font,Graphics2D g2d,String text) {
            GlyphVector fontGV = font.createGlyphVector(g2d.getFontRenderContext(), text);
            Rectangle size = fontGV.getPixelBounds(g2d.getFontRenderContext(), 8, 8);
            Shape textShape = fontGV.getOutline();
            this.textWidth = size.getWidth();
            this.textHeight = size.getHeight();
            //逆时针倾斜45° -(Math.PI / 4d)
            AffineTransform rotate45 = AffineTransform.getRotateInstance(- (Math.PI / 4d));
            this.rotatedText = rotate45.createTransformedShape(textShape);
            this.xStep = (Math.sqrt(2) * textWidth) / 2 + 50;
            this.yStep = Math.sqrt(textHeight * textHeight *2) + 25;
        }

        public double getTextWidth() {
            return textWidth;
        }

        public void setTextWidth(double textWidth) {
            this.textWidth = textWidth;
        }

        public double getTextHeight() {
            return textHeight;
        }

        public void setTextHeight(double textHeight) {
            this.textHeight = textHeight;
        }

        public Shape getRotatedText() {
            return rotatedText;
        }

        public void setRotatedText(Shape rotatedText) {
            this.rotatedText = rotatedText;
        }

        public double getxStep() {
            return xStep;
        }

        public void setxStep(double xStep) {
            this.xStep = xStep;
        }

        public double getyStep() {
            return yStep;
        }

        public void setyStep(double yStep) {
            this.yStep = yStep;
        }
    }


}
