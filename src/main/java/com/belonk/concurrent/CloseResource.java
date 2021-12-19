package com.belonk.concurrent;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/12/19.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class CloseResource {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * IO阻塞的线程，可以通过关闭资源实现中断。
	 */

	public static void main(String[] args) throws IOException, InterruptedException {
		ExecutorService executorService = Executors.newCachedThreadPool();

		// 创建 socket 服务端, 仅为了让客户端可以正常连接
		ServerSocket serverSocket = new ServerSocket(8080);
		// 创建 socket 客户端
		Socket socket = new Socket("localhost", 8080);
		// 获取客户端输入流
		InputStream socketInputStream = socket.getInputStream();

		executorService.execute(new IOInterrupt(socketInputStream));
		executorService.execute(new IOInterrupt(System.in));

		TimeUnit.SECONDS.sleep(1);

		// 关闭所有任务，会给每个任务线程发送中断信号
		System.out.println("Interrupting all tasks.");
		executorService.shutdownNow();

		// TODO 看起来，socketInputStream 被关闭时触发了线程中断，而 System.in 关闭时没有触发中断，不论它们关闭的前后顺序

		// 关闭 System.in 流
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Closing " + System.in.getClass().getSimpleName());
		System.in.close();

		// 关闭 socket 流，观察中断状态
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Closing " + socketInputStream.getClass().getSimpleName());
		socketInputStream.close(); // 关闭会触发线程中断

		/*
		Waiting for read:
		Waiting for read:
		Interrupting all tasks.
		Closing BufferedInputStream
		Exiting IOInterrupt.
		Closing SocketInputStream
		Blocked IO was interrupted.
		Exiting IOInterrupt.
		 */
	}
}