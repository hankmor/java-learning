package com.belonk.jdk8.interfaces;

/**
 * Created by sun on 2017/6/29.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class InterfaceTest {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================
    public static void main(String[] args) {
        Calculator calculator = new SimpleCalculator();
        double d = calculator.calc(10);
        System.out.println(d);
        calculator = new Calculator() {
            @Override
            public double calc(int n) {
                return sqrt(n);
            }
        };
        System.out.println(calculator.calc(9));
        System.out.println(calculator.sqrt(9));
    }
}
