package javasocket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author SimonChiou
 * @version 1.0
 * @description 单线和的Socket程序
 * @date 2021/7/4 下午4:38
 */
public class HttpServer01 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8801);
        while(true){
            try(Socket socket=serverSocket.accept()) {
                service(socket);
            }catch (IOException e){
                throw e;
            }
        }

    }

    private static void service(Socket socket) throws IOException {
        try(PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true)){
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-type:text/html;charset=utf-8");

            String body = "Hello, nio1";
            printWriter.println("Content-Length:"+body.getBytes().length);
            printWriter.println();
            printWriter.println(body);
        } catch (IOException e) {
            throw e;
        }
    }
}
