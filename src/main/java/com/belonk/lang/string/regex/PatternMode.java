package com.belonk.lang.string.regex;

import com.belonk.util.Printer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Created by sun on 2016/1/13.
 *
 * @author sun
 * @version 1.0
 * @since 2.2.3
 */
public class PatternMode {
	//~ Static fields/initializers =====================================================================================

	//~ Instance fields ================================================================================================

	//~ Methods ========================================================================================================
	public static void main(String[] args) {
		String s = "Very quietly I take my leave,\n" +
				"very quietly as I came here.\n" +
				"Quietly I wave good-bye,\n" +
				"To the rosy clouds in the western sky.";
		// 两个模式，多行和忽略大小写，结果一样
		//= Pattern pattern = Pattern.compile("(?i)(?m)^very");
		Pattern pattern = Pattern.compile("^very", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			Printer.println(matcher.group());
		}
	}
}
/* Output:
Very
very
 */