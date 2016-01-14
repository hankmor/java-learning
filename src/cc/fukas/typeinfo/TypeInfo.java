package cc.fukas.typeinfo;

import static cc.fukas.util.PrintHelper.println;

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
        println("Class     name : " + aClass.getName() + " is interface? [" + aClass.isInterface() + "]");
        println("Simple    name : " + aClass.getSimpleName());
        println("Canonical name : " + aClass.getCanonicalName());
    }

    public static void main(String[] args) {
        String className = "cc.fukas.typeinfo.FancyToy";
        Class c = null;
        try {
            // 自动初始化类
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            println(className + " not fount.");
        }
        printInfo(c);
        println();
        for (Class aClass : c.getInterfaces()) {
            printInfo(aClass);
        }
        println();
        Class superClass = c.getSuperclass();
        Object obj = null;
        try {
            obj = superClass.newInstance();
        } catch (InstantiationException e) {
            // 如果注释掉Toy的默认构造器，无法成功创建
            println("Super class can not instantiate.");
            System.exit(1);
        } catch (IllegalAccessException e) {
            println("Super class can not access.");
            System.exit(1);
        }
        printInfo(obj.getClass());
    }
}
/* Output :
Class     name : cc.fukas.typeinfo.FancyToy is interface? [false]
Simple    name : FancyToy
Canonical name : cc.fukas.typeinfo.FancyToy

Class     name : cc.fukas.typeinfo.Action is interface? [true]
Simple    name : Action
Canonical name : cc.fukas.typeinfo.Action
Class     name : cc.fukas.typeinfo.Size is interface? [true]
Simple    name : Size
Canonical name : cc.fukas.typeinfo.Size

Class     name : cc.fukas.typeinfo.Toy is interface? [false]
Simple    name : Toy
Canonical name : cc.fukas.typeinfo.Toy
 */