package com.converter.jwatermark.util.impl;

import com.converter.jwatermark.util.JRayWaterMarker;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class WithOutWaterMarker implements JRayWaterMarker {

    @Override
    public String getFileType(String mimeType) {
        return null;
    }

    @Override
    public Boolean support(String mimeType) {
        return false;
    }

    @Override
    public Boolean watermark(InputStream inputStream, OutputStream outputStream, String mimeType) {
        try {
            return IOUtils.copy(inputStream, outputStream) > 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
