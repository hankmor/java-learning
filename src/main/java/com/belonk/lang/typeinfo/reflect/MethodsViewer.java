package com.belonk.lang.typeinfo.reflect;

import com.belonk.util.Printer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import static com.belonk.util.Printer.println;

/**
 * <p>Created by sun on 2016/1/19.
 *
 * @author sun
 * @version 1.0
 * @since 2.2.3
 */
public class MethodsViewer {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    private Class aClass;
    // 过滤掉类型前缀
    private Pattern pattern = Pattern.compile("\\w+\\.");

    //~ Methods ========================================================================================================

    public MethodsViewer(Class aClass) {
        this.aClass = aClass;
    }

    public void showMethods(String partName) {
        Method[] method = aClass.getMethods();
        for (Method m : method) {
            if (partName == null || "".equals(partName))
                Printer.println(pattern.matcher(m.toString()).replaceAll(""));
            else {
                if (m.toString().contains(partName))
                    Printer.println(pattern.matcher(m.toString()).replaceAll(""));
            }
        }
        Constructor[] constructors = aClass.getConstructors();
        for (Constructor constructor : constructors) {
            if (partName == null || "".equals(partName))
                Printer.println(pattern.matcher(constructor.toString()).replaceAll(""));
            else {
                if (constructor.toString().contains(partName))
                    Printer.println(pattern.matcher(constructor.toString()).replaceAll(""));
            }
        }
    }

    public static void main(String[] args) {
        MethodsViewer methodsViewer = new MethodsViewer(MethodsViewer.class);
        methodsViewer.showMethods(null);
        Printer.println();
        methodsViewer.showMethods("show");
    }
}
/* Output:
public void showMethods(String)
public static void main(String[])
public final native void wait(long) throws InterruptedException
public final void wait() throws InterruptedException
public final void wait(long,int) throws InterruptedException
public boolean equals(Object)
public String toString()
public native int hashCode()
public final native Class getClass()
public final native void notify()
public final native void notifyAll()
public MethodsViewer(Class)

public void showMethods(String)
 */
