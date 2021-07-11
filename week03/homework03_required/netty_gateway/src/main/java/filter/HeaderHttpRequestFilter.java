package filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author SimonChiou
 * @version 1.0
 * @description
 * @date 2021/7/11 下午10:26
 */
public class HeaderHttpRequestFilter implements HttpRequestFilter{
    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        fullRequest.headers().set("mu-mu","big-world");
    }
}
