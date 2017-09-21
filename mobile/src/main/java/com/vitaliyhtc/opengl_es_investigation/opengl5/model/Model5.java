package com.vitaliyhtc.opengl_es_investigation.opengl5.model;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.vitaliyhtc.opengl_es_investigation.R;
import com.vitaliyhtc.opengl_es_investigation.opengl5.model.spec.Point3f;
import com.vitaliyhtc.opengl_es_investigation.opengl5.utils.OpenGLES20Utils;
import com.vitaliyhtc.opengl_es_investigation.opengl5.utils.ShaderUtils;
import com.vitaliyhtc.opengl_es_investigation.opengl5.utils.TextureUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vitaliyhtc on 17.09.21
 * See:
 * https://learnopengl.com/#!Advanced-OpenGL/Blending
 */

public class Model5 {

    private static final int COORDS_PER_VERTEX = 3;
    private static final int TEXCOORDS_PER_VERTEX = 2;
    private static final int SIZE_OF_FLOAT = 4;

    private Context mContext;

    private final int mProgram;

    private FloatBuffer mCubeVertexData;
    private FloatBuffer mPlaneVertexData;
    private FloatBuffer mTransparentVertexData;

    private int cubeTexture;
    private int floorTexture;
    private int transparentTexture;

    private List<Point3f> mWindowPositions;

    // vertex shader
    private int uModelMatrixLocation;
    private int uViewMatrixLocation;
    private int uProjectionMatrixLocation;
    private int aPositionLocation;
    private int aTextureLocation;

    // fragment shader
    private int uTexture1UnitLocation;


    private int vertexStride;

    private float[] mModelMatrix = new float[16];


    public Model5(Context context) {
        mContext = context;

        String vertexShaderCode = ShaderUtils.readShaderFileFromRawResource(context, R.raw.vertex_shader_5);
        String fragmentShaderCode = ShaderUtils.readShaderFileFromRawResource(context, R.raw.fragment_shader_5);
        mProgram = OpenGLES20Utils.getProgram(vertexShaderCode, fragmentShaderCode);
        GLES20.glUseProgram(mProgram);

        getLocations();
        prepareData();
    }

    private void getLocations() {
        // vertex shader
        uModelMatrixLocation = GLES20.glGetUniformLocation(mProgram, "u_ModelMatrix");
        uViewMatrixLocation = GLES20.glGetUniformLocation(mProgram, "u_ViewMatrix");
        uProjectionMatrixLocation = GLES20.glGetUniformLocation(mProgram, "u_ProjectionMatrix");
        aPositionLocation = GLES20.glGetAttribLocation(mProgram, "a_Position");
        aTextureLocation = GLES20.glGetAttribLocation(mProgram, "a_TexCoord");

        // fragment shader
        uTexture1UnitLocation = GLES20.glGetUniformLocation(mProgram, "texture1");
    }

    private void prepareData() {
        // set up vertex data (and buffer(s)) and configure vertex attributes
        // ------------------------------------------------------------------
        float cubeVertices[] = {
                // positions          // texture Coords
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
        };
        float planeVertices[] = {
                // positions          // texture Coords
                5.0f, -0.5f,  5.0f,  2.0f, 0.0f,
                -5.0f, -0.5f,  5.0f,  0.0f, 0.0f,
                -5.0f, -0.5f, -5.0f,  0.0f, 2.0f,

                5.0f, -0.5f,  5.0f,  2.0f, 0.0f,
                -5.0f, -0.5f, -5.0f,  0.0f, 2.0f,
                5.0f, -0.5f, -5.0f,  2.0f, 2.0f
        };
        float transparentVertices[] = {
                // positions         // texture Coords (swapped y coordinates because texture is flipped upside down)
                0.0f,  0.5f,  0.0f,  0.0f,  0.0f,
                0.0f, -0.5f,  0.0f,  0.0f,  1.0f,
                1.0f, -0.5f,  0.0f,  1.0f,  1.0f,

                0.0f,  0.5f,  0.0f,  0.0f,  0.0f,
                1.0f, -0.5f,  0.0f,  1.0f,  1.0f,
                1.0f,  0.5f,  0.0f,  1.0f,  0.0f
        };

        mCubeVertexData = ByteBuffer
                .allocateDirect(cubeVertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mCubeVertexData.put(cubeVertices);

        mPlaneVertexData = ByteBuffer
                .allocateDirect(planeVertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mPlaneVertexData.put(planeVertices);

        mTransparentVertexData = ByteBuffer
                .allocateDirect(transparentVertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mTransparentVertexData.put(transparentVertices);

        // load textures
        // -------------
        cubeTexture = TextureUtils.loadTexture(mContext, R.drawable.marble);
        floorTexture = TextureUtils.loadTexture(mContext, R.drawable.metal);
        transparentTexture = TextureUtils.loadTexture(mContext, R.drawable.window);

        // transparent window locations
        // --------------------------------
        mWindowPositions = new ArrayList<>();
        mWindowPositions.add(new Point3f(-1.5f, 0.0f, -0.48f));
        mWindowPositions.add(new Point3f( 1.5f, 0.0f, 0.51f));
        mWindowPositions.add(new Point3f(-0.3f, 0.0f, -2.3f));
        mWindowPositions.add(new Point3f( 0.5f, 0.0f, -0.6f));
        mWindowPositions.add(new Point3f( 0.0f, 0.0f, 0.7f));

        vertexStride = (COORDS_PER_VERTEX + TEXCOORDS_PER_VERTEX) * SIZE_OF_FLOAT;
    }

    private void bindData() {

    }

    public void draw(float[] viewMatrix, float[] projectionMatrix) {
        GLES20.glUseProgram(mProgram);

        GLES20.glUniformMatrix4fv(uViewMatrixLocation, 1, false, viewMatrix, 0);
        GLES20.glUniformMatrix4fv(uProjectionMatrixLocation, 1, false, projectionMatrix, 0);

        // draw cubes
        // Vertices coordinates
        mCubeVertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, mCubeVertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
        // Texture coordinates
        mCubeVertexData.position(COORDS_PER_VERTEX);
        GLES20.glVertexAttribPointer(aTextureLocation, TEXCOORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, mCubeVertexData);
        GLES20.glEnableVertexAttribArray(aTextureLocation);
        // Texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, cubeTexture);
        GLES20.glUniform1i(uTexture1UnitLocation, 0);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -1.0f, 0.0f, -1.0f);
        GLES20.glUniformMatrix4fv(uModelMatrixLocation, 1, false, mModelMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 2.0f, 0.0f, 0.0f);
        GLES20.glUniformMatrix4fv(uModelMatrixLocation, 1, false, mModelMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);

        // draw floor
        // Vertices coordinates
        mPlaneVertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, mPlaneVertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
        // Texture coordinates
        mPlaneVertexData.position(COORDS_PER_VERTEX);
        GLES20.glVertexAttribPointer(aTextureLocation, TEXCOORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, mPlaneVertexData);
        GLES20.glEnableVertexAttribArray(aTextureLocation);
        // Texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, floorTexture);
        GLES20.glUniform1i(uTexture1UnitLocation, 0);

        Matrix.setIdentityM(mModelMatrix, 0);
        //Matrix.translateM(mModelMatrix, 0, 2.0f, 0.0f, 0.0f);
        GLES20.glUniformMatrix4fv(uModelMatrixLocation, 1, false, mModelMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        // draw windows
        mTransparentVertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, mTransparentVertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
        // Texture coordinates
        mTransparentVertexData.position(COORDS_PER_VERTEX);
        GLES20.glVertexAttribPointer(aTextureLocation, TEXCOORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, mTransparentVertexData);
        GLES20.glEnableVertexAttribArray(aTextureLocation);
        // Texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, transparentTexture);
        GLES20.glUniform1i(uTexture1UnitLocation, 0);

        /**
         * TODO: write method that sorting transparent objects depending on distance between
         * object and camera position.
         * See in tutorial https://learnopengl.com/#!Advanced-OpenGL/Blending
         *
         * Don't break the order
         * To make blending work for multiple objects we have to draw the farthest object first and
         * the closest object as last. The normal non-blended objects can still be drawn as normal
         * using the depth buffer so they don't have to be sorted. We do have to make sure those are
         * drawn first before drawing the (sorted) transparent objects. When drawing a scene with
         * non-transparent and transparent objects the general outline is usually as follows:
         *
         * Draw all opaque objects first.
         * Sort all the transparent objects.
         * Draw all the transparent objects in sorted order.
         *
         */
        Point3f tp;
        for (int i = 0; i < mWindowPositions.size(); i++) {
            Matrix.setIdentityM(mModelMatrix, 0);
            tp = mWindowPositions.get(i);
            Matrix.translateM(mModelMatrix, 0, tp.x, tp.y, tp.z);
            GLES20.glUniformMatrix4fv(uModelMatrixLocation, 1, false, mModelMatrix, 0);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        }

    }

}
