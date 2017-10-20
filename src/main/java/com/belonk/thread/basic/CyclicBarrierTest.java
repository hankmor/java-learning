package main.java.com.belonk.thread.basic;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by sun on 2017/9/20.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class CyclicBarrierTest {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================

    public void test1() {
        int N = 4;
        CyclicBarrier barrier = new CyclicBarrier(N);
        for (int i = 0; i < N; i++)
            new Writer(barrier).start();
    }

    public void test2() {
        int N = 4;
        // runnable内的代码回选择一个线程来执行
        CyclicBarrier barrier = new CyclicBarrier(N, new Runnable() {
            @Override
            public void run() {
                System.out.println("当前线程" + Thread.currentThread().getName());
            }
        });

        for (int i = 0; i < N; i++)
            new Writer(barrier).start();
    }

    public static void main(String[] args) {
        CyclicBarrierTest cyclicBarrierTest = new CyclicBarrierTest();
//        cyclicBarrierTest.test1();
        cyclicBarrierTest.test2();
    }

    class Writer extends Thread {
        private CyclicBarrier cyclicBarrier;

        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("线程" + Thread.currentThread().getName() + "正在写入数据...");
            try {
                Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                System.out.println("线程" + Thread.currentThread().getName() + "写入数据完毕，等待其他线程写入完毕");
                cyclicBarrier.await(); // 执行完就等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            // 所有线程执行完，然后全部执行下边的代码
            System.out.println("所有线程写入完毕，继续处理其他任务...");
        }
    }
}
