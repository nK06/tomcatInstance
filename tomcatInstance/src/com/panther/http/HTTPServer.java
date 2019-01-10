package com.panther.http;

import sun.security.x509.IPAddressName;

import java.io.*;
import java.net.*;

/**
 * @ClassName: HTTPServer
 * @Description: TODO
 * @Author: makai
 * @Date: 2019-01-10 10:02
 * @Version: 1.0
 */
public class HTTPServer {

    public static String WEB_ROOT = resourceUrl();

    private static final String SHUTDONW_COMMAND = "/SHUTDOWN";

    private boolean shutdown = false;

    public static void main(String[] args) throws IOException {
        HTTPServer httpServer = new HTTPServer();
        httpServer.await();
    }

    public void await() {
        ServerSocket serverSocket = null;
        int port = 8080;
        try {
            serverSocket = new ServerSocket(port,1, InetAddress.getByName("127.0.0.1"));
        } catch (UnknownHostException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (!shutdown){
            Socket socket = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                // 监听Socket Server
                socket = serverSocket.accept();

                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                // creat Request
                Request request = new Request(inputStream);
                request.parse();

                //creat Response
                Response response = new Response(outputStream);
                response.setRequest(request);
                response.sendStaticResource();

                socket.close();

                //check if the pervious URI is a shutdown command
                shutdown = request.getUrl().equals(SHUTDONW_COMMAND);

            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public void printHTML (String resourceName) throws IOException {
        BufferedInputStream bufferedInputStream = (BufferedInputStream) getResource(resourceName).getContent();

        byte[] bs = new byte[1024*1024*1024];
        //从文件中按字节读取内容，到文件尾部时read方法将返回-1
        int bytesRead = 0;
        while ((bytesRead = bufferedInputStream.read(bs)) != -1) {

            //将读取的字节转为字符串对象
            String chunk = new String(bs, 0, bytesRead);
            System.out.print(chunk);
        }
        bufferedInputStream.close();
    }
    /**
     * 获取resource的URL
     * @param resourceName
     * @return
     */
    public static URL getResource(String resourceName){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(resourceName);
        return url;
    }

    public static String resourceUrl(){
        return Thread.currentThread().getClass().getResource("/").getPath()+"/templates/html";
    }
}

