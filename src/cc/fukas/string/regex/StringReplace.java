package cc.fukas.string.regex;

import static cc.fukas.util.PrintHelper.println;

/**
 * <p>Created by Dendy on 2016/1/12.
 *
 * @author Dendy
 * @version 0.1
 * @since 0.1
 */
public class StringReplace {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    //~ Methods ========================================================================================================
    public static void main(String[] args) {
        String s = StringSplit.words;
        // 用*替换第一个元音字母
        println(s.replaceFirst("[aeiou]", "*"));
        s = StringSplit.words;
        println(s.replaceAll("[aeiou]", "*"));
    }
}
/* Output:
Wh*n at Rome, do as Roman does.
Wh*n *t R*m*, d* *s R*m*n d**s.
 */