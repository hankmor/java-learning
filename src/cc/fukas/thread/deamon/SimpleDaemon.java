package cc.fukas.thread.deamon;

import cc.fukas.util.PrintHelper;

/**
 * Created by sun on 2017/3/5.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class SimpleDaemon implements Runnable {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================

    @Override
    public void run() {
        try {
            while (true) { // 循环打印输出
                Thread.sleep(1000);
                PrintHelper.println(Thread.currentThread() + " " + this);
            }
        } catch (InterruptedException e) {
            PrintHelper.print("sleep() interrupted");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new SimpleDaemon());
            thread.setDaemon(true);// 设置为后台线程，在线程启动前调用
            thread.start();
        }
        // 如果开启一个非后台线程，则程序永远不会终止
        // Thread thread = new Thread(new SimpleDaemon());
        // thread.start();
        PrintHelper.println("All daemons started");
        // 由于创建的都是后台线程，一旦main函数执行完成，程序退出
        Thread.sleep(1000);
    }
}
