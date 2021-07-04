package javasocket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author SimonChiou
 * @version 1.0
 * @description 每个请求一个线程
 * @date 2021/7/4 下午5:24
 */
public class HttpServer02 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8802);
        while(true){
            try{
                Socket socket=serverSocket.accept();
                new Thread(()->{
                    service(socket);
                }).start();
            }catch (IOException e){
                throw e;
            }
        }

    }

    private static void service(Socket socket)  {
        try(PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);socket){
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-type:text/html;charset=utf-8");

            String body = "Hello, nio2";
            printWriter.println("Content-Length:"+body.getBytes().length);
            printWriter.println();
            printWriter.println(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
