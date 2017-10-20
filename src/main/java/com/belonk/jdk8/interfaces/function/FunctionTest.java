package main.java.com.belonk.jdk8.interfaces.function;

import java.util.function.Function;

/**
 * Function 接口有一个参数并且返回一个结果，并附带了一些可以和其他函数组合的默认方法（compose, andThen）。
 * <p>
 * Created by sun on 2017/6/29.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class FunctionTest {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================
    public static void main(String[] args) {
        Function<String, Integer> integerFunction = Integer::valueOf;
        Integer res = integerFunction.apply("123");
        System.out.println(res);

        Function<String, String> stringFunction = integerFunction.andThen(String::valueOf);
        String s = stringFunction.apply("109");
        System.out.println(s);
    }
}
