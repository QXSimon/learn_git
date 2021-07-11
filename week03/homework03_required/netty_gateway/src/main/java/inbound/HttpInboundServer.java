package inbound;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author SimonChiou
 * @version 1.0
 * @description
 * @date 2021/7/11 下午4:04
 */
public class HttpInboundServer {
    private int port;
    private String proxyServer;

    public HttpInboundServer(int port ,String proxyServer){
        this.port=port;
        this.proxyServer=proxyServer;
    }

    public void run() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup(16);
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.SO_REUSEADDR,true)
                    .childOption(ChannelOption.SO_RCVBUF,32*1024)
                    .childOption(ChannelOption.SO_SNDBUF,32*1024)
                    .childOption(EpollChannelOption.SO_REUSEPORT,true)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpInboundInitializer(proxyServer));

            Channel ch = b.bind(port).sync().channel();
            System.out.println("开始Netty http 服务吕，监听地址和端口为 http://127.0.0.1:"+port+"/");
            ch.closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
