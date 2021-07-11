package filter;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @author SimonChiou
 * @version 1.0
 * @description
 * @date 2021/7/11 下午10:26
 */
public interface HttpResponseFilter {
    void filter(FullHttpResponse fullResponse);
}
