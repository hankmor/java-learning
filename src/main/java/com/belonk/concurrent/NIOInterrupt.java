package com.belonk.concurrent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/12/23.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class NIOInterrupt {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * NIO 能够自动响应中断
	 */

	public static void main(String[] args) {
		try {
			// 创建服务端
			new ServerSocket(8080);
			// 创建客户端
			InetSocketAddress socketAddress = new InetSocketAddress("localhost", 8080);
			// 打开两个Socket渠道
			SocketChannel sc1 = SocketChannel.open(socketAddress);
			SocketChannel sc2 = SocketChannel.open(socketAddress);

			// 创建线程池
			ExecutorService executorService = Executors.newCachedThreadPool();
			// 用 submit 提交一个阻塞任务
			NIOBlocked nioBlocked1 = new NIOBlocked(sc1);
			Future<?> future = executorService.submit(nioBlocked1);
			// 在执行一个阻塞任务
			NIOBlocked nioBlocked2 = new NIOBlocked(sc2);
			executorService.execute(nioBlocked2);
			// 立即关闭线程池，测试是否可以立即中断
			executorService.shutdown();
			System.out.println("ExecutorService shutdown.");

			// 线程池关闭，IO继续阻塞，再测试中断情况

			TimeUnit.SECONDS.sleep(1);
			// 取消任务，触发中断
			System.out.println("Canceling " + nioBlocked1); // 触发 ClosedByInterruptException
			future.cancel(true);
			TimeUnit.SECONDS.sleep(1);
			// 关闭sc2
			System.out.println("Closing sc2");
			sc2.close(); // 触发 AsynchronousCloseException
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		/*
		Prepare to read in NIOBlocked 1
		Prepare to read in NIOBlocked 2
		ExecutorService shutdown.
		Canceling NIOBlocked 1
		ClosedByInterruptException
		Exited NIOBlocked 1
		Closing sc2
		AsynchronousCloseException
		Exited NIOBlocked 2
		 */
	}
}

/**
 * NIO 阻塞任务
 */
class NIOBlocked implements Runnable {
	private static int count = 0;
	private final int id = ++count;
	private final SocketChannel socketChannel;

	public NIOBlocked(SocketChannel channel) {
		this.socketChannel = channel;
	}

	@Override
	public String toString() {
		return "NIOBlocked " + id;
	}

	@Override
	public void run() {
		System.out.println("Prepare to read in " + this);
		try {
			// 阻塞读取
			socketChannel.read(ByteBuffer.allocate(1));

			// 下边的三个异常是继承关系
			// 其他线程中断当前 IO 操作时抛出该异常，抛出前通道将关闭，而且线程的中断状态被设置
		} catch (ClosedByInterruptException e) {
			System.out.println("ClosedByInterruptException");
			// 其他线程关闭了阻塞的 IO 操作通道时，抛出该异常
		} catch (AsynchronousCloseException e) {
			System.out.println("AsynchronousCloseException");
		} catch (IOException e) {
			System.out.println("IOException");
		}
		System.out.println("Exited " + this);

		/*
		如果补捕获前两个异常，那么看到的情况是先输出 Exited XXX，然后抛出异常，说明socket channel先被关闭，然后再抛异常
		 */
	}
}