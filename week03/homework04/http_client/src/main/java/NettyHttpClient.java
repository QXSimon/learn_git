import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * @author SimonChiou
 * @version 1.0
 * @description
 * @date 2021/7/11 下午5:40
 */
public class NettyHttpClient {

    public static void main(String[] args) throws Exception {
        new NettyHttpClient().connect("127.0.0.1",8808);
    }

    public void connect(String host,int port) throws Exception{
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try{
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new HttpResponseDecoder());
                            socketChannel.pipeline().addLast(new HttpRequestEncoder());
                            socketChannel.pipeline().addLast(new NettyHttpClientOutboundHandler());
                        }
                    });

            ChannelFuture f = bootstrap.connect(host, port).sync();

            URI uri = new URI("/helloworld");
            String data = "Hello netty";
            FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,uri.toASCIIString(), Unpooled.wrappedBuffer(data.getBytes(StandardCharsets.UTF_8)));
            request.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

            f.channel().write(request);
            f.channel().flush();
            f.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
        }
    }
}
