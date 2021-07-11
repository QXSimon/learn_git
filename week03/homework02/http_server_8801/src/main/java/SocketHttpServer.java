import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author SimonChiou
 * @version 1.0
 * @description
 * @date 2021/7/11 下午3:33
 */
public class SocketHttpServer {
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() + 4);
        final ServerSocket serverSocket = new ServerSocket(8801);
        while(true){
            try{
                final Socket socket=serverSocket.accept();
                executorService.execute(()-> service(socket) );
            }catch (IOException e){
                throw e;
            }
        }

    }

    private static void service(Socket socket)  {
        try(PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true); socket){
            System.out.println("received remote request: "+socket.getRemoteSocketAddress().toString());

            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-type:text/html;charset=utf-8");

            String body = "Hello, socket http server 8801 support service homework02";
            printWriter.println("Content-Length:"+body.getBytes().length);
            printWriter.println();
            printWriter.println(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
