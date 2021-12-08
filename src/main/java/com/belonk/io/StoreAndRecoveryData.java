package com.belonk.io;

import java.io.*;

/**
 * Created by sun on 2021/12/8.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class StoreAndRecoveryData {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 使用DataOutputStream来写入流数据，并用 DataInputStream恢复写入的数据
	 */

	public static void main(String[] args) throws IOException {
		String outFile = "src/main/java/com/belonk/io/data.dt";

		// 写不同类型的数据
		DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
		dataOutputStream.writeDouble(3.1415926d);
		// 写入UTF字符串
		dataOutputStream.writeUTF("这是圆周率pi");
		dataOutputStream.writeInt(18);
		dataOutputStream.writeUTF("小花的年纪");
		dataOutputStream.close();

		// 恢复数据
		DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(outFile)));
		System.out.println(dataInputStream.readDouble());
		// 读取UTF字符串
		System.out.println(dataInputStream.readUTF());
		System.out.println(dataInputStream.readInt());
		System.out.println(dataInputStream.readUTF());
		dataInputStream.close();
		// 删除文件
		File file = new File(outFile);
		if (file.exists())
			file.delete();

		/*
		3.1415925
		这是圆周率pi
		18
		小花的年纪
		 */
	}
}