package com.vitaliyhtc.opengl_es_investigation.utils;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.vitaliyhtc.opengl_es_investigation.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by vitaliyhtc on 16.09.17.
 */

public class SquareWithTexture {

    private final String vertexShaderCode =
            /*
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}" +
                    */
            "attribute vec4 a_Position;" +
                    "uniform mat4 u_Matrix;" +
                    "attribute vec2 a_Texture;" +
                    "varying vec2 v_Texture;" +
                    "void main() {" +
                    "    gl_Position = u_Matrix * a_Position;" +
                    "    v_Texture = a_Texture;" +
                    "}";

    private final String fragmentShaderCode =
            /*
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}" +
                    */
            "precision mediump float;" +
                    "uniform sampler2D u_TextureUnit;" +
                    "varying vec2 v_Texture;" +
                    "void main() {" +
                    "    gl_FragColor = texture2D(u_TextureUnit, v_Texture);" +
                    "}";

    private FloatBuffer vertexData;
    private final int mProgram;

    private Context mContext;

    private int aPositionLocation;
    private int aTextureLocation;
    private int uTextureUnitLocation;
    private int uMatrixLocation;

    private int texture;


    private float[] mMatrix = new float[16];
    //private final float[] mMVPMatrix = new float[16];


    // number of coordinates per vertex in this array
    private final static int POSITION_COUNT = 3;
    private static final int TEXTURE_COUNT = 2;
    private static final int STRIDE = (POSITION_COUNT + TEXTURE_COUNT) * 4;

    static float vertices[] = {
            -4.0f, -4.0f, -2.0f,   0.0f, 0.0f,   //1 bottom left
            4.0f, -4.0f, -2.0f,   1.0f, 0.0f,   //2 bottom right
            -4.0f,  4.0f, -2.0f,   0.0f, 1.0f,   //0 top left
            4.0f,  4.0f, -2.0f,   1.0f, 1.0f }; //3 top right

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public SquareWithTexture(Context context) {
        mContext = context;

        mProgram = OpenGLES20Utils.getProgram(vertexShaderCode, fragmentShaderCode);
        GLES20.glUseProgram(mProgram);

        getLocations();
        prepareData();
        bindData();

    }

    private void prepareData() {

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);

        texture = TextureUtils.loadTexture(mContext, R.drawable.land1);
    }

    private void bindData() {
        // координаты вершин
        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GLES20.GL_FLOAT,
                false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        // координаты текстур
        vertexData.position(POSITION_COUNT);
        GLES20.glVertexAttribPointer(aTextureLocation, TEXTURE_COUNT, GLES20.GL_FLOAT,
                false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aTextureLocation);

        // помещаем текстуру в target 2D юнита 0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);

        // юнит текстуры
        GLES20.glUniform1i(uTextureUnitLocation, 0);
    }

    private void getLocations() {
        aPositionLocation = GLES20.glGetAttribLocation(mProgram, "a_Position");
        aTextureLocation = GLES20.glGetAttribLocation(mProgram, "a_Texture");
        uTextureUnitLocation = GLES20.glGetUniformLocation(mProgram, "u_TextureUnit");
        uMatrixLocation = GLES20.glGetUniformLocation(mProgram, "u_Matrix");
    }

    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        GLES20.glUseProgram(mProgram);


        bindData();

        Matrix.setIdentityM(mMatrix, 0);
        Matrix.multiplyMM(mMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);


        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
