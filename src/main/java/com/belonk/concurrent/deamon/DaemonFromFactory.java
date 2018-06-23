package com.belonk.concurrent.deamon;

import com.belonk.util.PrintHelper;

/**
 * Created by sun on 2017/3/5.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class DaemonFromFactory implements Runnable {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================

    @Override
    public void run() {
        try {
            while (true) { // 循环打印输出
                Thread.sleep(100);
                PrintHelper.println(Thread.currentThread() + " " + this);
            }
        } catch (InterruptedException e) {
            PrintHelper.print("sleep() interrupted");
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        ExecutorService executorService = Executors.newCachedThreadPool(new DaemonThreadFactory());
//        for (int i = 0; i < 10; i++) {
//            executorService.execute(new DaemonFromFactory());
//        }
//        PrintHelper.println("All daemons started");
//        // 由于创建的都是后台线程，一旦main函数执行完成，程序退出
//        Thread.sleep(1000);
        String s = "酒店郑州高铁东站店.............................................................................................";
        System.out.println(s.getBytes().length);
        s="美宿洲际酒店合肥宝业东城广场店...........................................................................";
        System.out.println(s.getBytes().length);
    }
}
