package com.belonk.concurrent;

/***
 * 测试在 java 多线程中，多核心 cpu 是否可以被充分利用
 */
public class CpuPerformance {
    private static int x = 0;

    // 不断消耗 cpu
    static void loop() {
        while (true) {
            x = x ^ 1;
        }
    }

    // 运行程序，可以看到，我的 macOS 机器上 8 核心 cpu 可以跑到 700% 以上，除了其他占用的 cpu，说明基本上是跑满了的，说明
    // java 多线程程序可以充分利用 cpu 资源
    public static void main(String[] args) {
        int cpuNum = Runtime.getRuntime().availableProcessors();
        System.out.printf("cpu count: %d\n", cpuNum);
        for (int i = 0; i < cpuNum; i++) {
            Thread t = new Thread(() -> loop());
            System.out.printf("thread %s is running\n", t.getName());
            t.start();
        }
    }
}