package com.belonk.thread.result;

import java.util.concurrent.Callable;

/**
 * Created by sun on 2017/3/5.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class TaskWithResult implements Callable<String> {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================
    private int id;

    //~ Constructors ===================================================================================================
    public TaskWithResult(int id) {
        this.id = id;
    }

    //~ Methods ========================================================================================================

    @Override
    public String call() throws Exception {
        return "Result is : " + id;
    }
}
