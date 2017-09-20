package com.vitaliyhtc.opengl_es_investigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Handler;
import android.support.wearable.watchface.Gles2WatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;

import com.vitaliyhtc.opengl_es_investigation.utils.SquareWithTexture;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Sample watch face using OpenGL. The watch face is rendered using
 * {@link Gles2ColoredTriangleList}s. The camera moves around in interactive mode and stops moving
 * when the watch enters ambient mode.
 */
public class OpenGLWatchFaceService extends Gles2WatchFaceService {

    private static final String TAG = "OpenGLWatchFaceService";

    /**
     * Expected frame rate in interactive mode.
     */
    private static final long FPS = 10;//60

    /**
     * Z distance from the camera to the watchface.
     */
    private static final float EYE_Z = 6.0f;

    /**
     * How long each frame is displayed at expected frame rate.
     */
    private static final long FRAME_PERIOD_MS = TimeUnit.SECONDS.toMillis(1) / FPS;

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends Gles2WatchFaceService.Engine {
        /**
         * Cycle time before the camera motion repeats.
         */
        private static final long CYCLE_PERIOD_SECONDS = 8;


        final Handler mHandler = new Handler();

        private long mLastFPSCheckTime;
        private int mFPSCount;


        private SquareWithTexture mSquareWithTexture;
        private Cube1 mCube1;


        /**
         * Projection transformation matrix. Converts from 3D to 2D.
         */
        private final float[] mProjectionMatrix = new float[16];

        /**
         * View transformation matricx to use in interactive mode. Converts from world to camera-
         * relative coordinates.
         */
        private final float[] mViewMatrix = new float[16];

        /**
         * The view transformation matrix to use in ambient mode
         */
        private final float[] mAmbientViewMatrix = new float[16];

        /**
         * Model transformation matrices. Converts from model-relative coordinates to world
         * coordinates. One matrix per degree of rotation.
         */
        private final float[] mModelMatrix = new float[16];

        /**
         * Products of {@link #mViewMatrix} and {@link #mProjectionMatrix}.
         */
        private final float[] mVpMatrix = new float[16];

        /**
         * The product of {@link #mAmbientViewMatrix} and {@link #mProjectionMatrix}
         */
        private final float[] mAmbientVpMatrix = new float[16];

        /**
         * Product of {@link #mModelMatrix}, {@link #mViewMatrix}, and
         * {@link #mProjectionMatrix}.
         */
        private final float[] mMvpMatrix = new float[16];

        private Calendar mCalendar = Calendar.getInstance();

        /**
         * Whether we've registered {@link #mTimeZoneReceiver}.
         */
        private boolean mRegisteredTimeZoneReceiver;

        private final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mCalendar.setTimeZone(TimeZone.getDefault());
                invalidate();
            }
        };

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onCreate");
            }
            super.onCreate(surfaceHolder);
            setWatchFaceStyle(new WatchFaceStyle.Builder(OpenGLWatchFaceService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setStatusBarGravity(Gravity.RIGHT | Gravity.TOP)
                    .setHotwordIndicatorGravity(Gravity.LEFT | Gravity.TOP)
                    .setShowSystemUiTime(false)
                    .build());
        }

        @Override
        public void onGlContextCreated() {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onGlContextCreated");
            }
            super.onGlContextCreated();


            mSquareWithTexture = new SquareWithTexture(getApplicationContext());
            mCube1 = new Cube1(getApplicationContext());

            Matrix.setRotateM(mModelMatrix, 0, 0, 0, 0, 1);

            Matrix.setLookAtM(mViewMatrix,
                    0, // dest index
                    0, 0, EYE_Z, // eye
                    0, 0, 0, // center
                    0, 1, 0); // up vector

            Matrix.setLookAtM(mAmbientViewMatrix,
                    0, // dest index
                    0, 0, EYE_Z, // eye
                    0, 0, 0, // center
                    0, 1, 0); // up vector
        }

        @Override
        public void onGlSurfaceCreated(int width, int height) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onGlSurfaceCreated: " + width + " x " + height);
            }
            super.onGlSurfaceCreated(width, height);

            GLES20.glClearColor(0f, 0f, 0f, 0f);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            GLES20.glEnable(GLES20.GL_CULL_FACE);
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

            // Update the projection matrix based on the new aspect ratio.
            final float aspectRatio = (float) width / height;
            Matrix.frustumM(mProjectionMatrix,
                    0 /* offset */,
                    -aspectRatio /* left */,
                    aspectRatio /* right */,
                    -1 /* bottom */,
                    1 /* top */,
                    2 /* near */,
                    11 /* far */);

            Matrix.multiplyMM(mVpMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

            Matrix.multiplyMM(mAmbientVpMatrix, 0, mProjectionMatrix, 0, mAmbientViewMatrix, 0);
        }

        /**
         * Destructively rotates the given coordinates in the XY plane about the origin by the given
         * angle.
         *
         * @param coords       flattened 3D coordinates
         * @param angleDegrees angle in degrees clockwise when viewed from negative infinity on the
         *                     Z axis
         */
        private void rotateCoords(float[] coords, int angleDegrees) {
            double angleRadians = Math.toRadians(angleDegrees);
            double cos = Math.cos(angleRadians);
            double sin = Math.sin(angleRadians);
            for (int i = 0; i < coords.length; i += 3) {
                float x = coords[i];
                float y = coords[i + 1];
                coords[i] = (float) (cos * x - sin * y);
                coords[i + 1] = (float) (sin * x + cos * y);
            }
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onAmbientModeChanged: " + inAmbientMode);
            }
            super.onAmbientModeChanged(inAmbientMode);
            invalidate();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onVisibilityChanged: " + visible);
            }
            super.onVisibilityChanged(visible);
            if (visible) {
                registerReceiver();

                // Update time zone in case it changed while we were detached.
                mCalendar.setTimeZone(TimeZone.getDefault());

                invalidate();
            } else {
                unregisterReceiver();
            }
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            OpenGLWatchFaceService.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            OpenGLWatchFaceService.this.unregisterReceiver(mTimeZoneReceiver);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "onTimeTick: ambient = " + isInAmbientMode());
            }
            invalidate();
        }

        @Override
        public void onDraw() {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "onDraw");
            }
            super.onDraw();
            final float[] vpMatrix;

            // Draw background color and select the appropriate view projection matrix. The
            // background should always be black in ambient mode. The view projection matrix used is
            // overhead in ambient. In interactive mode, it's tilted depending on the current time.
            if (isInAmbientMode()) {
                GLES20.glClearColor(0, 0, 0, 1);
                vpMatrix = mAmbientVpMatrix;
            } else {
                GLES20.glClearColor(0.5f, 0.2f, 0.2f, 1);
                vpMatrix = mVpMatrix;
            }
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

            mSquareWithTexture.draw(mViewMatrix, mProjectionMatrix);
            mCube1.draw(mViewMatrix, mProjectionMatrix);

            /*
            // Compute angle indices for the three hands.
            mCalendar.setTimeInMillis(System.currentTimeMillis());
            float seconds =
                    mCalendar.get(Calendar.SECOND) + mCalendar.get(Calendar.MILLISECOND) / 1000f;
            float minutes = mCalendar.get(Calendar.MINUTE) + seconds / 60f;
            float hours = mCalendar.get(Calendar.HOUR) + minutes / 60f;
            final int secIndex = (int) (seconds / 60f * 360f);
            final int minIndex = (int) (minutes / 60f * 360f);
            final int hoursIndex = (int) (hours / 12f * 360f);

            // Draw triangles from back to front. Don't draw the second hand in ambient mode.

            // Combine the model matrix with the projection and camera view.
            Matrix.multiplyMM(mMvpMatrix, 0, vpMatrix, 0, mModelMatrices[hoursIndex], 0);

            // Draw the triangle.
            mHourHandTriangle.draw(mMvpMatrix);

            // Combine the model matrix with the projection and camera view.
            Matrix.multiplyMM(mMvpMatrix, 0, vpMatrix, 0, mModelMatrices[minIndex], 0);

            // Draw the triangle.
            mMinuteHandTriangle.draw(mMvpMatrix);
            if (!isInAmbientMode()) {
                // Combine the model matrix with the projection and camera view.
                Matrix.multiplyMM(mMvpMatrix, 0, vpMatrix, 0, mModelMatrices[secIndex], 0);

                // Draw the triangle.
                mSecondHandTriangle.draw(mMvpMatrix);
            }

            // Draw the major and minor ticks.
            mMajorTickTriangles.draw(vpMatrix);
            mMinorTickTriangles.draw(vpMatrix);
            */

            // Draw every frame as long as we're visible and in interactive mode.

            if (isVisible() && !isInAmbientMode()) {
                invalidate();

                mFPSCount++;

                if (mLastFPSCheckTime < System.currentTimeMillis() - 1000) {
                    mLastFPSCheckTime = System.currentTimeMillis();
                    Log.e(TAG, "onDraw: mFPSCount: " + mFPSCount);
                    mFPSCount = 0;
                }



                /*
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                }, FRAME_PERIOD_MS);
                */
            }

        }
    }
}
