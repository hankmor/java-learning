package com.belonk.concurrent.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        executorService.execute(new TestTask(completableFuture));

        // 产生异常未捕获，CompletableFuture无法完成，导致永久阻塞
        // CompletableFuture<String> exceptionCompletableFuture = new CompletableFuture<>();
        // executorService.execute(new TaskWithException(exceptionCompletableFuture));

        // 捕获了异常，但是不知道异常信息
        CompletableFuture<String> exceptionCaughtCompletableFuture = new CompletableFuture<>();
        executorService.execute(new TaskWithCaughtException(exceptionCaughtCompletableFuture));

        // 捕获异常，并使用completeExceptionally方法返回异常信息
        CompletableFuture<String> exceptionWithInfoCompletableFuture = new CompletableFuture<>();
        executorService.execute(new TaskWithCaughtExceptionInfo(exceptionWithInfoCompletableFuture));

        System.out.println("Doing something else...");

        String result = completableFuture.get();
        System.out.println("TestTask result is : " + result);

        // 任务执行时抛出异常，导致get方法永久阻塞
        // result = exceptionCompletableFuture.get();
        // System.out.println("TaskWithException result is : " + result);

        // 捕获了异常，结果可以返回
        result = exceptionCaughtCompletableFuture.get();
        System.out.println("TaskWithCaughtException result is : " + result);

        // 捕获异常，CompletableFuture的completeExceptionally方法会将异常抛出
        try {
            result = exceptionWithInfoCompletableFuture.get();
            System.out.println("TaskWithCaughtExceptionInfo result is : " + result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        System.out.println("All tasks are finished");
        executorService.shutdown();
    }
}

/**
 * 基础测试任务
 */
class TestTask implements Runnable {
    private CompletableFuture<String> completableFuture;

    TestTask(CompletableFuture<String> completableFuture) {
        this.completableFuture = completableFuture;
    }

    @Override
    public void run() {
        System.out.println("TestTask start ... ");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("TestTask end ... ");
        completableFuture.complete("TestTask Result");
    }
}

/**
 * 任务执行时抛出异常
 */
class TaskWithException implements Runnable {
    private CompletableFuture<String> completableFuture;

    TaskWithException(CompletableFuture<String> completableFuture) {
        this.completableFuture = completableFuture;
    }

    @Override
    public void run() {
        System.out.println("TaskWithException start ... ");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("TaskWithException occur some exception ");
    }
}

class TaskWithCaughtException implements Runnable {
    private CompletableFuture<String> completableFuture;

    TaskWithCaughtException(CompletableFuture<String> completableFuture) {
        this.completableFuture = completableFuture;
    }

    @Override
    public void run() {
        System.out.println("TaskWithCaughtException start ... ");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            throw new RuntimeException("TaskWithCaughtException occur some exception ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        completableFuture.complete("Got result after catch the exception");
    }
}

class TaskWithCaughtExceptionInfo implements Runnable {
    private CompletableFuture<String> completableFuture;

    TaskWithCaughtExceptionInfo(CompletableFuture<String> completableFuture) {
        this.completableFuture = completableFuture;
    }

    @Override
    public void run() {
        System.out.println("TaskWithCaughtExceptionInfo start ... ");
        try {
            Thread.sleep(2000);
            throw new RuntimeException("TaskWithCaughtExceptionInfo occur some exception ");
        } catch (Exception e) {
            completableFuture.completeExceptionally(e);
        }
        completableFuture.complete("Got result after catch the exception");
    }
}