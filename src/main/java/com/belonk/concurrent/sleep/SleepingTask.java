package com.belonk.concurrent.sleep;

import com.belonk.concurrent.thread.LiftOff;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2017/3/5.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class SleepingTask extends LiftOff {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================

    @Override
    public void run() {
        while (countDown-- > 0) {
            System.out.print(status());
            try {
                // 睡眠后，CPU切换到其他线程执行，结果呈现一定的规律性
                TimeUnit.MICROSECONDS.sleep(100);
//                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executorService.execute(new SleepingTask());
        }
        executorService.shutdown();
    }
}
