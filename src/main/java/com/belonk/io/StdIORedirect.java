package com.belonk.io;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/12/10.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class StdIORedirect {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) throws IOException {
		// 创建标准输出流的引用，以便最后更改过后再还原
		PrintStream out = System.out;
		String srcFilePath = "src/main/java/com/belonk/io/StdIORedirect.java";
		String redirectFilePath = "src/main/java/com/belonk/io/redirect.txt";
		// 创建读取文件流
		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(srcFilePath));
		// 创建输出文件流
		PrintStream printStream = new PrintStream(new FileOutputStream(redirectFilePath));
		// 重新设置标准输入流为创建的文件读取流
		System.setIn(bufferedInputStream);
		// 重新设置标准输出流为创建的输出文件流，输入的内容会被重定向到文件中，此时的System.out被更改了
		System.setOut(printStream);
		System.setErr(printStream);

		// 更改了输入流，现在可以从输入流读取文件了
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			// 现在输出流被重定向到文件中，控制台不会打印任何东西了
			System.out.println(line);
		}
		// 关闭流
		bufferedReader.close();
		bufferedInputStream.close();
		// 关闭重新定向流，这样就可以写入文件
		printStream.close();

		// System.out被更改了，需要还原
		System.setOut(out);
		// 读取重定向输出的文件内容并打印到控制台（因为标准输出流被还原了，可以输出到控制台）
		System.out.println(BufferedInputFile.read(redirectFilePath));
		// 删除文件
		File file = new File(redirectFilePath);
		if (file.exists()) file.delete();
	}
}