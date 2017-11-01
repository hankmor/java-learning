package com.belonk.typeinfo.classes;

import com.belonk.util.PrintHelper;

import static com.belonk.util.PrintHelper.println;

interface Action {

}

interface Size {

}

// 玩具
class Toy {
    Toy() {
    }

    Toy(int i) {
    }
}

// 益智玩具
class FancyToy extends Toy implements Action, Size {
    FancyToy() {
        super(1);
    }
}

/**
 * <p>Created by sun on 2016/1/14.
 *
 * @author sun
 * @version 1.0
 * @since 2.2.3
 */
public class TypeInfo {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    //~ Methods ========================================================================================================

    /**
     * 打印类型信息。
     *
     * @param aClass
     */
    static void printInfo(Class aClass) {
        PrintHelper.println("Class     name : " + aClass.getName() + " is interface? [" + aClass.isInterface() + "]");
        PrintHelper.println("Simple    name : " + aClass.getSimpleName());
        PrintHelper.println("Canonical name : " + aClass.getCanonicalName());
    }

    public static void main(String[] args) {
        String className = "FancyToy";
        Class c = null;
        try {
            // 自动初始化类
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            PrintHelper.println(className + " not fount.");
        }
        printInfo(c);
        PrintHelper.println();
        for (Class aClass : c.getInterfaces()) {
            printInfo(aClass);
        }
        PrintHelper.println();
        Class superClass = c.getSuperclass();
        Object obj = null;
        try {
            obj = superClass.newInstance();
        } catch (InstantiationException e) {
            // 如果注释掉Toy的默认构造器，无法成功创建
            PrintHelper.println("Super class can not instantiate.");
            System.exit(1);
        } catch (IllegalAccessException e) {
            PrintHelper.println("Super class can not access.");
            System.exit(1);
        }
        printInfo(obj.getClass());
    }
}
/* Output :
Class     name : FancyToy is interface? [false]
Simple    name : FancyToy
Canonical name : FancyToy

Class     name : Action is interface? [true]
Simple    name : Action
Canonical name : Action
Class     name : Size is interface? [true]
Simple    name : Size
Canonical name : Size

Class     name : Toy is interface? [false]
Simple    name : Toy
Canonical name : Toy
 */