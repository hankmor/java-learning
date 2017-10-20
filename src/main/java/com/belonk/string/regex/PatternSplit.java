package main.java.com.belonk.string.regex;

import main.java.com.belonk.util.PrintHelper;

import java.util.regex.Pattern;

import static main.java.com.belonk.util.PrintHelper.println;

/**
 * <p>Created by sun on 2016/1/13.
 *
 * @author sun
 * @version 1.0
 * @since 2.2.3
 */
public class PatternSplit {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    //~ Methods ========================================================================================================
    public static void main(String[] args) {
        String s = "Very quietly I take my leave.";
        Pattern pattern = Pattern.compile("\\s");
        PrintHelper.println(pattern.split(s));
        // 第二个参数为limit，标示仅分割前指定个数
        PrintHelper.println(pattern.split(s, 3));
    }
}
/* Output :
[Very, quietly, I, take, my, leave.]
[Very, quietly, I take my leave.]
 */