package main.java.com.belonk.jdk8.optional;

import java.util.Optional;

/**
 * Optional 不是函数是接口，这是个用来防止NullPointerException异常的辅助类型，这是下一届中将要用到的重要概念，
 * 现在先简单的看看这个接口能干什么：
 * Optional 被定义为一个简单的容器，其值可能是null或者不是null。在Java 8之前一般某个函数应该返回非空对象但是
 * 偶尔却可能返回了null，而在Java 8中，不推荐你返回null而是返回Optional。
 * <p>
 * Created by sun on 2017/6/30.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class OptionalTest {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================
    public static void main(String[] args) {
        Optional<String> optional = Optional.of("bam");
        boolean present = optional.isPresent();           // true
        System.out.println(present);
        String value = optional.get();                 // "bam"
        System.out.println(value);
        String str = optional.orElse("fallback");    // "bam"
        System.out.println(str);
        optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"
        optional = optional.filter(s -> s.length() == 4);
        System.out.println(optional.isPresent()); // false
    }
}
