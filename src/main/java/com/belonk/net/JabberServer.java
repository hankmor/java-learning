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
public class JabberServer {
	//~ Static fields/constants/initializer

	// Choose a port outside of the range 1-1024:
	public static final int PORT = 8080;

	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) throws IOException {
		ServerSocket s = new ServerSocket(PORT);
		System.out.println("Started: " + s);
		try {
			// Blocks until a connection occurs:
			Socket socket = s.accept();
			try {
				System.out.println("Connection accepted: " + socket);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				// Output is automatically flushed by PrintWriter:
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				while (true) {
					String str = in.readLine();
					if (str.equals("END")) break;
					System.out.println("Echoing: " + str);
					out.println(str);
				}
				// Always close the two sockets...
			} finally {
				System.out.println("closing...");
				socket.close();
			}
		} finally {
			s.close();
		}
		/*
		Started: ServerSocket[addr=0.0.0.0/0.0.0.0,localport=8080]
		// 接受客户端127.0.0.1的端口48981的连接，监听本地的8080端口
		Connection accepted: Socket[addr=/127.0.0.1,port=49891,localport=8080]
		Echoing: howdy 0
		Echoing: howdy 1
		Echoing: howdy 2
		Echoing: howdy 3
		Echoing: howdy 4
		Echoing: howdy 5
		Echoing: howdy 6
		Echoing: howdy 7
		Echoing: howdy 8
		Echoing: howdy 9
		closing...
		 */
	}
}