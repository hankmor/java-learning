package com.belonk.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by sun on 2021/12/24.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class WhoAmI {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) throws UnknownHostException {
		System.out.println("==== getLocalHost: ");
		InetAddress inetAddress = InetAddress.getLocalHost();
		System.out.println(inetAddress);

		// 我电脑的名称，通过本地网络可以访问，如果主机名找不到，则抛出 UnknownHostException
		String[] hostnames = new String[]{"bruce-mac.local", "localhost", "127.0.0.1"};
		Arrays.stream(hostnames).forEach((hostname) -> {
			try {
				System.out.println("==== hostname: " + hostname);
				System.out.println("> getByName: ");
				InetAddress inetAddress1 = InetAddress.getByName(hostname);
				System.out.println(inetAddress1);
				System.out.println("> getAllByName: ");
				InetAddress[] inetAddresses = InetAddress.getAllByName(hostname);
				Arrays.stream(inetAddresses).forEach(System.out::println);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		});
		/*
		==== getLocalHost:
		bruce-mac.local/127.0.0.1
		==== hostname: bruce-mac.local
		> getByName:
		bruce-mac.local/127.0.0.1
		> getAllByName:
		bruce-mac.local/127.0.0.1
		bruce-mac.local/169.254.150.147
		bruce-mac.local/192.168.0.102
		bruce-mac.local/0:0:0:0:0:0:0:1
		bruce-mac.local/fe80:0:0:0:0:0:0:1%1
		bruce-mac.local/fe80:0:0:0:14b6:55e:9e7d:76e2%6
		bruce-mac.local/fe80:0:0:0:c1e:da46:b554:d9b%19
		==== hostname: localhost
		> getByName:
		localhost/127.0.0.1
		> getAllByName:
		localhost/127.0.0.1
		localhost/0:0:0:0:0:0:0:1
		==== hostname: 127.0.0.1
		> getByName:
		/127.0.0.1
		> getAllByName:
		/127.0.0.1
		 */
	}
}