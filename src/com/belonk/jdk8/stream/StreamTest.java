package com.belonk.jdk8.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * java.util.Stream 表示能应用在一组元素上一次执行的操作序列。Stream 操作分为中间操作或者最终操作两种，
 * 最终操作返回一特定类型的计算结果，而中间操作返回Stream本身，这样你就可以将多个操作依次串起来。
 * Stream 的创建需要指定一个数据源，比如 java.util.Collection的子类，List或者Set，Map不支持。
 * Stream的操作可以串行执行或者并行执行。
 * <p>
 * Created by sun on 2017/6/30.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class StreamTest {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================
    private Collection<String> prepareData() {
        List<String> stringCollection = new ArrayList<>();
        stringCollection.add("ddd2");
        stringCollection.add("aaa2");
        stringCollection.add("bbb1");
        stringCollection.add("aaa1");
        stringCollection.add("bbb3");
        stringCollection.add("ccc");
        stringCollection.add("bbb2");
        stringCollection.add("ddd1");
        return stringCollection;
    }

    /**
     * 过滤通过一个predicate接口来过滤并只保留符合条件的元素，该操作属于中间操作，
     * 所以我们可以在过滤后的结果来应用其他Stream操作（比如forEach）。forEach需要一个函数来对过滤后的元素依次执行。
     * forEach是一个最终操作，所以我们不能在forEach之后来执行其他Stream操作。
     */
    private void filter() {
        prepareData()
                .stream()
                .filter((s) -> s.startsWith("a")) // 只有以a开头的字符串会保留，其他被过滤掉
                .forEach(System.out::println);
    }

    /**
     * 排序是一个中间操作，返回的是排序好后的Stream。如果你不指定一个自定义的Comparator则会使用默认排序。
     * 需要注意的是，排序只创建了一个排列好后的Stream，而不会影响原有的数据源，
     * 排序之后原数据stringCollection是不会被修改的：
     */
    private void sort() {
        prepareData()
                .stream()
                .sorted()
                .filter(s -> s.startsWith("a"))
                .forEach(System.out::println);
        System.out.println(prepareData());
    }

    /**
     * 中间操作map会将元素根据指定的Function接口来依次将元素转成另外的对象，下面的示例展示了将字符串转换为大写字符串。
     * 你也可以通过map来讲对象转换成其他类型，map返回的Stream类型是根据你map传递进去的函数的返回值决定的。
     */
    private void map() {
        prepareData()
                .stream()
                .map(String::toUpperCase)
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
//                .forEachOrdered(System.out::println);
    }

    /**
     * Stream提供了多种匹配操作，允许检测指定的Predicate是否匹配整个Stream。
     * 所有的匹配操作都是最终操作，并返回一个boolean类型的值。
     */
    private void match() {
        boolean anyMatchA = prepareData().stream().anyMatch(s -> s.startsWith("a"));
        System.out.println(anyMatchA);//true
        boolean allMatchA = prepareData().stream().allMatch(s -> s.startsWith("a"));
        System.out.println(allMatchA);//false
        boolean noneMatchZ = prepareData().stream().noneMatch(s -> s.startsWith("z"));
        System.out.println(noneMatchZ); //true
    }

    public static void main(String[] args) {
        StreamTest streamTest = new StreamTest();
//        streamTest.filter();
//        streamTest.sort();
//        streamTest.map();
//        streamTest.match();
    }
}
