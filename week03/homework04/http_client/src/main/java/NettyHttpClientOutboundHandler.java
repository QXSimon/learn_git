import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author SimonChiou
 * @version 1.0
 * @description
 * @date 2021/7/11 下午6:00
 */
public class NettyHttpClientOutboundHandler extends ChannelInboundHandlerAdapter {
    private final ByteBuf firstMessage;

    public NettyHttpClientOutboundHandler(){
        byte[] req = "Lalalalala".getBytes(StandardCharsets.UTF_8);
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("msg: "+msg);
        if(msg instanceof FullHttpResponse){
            FullHttpResponse response = (FullHttpResponse) msg;
            ByteBuf buff = response.content();
            System.out.println("response:"+buff.toString(CharsetUtil.UTF_8));
        }
//        ByteBuf buf = (ByteBuf) msg;
//        byte[] req =new byte[buf.readableBytes()];
//        buf.readBytes(req);
//        String body = new String(req,StandardCharsets.UTF_8);
//        System.out.println("now is: "+body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Unexpected exception from downstream: "+cause.getMessage());
        ctx.close();
    }
}
