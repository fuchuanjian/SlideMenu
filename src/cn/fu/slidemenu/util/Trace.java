package cn.fu.slidemenu.util;

import android.util.Log;

public class Trace
{
	public static final int ERROR = 6;
	public static final int WARN = 5;
	public static final int INFO = 4;
	public static final int DEBUG = 3;
	public static final int VERBOSE = 2;
	
	private static boolean IS_LOG = true;
	
	public static void i(String tag, String message) {
		trace(tag, message, INFO);
	}

	public static void w(String tag, String message) {
		trace(tag, message, WARN);
	}

	public static void e(String tag, String message) {
		trace(tag, message, ERROR);
	}

	public static void d(String tag, String message) {
		trace(tag, message, DEBUG);
	}

	public static void v(String tag, String message) {
		trace(tag, message, VERBOSE);
	}

	public static void e(String tag, String message, Throwable e) {
		if (IS_LOG) {
			Log.e(tag, message, e);
		}
	}
	
	private static void trace(String tag, String message, int level) {
		// if you want to choose print some logs, please add "&&level==" after
		// IsLog
		if (IS_LOG) {
			long sec = (System.currentTimeMillis() / 1000) % 1000;
			StringBuilder b = new StringBuilder("[")
					.append(Thread.currentThread().getName()).append("] ")
					.append("@").append(sec).append(" ").append(message);
			Log.println(level, tag, b.toString());
		}
	}
}
