package com.panther.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName: Request
 * @Description: TODO
 * @Author: makai
 * @Date: 2019-01-10 13:30
 * @Version: 1.0
 */
public class Request {

    private InputStream inputStream;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Request(InputStream inputStream){
        this.inputStream = inputStream;
    }

    // get the HttpRequest URL
    public void parse() {
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try{
            i = inputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j =0 ; j< i; j++) {
            request.append((char)buffer[j]);
        }
        System.out.println(request.toString());
        url = parseUrl(request.toString());
    }

    // get URL from requestString
    public String parseUrl(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if(index2 > index1) {
                return requestString.substring(index1+1,index2);
            }
        }
        return null;
    }

}
