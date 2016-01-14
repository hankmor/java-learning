package cc.fukas.typeinfo;

import static cc.fukas.util.PrintHelper.println;

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
        println("aClass.isPrimitive()    : " + aClass.isPrimitive());
        println("chars instanceof Object : " + (chars instanceof Object));
        println("chars instanceof char[] : " + (chars instanceof char[]));
    }

    static void checkChar() {
        Class aClass = char.class;
        println("aClass.isPrimitive()    : " + aClass.isPrimitive());
    }

    public static void main(String[] args) {
        char[] chars = {'a', 'b', 'c'};
        check(chars);
        println();
        checkChar();
    }
}
/* Output :
aClass.isPrimitive()    : false
chars instanceof Object : true
chars instanceof char[] : true

aClass.isPrimitive()    : true
 */