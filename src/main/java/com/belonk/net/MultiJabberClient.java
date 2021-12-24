package com.belonk.net;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/12/24.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class MultiJabberClient {
	//~ Static fields/constants/initializer

	/**
	 * 最大线程数，也就是最大客户端数
	 */
	static final int MAX_THREADS = 10;

	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args)
			throws IOException, InterruptedException {
		InetAddress addr = InetAddress.getByName(null);
		while (true) {
			// 创建客户端
			if (JabberClientThread.threadCount() < MAX_THREADS)
				new JabberClientThread(addr);
			TimeUnit.MILLISECONDS.sleep(100);
		}
	}
}

class JabberClientThread extends Thread {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	// ID计数器，没创建一个实例加1
	private static int counter = 0;
	// 客户端id
	private final int id = counter++;
	/**
	 * 线程数量
	 */
	private static int threadcount = 0;

	public static int threadCount() {
		return threadcount;
	}

	public JabberClientThread(InetAddress addr) {
		System.out.println("Making client " + id);
		threadcount++;
		try {
			socket = new Socket(addr, MultiJabberServer.PORT);
		} catch (IOException e) {
			// If the creation of the socket fails, nothing needs to be cleaned up.
		}
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// Enable auto-flush:
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			start();
		} catch (IOException e) {
			// The socket should be closed on any failures other than the socket constructor:
			try {
				socket.close();
			} catch (IOException e2) {
			}
		}
		// Otherwise the socket will be closed by the run() method of the thread.
	}

	public void run() {
		try {
			// 每个客户端发送25次请求
			for (int i = 0; i < 25; i++) {
				out.println("Client " + id + ": " + i);
				String str = in.readLine();
				System.out.println(str);
			}
			// 发送断开连接标识
			out.println("END");
		} catch (IOException e) {
		} finally {
			System.out.println("Closing");
			// Always close it:
			try {
				socket.close();
			} catch (IOException e) {
			}
			threadcount--; // 运行完成，则线程数减1
		}
	}
}
