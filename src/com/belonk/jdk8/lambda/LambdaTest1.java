package com.belonk.jdk8.lambda;

/**
 * Created by sun on 2017/6/29.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
interface Converter<I, S> {
    S convert(I from);
}

public class LambdaTest1 {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================
    public static void main(String[] args) {
        int num = 2; // 不用申明final，但是具有final的特性，即不能被修改
        Converter<Integer, String> stringConverter = from -> String.valueOf(from + num);
        String result = stringConverter.convert(5);
        System.out.println(result);
//        num = 4; // 无法编译

        stringConverter = from -> String.valueOf(from + num);
        result = stringConverter.convert(5);
        System.out.println(result);
    }
}
