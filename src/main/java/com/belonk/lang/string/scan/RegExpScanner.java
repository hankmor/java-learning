package com.belonk.lang.string.scan;

import com.belonk.util.Printer;

import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Created by sun on 2016/1/14.
 *
 * @author sun
 * @version 1.0
 * @since 2.2.3
 */
public class RegExpScanner {
	//~ Static fields/initializers =====================================================================================

	//~ Instance fields ================================================================================================
	public static final String POEM = "Very quietly I take my leave\n" +
			"As quietly as I came here\n" +
			"Quietly I wave good-bye\n" +
			"To the rosy clouds in the western sky";

	//~ Methods ========================================================================================================
	public static void main(String[] args) {
		Scanner scanner = new Scanner(POEM);
		String regex = "(?i)\\b\\w*[aei]\\w*\\b";
		Matcher matcher = Pattern.compile(regex).matcher(POEM);
		while (matcher.find())
			Printer.print(matcher.group() + " ");
		Printer.println();
		// 仅仅针对下一个输入匹配，如果不匹配返回false，此处匹配到my时失败
		while (scanner.hasNext(regex)) {
			scanner.next(regex);
			MatchResult mr = scanner.match();
			Printer.print(mr.group() + " ");
		}
	}
}
/* Output :
Very quietly I take leave As quietly as I came here Quietly I wave bye the in the western
Very quietly I take
 */
