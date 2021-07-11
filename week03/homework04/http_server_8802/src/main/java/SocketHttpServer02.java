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
 * @date 2021/7/11 下午11:18
 */
public class SocketHttpServer02 {
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        final ServerSocket serverSocket = new ServerSocket(8802);
        final SocketHttpServer02 server02 = new SocketHttpServer02();
        while(true){
            try{
                final Socket socket=serverSocket.accept();
                executorService.execute(()-> server02.service(socket) );
            }catch (IOException e){
                throw e;
            }
        }

    }

    private void service(Socket socket)  {
        try(PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true); socket){
            System.out.println("8802 received remote request: "+socket.getRemoteSocketAddress().toString());
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-type:text/html;charset=utf-8");

            String body = "Hello, socket http server 8802 support service";
            printWriter.println("Content-Length:"+body.getBytes().length);
            printWriter.println();
            printWriter.println(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
