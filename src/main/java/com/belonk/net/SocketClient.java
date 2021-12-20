package com.belonk.net;

import java.io.*;
import java.net.Socket;

/**
 * Created by sun on 2021/12/20.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class SocketClient {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) throws IOException {
		Socket socket = new Socket("localhost", 8080);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		// 从控制台输入中读取行
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		// 循环，不停的发送读取到的数据给服务端
		while (true) {
			String line = reader.readLine();
			// 约定字符q表示退出和关闭连接
			if (!line.equals("q")) {
				System.out.println("Sending : " + line);
				writer.write(line);
				// 写入换行符，约定换行符表示数据发送完成
				writer.write("\r");
				writer.flush();
			} else {
				System.out.println("Break connection.");
				break;
			}
		}
		reader.close();
		writer.close();
		socket.close();
	}
}