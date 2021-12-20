package com.belonk.net;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by sun on 2021/12/20.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class SimpleSocketClient {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		try {
			System.out.println("client...");
			// 创建客户端，连接到localhost的8080端口
			Socket socket = new Socket("localhost", SimpleSocketServer.port);
			// 客户端输出流，写入数据即可以向服务端发送消息
			OutputStream outputStream = socket.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
			// 写入数据
			String content = "Hello, socket，哈哈!";
			writer.write(content);
			// 刷新，此时服务端可以正确读取到数据，否则收到的数据为null
			writer.flush();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}