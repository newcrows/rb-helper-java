package com.crowsnet.rbhelper.v2;

/**
 * Created by CrowsNet on 26.11.2017.
 */
public class DebugTask implements Chain.Task {

    private static int debugTaskCount = 0;
    private int succeedAfter, myNumber;

    public DebugTask(int succeedAfter) {
        this.succeedAfter = succeedAfter;
        this.myNumber = debugTaskCount++;
    }

    @Override
    public boolean run() {
        System.err.println(myNumber + ":run() invoked");
        succeedAfter--;
        return succeedAfter < 1;
    }

    @Override
    public void onSuccess() {
        System.err.println(myNumber + ":onSuccess() invoked");
    }

    @Override
    public void onFail() {
        System.err.println(myNumber + ":onFail() invoked");
        System.exit(0);
    }
}
