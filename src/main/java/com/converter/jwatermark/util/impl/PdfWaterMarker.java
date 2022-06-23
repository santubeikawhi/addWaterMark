package com.converter.jwatermark.util.impl;

import com.converter.jwatermark.constant.MimeTypeFileType;
import com.converter.jwatermark.util.JRayWaterMarker;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class PdfWaterMarker implements JRayWaterMarker {
    
    private static final int HEIGHT_STEP = 100;

    private static final int WIDTH_STEP = 80;

    private static final double THETA = 30;

    private static final String WATER_MARK_PATH = "watermark/watermark.png";

    @Override
    public String getFileType(String mimeType) {
        return MimeTypeFileType.PDF.findFileType(mimeType);
    }
    
    @Override
    public Boolean watermark(InputStream inputStream, OutputStream outputStream, String mimeType) {
        try {
            //打开pdf文件
            PDDocument doc = PDDocument.load(inputStream);
            doc.setAllSecurityToBeRemoved(true);
            //遍历pdf所有页
            for (PDPage page : doc.getPages()) {
                ClassPathResource classPathResource = new ClassPathResource(WATER_MARK_PATH);
                //创建PDImageXObject对象
                PDImageXObject imageXObject = createFromFileByExtension(classPathResource.getInputStream(),classPathResource.getFilename(),doc);
                PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
                PDExtendedGraphicsState pdExtGfxState = new PDExtendedGraphicsState();
                // 水印透明度
                pdExtGfxState.setNonStrokingAlphaConstant(0.8f);
                pdExtGfxState.setAlphaSourceFlag(true);
                pdExtGfxState.getCOSObject().setItem(COSName.BM, COSName.MULTIPLY);
                cs.setGraphicsStateParameters(pdExtGfxState);
                for(int y= 0; y <= page.getMediaBox().getHeight();y =  (y+imageXObject.getHeight()+HEIGHT_STEP)){
                    for(int x= 0; x <= page.getMediaBox().getWidth();x = (x+imageXObject.getWidth()+WIDTH_STEP)){
                        cs.saveGraphicsState();
                        Matrix rotate = new Matrix();
                        rotate.rotate(Math.toRadians(THETA));
                        cs.transform(rotate);
                        cs.drawImage(imageXObject, x, y);
                        cs.restoreGraphicsState();
                    }
                }
                cs.close();
            }
            doc.save(outputStream);
            doc.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static PDImageXObject createFromFileByExtension(InputStream inputStream,String filename, PDDocument doc) throws IOException
    {
        int dot = filename.lastIndexOf('.');
        if (dot == -1)
        {
            throw new IllegalArgumentException("Image type not supported: " + filename);
        }
        String ext = filename.substring(dot + 1).toLowerCase();
        if ("jpg".equals(ext) || "jpeg".equals(ext))
        {
            PDImageXObject imageXObject = JPEGFactory.createFromStream(doc, inputStream);
            inputStream.close();
            return imageXObject;
        }
        if ("gif".equals(ext) || "bmp".equals(ext) || "png".equals(ext))
        {
            BufferedImage bim = ImageIO.read(inputStream);
            return LosslessFactory.createFromImage(doc, bim);
        }
        throw new IllegalArgumentException("Image type not supported: " + filename);
    }

}
