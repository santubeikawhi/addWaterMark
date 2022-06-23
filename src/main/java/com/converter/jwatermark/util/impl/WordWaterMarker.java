package com.converter.jwatermark.util.impl;

import com.converter.jwatermark.constant.MimeTypeFileType;
import com.converter.jwatermark.util.JRayWaterMarker;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.PictureWatermark;
import com.spire.doc.Section;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class WordWaterMarker implements JRayWaterMarker {

    /**
     * 水印的图片
     */
    private static final String WATER_MARK_PATH = "watermark/watermark-rotate.png";

    private static final Integer SCALING = 120;

    @Override
    public String getFileType(String mimeType) {
        return MimeTypeFileType.WORD.findFileType(mimeType);
    }

    @Override
    public Boolean watermark(InputStream inputStream, OutputStream outputStream, String mimeType) {
        ByteArrayOutputStream spire = null;
        try {
            spire = new ByteArrayOutputStream();

            //加载Word文档
            Document document = new Document();
            document.loadFromStream(inputStream,FileFormat.Auto);

            //获取第一个节,由于Free Spire for java 会在word开头添加文字 The document was created with Spire.DOC for Java
            //解决方案是 ，先添加空白页，然后使用Apache POI 删除空白页
            Section section = new Section(document);
            document.getSections().insert(0,section);
            
            //创建PictureWatermark实例
            PictureWatermark picture = new PictureWatermark();
            ClassPathResource classPathResource = new ClassPathResource(WATER_MARK_PATH);

            //设置水印图片属性
            picture.setPicture(new FileInputStream(classPathResource.getFile()));
            picture.setScaling(SCALING);
            picture.isWashout(false);

            //添加水印图片到文档
            document.setWatermark(picture);

            //保存结果文件
            document.saveToFile(spire, FileFormat.Doc );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (spire != null) {
                try {
                    spire.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
