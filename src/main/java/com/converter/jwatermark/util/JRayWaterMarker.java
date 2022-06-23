package com.converter.jwatermark.util;

import org.apache.commons.lang3.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

/**
 * 策略模式
 */
public interface JRayWaterMarker {

    /**
     * 获取流大小，反射获取contentWritten
     * 目前没找到其他好方法
     * @param res
     * @return
     */
    default Long getLength(HttpServletResponse res){
        try {
            Field contentWritten = res.getClass().getSuperclass().getDeclaredField("contentWritten");
            contentWritten.setAccessible(true);
            return (long) contentWritten.get(res);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件类型
     * @param mimeType
     * @return
     */
    String getFileType(String mimeType);

    /**
     * 是否支持
     * @param mimeType
     * @return
     */
    default Boolean support(String mimeType){
        return ObjectUtils.isNotEmpty(getFileType(mimeType));
    }

    /**
     * 加水印
     * @param inputStream
     * @param outputStream
     * @param mimeType
     */
    Boolean watermark(InputStream inputStream, OutputStream outputStream, String mimeType);
}
