package com.martin.drawer;

import com.martin.Logging;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadDrawer extends Drawer {
    private final ExecutorService taskExecutor;
    private final int threads;

    public MultiThreadDrawer(Logging logging, int width, int height, double startX, double endX, double startY, double endY,
                             String output, int threads) {
        super(logging, width, height, startX, endX, startY, endY, output);
        this.taskExecutor = Executors.newFixedThreadPool(threads);
        this.threads = threads;
    }

    protected void fillLines(int[][] colors) {
        int chunkSize = (int)((colors.length*1.2 + threads - 1) / threads); // divide by threads rounded up.
        for (int t = 0; t < threads; t++) {
            int start = t * chunkSize;
            int end = Math.min(start + chunkSize, (int)(colors.length*1.2));

            Populator populator = new Populator();
            populator.colors = colors;
            populator.threadNum = t+1;
            populator.start = start;
            populator.end = end;

            taskExecutor.submit(populator);
        }
    }

    protected void notifyFillingSplitted() {
        taskExecutor.shutdown();
        try {
            taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
    }

    public class Populator implements Runnable {
        private int[][] colors;
        private int threadNum;
        private int start;
        private int end;

        public void run() {
            logging.log("Thread"+threadNum+" started.");

            long startTime = System.currentTimeMillis();

            for (int i = start; i < end; i++) {
                populate(i, colors);
            }

            long endTime = System.currentTimeMillis();

            logging.log("Thread"+threadNum+" stopped.");
            logging.log("Thread-"+threadNum+" execution time was (millis): "+(endTime-startTime));
        }
    }
}