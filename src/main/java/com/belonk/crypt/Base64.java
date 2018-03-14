package com.belonk.crypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Base64是网络上最常见的用于传输8Bit字节码的编码方式之一，Base64就是一种基于64个可打印字符来表示二进制数据的方法。
 * 可查看RFC2045～RFC2049，上面有MIME的详细规范。
 * <p>
 * Base64编码是从二进制到字符的过程，可用于在HTTP环境下传递较长的标识信息。例如，在Java Persistence系统Hibernate中，
 * 就采用了Base64来将一个较长的唯一标识符（一般为128-bit的UUID）编码为一个字符串，用作HTTP表单和HTTP GET URL中的参数。
 * 在其他应用程序中，也常常需要把二进制数据编码为适合放在URL（包括隐藏表单域）中的形式。此时，采用Base64编码具有不可
 * 读性，需要解码后才能阅读。
 * <p>
 * <p>Created by Dendy on 2015/12/7.
 *
 * @author sunfuchang03@126.com
 * @version 0.1
 * @since 1.0
 */
public class Base64 {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    private static BASE64Encoder base64Encoder = new BASE64Encoder();
    private static BASE64Decoder base64Decoder = new BASE64Decoder();

    //~ Methods ========================================================================================================

    public static String encode(String encodeStr) throws UnsupportedEncodingException {
        return base64Encoder.encode(encodeStr.getBytes("utf-8"));
    }

    public static String encode(byte[] encodeBytes) {
        return base64Encoder.encode(encodeBytes);
    }

    public static byte[] decode(String decodeStr) throws IOException {
        return base64Decoder.decodeBuffer(decodeStr);
    }

    public static void main(String[] args) throws Exception {
        String s = "这里是base64测试字符串";
        String ret = encode(s);
        System.out.println("加密后：" + ret);
        byte[] d = decode(ret);
        System.out.println("解密后：" + new String(d, "utf-8"));
    }
}
