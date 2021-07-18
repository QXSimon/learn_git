import java.util.concurrent.*;

/**
 * @author SimonChiou
 * @version 1.0
 * @description
 * @date 2021/7/18 下午10:38
 */
public class Demo01 {

    public static void main(String[] args) {
        long start=System.currentTimeMillis();
        ExecutorService es = new ThreadPoolExecutor(1,1,60L, TimeUnit.SECONDS,new SynchronousQueue());
        Future<Integer> future = es.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Integer sum = 0;
                for (int i = 1; i <= 100_000_000; i++) {
                    sum+=i;
                }
                return sum;
            }
        });

        while(!future.isDone()){
            try {
                System.out.println("wait result 100 ms");
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Integer result = null;
        try {
            result = future.get();
            es.shutdown();
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("result: "+result);

        System.out.println("used time :"+(System.currentTimeMillis()-start)+" ms");
    }


}
