package com.belonk.io.nio;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created by sun on 2017/11/3.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class PathTest {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================

    public static void main(String[] args) throws IOException {
        pathTest();
        fileOperate();
    }

    public static void pathTest() throws IOException {
        Path path = Paths.get("src", "main", "resources", "1.jpg");
        // 赋值的文件名，带路径
        System.out.println(path);
        // 得到文件名，不带路径
        System.out.println(path.getFileName());
        // 得到目录及文件名数目
        System.out.println(path.getNameCount());
        // 得到不带路径的文件名
        System.out.println(path.getName(path.getNameCount() - 1));
        // 得到真实路径
        Path realPath = path.toRealPath(LinkOption.NOFOLLOW_LINKS);
        System.out.println(realPath);
        FileSystem fs = path.getFileSystem();
        System.out.println(fs);
//        path.register()
    }

    public static void fileOperate() throws IOException {
        Path source = Paths.get("src", "main", "resources", "640.jpg");
        System.out.println(source.getFileName());

        Path target = Paths.get("src", "main", "resources", "640-1.jpg");
        // 拷贝文件，注意拷贝参数(如果存在，替换目标文件)
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

//        Files.delete(target);
//        System.out.println("File deleted.");

        Path newDir = Paths.get("files/newdir");
        Files.createDirectories(newDir);

        Files.move(source, newDir.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
    }
}
