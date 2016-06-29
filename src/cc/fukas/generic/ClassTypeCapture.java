package cc.fukas.generic;

/**
 * <p>Created by sun on 2016/6/28.
 *
 * @author sun
 * @since 1.0
 */
public class ClassTypeCapture<T> {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================
    Class<T> kind;

    //~ Methods ========================================================================================================

    public ClassTypeCapture(Class<T> kind) {
        this.kind = kind;
    }

    public boolean capture(Object arg) {
        return kind.isInstance(arg);
    }

    public static void main(String[] args) {
        ClassTypeCapture<Person> personClassTypeCapture = new ClassTypeCapture<Person>(Person.class);
        System.out.println(personClassTypeCapture.capture(new Person()));
        System.out.println(personClassTypeCapture.capture(new Jim()));

        ClassTypeCapture<Jim> jimClassTypeCapture = new ClassTypeCapture<Jim>(Jim.class);
        System.out.println(jimClassTypeCapture.capture(new Person()));
        System.out.println(jimClassTypeCapture.capture(new Jim()));
    }
}


class Person {

}

class Jim extends Person {
}

/*
true
true
false
true
 */