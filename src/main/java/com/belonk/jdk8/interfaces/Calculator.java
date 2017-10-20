package main.java.com.belonk.jdk8.interfaces;

/**
 * Created by sun on 2017/6/29.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public interface Calculator {
    double calc(int n);

    // 默认方法，扩展方法
    default double sqrt(int n) {
        return Math.sqrt(n);
    }
}
