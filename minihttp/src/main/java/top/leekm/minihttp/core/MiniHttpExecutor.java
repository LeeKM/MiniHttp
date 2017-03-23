package top.leekm.minihttp.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lkm on 2017/3/20.
 */
public class MiniHttpExecutor extends ThreadPoolExecutor {

    public static MiniHttpExecutor defaultExecutor() {
        return generateExecutor(64);
    }

    public static MiniHttpExecutor generateExecutor(int maxConcurrent) {
        MiniHttpExecutor executor = new MiniHttpExecutor(maxConcurrent, maxConcurrent, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    public MiniHttpExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public MiniHttpExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public MiniHttpExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public MiniHttpExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        // TODO: 2017/3/20
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        // TODO: 2017/3/20
    }

    private class MiniHttpRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (r instanceof  Rejactable) {
                ((Rejactable) r).onReject();
            }
        }
    }

    public interface Rejactable {
        void onReject();
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private static AtomicInteger factoryCount = new AtomicInteger(0);
        private AtomicInteger threadCount = new AtomicInteger(0);
        private int factoryIndex = factoryCount.incrementAndGet();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "http-worker:" +
                    factoryIndex + "-" +
                    threadCount.incrementAndGet());
            return thread;
        }
    }
}
