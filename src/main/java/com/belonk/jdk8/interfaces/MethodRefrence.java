package main.java.com.belonk.jdk8.interfaces;

/**
 * Created by sun on 2017/6/29.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
interface It {
    boolean startsWith(String src, String dest);
}

public class MethodRefrence {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================
    public static boolean startsWith(String src, String dest) {
        return src.startsWith(dest);
    }

    public static void main(String[] args) {
        String src = "hello";
        String dest = "h";

        boolean result = MethodRefrence.startsWith(src, dest);
        System.out.println(result);

        It it = String::startsWith; // 方法引用
        result = it.startsWith(src, dest);
        System.out.println(result);

        // ::绑定到构造函数
        UserCreator userCreator = User::new;
        User u = userCreator.create("张三");
        System.out.println(u);
    }
}

class User {
    private String name;
    private String id;

    public User(String name) {
        this.name = name;
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public User() {

    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}

interface UserCreator<U extends User> {
    U create(String name);

//    U create(String id, String name);
}
