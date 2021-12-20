package com.belonk.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by sun on 2021/12/20.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class SocketServer {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(8080);
		Socket socket = serverSocket.accept();
		System.out.println(socket.getInetAddress().getHostAddress() + " connected.");
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		// 循环不断地从客户端中按行读取数据
		while (true) {
			String line = reader.readLine();
			// 约定字符q表示退出和关闭连接
			if (line != null && !line.equals("q")) {
				System.out.println("Server received: " + line);
			} else {
				break;
			}
		}
		serverSocket.close();
	}
}