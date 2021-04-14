package com.belonk.lang.typeinfo.reflect;

/**
 * Created by sun on 2017/9/5.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class MethodCast {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods =======================================================================================================
    @org.junit.Test
    public void test() {
        String str = "adfas";
        String s = String.class.cast(str);
        System.out.println(str.equals(s));
    }
}
