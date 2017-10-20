package main.java.com.belonk.jdk8.interfaces.function;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Predicate 接口只有一个参数，返回boolean类型。该接口包含多种默认方法来将Predicate组合成其他复杂的逻辑（比如：与，或，非）
 * <p>
 * Created by sun on 2017/6/29.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class PredicateTest {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================
    public static void main(String[] args) {
        Predicate<String> stringPredicate = s -> s != null && s.length() > 0;
        boolean res = stringPredicate.test("a");
        System.out.println(res);
        res = stringPredicate.test(null);
        System.out.println(res);

        Predicate<Boolean> booleanPredicate1 = Objects::nonNull;
        res = booleanPredicate1.test(Boolean.FALSE); // true
        System.out.println(res);

        Predicate<Boolean> booleanPredicate2 = Boolean::booleanValue;
        res = booleanPredicate2.test(Boolean.FALSE); // false
        System.out.println(res);

        Predicate<Boolean> booleanPredicate = booleanPredicate2.and(booleanPredicate1);
        res = booleanPredicate.test(Boolean.FALSE); // false
        System.out.println(res);

        booleanPredicate = booleanPredicate2.or(booleanPredicate1);
        res = booleanPredicate.test(Boolean.FALSE); // true
        System.out.println(res);
    }
}
