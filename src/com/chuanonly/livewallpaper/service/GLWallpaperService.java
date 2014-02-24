
package com.chuanonly.livewallpaper.service;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.chuanonly.livewallpaper.data.Constant;
import com.chuanonly.livewallpaper.util.Trace;

public abstract class GLWallpaperService extends WallpaperService {
    private static final String TAG = "fu";

    public static final boolean isCanLog = Constant.DEBUG;

    interface OpenGLEngine {
        void setEGLContextClientVersion(int version);

        void setRenderer(Renderer renderer);

        void setRenderMode(int mode);
    }

    public class GLSurfaceViewEngine extends Engine implements OpenGLEngine {
        public final static int RENDERMODE_WHEN_DIRTY = 0;

        public final static int RENDERMODE_CONTINUOUSLY = 1;

        private int mRenderMode = 1;

        private boolean mVisble = false;

        class WallpaperGLSurfaceView extends GLSurfaceView {
            private static final String TAG = "WallpaperGLSurfaceView";

            WallpaperGLSurfaceView(Context context) {
                super(context);
                if (isCanLog) {
                    Trace.d(TAG, "WallpaperGLSurfaceView(" + context + ")");
                }
            }

            @Override
            public SurfaceHolder getHolder() {
                if (isCanLog) {
                    Trace.d(TAG, "getHolder(): returning " + getSurfaceHolder());
                }

                return getSurfaceHolder();
            }

            public void onDestroy() {
                if (isCanLog) {
                    Trace.d(TAG, "onDestroy()");
                }

                super.onDetachedFromWindow();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // TODO Auto-generated method stub
                super.surfaceCreated(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                // TODO Auto-generated method stub
                super.surfaceChanged(holder, format, w, h);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // TODO Auto-generated method stub
                super.surfaceDestroyed(holder);
            }
        }

        private static final String TAG = "GLSurfaceViewEngine";

        private WallpaperGLSurfaceView glSurfaceView;

        private boolean rendererHasBeenSet;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            if (isCanLog) {
                Trace.d(TAG, "onCreate(" + surfaceHolder + ")");
            }

            super.onCreate(surfaceHolder);

            glSurfaceView = new WallpaperGLSurfaceView(GLWallpaperService.this);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (isCanLog) {
                Trace.d(TAG, "onVisibilityChanged(" + visible + ")");
            }
            mVisble = visible;
            super.onVisibilityChanged(visible);

            if (rendererHasBeenSet) {
                if (visible) {
                    glSurfaceView.onResume();
                } else {
                    glSurfaceView.onPause();
                }
            }
        }

        @Override
        public void onDestroy() {
            if (isCanLog) {
                Trace.d(TAG, "onDestroy()");
            }
            glSurfaceView.destroyDrawingCache();
            glSurfaceView.onDestroy();
            glSurfaceView = null;
            super.onDestroy();
        }

        public void setRenderer(Renderer renderer) {
            if (isCanLog) {
                Trace.d(TAG, "setRenderer(" + renderer + ")");
            }

            glSurfaceView.setRenderer(renderer);
            rendererHasBeenSet = true;
        }

        public void setEGLContextClientVersion(int version) {
            if (isCanLog) {
                Trace.d(TAG, "setEGLContextClientVersion(" + version + ")");
            }

            glSurfaceView.setEGLContextClientVersion(version);
            glSurfaceView.setEGLConfigChooser(false);
        }

        public void setRenderMode(int renderMode) {
            mRenderMode = renderMode;
            glSurfaceView.setRenderMode(renderMode);
        }

        public int getRenderMode() {
            return mRenderMode;
        }

        public void requestRender() {
            glSurfaceView.requestRender();
        }
    }
}
