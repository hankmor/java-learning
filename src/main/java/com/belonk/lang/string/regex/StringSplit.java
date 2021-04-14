package com.belonk.lang.string.regex;

import com.belonk.util.PrintHelper;

import static com.belonk.util.PrintHelper.println;

/**
 * <p>Created by Dendy on 2016/1/12.
 *
 * @author Dendy
 * @version 0.1
 * @since 0.1
 */
public class StringSplit {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    //~ Methods ========================================================================================================

    public static String words = "When at Rome, do as Roman does.";

    public static void main(String[] args) {
        // 以空格分割
        PrintHelper.println(words.split(" "));
        // 以非单词分割，这里为空格，,和.
        PrintHelper.println(words.split("\\W+"));
        // 以n后边跟一个非单词分割，此处为“n ”、“n,”、“n.”
        PrintHelper.println(words.split("n\\W+"));
        // 以一个或多个元音字母分割
        PrintHelper.println(words.split("[aeiou]+"));
    }
}
/* Output:
[When, at, Rome,, do, as, Roman, does.]
[When, at, Rome, do, as, Roman, does]
[Whe, at Rome, do as Roma, does.]
[Wh, n , t R, m, , d,  , s R, m, n d, s.]
 */