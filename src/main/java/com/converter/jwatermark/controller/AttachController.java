package com.converter.jwatermark.controller;

import com.converter.jwatermark.req.AttachWaterMarkReq;
import com.converter.jwatermark.util.JRayWaterMarkFactory;
import com.converter.jwatermark.util.JRayWaterMarker;
import feign.form.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.rmi.ServerException;

@RestController
@RequestMapping("attachs")
@CrossOrigin
@Slf4j
public class AttachController {

  
  @Resource
  private JRayWaterMarkFactory factory;

  
  private static final String CONTENT_DISPOSITION = "Content-Disposition";

  private static final String CONTENT_LENGTH = "Content-Length";

  private static final String ATTACHMENT_FILENAME ="attachment;filename=";

  public static void main(String[] args) throws IOException {
/*    System.out.println(this.getClass().getClassLoader().getResource("com/lavasoft/res/a.txt"));
    System.out.println(this.getClass().getResource("/com/lavasoft/res/a.txt"));
  System.out.println(this.getClass().getResource("").getPath());
    System.out.println(this.getClass().getResource("/").getPath());
    System.out.println(this.getClass().getClassLoader().getResource("").getPath());
    System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
   System.out.println(System.getProperty("user.dir"));
    System.out.println(this.getClass().getClassLoader().getResource("com/lavasoft/res/a.txt"));
    System.out.println(this.getClass().getResource("/com/lavasoft/res/a.txt"));
    System.out.println(this.getClass().getResource("").getPath());
    System.out.println(this.getClass().getResource("/").getPath());
    System.out.println(this.getClass().getClassLoader().getResource("").getPath());*/
    System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
    System.out.println(System.getProperty("user.dir"));
    
    FileInputStream fileInputStream = null;
    try {
      fileInputStream = new FileInputStream( Thread.currentThread().getContextClassLoader().getResource("").getPath() + "demo/myImage.jpg");
    } catch (Exception e) {
      e.printStackTrace();
    } finally{
      if (ObjectUtils.isNotEmpty(fileInputStream)) {
        fileInputStream.close();
      }
    }
  }

  @GetMapping(value = "download/watermark")
  public void watermark( AttachWaterMarkReq req, HttpServletResponse res) throws Exception {
    FileInputStream fileInputStream = null;
    try {
      String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
      fileInputStream = new FileInputStream( path + "/demo/"+req.getFileName());
      // 清空response
      res.reset();
      // 设置response的Header
      res.setCharacterEncoding(CharsetUtil.UTF_8.name());
      //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
      //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
      // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
      res.addHeader(CONTENT_DISPOSITION, (ATTACHMENT_FILENAME + URLEncoder.encode(req.getFileName(), CharsetUtil.UTF_8.name())).replaceAll("\\+","%20"));
      //设置Content-type
      res.setContentType(req.getContentType());
      JRayWaterMarker waterMarker = factory.support(req.getContentType());
      if(waterMarker == null){
        throw new ServerException("water mark not support this file type");
      }
      waterMarker.watermark(fileInputStream,res.getOutputStream(),req.getContentType());
      // 告知浏览器文件的大小
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      if(ObjectUtils.isNotEmpty(fileInputStream)){
        fileInputStream.close();
      }
      if(ObjectUtils.isNotEmpty(res.getOutputStream())){
        res.getOutputStream().close();
      }
    }
  }
}
