package com.converter.jwatermark.constant;

import lombok.Getter;

public class MimeTypeFileType {

    @Getter
    public enum PDF  {

        PDF("application/pdf","pdf");


        private String mimeType;

        private String fileType;

        PDF(String mimeType, String fileType) {
            this.fileType = fileType;
            this.mimeType = mimeType;
        }


        /**
         * 根据mimeType获取
         * @param mimeType
         * @return
         */
        public static final String findFileType(String mimeType){
            PDF[] values = PDF.values();
            for(PDF iv : values){
                if(mimeType.equals(iv.getMimeType())){
                    return iv.getFileType();
                }
            }
            return null;
        }

    }
    
    @Getter
    public enum IMAGE  {

        PNG("image/png","png"),
        DWG("image/vnd.dwg","dwg"),
        DXF("image/vnd.dxf","dxf"),
        JPG("image/jpeg","jpg"),
        TIFF("image/tiff","tiff");


        private String mimeType;

        private String fileType;

        IMAGE(String mimeType, String fileType) {
            this.fileType = fileType;
            this.mimeType = mimeType;
        }


        /**
         * 根据mimeType获取
         * @param mimeType
         * @return
         */
        public static final String findFileType(String mimeType){
            IMAGE[] values = IMAGE.values();
            for(IMAGE iv : values){
                if(mimeType.equals(iv.getMimeType())){
                    return iv.getFileType();
                }
            }
            return null;
        }

    }
    
    @Getter
    public enum WORD  {
        
        DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document","docx"),
        DOCX_1("application/octet-stream","docx"),
        DOC("application/msword","doc");


        private String mimeType;
        
        private String fileType;

        WORD(String mimeType, String fileType) {
            this.fileType = fileType;
            this.mimeType = mimeType;
        }


        /**
         * 根据mimeType获取
         * @param mimeType
         * @return
         */
        public static final String findFileType(String mimeType){
            WORD[] values = WORD.values();
            for(WORD iv : values){
                if(mimeType.equals(iv.getMimeType())){
                    return iv.getFileType();
                }
            }
            return null;
        }

    }
}
