package com.belonk.concurrent.ui;

import java.io.IOException;

class UnresponsiveUI {
    private volatile double d = 1d;

    public UnresponsiveUI() throws IOException {
        while (d > 0) {
            d = d + (Math.E + Math.PI) / d;
        }
        System.in.read(); // 不会执行
    }
}

/**
 * 模拟线程在图形界面计算。
 * <p>
 * Created by sun on 2017/3/6.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class ResponsiveUI extends Thread {
    //~ Static fields/initializers =====================================================================================
    public static volatile double d = 1d;

    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================
    public ResponsiveUI() {
        setDaemon(true);
        start();
    }

    //~ Methods ========================================================================================================

    // 线程单独执行运算任务
    @Override
    public void run() {
        while (true) {
            d = d + (Math.PI + Math.E) / d;
        }
    }

    public static void main(String[] args) throws IOException {
        // new UnresponsiveUI();
        new ResponsiveUI();
        System.in.read(); // 等待用户输入
        System.out.println(d); // 打印线程计算结果
    }
}
