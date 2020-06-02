package com.martin;

import com.martin.drawer.Drawer;
import com.martin.drawer.MultiThreadDrawer;
import com.martin.drawer.SingleThreadDrawer;
import org.apache.commons.cli.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ParseException
    {
    	new App().run(args);
    }

    public void run(String[] args) throws ParseException {
    	long start = System.currentTimeMillis();

    	Options options = new Options();
		options.addOption("s", "size", true, "Image size");
		options.addOption("r", "rect", true,
				"Rectangle to zoom into");
    	options.addOption("q", "Quiet mode");
    	options.addOption("t", true, "Number of threads");
    	options.addOption("o", true, "Output file");

    	CommandLineParser parser = new DefaultParser();
    	CommandLine cmd = parser.parse( options, args);

    	// Default number of threads will be 1
    	int threads = 1;

    	boolean isQuiet = cmd.hasOption('q');
    	Logging logging = new Logging(isQuiet);
    	Drawer drawer;

    	if (cmd.hasOption('t')) {
    		threads = Integer.parseInt(cmd.getOptionValue("t"));
    	}

    	double startX = -2;
		double endX = 2;
		double startY = -2;
		double endY = 2;

    	if (cmd.hasOption("r")) {
			String rect = cmd.getOptionValue("r");

			String[] parts = rect.split(":");

			startX = Double.parseDouble(parts[0]);
			endX = Double.parseDouble(parts[1]);
			startY = Double.parseDouble(parts[2]);
			endY = Double.parseDouble(parts[3]);
		}

    	int width = 640;
    	int height = 480;

    	if (cmd.hasOption("s")) {
			String size = cmd.getOptionValue("s");

			String parts[] = size.split("x");

			width = Integer.parseInt(parts[0]);
			height = Integer.parseInt(parts[1]);
		}

		String outputFile = "zad15.png";

    	if (cmd.hasOption('o')) {
    		outputFile = cmd.getOptionValue("o");

    	}

    	if (threads == 1) {
    		drawer = new SingleThreadDrawer(logging,  width, height, startX, endX, startY, endY, outputFile);
		} else {
			drawer = new MultiThreadDrawer(logging,  width, height, startX, endX, startY, endY, outputFile, threads);
		}

    	drawer.draw();
    	long end = System.currentTimeMillis();

    	logging.logImportant("Total execution time for current run (millis): "+(end-start));
    }
}