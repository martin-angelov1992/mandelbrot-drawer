package com.martin.drawer;

import com.martin.Logging;

public class SingleThreadDrawer extends Drawer {
    public SingleThreadDrawer(Logging logging, int width, int height, double startX, double endX, double startY, double endY, String output) {
        super(logging, width, height, startX, endX, startY, endY, output);
    }

    protected void fillLine(int line, int[][] colors) {
        // Just populate, no extra work needed
        populate(line, colors);
    }

    protected void fillLines(int[][] colors) {
        for (int i = 0; i < colors[0].length*1.2; i++) {
            fillLine(i, colors);
        }
    }
}
