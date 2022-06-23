package com.converter.jwatermark.demo.pdf.pdfbox;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

public class WaterMarkImageInputStreamUtil {

    private static final int HEIGHT_STEP = 100;
    
    private static final int WIDTH_STEP = 60;

    private static final String WATER_MARK_PATH = "watermark/watermark.png";
    
    public static void main(String[] args) {
        try {
            watermark(new FileInputStream("D:\\1-尚硅谷项目课程系列之Elasticsearch.pdf"),new FileOutputStream("D:\\watermar1k.pdf"),30);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void watermark(InputStream inputStream, OutputStream outputStream, double theta) throws Exception {
        //打开pdf文件
        PDDocument doc = PDDocument.load(inputStream);
        doc.setAllSecurityToBeRemoved(true);
        //遍历pdf所有页
        for (PDPage page : doc.getPages()) {
            
            ClassPathResource classPathResource = new ClassPathResource(WATER_MARK_PATH);
            //创建PDImageXObject对象
            PDImageXObject imageXObject =  PDImageXObject.createFromFileByContent(classPathResource.getFile(), doc);
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
                    rotate.rotate(Math.toRadians(theta));
                    cs.transform(rotate);
                    cs.drawImage(imageXObject, x, y);
                    cs.restoreGraphicsState();
                }
                
            }
            
            cs.close();
        }
        doc.save(outputStream);
        doc.close();
    }
}
