package filter;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @author SimonChiou
 * @version 1.0
 * @description
 * @date 2021/7/11 下午10:26
 */
public class HeaderHttpResponseFilter implements HttpResponseFilter{

    @Override
    public void filter(FullHttpResponse fullResponse) {
        fullResponse.headers().set("sc","java-nio-netty");
    }
}
