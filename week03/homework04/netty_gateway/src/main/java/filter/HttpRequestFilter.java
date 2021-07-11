package filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author SimonChiou
 * @version 1.0
 * @description
 * @date 2021/7/11 下午10:25
 */
public interface HttpRequestFilter {
    void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx);
}
