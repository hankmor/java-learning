package com.belonk.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Created by sun on 2021/12/11.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class OSExecute {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * Java中执行外部操作系统命令，并显示输出
	 */

	public static void main(String[] args) throws IOException {
		String cmd;

		// TODO 无法执行，找不到echo
		// System.out.println("echo测试：");
		// if (isWindows()) {
		// 	cmd = "C/:'Program Files'/Git/usr/bin/echo.exe %path%";
		// } else {
		// 	cmd = "echo $path";
		// }
		// command(cmd);

		System.out.println("javap测试：");
		cmd = "javap target/classes/com/belonk/io/OSExecute.class";
		command(cmd);
	}

	private static boolean isWindows() {
		return System.getProperty("os.name").contains("Windows");
	}

	public static void command(String command) throws IOException {
		System.out.println("command: " + command);
		try {
			// 创建进程，参数为命令及命令参与，用空格分隔，如：javap xxx.class
			// ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
			// Process process = processBuilder.start();
			// 上边的两行可以简写为下边的形式，效果相同
			Process process = Runtime.getRuntime().exec(command);
			// 读取进程的输出结果
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println("info: " + line);
			}

			// 读取进程输出的错误信息
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), isWindows() ? "gbk" : StandardCharsets.UTF_8.name()));
			String err;
			boolean hasError = false;
			while ((err = errorReader.readLine()) != null) {
				System.err.println("error: " + err);
				hasError = true;
			}

			int exitValue = process.exitValue();
			System.out.println("exitValue: " + exitValue);

			// 进程执行有错误，转为RuntimeException，由java程序自行处理
			if (hasError) {
				throw new RuntimeException();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}