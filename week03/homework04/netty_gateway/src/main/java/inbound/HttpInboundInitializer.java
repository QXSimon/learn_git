package inbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.List;

/**
 * @author SimonChiou
 * @version 1.0
 * @description
 * @date 2021/7/11 下午3:12
 */
public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {
    private List<String> proxyServers;

    public HttpInboundInitializer(List<String> proxyServers) {
        this.proxyServers = proxyServers;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(1024*1024));
        p.addLast(new HttpInboundHandler(this.proxyServers));
    }
}
