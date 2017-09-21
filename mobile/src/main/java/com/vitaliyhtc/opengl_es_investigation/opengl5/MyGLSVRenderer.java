package com.vitaliyhtc.opengl_es_investigation.opengl5;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.vitaliyhtc.opengl_es_investigation.opengl5.model.Model5;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by vitaliyhtc on 21.09.17.
 */

@SuppressWarnings("WeakerAccess")
public class MyGLSVRenderer implements GLSurfaceView.Renderer {
    //private static final String TAG = "MyGLSVRenderer";

    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    //private final float[] mModel0Matrix = new float[16];
    //private final float[] mModel1Matrix = new float[16];
    //private final float[] mModel2Matrix = new float[16];

    private Context mContext;

    //private Square1 mSquare1;
    //private Cube3 mCube3_0;
    //private Cube3 mCube3_1;
    //private Cube3Map mCube3_2;
    //private Cube4 mCube4;
    private Model5 mModel5;

    public MyGLSVRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        init();
    }

    private void init() {
        //mCube3_0 = new Cube3(mContext, R.drawable.box0, materialLightSpecCube0, lightSourceSpec);
        //mCube3_1 = new Cube3(mContext, R.drawable.white, materialLightSpecCube1, lightSourceSpec);
        //mCube3_2 = new Cube3Map(mContext, R.drawable.container2, materialLightMapSpecCube2, lightSourceSpec);
        //mCube4 = new Cube4(mContext);
        mModel5 = new Model5(mContext);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        float ratio;
        float left = -1;
        float right = 1;
        float bottom = -1;
        float top = 1;
        float near = 2;
        float far = 24;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
            bottom *= ratio;
            top *= ratio;
        }

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);

        // Set the camera position (View matrix)
        setViewMatrix();
        setModelMatrices();
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        //setModelMatrices();

        //mCube3_0.draw(mViewMatrix, mProjectionMatrix, mModel0Matrix);
        //mCube3_1.draw(mViewMatrix, mProjectionMatrix, mModel1Matrix);
        //mCube3_2.draw(mViewMatrix, mProjectionMatrix, mModel2Matrix);
        //mSquare1.draw(mViewMatrix, mProjectionMatrix);
        //mCube4.draw(mViewMatrix, mProjectionMatrix);

        mModel5.draw(mViewMatrix, mProjectionMatrix);
    }

    private void setViewMatrix() {
        // TODO: camera position relation there and in Cube4 mCameraPos !!! refactor this!

        // точка положения камеры
        float eyeX = 1;
        float eyeY = 2; //3
        float eyeZ = 4; //4

        // точка направления камеры
        float centerX = 0;
        float centerY = 0;
        float centerZ = -3;

        // up-вектор
        float upX = 0;
        float upY = 1;
        float upZ = 0;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    private void setModelMatrices() {
        /*
        Matrix.setIdentityM(mModel0Matrix, 0);
        Matrix.setIdentityM(mModel1Matrix, 0);
        Matrix.setIdentityM(mModel2Matrix, 0);

        Matrix.translateM(mModel0Matrix, 0, -1.0f, 0f, 0f);
        Matrix.translateM(mModel1Matrix, 0, 1.0f, 0f, 0f);
        Matrix.translateM(mModel2Matrix, 0, 0f, 0.8f, -2.5f);

        Matrix.scaleM(mModel0Matrix, 0, 0.5f, 0.5f, 0.5f);
        Matrix.scaleM(mModel1Matrix, 0, 0.5f, 0.5f, 0.5f);
        //Matrix.scaleM(mModel2Matrix, 0, 0.8f, 0.8f, 0.8f);

        float angle = (float) (SystemClock.uptimeMillis() % 10000L) / 10000L * 360;
        float angle1 = (float) (SystemClock.uptimeMillis() % 47000L) / 47000L * 360;
        Matrix.rotateM(mModel0Matrix, 0, angle, 0, 1, 0);
        Matrix.rotateM(mModel0Matrix, 0, angle1, 0, 0, 1);
        Matrix.rotateM(mModel1Matrix, 0, angle, 0, 1, 0);
        Matrix.rotateM(mModel1Matrix, 0, angle1, 0, 0, 1);
        Matrix.rotateM(mModel2Matrix, 0, angle, 0, 1, 0);
        Matrix.rotateM(mModel2Matrix, 0, angle1, 0, 0, 1);
        */
    }

}
