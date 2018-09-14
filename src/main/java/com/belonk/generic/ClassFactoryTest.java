package com.belonk.generic;

interface ClassFactory<T> {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    //~ Methods ========================================================================================================
    T create();
}


/**
 * <p>Created by sun on 2016/6/28.
 *
 * @author sun
 * @since 1.0
 */
public class ClassFactoryTest {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    //~ Methods ========================================================================================================
    public static void main(String[] args) {
        System.out.println(new Holder<HasColor>(new HasColor.AnimalFactory()));
        System.out.println(new Holder<Integer>(new IntegerFactory()));
    }
}

/**
 * bean创建对象。
 *
 * @param <T>
 */
class Holder<T> {
    T t;

    public <F extends ClassFactory<T>> Holder(F factory) {
        t = factory.create();
    }
}

class Animal {
    public static class AnimalFactory implements ClassFactory<HasColor> {
        @Override
        public HasColor create() {
            return new HasColor();
        }
    }
}

class IntegerFactory implements ClassFactory<Integer> {
    @Override
    public Integer create() {
        return new Integer(0);
    }
}