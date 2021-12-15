package com.belonk.lang.string.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.belonk.util.Printer.println;

/**
 * Created by sun on 16-1-12.
 *
 * @author sun
 * @version 1.0
 * @since 1.0
 */
public class StartEnd {
	//~ Static fields/initializers =====================================================================================

	//~ Instance fields ================================================================================================

	public static final String POEM =
			"Very quietly I take my leave,\n" +
					"As quietly as I came here.\n" +
					"Quietly I wave good-bye,\n" +
					"To the rosy clouds in the western sky.\n" +
					"End";

	//~ Methods ========================================================================================================

	public static void main(String[] args) {
		// 匹配以大写字母开头的单词
		String regex = "\\b[A-Z]\\w*\\b";
		println(regex);
		for (String str : POEM.split("\n")) {
			println("input : " + str);
			Matcher matcher = Pattern.compile(regex).matcher(str);
			while (matcher.find()) {
				println("find() " + matcher.group() + " start = " + matcher.start() + " end = " + matcher.end());
			}
			// lookingAt仅能匹配输入字符串开始部分
			if (matcher.lookingAt())
				println("lookingAt() " + matcher.group() + " start = " + matcher.start() + " end = " + matcher.end());
			// 仅能匹配整个输入字符串
			if (matcher.matches())
				println("matches() " + matcher.group() + " start = " + matcher.start() + " end = " + matcher.end());
		}
	}
}
/* Output ：
\b[A-Z]\w*\b
input : Very quietly I take my leave,
find() Very start = 0 end = 4
find() I start = 13 end = 14
lookingAt() Very start = 0 end = 4
input : As quietly as I came here.
find() As start = 0 end = 2
find() I start = 14 end = 15
lookingAt() As start = 0 end = 2
input : Quietly I wave good-bye,
find() Quietly start = 0 end = 7
find() I start = 8 end = 9
lookingAt() Quietly start = 0 end = 7
input : To the rosy clouds in the western sky.
find() To start = 0 end = 2
lookingAt() To start = 0 end = 2
input : End
find() End start = 0 end = 3
lookingAt() End start = 0 end = 3
matches() End start = 0 end = 3
*/
