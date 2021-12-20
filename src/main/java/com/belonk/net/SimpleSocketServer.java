package com.belonk.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/12/20.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class SimpleSocketServer {
	//~ Static fields/constants/initializer

	public static int port = 1234;

	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		try {
			// 创建 socket 服务端
			ServerSocket serverSocket = new ServerSocket(port);
			// 接收客户端的连接，一直阻塞直到有客户端链接上来
			Socket socket = serverSocket.accept();
			// 客户端发送的数据流
			InputStream inputStream = socket.getInputStream();
			// 读取数据流，这里仅读取一行
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String content = bufferedReader.readLine();
			System.out.println("Server received ：" + content);
			// 关闭流
			inputStream.close();
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}