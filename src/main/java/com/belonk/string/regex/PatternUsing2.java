package main.java.com.belonk.string.regex;

import main.java.com.belonk.util.PrintHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static main.java.com.belonk.util.PrintHelper.println;

/**
 * Created by sun on 16-1-12.
 *
 * @author sun
 * @version 1.0
 * @since 1.0
 */
public class PatternUsing2 {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    static String words = "Arline ate eight apples and one orange while Anita hadn't any";

    //~ Methods ========================================================================================================

    public static void main(String[] args) {
        // 匹配一元音字母或空格+原因字母开头，并且以原因字母结束的单词
        String regex = "(?i)((^[aeiou])|(\\s+[aeiou]))\\w+?[aeiou]\\b";
        Matcher matcher = Pattern.compile(regex).matcher(words);
        while (matcher.find()) {
            PrintHelper.println(matcher.group());
        }
    }
}
