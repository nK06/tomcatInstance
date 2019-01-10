package com.panther.http;

import com.sun.deploy.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @ClassName: Response
 * @Description: TODO
 * @Author: makai
 * @Date: 2019-01-10 13:31
 * @Version: 1.0
 */
public class Response {

    private static final int BUFFER_SIZE = 1024;
    private Request request;
    private OutputStream outputStream;

    public Response (OutputStream outputStream){
        this.outputStream = outputStream;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fileInputStream = null;
        try{
            if(request.getUrl() == null || "".equals(request.getUrl()) || "/".equals(request.getUrl())){
               request.setUrl("/index.html");
            }
            File file = new File(HTTPServer.WEB_ROOT,request.getUrl());
            if(file.exists()){
                fileInputStream = new FileInputStream(file);
                int ch = fileInputStream.read(bytes,0,BUFFER_SIZE);
                while (ch != -1){
                    outputStream.write(("HTTP/1.1 200 OK\n" +
                            "Server: Microsoft-IIS/4.0\n" +
                            "Date: Mon, 5 Jan 2019 13:13:33 GMT  Content-Type: text/html\n" +
                            "Last-Modified: Mon, 5 Jan 2019 13:13:12 GMT  Content-Length: 555\n\n").getBytes());
                    outputStream.write(bytes,0,ch);
                    ch = fileInputStream.read(bytes,0,BUFFER_SIZE);
                }
            }else {
                // file not found
                String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 23\r\n" +
                        "\r\n" +
                        "<h1>File Not Found</h1>";
                outputStream.write(errorMessage.getBytes());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }
}
