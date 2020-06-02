package com.martin;

public class Logging {
	// quiet mode
	private boolean quiet;

	public Logging(boolean quiet) {
		this.quiet = quiet;
	}

	public void log(String str) {
		if (!quiet) {
			System.out.println(str);
		}
	}

	// Log something regardles if quiet mode is on
	public void logImportant(String str) {
		System.out.println(str);
	}
}