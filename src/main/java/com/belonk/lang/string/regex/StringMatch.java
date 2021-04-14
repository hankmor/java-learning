package com.belonk.lang.string.regex;

import com.belonk.util.Printer;

import static com.belonk.util.Printer.println;

/**
 * <p>Created by Dendy on 2016/1/12.
 *
 * @author Dendy
 * @version 0.1
 * @since 0.1
 */
public class StringMatch {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    //~ Methods ========================================================================================================

    public static void main(String[] args) {
        Printer.println("haha".matches("\\w*"));
        Printer.println("-123456".matches("-?\\d+"));
        Printer.println("+123456".matches("\\+?\\d+"));
        //(-|\+)?\\d+ 以-或+开头或者没有，然后跟一个或多个数字
        Printer.println("+123456".matches("(-|\\+)?\\d+"));
    }
}
/* output:
true
true
true
true
 */