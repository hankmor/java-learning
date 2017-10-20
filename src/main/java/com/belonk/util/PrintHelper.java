package main.java.com.belonk.util;

import java.util.Arrays;

/**
 * <p>Created by Dendy on 2016/1/12.
 *
 * @author Dendy
 * @version 0.1
 * @since 0.1
 */
public class PrintHelper {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    //~ Methods ========================================================================================================
    public static void println() {
        System.out.println();
    }

    public static void println(Object obj) {
        if (obj.getClass().isArray())
            System.out.println(Arrays.toString((Object[]) obj));
        else
            System.out.println(obj);
    }

    public static void print(Object obj) {
        System.out.print(obj);
    }

    public static void printErr(Object obj) {
        System.err.print(obj);
    }

    public static void printlnErr(Object obj) {
        System.err.println(obj);
    }
}
