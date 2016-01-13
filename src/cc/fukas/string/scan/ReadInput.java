package cc.fukas.string.scan;

import java.util.Scanner;

import static cc.fukas.util.PrintHelper.println;

/**
 * <p>Created by sun on 2016/1/13.
 *
 * @author sun
 * @version 1.0
 * @since 2.2.3
 */
public class ReadInput {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    //~ Methods ========================================================================================================
    public static void main(String[] args) {
        Scanner scanner = new Scanner("sun\n29 black");
        println("What's you name?");
        println(scanner.nextLine());
        println("How old are you?");
        println(scanner.nextInt());
        println("What's you favorite color?");
        println(scanner.next());
    }
}
