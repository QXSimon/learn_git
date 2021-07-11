package thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author SimonChiou
 * @version 1.0
 * @description
 * @date 2021/7/11 下午4:24
 */
public class NamedThreadFactory implements ThreadFactory {
    private final ThreadGroup group;
    private final AtomicInteger threadNumber=new AtomicInteger(1);

    private final String namePrefix;
    private final boolean daemon;

    public NamedThreadFactory(String namePrefix, boolean daemon) {
        this.daemon=daemon;
        this.namePrefix=namePrefix;

        SecurityManager s= System.getSecurityManager();
        group=(s!=null)? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    public NamedThreadFactory(String namePrefix) {
        this(namePrefix,false);
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread t = new Thread(group,runnable,namePrefix+"-thread-"+threadNumber.getAndIncrement(),0);
        t.setDaemon(daemon);
        return t;
    }
}
