package com.rcdiamondgh.utils.thread;

public abstract class TempTaskExecutor {

    public static void runNewTask(final ThreadTaskGiver task) {
        new Thread(new Thread() {

            @Override
            public void run() {
                task.task();
            }
        }).start();
    }
}
