package com.belonk.string.regex;

import com.belonk.util.PrintHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Created by Dendy on 2016/1/12.
 *
 * @author Dendy
 * @version 0.1
 * @since 0.1
 */
public class PatternUsing {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    static String words = "Java now has regular expressions";

    //~ Methods ========================================================================================================
    public static void main(String[] args) {
        // 以Java开始
        match("^Java");
        // 非单词边界开始
        match("\\Breg.*");
        // n后接任意字符 + w后接空格 + h + a或i + s
        match("n.w\\s+h(a|i)s");
        match("s?");
        match("s*");
        match("s+");
        match("s{4}");
        match("s{1}");
        match("s{0,3}");
    }

    public static void match(String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(words);
        PrintHelper.print(regex + " : ");
        while (matcher.find()) {
            PrintHelper.print(matcher.group());
        }
        PrintHelper.println();
    }
}
/* Output :
^Java : Java
\Breg.* :
n.w\s+h(a|i)s : now has
s? : ssss
s* : ssss
s+ : ssss
s{4} :
s{1} : ssss
s{0,3} : ssss
*/
