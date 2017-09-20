package com.vitaliyhtc.opengl_es_investigation.opengl4.model;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.vitaliyhtc.opengl_es_investigation.R;
import com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec.LightSourceSpec;
import com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec.MaterialLightSpec;
import com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec.Point3f;
import com.vitaliyhtc.opengl_es_investigation.opengl4.utils.OpenGLES20Utils;
import com.vitaliyhtc.opengl_es_investigation.opengl4.utils.ShaderUtils;
import com.vitaliyhtc.opengl_es_investigation.util.TextureUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Cube3 {
    //private static final String TAG = "Cube3";

    private static final int COORDS_PER_VERTEX = 3;
    private static final int COLORS_PER_VERTEX = 4;
    private static final int TEXCOORDS_PER_VERTEX = 2;
    private static final int NORMALS_PER_VERTEX = 3;
    private static final int SIZE_OF_FLOAT = 4;

    private Context mContext;

    private final int mProgram;
    private FloatBuffer vertexData;
    private ByteBuffer indexArray;

    private int mTextureResId = R.drawable.box;
    private MaterialLightSpec mMaterialLightSpec;
    private LightSourceSpec mLightSourceSpec;


    private int uModelViewMatrixLocation;
    private int uProjectionMatrixLocation;
    private int aPositionLocation;
    private int aTextureUnitLocation;
    private int aNormalLocation;
    private int uTextureLocation;

    private int uMaterialAmbientIntensityLocation;
    private int uMaterialDiffuseIntensityLocation;
    private int uMaterialSpecularIntensityLocation;
    private int uMaterialShininessLocation;

    private int uLightColorLocation;
    private int uLightDirectionLocation;


    private int texture;
    private int vertexStride;


    private float[] mMatrix = new float[16];


    public Cube3(Context context,
                 int textureResId,
                 MaterialLightSpec materialLightSpec,
                 LightSourceSpec lightSourceSpec
    ) {
        mContext = context;
        mTextureResId = textureResId;
        mMaterialLightSpec = materialLightSpec;
        mLightSourceSpec = lightSourceSpec;

        String vertexShaderCode = ShaderUtils.readShaderFileFromRawResource(context, R.raw.vertex_shader_3_cube3);
        String fragmentShaderCode = ShaderUtils.readShaderFileFromRawResource(context, R.raw.fragment_shader_3_cube3);
        mProgram = OpenGLES20Utils.getProgram(vertexShaderCode, fragmentShaderCode);
        GLES20.glUseProgram(mProgram);

        getLocations();
        prepareData();
    }

    private void getLocations() {
        uModelViewMatrixLocation = GLES20.glGetUniformLocation(mProgram, "u_ModelViewMatrix");
        uProjectionMatrixLocation = GLES20.glGetUniformLocation(mProgram, "u_ProjectionMatrix");
        aPositionLocation = GLES20.glGetAttribLocation(mProgram, "a_Position");
        aTextureUnitLocation = GLES20.glGetAttribLocation(mProgram, "a_TexCoord");
        aNormalLocation = GLES20.glGetAttribLocation(mProgram, "a_Normal");
        uTextureLocation = GLES20.glGetUniformLocation(mProgram, "u_Texture");

        uMaterialAmbientIntensityLocation = GLES20.glGetUniformLocation(mProgram, "u_Material.AmbientIntensity");
        uMaterialDiffuseIntensityLocation = GLES20.glGetUniformLocation(mProgram, "u_Material.DiffuseIntensity");
        uMaterialSpecularIntensityLocation = GLES20.glGetUniformLocation(mProgram, "u_Material.SpecularIntensity");
        uMaterialShininessLocation = GLES20.glGetUniformLocation(mProgram, "u_Material.Shininess");

        uLightColorLocation = GLES20.glGetUniformLocation(mProgram, "u_Light.Color");
        uLightDirectionLocation = GLES20.glGetUniformLocation(mProgram, "u_Light.Direction");
    }

    private void prepareData() {
        // coords, colors, texture coords, normal vector
        float vertices[] = {
                // Front
                1, -1, 1,      1, 0, 0, 1,    2, 0,    0, 0, 1, // 0
                1, 1, 1,       0, 1, 0, 1,    2, 2,    0, 0, 1, // 1
                -1, 1, 1,      0, 0, 1, 1,    0, 2,    0, 0, 1, // 2
                -1, -1, 1,     0, 0, 0, 1,    0, 0,    0, 0, 1, // 3

                // Back
                -1, -1, -1,    0, 0, 1, 1,    1, 0,    0, 0, -1, // 4
                -1, 1, -1,     0, 1, 0, 1,    1, 1,    0, 0, -1, // 5
                1, 1, -1,      1, 0, 0, 1,    0, 1,    0, 0, -1, // 6
                1, -1, -1,     0, 0, 0, 1,    0, 0,    0, 0, -1, // 7

                // Left
                -1, -1, 1,     1, 0, 0, 1,    1, 0,    -1, 0, 0, // 8
                -1, 1, 1,      0, 1, 0, 1,    1, 1,    -1, 0, 0, // 9
                -1, 1, -1,     0, 0, 1, 1,    0, 1,    -1, 0, 0, // 10
                -1, -1, -1,    0, 0, 0, 1,    0, 0,    -1, 0, 0, // 11

                // Right
                1, -1, -1,     1, 0, 0, 1,    1, 0,    1, 0, 0, // 12
                1, 1, -1,      0, 1, 0, 1,    1, 1,    1, 0, 0, // 13
                1, 1, 1,       0, 0, 1, 1,    0, 1,    1, 0, 0, // 14
                1, -1, 1,      0, 0, 0, 1,    0, 0,    1, 0, 0, // 15

                // Top
                1, 1, 1,       1, 0, 0, 1,    1, 0,    0, 1, 0, // 16
                1, 1, -1,      0, 1, 0, 1,    1, 1,    0, 1, 0, // 17
                -1, 1, -1,     0, 0, 1, 1,    0, 1,    0, 1, 0, // 18
                -1, 1, 1,      0, 0, 0, 1,    0, 0,    0, 1, 0, // 19

                // Bottom
                1, -1, -1,     1, 0, 0, 1,    1, 0,    0, -1, 0, // 20
                1, -1, 1,      0, 1, 0, 1,    1, 1,    0, -1, 0, // 21
                -1, -1, 1,     0, 0, 1, 1,    0, 1,    0, -1, 0, // 22
                -1, -1, -1,    0, 0, 0, 1,    0, 0,    0, -1, 0, // 23
        };

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);

        indexArray = ByteBuffer.allocateDirect(36)
                .put(new byte[]{
                        // Front
                        0, 1, 2,
                        2, 3, 0,

                        // Back
                        4, 5, 6,
                        6, 7, 4,

                        // Left
                        8, 9, 10,
                        10, 11, 8,

                        // Right
                        12, 13, 14,
                        14, 15, 12,

                        // Top
                        16, 17, 18,
                        18, 19, 16,

                        // Bottom
                        20, 21, 22,
                        22, 23, 20
                });
        indexArray.position(0);

        texture = TextureUtils.loadTexture(mContext, mTextureResId);

        vertexStride = (COORDS_PER_VERTEX + COLORS_PER_VERTEX + TEXCOORDS_PER_VERTEX + NORMALS_PER_VERTEX) * SIZE_OF_FLOAT;
    }

    private void bindData() {
        // texture unit, place texture in target 2D of unit 0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1i(uTextureLocation, 0);

        // Vertices coordinates
        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        // Texture coordinates
        vertexData.position(COORDS_PER_VERTEX + COLORS_PER_VERTEX);
        GLES20.glVertexAttribPointer(aTextureUnitLocation, TEXCOORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexData);
        GLES20.glEnableVertexAttribArray(aTextureUnitLocation);

        // Normal
        vertexData.position(COORDS_PER_VERTEX + COLORS_PER_VERTEX + TEXCOORDS_PER_VERTEX);
        GLES20.glVertexAttribPointer(aNormalLocation, NORMALS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexData);
        GLES20.glEnableVertexAttribArray(aNormalLocation);

        Point3f ambient = mMaterialLightSpec.getAmbientIntensity();
        Point3f diffuse = mMaterialLightSpec.getDiffuseIntensity();
        Point3f specular = mMaterialLightSpec.getSpecularIntencity();
        GLES20.glUniform3f(uMaterialAmbientIntensityLocation, ambient.x, ambient.y, ambient.z);
        GLES20.glUniform3f(uMaterialDiffuseIntensityLocation, diffuse.x, diffuse.y, diffuse.z);
        GLES20.glUniform3f(uMaterialSpecularIntensityLocation, specular.x, specular.y, specular.z);
        GLES20.glUniform1f(uMaterialShininessLocation, mMaterialLightSpec.getShininess());

        Point3f liC = mLightSourceSpec.getColor();
        Point3f liD = mLightSourceSpec.getDirecttion();
        GLES20.glUniform3f(uLightColorLocation, liC.x, liC.y, liC.z);
        GLES20.glUniform3f(uLightDirectionLocation, liD.x, liD.y, liD.z);

    }

    public void draw(float[] viewMatrix, float[] projectionMatrix, float[] modelMatrix) {
        GLES20.glUseProgram(mProgram);

        Matrix.setIdentityM(mMatrix, 0);

        Matrix.multiplyMM(mMatrix, 0, viewMatrix, 0, modelMatrix, 0);

        GLES20.glUniformMatrix4fv(uProjectionMatrixLocation, 1, false, projectionMatrix, 0);
        GLES20.glUniformMatrix4fv(uModelViewMatrixLocation, 1, false, mMatrix, 0);

        bindData();

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_BYTE, indexArray);

    }

}
