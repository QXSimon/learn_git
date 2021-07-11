package outbound;

import filter.HeaderHttpResponseFilter;
import filter.HttpRequestFilter;
import filter.HttpResponseFilter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import router.HttpEndpointRouter;
import router.RandomHttpEndpointRouter;
import thread.NamedThreadFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author SimonChiou
 * @version 1.0
 * @description
 * @date 2021/7/11 下午4:14
 */
public class HttpOutboundHandler {
    private CloseableHttpAsyncClient httpclient;
    private ExecutorService proxyService;
    private List<String> backendUrls;

    private HttpResponseFilter filter = new HeaderHttpResponseFilter();
    private HttpEndpointRouter router = new RandomHttpEndpointRouter();

    public HttpOutboundHandler(List<String> proxyServers) {
        backendUrls=proxyServers.stream().map(this::formatUrl).collect(Collectors.toList()) ;

        int cores = Runtime.getRuntime().availableProcessors();
        long keepAliveTime=1000;
        int queueSize=2048;

        RejectedExecutionHandler handler= new ThreadPoolExecutor.CallerRunsPolicy();
        proxyService = new ThreadPoolExecutor(cores,cores,keepAliveTime,TimeUnit.MILLISECONDS
                ,new ArrayBlockingQueue(queueSize),new NamedThreadFactory("proxyService"),handler);

        IOReactorConfig ioConfig=IOReactorConfig.custom()
                .setConnectTimeout(1000)
                .setSoTimeout(1000)
                .setIoThreadCount(cores)
                .setRcvBufSize(32 * 1024)
                .build();

        httpclient = HttpAsyncClients.custom()
                .setMaxConnTotal(40)
                .setMaxConnPerRoute(8)
                .setDefaultIOReactorConfig(ioConfig)
                .setKeepAliveStrategy((response,context) -> 6000)
                .build();

        httpclient.start();
    }

    private String formatUrl(String backend){
        return backend.endsWith("/")? backend.substring(0,backend.length()-1): backend;
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpRequestFilter filter){
        String backendUrl = router.route(this.backendUrls);

        final String url = backendUrl + fullRequest.uri();
        filter.filter(fullRequest,ctx);
        proxyService.submit(() -> fetchGet(fullRequest,ctx,url));
    }

    private void fetchGet(FullHttpRequest inboundRequest, ChannelHandlerContext ctx, String url) {
        final HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(HTTP.CONN_DIRECTIVE,HTTP.CONN_KEEP_ALIVE);

        httpclient.execute(httpGet, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse endpointResponse) {
                try{
                    handleResponse(inboundRequest,ctx,endpointResponse);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Exception e) {
                httpGet.abort();
                e.printStackTrace();
            }

            @Override
            public void cancelled() {
                httpGet.abort();
            }
        });
    }

    private void handleResponse(final FullHttpRequest inboundRequest, final ChannelHandlerContext ctx, final HttpResponse endpointResponse) throws Exception{
        FullHttpResponse gatewayResponse = null;
        try {
            byte[] body = EntityUtils.toByteArray(endpointResponse.getEntity());

            gatewayResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(body));

            gatewayResponse.headers().set("Content-Type", "Application/json")
                    .setInt("Content-Length", Integer.parseInt(endpointResponse.getFirstHeader("Content-Length").getValue()));
            filter.filter(gatewayResponse);
        }catch (Exception e){
            e.printStackTrace();
            gatewayResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.NO_CONTENT);
            exceptionCaught(ctx,e);
        }finally {
            if(inboundRequest!=null){
                if(!HttpUtil.isKeepAlive(inboundRequest)){
                    ctx.write(gatewayResponse).addListener(ChannelFutureListener.CLOSE);
                }else{
                    ctx.write(gatewayResponse);
                }
            }
            ctx.flush();
        }

    }

    private void exceptionCaught(ChannelHandlerContext ctx, Exception e) {
        e.printStackTrace();
        ctx.close();
    }
}
