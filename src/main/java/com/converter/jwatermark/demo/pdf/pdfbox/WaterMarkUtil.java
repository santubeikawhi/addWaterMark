package com.converter.jwatermark.demo.pdf.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;

import java.io.File;
import java.io.FileInputStream;

public class WaterMarkUtil {

    public static void main(String[] args) {
        try {
            watermark(new File("D:\\1-尚硅谷项目课程系列之Elasticsearch.pdf"),"D:\\watermark.pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void watermark(File file, String path) throws Exception {
        //创建新pdf文件
        File tmpPDF = new File(path);
        //打开pdf文件
        PDDocument doc = PDDocument.load(file);
        doc.setAllSecurityToBeRemoved(true);
        //遍历pdf所有页
        for (PDPage page : doc.getPages()) {
            PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
            //水印名称
            String ts = "测试";
            //引入字体文件 解决中文汉字乱码问题
            PDFont font = PDType0Font.load(doc, new FileInputStream("D:\\STXINGKA.TTF"), true);
            float fontSize = 30;
            PDResources resources = page.getResources();
            PDExtendedGraphicsState r0 = new PDExtendedGraphicsState();
            // 水印透明度
            r0.setNonStrokingAlphaConstant(0.2f);
            r0.setAlphaSourceFlag(true);
            cs.setGraphicsStateParameters(r0);
            //水印颜色
            cs.setNonStrokingColor(200, 0, 0);
            cs.beginText();
            cs.setLeading(16.5f);
            cs.setFont(font, fontSize);
            //根据水印文字大小长度计算横向坐标需要渲染几次水印
            float h = ts.length() * fontSize;
            for (int i = 0; i <= 10; i++) {
                // 获取旋转实例
                cs.setTextMatrix(Matrix.getRotateInstance(-150, i * 100, 0));
                cs.showText(ts);
                for (int j = 0; j < 20; j++) {
                    cs.setTextMatrix(Matrix.getRotateInstance(-150, i * 100, j * h));
                    cs.showText(ts);
                    cs.newLine();
                    cs.showText("测试2");
                }
            }
            cs.endText();
            cs.restoreGraphicsState();
            cs.close();
        }
        doc.save(tmpPDF);
        doc.close();
    }
}
