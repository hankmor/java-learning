package com.belonk.image;

import net.coobird.thumbnailator.Thumbnails;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.Arrays;

/**
 * Created by sun on 2017/11/1.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class ImgTest {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================
    public static void main(String[] args) throws IOException {
        read();
//        read1();
//        thumbnail();
//        convertByBase64();
    }

    public static void thumbnail() throws FileNotFoundException {
        int width = 800;
        int height = 600;
        String path = "C:\\Users\\sunfu\\Desktop\\1.jpg";
        String path1 = "C:\\Users\\sunfu\\Desktop\\1-2.jpg";
        try {
            Thumbnails.of(path)
                    .size(width, height)
                    .toFile(path1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read() throws FileNotFoundException {
        String[] ss = ImageIO.getReaderFormatNames();
        System.out.println(Arrays.asList(ss));

        String path = "C:\\Users\\sunfu\\Desktop\\1.jpg";
//        InputStream in = new FileInputStream(new File(ImgTest.class.getResource("1.jpg").getFile()));
        FileInputStream in = new FileInputStream(path);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(in);
            System.out.println(bi);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read1() throws FileNotFoundException {
        String[] ss = ImageIO.getReaderFormatNames();
        System.out.println(Arrays.asList(ss));

        String path = "C:\\Users\\sunfu\\Desktop\\1.jpg";
        Image image = Toolkit.getDefaultToolkit().getImage(path);
        System.out.println(image.getHeight(new ImageObserver() {
            @Override
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                System.out.println(image);
                System.out.println(x);
                System.out.println(y);
                System.out.println(width);
                System.out.println(height);
                return false;
            }
        }));
    }

    public static void convertByBase64() throws IOException {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream("C:\\Users\\sunfu\\Desktop\\1.jpg");
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        String base64String = encoder.encode(data);
        System.out.println(base64String);
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64String);

        String fileName = "C:\\Users\\sunfu\\Desktop\\1-1.jpg";

        FileOutputStream out = new FileOutputStream(fileName);
        out.write(buffer);
        out.close();
    }
}
