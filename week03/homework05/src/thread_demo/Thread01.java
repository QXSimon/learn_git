package thread_demo;

/**
 * @author SimonChiou
 * @version 1.0
 * @description 试试守护线程
 * @date 2021/7/12 下午10:17
 */
public class Thread01 {
    public static void main(String[] args) throws InterruptedException {
        //实现Runnable示例1，
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Thread t = Thread.currentThread();
                System.out.println("current Thread : "+t.getName());
            }
        };
        Thread thread = new Thread(task);
        thread.setName("test-thread-1");
        //设置为守护线程,如果当前只有守护线程在运行，JVM就会把当前进程直接停止, 所以线程内的sleep(5000)还没醒，謞停止了。
        thread.setDaemon(true);
        thread.start();

        //要完整执行线程内的代码，有两个方法
        //方法一，去掉设置为守护线程：thread.setDaemon(false); //default = false
        //方法二，主线程，主动等待一定时间，让线程执行完
        Thread.sleep(6000);
    }

    //实现Runnable接口，示例2
    private static class CustomThread implements Runnable{

        @Override
        public void run() {
            System.out.println("balabalbalbalbalbabala");
        }
    }
}
