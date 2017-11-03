package com.belonk.nio;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * FileVisitor中的四个方法会返回一个FileVisitorResult，它代是一个枚举类，代表访问之后的行为。
 * FileVisitor定义了如下几种行为：
 * CONTINUE:代表访问之后的继续行为
 * SKIP_SIBLINGS:代表继续访问，但不访问该文件或目录的兄弟文件或目录
 * SKIP_SUBTREE:继续访问，但不访问该目录或文件的子目录
 * TERMINATE:终止访问
 * <p>
 * Created by sun on 2017/11/3.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class FindFileVisitor extends SimpleFileVisitor<Path> {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods =======================================================================================================

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exec) throws IOException {
        // 访问文件夹时调用
        System.out.println("Just visited " + dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        // 访问文件夹之前调用
        System.out.println("About to visit " + dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        // 访问文件后调用
        if (attrs.isRegularFile())
            System.out.print("Regular File:");
        System.out.println(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        // 文件不可访问时调用
        System.out.println(exc.getMessage());
        return FileVisitResult.CONTINUE;
    }

    public static void main(String[] args) throws IOException {
        Path fileDir = Paths.get("D:\\work\\roomPics\\roomPics");
        FindFileVisitor visitor = new FindFileVisitor();
        Files.walkFileTree(fileDir, visitor);
    }
}
