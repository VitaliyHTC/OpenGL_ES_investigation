package com.vitaliyhtc.opengl_es_investigation.opengl2;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.vitaliyhtc.opengl_es_investigation.R;
import com.vitaliyhtc.opengl_es_investigation.opengl2.utils.OpenGLES20Utils;
import com.vitaliyhtc.opengl_es_investigation.opengl2.utils.ShaderUtils;
import com.vitaliyhtc.opengl_es_investigation.util.TextureUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by vitaliyhtc on 22.08.17.
 */

public class Cube1_backup {

    private final static int POSITION_COUNT = 3;

    private Context mContext;

    private final String vertexShaderCode;

    private final String fragmentShaderCode;

    private FloatBuffer vertexData;
    private ByteBuffer indexArray;
    private final int mProgram;
    private int aPositionLocation;
    private int uTextureUnitLocation;
    private int uMatrixLocation;

    private int texture;

    //private float[] mModelMatrix = new float[16];
    private float[] mMatrix = new float[16];


    public Cube1_backup(Context context) {
        mContext = context;

        vertexShaderCode = ShaderUtils.readShaderFileFromRawResource(context, R.raw.vertex_shader_2);
        fragmentShaderCode = ShaderUtils.readShaderFileFromRawResource(context, R.raw.fragment_shader_2);
        mProgram = OpenGLES20Utils.getProgram(vertexShaderCode, fragmentShaderCode);
        GLES20.glUseProgram(mProgram);

        getLocations();
        prepareData();
        bindData();
    }

    private void getLocations() {
        aPositionLocation = GLES20.glGetAttribLocation(mProgram, "a_Position");
        uTextureUnitLocation = GLES20.glGetUniformLocation(mProgram, "u_TextureUnit");
        uMatrixLocation = GLES20.glGetUniformLocation(mProgram, "u_Matrix");
    }

    private void prepareData() {

        float[] vertices = {
                // вершины куба
                -1, 1, 1,     // верхняя левая ближняя
                1, 1, 1,     // верхняя правая ближняя
                -1, -1, 1,     // нижняя левая ближняя
                1, -1, 1,     // нижняя правая ближняя
                -1, 1, -1,     // верхняя левая дальняя
                1, 1, -1,     // верхняя правая дальняя
                -1, -1, -1,     // нижняя левая дальняя
                1, -1, -1      // нижняя правая дальняя
        };

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);

        indexArray = ByteBuffer.allocateDirect(36)
                .put(new byte[]{
                        // грани куба
                        // ближняя
                        1, 0, 3,
                        0, 2, 3,

                        // дальняя
                        4, 5, 6,
                        5, 7, 6,

                        // левая
                        0, 4, 2,
                        4, 6, 2,

                        // правая
                        5, 1, 7,
                        1, 3, 7,

                        // верхняя
                        5, 4, 1,
                        4, 0, 1,

                        // нижняя
                        6, 7, 2,
                        7, 3, 2
                });
        indexArray.position(0);

        texture = TextureUtils.loadTextureCube(
                mContext,
                new int[]{
                        R.drawable.box0, R.drawable.box1,
                        R.drawable.box2, R.drawable.box3,
                        R.drawable.box4, R.drawable.box5
                }
        );
    }

    private void bindData() {
        // координаты вершин
        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GLES20.GL_FLOAT, false, 0, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        // помещаем текстуру в target CUBE_MAP юнита 0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, texture);

        // юнит текстуры
        GLES20.glUniform1i(uTextureUnitLocation, 0);

    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgram);

        getLocations();
        //prepareData();
        bindData();
        //setModelMatrix();
        Matrix.setIdentityM(mMatrix, 0);

        float angle = (float) (SystemClock.uptimeMillis() % 10000L) / 10000L * 360;
        Matrix.rotateM(mMatrix, 0, angle, 0, 1, 0);
        Matrix.multiplyMM(mMatrix, 0, mvpMatrix, 0, mMatrix, 0);

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);
        OpenGLES20Utils.checkGlError("glUniformMatrix4fv");

        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_BYTE, indexArray);
    }
}
