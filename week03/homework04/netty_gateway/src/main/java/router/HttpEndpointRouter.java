package router;

import java.util.List;

/**
 * @author SimonChiou
 * @version 1.0
 * @description
 * @date 2021/7/11 下午10:56
 */
public interface HttpEndpointRouter {
    String route(List<String> endpoints);
}
