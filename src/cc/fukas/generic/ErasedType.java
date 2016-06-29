package cc.fukas.generic;

import java.util.*;

class A {
}

class B {
}

class C<Q> {
}

class D<POINTX, POINTY> {
}

/**
 * <p>Created by sun on 2016/6/28.
 *
 * @author sun
 * @since 1.0
 */
public class ErasedType {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    //~ Methods ========================================================================================================
    public static void main(String[] args) {
        List<A> list = new ArrayList<A>();
        Map<A, B> map = new HashMap<A, B>();
        C<A> c = new C<A>();
        D<Long, Long> pointD = new D<Long, Long>();

        System.out.println(Arrays.toString(list.getClass().getTypeParameters()));
        System.out.println(Arrays.toString(map.getClass().getTypeParameters()));
        System.out.println(Arrays.toString(c.getClass().getTypeParameters()));
        System.out.println(Arrays.toString(pointD.getClass().getTypeParameters()));
    }
}

/* output:
[E]
[K, V]
[Q]
[POINTX, POINTY]
 */

