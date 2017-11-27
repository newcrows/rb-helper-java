package com.crowsnet.rbhelper.v2;


import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CrowsNet on 26.11.2017.
 */
public class Chain extends Thread {

    private int maxRepeat, interval;
    private List<Task> tasks;
    private final Object lock = new Object();
    private boolean infinite;

    private int currentTask;
    private boolean isSuccess;

    public Chain(int maxRepeat, int interval, boolean infinite) {
        this.maxRepeat = maxRepeat;
        this.interval = interval;
        tasks = new ArrayList<>();
        this.infinite = infinite;

        setDaemon(true);
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public void remove(Task task) {
        tasks.remove(task);
    }

    @Override
    public void run() {
        try {
            currentTask = 0;
            for (;currentTask < tasks.size() || infinite; currentTask++) {
                //long time_before = System.currentTimeMillis();
                if (currentTask >= tasks.size())
                    currentTask = 0;

                boolean isFailed = false;
                int currentRepeat = 0;
                for (; currentRepeat < maxRepeat; currentRepeat++) {
                    //the task wrapper runnable
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            Task task = tasks.get(currentTask);
                            try {
                                boolean isSuccess = task.run();
                                if (isSuccess)
                                    task.onSuccess();

                                synchronized (lock) {
                                    Chain.this.notify(isSuccess);
                                }
                            } catch (Exception e) {
                                synchronized (lock) {
                                    Chain.this.notify(false);
                                }
                            }
                        }
                    };

                    //if async, run on this (background) thread instead of foreground
                    if (tasks.get(currentTask) instanceof AsyncTask) {
                        r.run();
                    } else {
                        Platform.runLater(r);
                        synchronized (lock) {
                            lock.wait();
                        }
                    }
                    if (isSuccess) {
                        break;
                    } else if (currentRepeat == maxRepeat - 1) {
                        tasks.get(currentTask).onFail();
                        isFailed = true;
                    }

                    sleep(interval);
                }
                //System.out.println("task_time_" + (System.currentTimeMillis() - time_before) + "millis");
                if (isFailed)
                    break;
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public void notify(boolean isSuccess) {
        this.isSuccess = isSuccess;
        lock.notify();
    }

    public interface Task {
        boolean run() throws Exception;

        void onSuccess();

        void onFail();
    }

    public interface AsyncTask extends Task {
        //just another interface so it can be recognised
    }
}
