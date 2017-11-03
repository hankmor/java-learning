package com.belonk.orderno;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sun on 2017/10/26.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class OrderNoGenerator {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================
    private final int maxValue;
    private final AtomicInteger atomicInteger;

    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================

    public OrderNoGenerator(final int minValue, final int maxValue) {
        this.atomicInteger = new AtomicInteger(minValue);
        this.maxValue = maxValue;
    }

    public Integer incrAndGet() {
        final int value = this.atomicInteger.incrementAndGet();
        if (value > maxValue) {
            return null;
        }
        return value;
    }

    public static void main(String[] args) {
        OrderNoGenerator atomicRangeInteger = new OrderNoGenerator(0, 1 << 32 - 1);
        final long currentTimeMillis = System.currentTimeMillis();
        Long id = currentTimeMillis << 32 | atomicRangeInteger.incrAndGet();
        System.out.println(id);
    }
}
