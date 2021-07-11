package router;

import java.util.List;
import java.util.Random;

/**
 * @author SimonChiou
 * @version 1.0
 * @description
 * @date 2021/7/11 下午10:57
 */
public class RandomHttpEndpointRouter implements HttpEndpointRouter{
    @Override
    public String route(List<String> urls) {
        int size = urls.size();
        Random random = new Random(System.currentTimeMillis());

        return urls.get(random.nextInt(size));
    }
}
