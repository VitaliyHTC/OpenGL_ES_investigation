package com.vitaliyhtc.opengl_es_investigation.opengl2.broken;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.vitaliyhtc.opengl_es_investigation.opengl2.Square1;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by vitaliyhtc on 22.08.17.
 */

public class Renderer implements GLSurfaceView.Renderer {
    private static final String TAG = "MyGLSVRenderer";

    //private static final float ONE_SEC = 1000.0f; // 1 second


    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private final float[] mTransformMatrix = new float[16];

    private Context mContext;

    private Square1 mSquare1;

    //private CubeModel mCubeModel;
    //private long lastTimeMillis = 0L;

    public Renderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        mSquare1 = new Square1();


        /*
        ShaderProgram shader = new ShaderProgram(
                ShaderUtils.readShaderFileFromRawResource(mContext, R.raw.vertex_shader_2_backup),
                ShaderUtils.readShaderFileFromRawResource(mContext, R.raw.fragment_shader_2_backup)
        );
        int textureName = TextureUtils.loadTexture(mContext, R.drawable.box);
        mCubeModel = new CubeModel("CubeModel", shader);
        mCubeModel.setPosition(new Float3(0.0f, 0.0f, 0.0f));
        mCubeModel.setTexture(textureName);
        */
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);


        /*
        //for CubeModel
        Matrix4f perspective = new Matrix4f();
        perspective.loadPerspective(85.0f, (float) width / (float) height, 1.0f, -150.0f);
        if (mCubeModel != null) {
            mCubeModel.setProjection(perspective);
        }
        */
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);


        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        //mSquare1.draw(mMVPMatrix);
        mSquare1.draw(mViewMatrix, mProjectionMatrix);


        //float[] scratch = new float[16];

        /*
        long currentTimeMillis = System.currentTimeMillis();
        updateWithDelta(currentTimeMillis - lastTimeMillis);
        lastTimeMillis = currentTimeMillis;
        */
    }

    /*
    public void updateWithDelta(long dt) {

        Matrix4f camera2 = new Matrix4f();
        camera2.translate(0.0f, 0.0f, -5.0f);
        mCubeModel.setCamera(camera2);
        mCubeModel.setRotationY((float) (mCubeModel.getRotationY() + Math.PI * dt / (ONE_SEC * 0.1f)));
        mCubeModel.setRotationZ((float) (mCubeModel.getRotationZ() + Math.PI * dt / (ONE_SEC * 0.1f)));
        mCubeModel.draw(dt);
    }
    */
}
