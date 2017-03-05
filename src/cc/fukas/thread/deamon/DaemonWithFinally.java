package cc.fukas.thread.deamon;

import cc.fukas.util.PrintHelper;

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
        PrintHelper.println("starting a daemon thread.");
        try {
            // 睡眠2毫秒，finally不会执行
            Thread.sleep(2);
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
        // 睡眠1毫秒
        Thread.sleep(1);
    }
}
