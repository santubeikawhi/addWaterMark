# addWaterMark
水印，图片，word，pdf的水印添加

描述：
图片通过ImageIO对图片进行水印添加

word 通过spire.doc 开源版 【缺点，首页上存在warn标识，可以通过poi+ spire.doc.free 来处理这个，删除第一页，目前项目内未实现。

pdf 通过pdfbox 添加水印

测试方式：

http://localhost:8080/attachs/download/watermark?contentType=application/pdf&fileName=myPdf.pdf

http://localhost:8080/attachs/download/watermark?contentType=application/msword&fileName=myWord.doc

http://localhost:8080/attachs/download/watermark?contentType=image/png&fileName=myImage.jpg
