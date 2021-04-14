package com.belonk.io.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * https://bitbucket.org/luciad/webp-imageio
 * https://developers.google.com/speed/webp/
 * <p>
 * Created by sun on 2017/11/1.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class WebpImageTest {
	//~ Static fields/initializers =====================================================================================


	//~ Instance fields ================================================================================================


	//~ Constructors ===================================================================================================


	//~ Methods =======================================================================================================


	public static void main(String[] args) throws IOException {
		String path = "C:\\Users\\Administrator\\Desktop\\640.jpg";
//        BufferedImage bi = ImageIO.read(new File(path));
//        System.out.println(bi);
		String javaLibPath = System.getProperty("(java.library.path");
		System.out.println(javaLibPath);

		File file = new File(path);

		try {
			FileInputStream in = new FileInputStream(file);
			byte[] bytes = new byte[30];
			in.read(bytes);
			if ("WEBP".equals(new String(bytes, 8, 4, "utf-8"))) {//先判断图片格式为webp
				int w = (((int) bytes[27] & 0xff) << 8) | (int) bytes[26] & 0xff;
				int h = (((int) bytes[29] & 0xff) << 8) | (int) bytes[28] & 0xff;
				System.out.print("宽:" + w + ",高:" + h);
			} else {
				System.out.print("文件不是webp格式");
			}
			in.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
