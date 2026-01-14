package com.rcdiamondgh.utils;

public class TaskExecutor {

    public static void runTask(final ThreadTask task) {
        final ThreadTask t = task;
        new Thread(new Runnable() {

            @Override
            public void run() {
                t.task();
            }
        }).start();
    }
}
