package com.belonk.jdk8.lambda;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sun on 2017/6/29.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class LambdaTest {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================

    public void sort(List<String> ss) {
        Collections.sort(ss, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
    }

    public void sort1(List<String> ss) {
        Collections.sort(ss, (String a, String b) -> {
            return a.compareTo(b);
        });
    }

    public void sort2(List<String> ss) {
        Collections.sort(ss, (String a, String b) -> a.compareTo(b));
    }

    public void sort3(List<String> ss) {
        Collections.sort(ss, (a, b) -> a.compareTo(b));
    }

    public void sort4(List<String> ss) {
        Collections.sort(ss, String::compareTo);
    }

    public static void main(String[] args) {
        List<String> ss = Arrays.asList("dd", "cc", "ee", "aa", "bb");

        LambdaTest lambdaTest = new LambdaTest();
        lambdaTest.sort(ss);
        System.out.println(ss);

        ss = Arrays.asList("dd", "cc", "ee", "aa", "bb");
        lambdaTest.sort1(ss);
        System.out.println(ss);

        ss = Arrays.asList("dd", "cc", "ee", "aa", "bb");
        lambdaTest.sort2(ss);
        System.out.println(ss);

        ss = Arrays.asList("dd", "cc", "ee", "aa", "bb");
        lambdaTest.sort3(ss);
        System.out.println(ss);

        ss = Arrays.asList("dd", "cc", "ee", "aa", "bb");
        lambdaTest.sort4(ss);
        System.out.println(ss);
    }
}
