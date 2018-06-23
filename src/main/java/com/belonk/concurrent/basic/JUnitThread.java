package com.belonk.concurrent.basic;

import org.junit.Test;

/**
 * Created by sun on 2017/9/5.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class JUnitThread {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================
    public static class MyThread extends Thread {
        public static int count = 0;

        @Override
        public void run() {
            count++;
            System.out.println(MyThread.count);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new MyThread().start();
        }
    }

    @Test
    public void test() {
        for (int i = 0; i < 100; i++) {
            new MyThread().start();
        }
    }
}
