package main.java.com.belonk.jdk8.interfaces.function;

/**
 * 而“函数式接口”是指仅仅只包含一个抽象方法的接口，每一个该类型的lambda表达式都会被匹配到这个抽象方法。
 * <p>
 * Created by sun on 2017/6/29.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@FunctionalInterface
interface FuncInterface {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================
    void say(String word);

    // 多于一个抽象方法，编译错误
//    void speak(String word);

    // 扩展方法非抽象，可以有多个
    default void speak(String word) {
        System.out.println(word);
    }
}

public class FunctionInterfaceTest {
    public static void main(String[] args) {
        // 将lambda表达式映射到一个单方法的接口上
        FuncInterface funcInterface = word -> System.out.println(word);
        funcInterface.say("hello");
        funcInterface.speak("world");

        funcInterface = System.out::println;
        funcInterface.say("hello");
        funcInterface.speak("world");
    }
}
