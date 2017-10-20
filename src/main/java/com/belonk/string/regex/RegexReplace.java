package main.java.com.belonk.string.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static main.java.com.belonk.util.PrintHelper.println;

/**
 * <p>Created by sun on 2016/1/13.
 *
 * @author sun
 * @version 1.0
 * @since 2.2.3
 */
public class RegexReplace {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    //~ Methods ========================================================================================================
    public static void main(String[] args) {
        String str = "very quietly i take my leave,\n" +
                "as quietly as i came here.\n" +
                "this is a test words.";
        String regex = "[aeiou]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        matcher.find();
        // matcher.group().toUpperCase() = E，替换第一个元音字母
        println("replaceFirst : " + matcher.replaceFirst(matcher.group().toUpperCase()));
        println();
        // matcher.group().toUpperCase() = E，替换所有的原因字母
        println("replaceAll : " + matcher.replaceAll(matcher.group().toUpperCase()));
        println();

        // 重新设置，重新从头开始匹配
        matcher.reset();

        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            // 将元音字母转为大写，替换原来的字符串，放入stringBuffer
            matcher.appendReplacement(stringBuffer, matcher.group().toUpperCase());
        }
        println("appendReplacement : " + stringBuffer.toString());
        println();
        // 将剩余未匹配的部分添加到stringBuffer
        matcher.appendTail(stringBuffer);
        println("appendTail : " + stringBuffer.toString());
    }
}
/* Output :
replaceFirst : vEry quietly i take my leave,
as quietly as i came here.
this is a test words.

replaceAll : vEry qEEEtly E tEkE my lEEvE,
Es qEEEtly Es E cEmE hErE.
thEs Es E tEst wErds.

appendReplacement : vEry qUIEtly I tAkE my lEAvE,
As qUIEtly As I cAmE hErE.
thIs Is A tEst wO

appendTail : vEry qUIEtly I tAkE my lEAvE,
As qUIEtly As I cAmE hErE.
thIs Is A tEst wOrds.
 */
