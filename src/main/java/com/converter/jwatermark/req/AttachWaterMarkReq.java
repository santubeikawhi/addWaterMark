package com.converter.jwatermark.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yangjie
 * @description
 * @time 2021-07-02
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachWaterMarkReq {

    /**
     * 文件类型
     */
    private String attachId;

    /**
     * 附件名称
     */
    private String fileName;

    /**
     * 附件内容
     */
    private String attachContent;
    
    private String mimeType;
    
    private String contentType;

}
