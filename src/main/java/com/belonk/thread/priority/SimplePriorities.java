package main.java.com.belonk.thread.priority;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sun on 2017/3/5.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class SimplePriorities implements Runnable {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================
    private int countDown = 5;
    private volatile double d;
    private int priority;

    //~ Constructors ===================================================================================================
    public SimplePriorities(int priority) {
        this.priority = priority;
    }

    //~ Methods ========================================================================================================


    @Override
    public String toString() {
        return Thread.currentThread() + " : " + countDown;
    }

    @Override
    public void run() {
        // 设置优先级
        Thread.currentThread().setPriority(priority);
        while (true) {
            // 模拟进行复杂运算，看看线程调度情况
//            for (int i = 0; i < 10_000_000; i++) {
//                d += (Math.PI + Math.E) / (double) i;
//                if (i % 1000 == 0)
//                    Thread.yield();
//            }
            System.out.println(this);
            if (--countDown == 0) return;
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executorService.execute(new SimplePriorities(Thread.MIN_PRIORITY));
        }
        executorService.execute(new SimplePriorities(Thread.MAX_PRIORITY));
        executorService.shutdown();
    }
}
