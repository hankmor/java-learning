package com.belonk.jdk8.interfaces.function;

import java.util.function.Supplier;

/**
 * Created by sun on 2017/6/29.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
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
