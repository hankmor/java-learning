package com.belonk.typeinfo.classes;

import java.util.Random;

import static com.belonk.util.PrintHelper.println;

class Class1 {
    static final int staticFinal = 47;
    static final int randomStaticFinal = ClassInitialization.RANDOM.nextInt(10);

    {
        println("Class1 code block");
    }

    static {
        println("Class1 static code block");
    }
}

class Class2 {
    static int nonStaticFinal = 147;

    {
        println("Class2 code block");
    }

    static {
        println("Class2 static code block");
    }
}

class Class3 {
    static int nonStaticFinal = 247;

    {
        println("Class3 code block");
    }

    static {
        println("Class3 static code block");
    }
}

/**
 * <p>Created by sun on 2016/1/14.
 *
 * @author sun
 * @version 1.0
 * @since 2.2.3
 */
public class ClassInitialization {
    //~ Static fields/initializers =====================================================================================
    public static final Random RANDOM = new Random(47);

    //~ Instance fields ================================================================================================

    //~ Methods ========================================================================================================
    public static void main(String[] args) throws ClassNotFoundException {
        // 类初始化经过三个步骤：
        // 1、加载 ：找class文件，创建实例
        // 2、连接 ：初始化静态字段，解析对其他类的引用
        // 3、初始化：初始化字段，执行静态代码块
        Class aClass1 = Class1.class;
        println("after creating Class1 ref");
        // 不初始化类，static final属性为编译器常量，编译时确定
        println(Class1.staticFinal);
        // 有对其他类的引用，需要初始化
        println(Class1.randomStaticFinal);
        // 非final的静态字段，非编译器常量，需要初始化
        println(Class2.nonStaticFinal);
        // 立即初始化类
        Class class3 = Class.forName("main.java com.belonk.typeinfo.classes.Class3");
        println("after creating Class3 ref");
        println(Class3.nonStaticFinal);
        new Class1();
    }
}
/* Output :
after creating Class1 ref
47
Class1 static code block
8
Class2 static code block
147
Class3 static code block
after creating Class3 ref
247
Class1 code block
 */
