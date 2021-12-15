package com.belonk.io.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

class WebpImgFileVisitor extends SimpleFileVisitor<Path> {
	private int deleteFileCount;
	private int deleteDirCount;

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		return super.preVisitDirectory(dir, attrs);
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		File f = file.toFile();
		FileInputStream in = new FileInputStream(f);
		byte[] bytes = new byte[30];
		in.read(bytes);
		if ("WEBP".equals(new String(bytes, 8, 4, "utf-8"))) {
			System.out.println("WEBP格式图片：" + file);
		} else {
			System.out.println("正常图片，删除：" + file);
			in.close(); // 删除前需要关闭流
			Files.deleteIfExists(file);
			deleteFileCount++;
		}
//        if (ImageIO.read(f) == null ) { // 不能读取，说明是WEBP格式，这种方式不准确
//            System.out.println("Can not read file : " + file);
//        }
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		System.out.println("文件访问失败 : " + file);
		return super.visitFileFailed(file, exc);
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		File file = dir.toFile();
		if (file.listFiles() == null || file.listFiles().length == 0) {
			System.out.println("空文件夹，删除 : " + dir);
			Files.deleteIfExists(dir);
			deleteDirCount++;
		}
		return super.postVisitDirectory(dir, exc);
	}

	public int deleteDirCount() {
		return deleteDirCount;
	}

	public int deleteFileCount() {
		return deleteFileCount;
	}
}

/**
 * Created by sun on 2017/11/3.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class FindWebpImageUtil extends SimpleFileVisitor<Path> {
	//~ Static fields/initializers =====================================================================================


	//~ Instance fields ================================================================================================


	//~ Constructors ===================================================================================================


	//~ Methods ========================================================================================================
	public static int[] filterWebpImage(String rootPath) throws IOException {
		Path fileDir = Paths.get(rootPath);
		WebpImgFileVisitor visitor = new WebpImgFileVisitor();
		Files.walkFileTree(fileDir, visitor);
		return new int[]{visitor.deleteFileCount(), visitor.deleteDirCount()};
	}

	public static void main(String[] args) throws IOException {
		int[] res = FindWebpImageUtil.filterWebpImage("D:\\work\\roomPics\\roomPics");
//        int[] res = FindWebpImageUtil.filterWebpImage("D:\\work\\roomPics\\roomPics\\9235");
		System.out.println("删除文件：" + res[0] + "，删除目录：" + res[1]);
	}
}
