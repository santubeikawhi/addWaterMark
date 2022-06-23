package com.converter.jwatermark.demo.word.spire;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.PictureWatermark;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

public class WordTextWaterMark {

    private static final String WATER_MARK_PATH = "watermark/watermark-rotate.png";
    
    private static final Integer SCALING = 120;

    public static void main(String[] args) throws IOException {
        addImageWatermark(new FileInputStream("D:\\myWord.docx"), new FileOutputStream("D:\\text1.doc"));
    }
    
    public static void addImageWatermark(InputStream inputStream, OutputStream outputStream) throws IOException {

        //加载Word文档
        Document document = new Document();
        document.loadFromStream(inputStream,FileFormat.Auto);

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
        document.saveToFile(outputStream, FileFormat.Doc );
    }

}
