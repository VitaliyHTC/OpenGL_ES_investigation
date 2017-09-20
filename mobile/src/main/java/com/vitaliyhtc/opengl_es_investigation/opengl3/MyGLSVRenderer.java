package com.vitaliyhtc.opengl_es_investigation.opengl3;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.vitaliyhtc.opengl_es_investigation.R;
import com.vitaliyhtc.opengl_es_investigation.opengl3.model.Cube3;
import com.vitaliyhtc.opengl_es_investigation.opengl3.model.Cube3Map;
import com.vitaliyhtc.opengl_es_investigation.opengl3.model.LightSourceSpec;
import com.vitaliyhtc.opengl_es_investigation.opengl3.model.MaterialLightMapSpec;
import com.vitaliyhtc.opengl_es_investigation.opengl3.model.MaterialLightSpec;
import com.vitaliyhtc.opengl_es_investigation.opengl3.model.Point3f;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by vitaliyhtc on 19.09.17.
 * trying to cleanup this.
 */

@SuppressWarnings("WeakerAccess")
public class MyGLSVRenderer implements GLSurfaceView.Renderer {
    //private static final String TAG = "MyGLSVRenderer";

    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private final float[] mModel0Matrix = new float[16];
    private final float[] mModel1Matrix = new float[16];
    private final float[] mModel2Matrix = new float[16];

    private Context mContext;

    //private Square1 mSquare1;
    private Cube3 mCube3_0;
    private Cube3 mCube3_1;
    private Cube3Map mCube3_2;

    public MyGLSVRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        init();
    }

    private void init() {

        MaterialLightSpec materialLightSpecCube0 = new MaterialLightSpec(
                new Point3f(0.19125f, 0.0735f, 0.0225f),
                new Point3f(0.7038f, 0.27048f, 0.0828f),
                new Point3f(0.256777f, 0.137622f, 0.086014f),
                12.8f
        );

        MaterialLightSpec materialLightSpecCube1 = new MaterialLightSpec(
                new Point3f(0.02f, 0.02f, 0.02f),
                new Point3f(0.01f, 0.01f, 0.01f),
                new Point3f(0.4f, 0.4f, 0.4f),
                10
        );

        MaterialLightMapSpec materialLightMapSpecCube2 = new MaterialLightMapSpec(
                R.drawable.container2,
                R.drawable.container2_specular,
                12.8f
        );

        LightSourceSpec lightSourceSpec = new LightSourceSpec(
                new Point3f(1.0f, 1.0f, 1.0f),
                new Point3f(0.0f, 1.0f, -0.5f)
        );


        mCube3_0 = new Cube3(mContext, R.drawable.box0, materialLightSpecCube0, lightSourceSpec);
        mCube3_1 = new Cube3(mContext, R.drawable.white, materialLightSpecCube1, lightSourceSpec);
        mCube3_2 = new Cube3Map(mContext, R.drawable.container2, materialLightMapSpecCube2, lightSourceSpec);
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

        setModelMatrices();

        mCube3_0.draw(mViewMatrix, mProjectionMatrix, mModel0Matrix);
        mCube3_1.draw(mViewMatrix, mProjectionMatrix, mModel1Matrix);
        mCube3_2.draw(mViewMatrix, mProjectionMatrix, mModel2Matrix);

        //mSquare1.draw(mViewMatrix, mProjectionMatrix);
    }

    private void setViewMatrix() {
        // точка положения камеры
        float eyeX = 0;
        float eyeY = 3;
        float eyeZ = 4;

        // точка направления камеры
        float centerX = 0;
        float centerY = 0;
        float centerZ = 0;

        // up-вектор
        float upX = 0;
        float upY = 1;
        float upZ = 0;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    private void setModelMatrices() {
        //TODO: set mModel0Matrix, mModel1Matrix
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
    }

}
