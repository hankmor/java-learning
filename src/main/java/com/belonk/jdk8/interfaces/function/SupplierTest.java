package main.java.com.belonk.jdk8.interfaces.function;

import java.util.function.Supplier;

class User {
    private String id;

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                '}';
    }
}

/**
 * Supplier 接口返回一个任意范型的值，和Function接口不同的是该接口没有任何参数
 */
public class SupplierTest {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================
    public static void main(String[] args) {
        Supplier<User> supplier = User::new;
        System.out.println(supplier.get());
    }
}
