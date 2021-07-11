import inbound.HttpInboundServer;

/**
 * @author SimonChiou
 * @version 1.0
 * @description
 * @date 2021/7/11 下午3:14
 */
public class NettyServerApplication {
    public final static String GATEWAY_NAME="NIO gateway";
    public final static String GATEWAY_VERSION="1.0.0";

    public static void main(String[] args) {
        String proxyPort = System.getProperty("proxyPort","8808");
        String proxyServer = System.getProperty("proxyServer","http://localhost:8801");

        int port = Integer.parseInt(proxyPort);
        System.out.println(GATEWAY_NAME+" "+GATEWAY_VERSION+" starting......");
        HttpInboundServer inboundServer = new HttpInboundServer(port,proxyServer);
        System.out.println(GATEWAY_NAME+" "+GATEWAY_VERSION+" started at http://localhost:"+port+" for server :"+proxyServer);
        try{
            inboundServer.run();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
