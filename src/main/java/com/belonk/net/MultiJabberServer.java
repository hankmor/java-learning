package com.belonk.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by sun on 2021/12/24.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class MultiJabberServer {
	//~ Static fields/constants/initializer

	static final int PORT = 8080;

	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) throws IOException {
		ServerSocket s = new ServerSocket(PORT);
		System.out.println("Server Started");
		try {
			while(true) {
				// Blocks until a connection occurs:
				Socket socket = s.accept();
				try {
					new ServeOneJabber(socket);
				} catch(IOException e) {
					// If it fails, close the socket,
					// otherwise the thread will close it:
					socket.close();
				} }
		} finally {
			s.close();
		}
	}
}

class ServeOneJabber extends Thread {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public ServeOneJabber(Socket s) throws IOException {
		socket = s;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		// parameter TRUE is set to enable auto-flush:
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		// If any of the above calls throw an exception, the caller is responsible for closing the socket. Otherwise the
		// thread will close it.
		start(); // 开启单独的线程来收取和影响客户端数据
	}

	public void run() {
		try {
			while (true) {
				String str = in.readLine();
				// 收到 END 退出
				if (str.equals("END")) break;
				System.out.println("Echoing: " + str);
				out.println(str);
			}
			System.out.println("closing...");
		} catch (IOException e) {
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
}