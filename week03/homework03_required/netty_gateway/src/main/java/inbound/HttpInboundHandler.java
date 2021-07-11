package inbound;

import filter.HeaderHttpRequestFilter;
import filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import outbound.HttpOutboundHandler;


/**
 * @author SimonChiou
 * @version 1.0
 * @description
 * @date 2021/7/11 下午3:11
 */
public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    private HttpOutboundHandler outboundHandler;
    private HttpRequestFilter filter = new HeaderHttpRequestFilter();

    private final String proxyServer;

    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
        this.outboundHandler=new HttpOutboundHandler(proxyServer);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            FullHttpRequest fullRequest = (FullHttpRequest)msg;
            outboundHandler.handle(fullRequest,ctx,filter);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

