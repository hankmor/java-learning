package com.belonk.jdk8.interfaces.function;

import java.util.Comparator;
import java.util.function.Function;

/**
 * Comparator 是老Java中的经典接口， Java 8在此之上添加了多种默认方法
 *
 * Created by sun on 2017/6/30.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class ComparatorTest {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================
    public static void main(String[] args) {
//        Comparator<Person> comparator = (p1, p2) -> p1.getId().compareTo(p2.getId());
        Comparator<Person> comparator = Comparator.comparing(Person::getId);
        Person p1 = new Person("a");
        Person p2 = new Person("b");
        int res = comparator.compare(p1, p2);
        System.out.println(res);
        res = comparator.reversed().compare(p1, p2);
        System.out.println(res);

    }
}
