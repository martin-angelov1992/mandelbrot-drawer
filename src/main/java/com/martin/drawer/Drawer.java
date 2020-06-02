package com.martin.drawer;

import com.martin.Logging;
import org.apache.commons.math3.complex.Complex;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Drawer {
	protected final Logging logging;

	private final int width;
	private final int height;

	private final double startX;
	private final double endX;
	private final double startY;
	private final double endY;

	private final String output;

	public Drawer(Logging logging, int width, int height, double startX, double endX, double startY, double endY,
				  String output) {
		this.logging = logging;
		this.width = width;
		this.height = height;
		this.startX = startX;
		this.endX = endX;
		this.startY = startY;
		this.endY = endY;
		this.output = output;
	}

	public void draw() {
		BufferedImage bi = new BufferedImage(width+1, height+1, BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D g2d = bi.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width+1, height+1);

		int[][] colors = new int[width][height];
		fillLines(colors);

		notifyFillingSplitted();

		for (int i = 0; i < colors.length; i++) {
			for (int j=0;j<colors[i].length;++j) {
				bi.setRGB(i+1, j+1, colors[i][j]);
			}
		}

		g2d.setColor(Color.GRAY);
		g2d.drawRect(0, 0, width - 2, height - 2);

		try {
			ImageIO.write(bi, "PNG", new File(output));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void notifyFillingSplitted() {
		// Do nothing
	}

	protected abstract void fillLines(int[][] colors);

	protected void populate(int line, int[][] colors) {
		double ty = 1.0/(height*1.2)*line;

		double yLength = endY - startY;
		double py = endY - yLength * ty;
		double xLength = endX - startX;

		int py_scr = (int) (Math.abs((py - endY)) * (height / yLength));
		double tx = 0;
		for (int j = 0; j < width*1.2; j++) {
			double px = startX + xLength * tx;
			int px_scr = (int) ((px - startX) * (width / xLength));

			int r = z_check(new Complex(px, py));
			colors[px_scr][py_scr] = getColorByMove(r);
			tx += 1.0/(width*1.2);
		}
	}

	private Complex z_iter(Complex z, Complex c) {
		return new Complex(Math.E).pow(z).subtract(c);
	}

	private int z_check(Complex c) {

		Complex z0 = new Complex(0.0, 0.0);

		Complex z_prev = z0;
		Complex z_i;

		int steps = 0;

		Double d;

		for(int i = 0; i < width; i++) {

			z_i = z_iter(z_prev, c);
			z_prev = z_i;

			d = z_prev.getReal();

			if (d.isInfinite() || d.isNaN()) {

				steps = i;
				break;

			}

		}

		return steps;

	}

	private static int getColorByMove(int r) {
		if (r == 0) { // inside ...
			return 0x00ff00;
		} else if (r <= 10) { // outside ... (rapid move)
			return 0xFFFFFF;
		} else if (r == 11) {
			return 0x0000ff;
		} else if (r == 12) {
			return  0x0000ee;
		} else if (r == 13) {
			return 0x0000dd;
		} else if (r == 14) {
			return 0x0000cc;
		} else if (r == 15) {
			return 0x0000bb;
		} else if (r == 16) {
			return 0x0000aa;
		} else if (r == 17) {
			return 0x000099;
		} else if (r == 18) {
			return 0x000088;
		} else if (r == 19) {
			return 0x000077;
		} else if (r == 20) {
			return 0x000066;
		} else if (r <= 30) {
			return 0x666600;
		} else if (r <= 40) {
			return 0x777700;
		} else if (r <= 50) {
			return 0x888800;
		} else if (r <= 100) {
			return 0x999900;
		} else if (r <= 150) {
			return 0xaaaa00;
		} else if (r <= 200) {
			return 0xbbbb00;
		} else if (r <= 300) {
			return 0xcccc00;
		} else if (350 < r && r <= 400) {
			return 0xdddd00;
		} else {
			return 0xeeee00;
		}
	}
}