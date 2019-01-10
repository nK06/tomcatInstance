package com.panther.socket;

import java.io.*;
import java.net.Socket;

/**
 * @ClassName: SocketTest
 * @Description: TODO
 * @Author: makai
 * @Date: 2019-01-09 16:19
 * @Version: 1.0
 */
public class SocketTest {


    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1",59951);
        OutputStream os = socket.getOutputStream();
        boolean autoflush = true;
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),autoflush);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // send HTTP request
        printWriter.println("GET /index.html HTTP/1.1");
        printWriter.println("Host: localhost:59951");
        printWriter.println("Connecting: Close");
        printWriter.println();

        // read response

        boolean loop = true;
        StringBuffer stringBuffer = new StringBuffer(8096);
        while (loop){
            if (bufferedReader.ready()) {
                int i = 0 ;
                while (i != -1){
                    i = bufferedReader.read();
                    stringBuffer.append((char)i);
                }
                loop = false;
            }
            Thread.currentThread().sleep(1000);
        }

        System.out.println(stringBuffer.toString());
        socket.close();
    }
}
