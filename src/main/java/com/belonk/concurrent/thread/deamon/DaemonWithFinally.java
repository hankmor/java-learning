package com.belonk.concurrent.thread.deamon;

import com.belonk.util.PrintHelper;

/**
 * Created by sun on 2017/3/6.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class DaemonWithFinally implements Runnable {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================

    @Override
    public void run() {
        PrintHelper.println("starting a daemon concurrent.");
        try {
            // 睡眠2秒，finally不会执行
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            PrintHelper.println("Finally may not run.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new DaemonWithFinally());
        thread.setDaemon(true);
        thread.start();
        // 主线程睡眠1秒
        Thread.sleep(1000);
    }
}
