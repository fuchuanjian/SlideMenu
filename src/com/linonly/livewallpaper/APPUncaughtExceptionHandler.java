package com.linonly.livewallpaper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class APPUncaughtExceptionHandler implements UncaughtExceptionHandler {

    private static final String TAG = "WeatherExceptionHandler";

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private Context mContext;

    private static final APPUncaughtExceptionHandler INSTANCE = new APPUncaughtExceptionHandler();

    private APPUncaughtExceptionHandler() {
    }

    public static APPUncaughtExceptionHandler getInstance() {
        return INSTANCE;
    }

    /**
     * Handle exception.
     * <p>
     * Should always call {@link #release()} to release the handler.
     * </p>
     * 
     * @param context Application context
     */
    public void handle(Context context) {
        this.mContext = context;
        try {
            UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            if (defaultHandler != this) {
                mDefaultHandler = defaultHandler;
                Thread.setDefaultUncaughtExceptionHandler(this);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Release exception.
     * <p>
     * Release the handler.
     * </p>
     */
    public void release() {
        try {
            if (mDefaultHandler != null) {
                Thread.setDefaultUncaughtExceptionHandler(mDefaultHandler);
                mDefaultHandler = null;
            }
        } catch (Exception e) {
        }
    }

    public void uncaughtException(Thread thread, final Throwable throwable) {
        try {
//            Trace.e(TAG, "Uncaught exception! ");
//            Trace.e("fu", "exception " + throwable);

            // ignore
            if (throwable.toString().contains("eglSwapBuffers"))
                return;
//            saveCrashInfo2File(throwable);
            if (false) {
                finishSelf();
            } else {
                if (mDefaultHandler != null) {
                    mDefaultHandler.uncaughtException(thread, throwable);
                }
            }
        } catch (Throwable e) {
            if (mDefaultHandler != null) {
                mDefaultHandler.uncaughtException(thread, throwable);
            }
        }
    }

    /**
     * Finish self.
     */
    private void finishSelf() {
        // Kill self
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
    private String saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			String fileName = "Crash.log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String path =Environment.getExternalStorageDirectory().getAbsolutePath();
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path+"/" + fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}
}
