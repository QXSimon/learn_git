import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * @author SimonChiou
 * @version 1.0
 * @description socket访问客户端
 * @date 2021/7/4 下午7:21
 */
public class HttpSocketClient {
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        HttpClient httpClient = HttpClient.newBuilder().build();

        String uri ="http://localhost:8801";

        HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                .header("User-Agent","HttpSocketClient")
                .header("Accept","*/*")
                .timeout(Duration.ofSeconds(10))
                .version(HttpClient.Version.HTTP_1_1)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("response status code:"+response.statusCode());
        System.out.println("response header:"+response.headers());
        System.out.println("response body:"+response.body());
    }
}
