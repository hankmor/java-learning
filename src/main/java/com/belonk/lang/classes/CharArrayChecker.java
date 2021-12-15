package com.belonk.lang.classes;

import com.belonk.util.Printer;

/**
 * <p>Created by sun on 2016/1/14.
 *
 * @author sun
 * @version 1.0
 * @since 2.2.3
 */
public class CharArrayChecker {
	//~ Static fields/initializers =====================================================================================

	//~ Instance fields ================================================================================================

	//~ Methods ========================================================================================================

	static void check(char[] chars) {
		Class aClass = chars.getClass();
		Printer.println("aClass.isPrimitive()    : " + aClass.isPrimitive());
		Printer.println("chars instanceof Object : " + (chars instanceof Object));
		Printer.println("chars instanceof char[] : " + (chars instanceof char[]));
	}

	static void checkChar() {
		Class aClass = char.class;
		Printer.println("aClass.isPrimitive()    : " + aClass.isPrimitive());
	}

	public static void main(String[] args) {
		char[] chars = {'a', 'b', 'c'};
		check(chars);
		Printer.println();
		checkChar();
	}
}
/* Output :
aClass.isPrimitive()    : false
chars instanceof Object : true
chars instanceof char[] : true

aClass.isPrimitive()    : true
 */