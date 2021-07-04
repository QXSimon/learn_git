package javasocket.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.ReferenceCountUtil;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * @author SimonChiou
 * @version 1.0
 * @description 处理请求的Handler
 * @date 2021/7/4 下午6:19
 */
public class HttpHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            FullHttpRequest fullRequest = (FullHttpRequest)msg;
            String uri = fullRequest.uri();
            if (uri.contains("/test")){
                handlerTest(fullRequest,ctx,"Hello SimonChiou");
            }else{
                handlerTest(fullRequest,ctx,"Hello Others");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            ReferenceCountUtil.release(msg);
        }
    }

    private void handlerTest(FullHttpRequest fullRequest, ChannelHandlerContext ctx, String body) {
        FullHttpMessage response = null;
        try{
            String value = body;

            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,OK, Unpooled.wrappedBuffer(value.getBytes()));
            response.headers().set("Content-Type","application/json");
            response.headers().setInt("Content-Length",response.content().readableBytes());
        }catch (Exception e){
            System.out.println("处理出错："+e.getMessage());
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,NO_CONTENT);
        }finally {
            if(fullRequest!=null){
                if(!HttpUtil.isKeepAlive(fullRequest)){
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                }else{
                    response.headers().set(CONNECTION,KEEP_ALIVE);
                    ctx.write(response);
                }
            }
        }
    }
}
