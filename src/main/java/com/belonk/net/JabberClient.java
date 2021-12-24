package com.belonk.net;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by sun on 2021/12/24.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class JabberClient {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) throws IOException {
		// Passing null to getByName() produces the special "Local Loopback" IP address, for testing on one machine w/o a
		// network:
		InetAddress addr = InetAddress.getByName(null);
		// Alternatively, you can use the address or name:
		// InetAddress addr = InetAddress.getByName("127.0.0.1");
		// InetAddress addr = InetAddress.getByName("localhost");
		System.out.println("addr = " + addr);
		Socket socket = new Socket(addr, JabberServer.PORT);
		// Guard everything in a try-finally to make sure that the socket is closed:
		try {
			System.out.println("socket = " + socket);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// Output is automatically flushed by PrintWriter:
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			for (int i = 0; i < 10; i++) {
				out.println("howdy " + i);
				String str = in.readLine();
				System.out.println(str);
			}
			out.println("END");
		} finally {
			System.out.println("closing...");
			socket.close();
		}

		/*
		addr = localhost/127.0.0.1
		// 客户端用本地的端口48981与服务器127.0.0.1的8080端口建立了连接
		socket = Socket[addr=localhost/127.0.0.1,port=8080,localport=49891]
		howdy 0
		howdy 1
		howdy 2
		howdy 3
		howdy 4
		howdy 5
		howdy 6
		howdy 7
		howdy 8
		howdy 9
		closing...
		*/
	}
}