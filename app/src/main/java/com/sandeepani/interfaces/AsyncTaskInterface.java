package com.sandeepani.interfaces;

/**
 * Interface class that generates an event whenever an async task thread is finished.
 * Class that starts an async task, needs to implement this interface.
 *
 * @author saikrishna
 */
public interface AsyncTaskInterface {

    /**
     * This is a call back method that is called whenever
     * the async task thread is finished.
     */
    public void setAsyncTaskCompletionListener(String object);
}
