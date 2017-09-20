package com.vitaliyhtc.opengl_es_investigation.opengl4.model;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.vitaliyhtc.opengl_es_investigation.R;
import com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec.DirLightSpec;
import com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec.MaterialLightMapSpec;
import com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec.Point3f;
import com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec.PointLightSpec;
import com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec.SpotLightSpec;
import com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec.locationholders.DirLightLocationHolder;
import com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec.locationholders.PointLightLocationHolder;
import com.vitaliyhtc.opengl_es_investigation.opengl4.model.spec.locationholders.SpotLightLocationHolder;
import com.vitaliyhtc.opengl_es_investigation.opengl4.utils.OpenGLES20Utils;
import com.vitaliyhtc.opengl_es_investigation.opengl4.utils.ShaderUtils;
import com.vitaliyhtc.opengl_es_investigation.util.TextureUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * See: https://learnopengl.com/#!Lighting/Multiple-lights
 */
public class Cube4 {

    private static final int COORDS_PER_VERTEX = 3;
    private static final int COLORS_PER_VERTEX = 4;
    private static final int TEXCOORDS_PER_VERTEX = 2;
    private static final int NORMALS_PER_VERTEX = 3;
    private static final int SIZE_OF_FLOAT = 4;

    private Context mContext;

    private final int mProgram;
    private FloatBuffer vertexData;
    private ByteBuffer indexArray;

    //private int mTextureResId = R.drawable.box;
    private MaterialLightMapSpec mMaterialLightSpec;

    private DirLightSpec mDirLightSpec;
    private PointLightSpec mPointLightSpec0;
    private PointLightSpec mPointLightSpec1;
    private PointLightSpec mPointLightSpec2;
    private PointLightSpec mPointLightSpec3;
    private SpotLightSpec mSpotLightSpec;


    // vertex shader
    private int uModelViewMatrixLocation;
    private int uProjectionMatrixLocation;
    private int aPositionLocation;
    private int aTextureUnitLocation;
    private int aNormalLocation;

    // fragment shader
    private int uMaterialDiffuseMapLocation;
    private int uMaterialSpecularMapLocation;
    private int uMaterialShininessLocation;
    private DirLightLocationHolder uDirLightLocationHolder;
    private Map<Integer, PointLightLocationHolder> uPointLightLocationHoldersMap;
    private SpotLightLocationHolder uSpotLightLocationHolder;
    private int uViewPosLocation;

    private Point3f mCameraPos;


    //private int texture;
    private int textureMap;
    private int textureMapSpecular;
    private int vertexStride;


    private float[] mMatrix = new float[16];


    public Cube4(Context context) {
        mContext = context;

        uPointLightLocationHoldersMap = new HashMap<>();
        mCameraPos = new Point3f(0.0f, 0.0f, 3.0f);

        //mTextureResId = R.drawable.container2;

        initSpecs();

        String vertexShaderCode = ShaderUtils.readShaderFileFromRawResource(context, R.raw.vertex_shader_4_ml);
        String fragmentShaderCode = ShaderUtils.readShaderFileFromRawResource(context, R.raw.fragment_shader_4_ml);
        mProgram = OpenGLES20Utils.getProgram(vertexShaderCode, fragmentShaderCode);
        GLES20.glUseProgram(mProgram);

        getLocations();
        prepareData();
    }

    private void initSpecs() {
        mMaterialLightSpec = new MaterialLightMapSpec(
                R.drawable.container2,
                R.drawable.container2_specular,
                12.8f
        );
        mDirLightSpec = new DirLightSpec(
                new Point3f(-0.2f, -1.0f, -0.3f),
                new Point3f(0.05f, 0.05f, 0.05f),
                new Point3f(0.4f, 0.4f, 0.4f),
                new Point3f(0.5f, 0.5f, 0.5f)
        );
        /*
        glm::vec3 pointLightPositions[] = {
            glm::vec3( 0.7f,  0.2f,  2.0f),
            glm::vec3( 2.3f, -3.3f, -4.0f),
            glm::vec3(-4.0f,  2.0f, -12.0f),
            glm::vec3( 0.0f,  0.0f, -3.0f)
        };
         */
        mPointLightSpec0 = new PointLightSpec(
                new Point3f(0.7f, 0.2f, 2.0f), // pointLights[0].position
                1.0f, 0.09f, 0.032f,
                new Point3f(0.05f, 0.05f, 0.05f),
                new Point3f(0.8f, 0.8f, 0.8f),
                new Point3f(1.0f, 1.0f, 1.0f)
        );
        mPointLightSpec1 = new PointLightSpec(
                new Point3f(2.3f, -3.3f, -4.0f), // pointLights[1].position
                1.0f, 0.09f, 0.032f,
                new Point3f(0.05f, 0.05f, 0.05f),
                new Point3f(0.8f, 0.8f, 0.8f),
                new Point3f(1.0f, 1.0f, 1.0f)
        );
        mPointLightSpec2 = new PointLightSpec(
                new Point3f(-4.0f, 2.0f, -12.0f), // pointLights[2].position
                1.0f, 0.09f, 0.032f,
                new Point3f(0.05f, 0.05f, 0.05f),
                new Point3f(0.8f, 0.8f, 0.8f),
                new Point3f(1.0f, 1.0f, 1.0f)
        );
        mPointLightSpec3 = new PointLightSpec(
                new Point3f(0.0f, 0.0f, -3.0f), // pointLights[3].position
                1.0f, 0.09f, 0.032f,
                new Point3f(0.05f, 0.05f, 0.05f),
                new Point3f(0.8f, 0.8f, 0.8f),
                new Point3f(1.0f, 1.0f, 1.0f)
        );
        mSpotLightSpec = new SpotLightSpec(
                mCameraPos, // camera.Position
                new Point3f(0.0f, 0.0f, 0.0f), // camera.Front
                (float) Math.cos(Math.toRadians(12.5f)),
                (float) Math.cos(Math.toRadians(15.0f)),
                1.0f, 0.09f, 0.032f,
                new Point3f(0.0f, 0.0f, 0.0f),
                new Point3f(1.0f, 1.0f, 1.0f),
                new Point3f(1.0f, 1.0f, 1.0f)
        );
    }

    private void getLocations() {
        // vertex shader
        uModelViewMatrixLocation = GLES20.glGetUniformLocation(mProgram, "u_ModelViewMatrix");
        uProjectionMatrixLocation = GLES20.glGetUniformLocation(mProgram, "u_ProjectionMatrix");
        aPositionLocation = GLES20.glGetAttribLocation(mProgram, "a_Position");
        aTextureUnitLocation = GLES20.glGetAttribLocation(mProgram, "a_TexCoord");
        aNormalLocation = GLES20.glGetAttribLocation(mProgram, "a_Normal");

        // fragment shader
        uMaterialDiffuseMapLocation = GLES20.glGetUniformLocation(mProgram, "u_Material.DiffuseMap");
        uMaterialSpecularMapLocation = GLES20.glGetUniformLocation(mProgram, "u_Material.SpecularMap");
        uMaterialShininessLocation = GLES20.glGetUniformLocation(mProgram, "u_Material.Shininess");
        uDirLightLocationHolder = new DirLightLocationHolder(
                GLES20.glGetUniformLocation(mProgram, "dirLight.direction"),
                GLES20.glGetUniformLocation(mProgram, "dirLight.ambient"),
                GLES20.glGetUniformLocation(mProgram, "dirLight.diffuse"),
                GLES20.glGetUniformLocation(mProgram, "dirLight.specular")
        );

        uPointLightLocationHoldersMap.put(0,
                new PointLightLocationHolder(
                        GLES20.glGetUniformLocation(mProgram, "pointLights[0].position"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[0].constant"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[0].linear"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[0].quadratic"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[0].ambient"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[0].diffuse"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[0].specular")
                )
        );

        uPointLightLocationHoldersMap.put(1,
                new PointLightLocationHolder(
                        GLES20.glGetUniformLocation(mProgram, "pointLights[1].position"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[1].constant"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[1].linear"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[1].quadratic"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[1].ambient"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[1].diffuse"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[1].specular")
                )
        );

        uPointLightLocationHoldersMap.put(2,
                new PointLightLocationHolder(
                        GLES20.glGetUniformLocation(mProgram, "pointLights[2].position"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[2].constant"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[2].linear"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[2].quadratic"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[2].ambient"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[2].diffuse"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[2].specular")
                )
        );

        uPointLightLocationHoldersMap.put(3,
                new PointLightLocationHolder(
                        GLES20.glGetUniformLocation(mProgram, "pointLights[3].position"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[3].constant"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[3].linear"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[3].quadratic"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[3].ambient"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[3].diffuse"),
                        GLES20.glGetUniformLocation(mProgram, "pointLights[3].specular")
                )
        );

        uSpotLightLocationHolder = new SpotLightLocationHolder(
                GLES20.glGetUniformLocation(mProgram, "spotLight.position"),
                GLES20.glGetUniformLocation(mProgram, "spotLight.direction"),
                GLES20.glGetUniformLocation(mProgram, "spotLight.cutOff"),
                GLES20.glGetUniformLocation(mProgram, "spotLight.outerCutOff"),
                GLES20.glGetUniformLocation(mProgram, "spotLight.constant"),
                GLES20.glGetUniformLocation(mProgram, "spotLight.linear"),
                GLES20.glGetUniformLocation(mProgram, "spotLight.quadratic"),
                GLES20.glGetUniformLocation(mProgram, "spotLight.ambient"),
                GLES20.glGetUniformLocation(mProgram, "spotLight.diffuse"),
                GLES20.glGetUniformLocation(mProgram, "spotLight.specular")
        );

        uViewPosLocation = GLES20.glGetUniformLocation(mProgram, "viewPos");
    }

    private void prepareData() {
        // coords, colors, texture coords, normal vector
        float vertices[] = {
                // Front
                1, -1, 1, 1, 0, 0, 1, 2, 0, 0, 0, 1, // 0
                1, 1, 1, 0, 1, 0, 1, 2, 2, 0, 0, 1, // 1
                -1, 1, 1, 0, 0, 1, 1, 0, 2, 0, 0, 1, // 2
                -1, -1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, // 3

                // Back
                -1, -1, -1, 0, 0, 1, 1, 1, 0, 0, 0, -1, // 4
                -1, 1, -1, 0, 1, 0, 1, 1, 1, 0, 0, -1, // 5
                1, 1, -1, 1, 0, 0, 1, 0, 1, 0, 0, -1, // 6
                1, -1, -1, 0, 0, 0, 1, 0, 0, 0, 0, -1, // 7

                // Left
                -1, -1, 1, 1, 0, 0, 1, 1, 0, -1, 0, 0, // 8
                -1, 1, 1, 0, 1, 0, 1, 1, 1, -1, 0, 0, // 9
                -1, 1, -1, 0, 0, 1, 1, 0, 1, -1, 0, 0, // 10
                -1, -1, -1, 0, 0, 0, 1, 0, 0, -1, 0, 0, // 11

                // Right
                1, -1, -1, 1, 0, 0, 1, 1, 0, 1, 0, 0, // 12
                1, 1, -1, 0, 1, 0, 1, 1, 1, 1, 0, 0, // 13
                1, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, // 14
                1, -1, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, // 15

                // Top
                1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, // 16
                1, 1, -1, 0, 1, 0, 1, 1, 1, 0, 1, 0, // 17
                -1, 1, -1, 0, 0, 1, 1, 0, 1, 0, 1, 0, // 18
                -1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, // 19

                // Bottom
                1, -1, -1, 1, 0, 0, 1, 1, 0, 0, -1, 0, // 20
                1, -1, 1, 0, 1, 0, 1, 1, 1, 0, -1, 0, // 21
                -1, -1, 1, 0, 0, 1, 1, 0, 1, 0, -1, 0, // 22
                -1, -1, -1, 0, 0, 0, 1, 0, 0, 0, -1, 0, // 23
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

        //texture = TextureUtils.loadTexture(mContext, mTextureResId);
        textureMap = TextureUtils.loadTexture(mContext, mMaterialLightSpec.getDiffuseMap());
        textureMapSpecular = TextureUtils.loadTexture(mContext, mMaterialLightSpec.getSpecularMap());

        vertexStride = (COORDS_PER_VERTEX + COLORS_PER_VERTEX + TEXCOORDS_PER_VERTEX + NORMALS_PER_VERTEX) * SIZE_OF_FLOAT;
    }

    private void bindData() {
        // texture unit, place texture in target 2D of unit 0
        //GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        //GLES20.glUniform1i(uTextureLocation, 0);

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

        //
        // texture unit, place texture in target 2D of unit 1
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureMap);
        GLES20.glUniform1i(uMaterialDiffuseMapLocation, 1);
        // texture unit, place texture in target 2D of unit 2
        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureMapSpecular);
        GLES20.glUniform1i(uMaterialSpecularMapLocation, 2);
        GLES20.glUniform1f(uMaterialShininessLocation, mMaterialLightSpec.getShininess());

        GLES20.glUniform3f(uViewPosLocation, mCameraPos.x, mCameraPos.y, mCameraPos.z);

        GLES20.glUniform3f(uDirLightLocationHolder.getuDirectionLocation(),
                mDirLightSpec.getDirection().x, mDirLightSpec.getDirection().y, mDirLightSpec.getDirection().z);
        GLES20.glUniform3f(uDirLightLocationHolder.getuAmbientLocation(),
                mDirLightSpec.getAmbient().x, mDirLightSpec.getAmbient().y, mDirLightSpec.getAmbient().z);
        GLES20.glUniform3f(uDirLightLocationHolder.getuDiffuseLocation(),
                mDirLightSpec.getDiffuse().x, mDirLightSpec.getDiffuse().y, mDirLightSpec.getDiffuse().z);
        GLES20.glUniform3f(uDirLightLocationHolder.getuSpecularLocation(),
                mDirLightSpec.getSpecular().x, mDirLightSpec.getSpecular().y, mDirLightSpec.getSpecular().z);

        PointLightSpec pls = null;
        for (int i = 0; i < 4; i++) {
            if (i == 0) pls = mPointLightSpec0;
            if (i == 1) pls = mPointLightSpec1;
            if (i == 2) pls = mPointLightSpec2;
            if (i == 3) pls = mPointLightSpec3;

            GLES20.glUniform3f(uPointLightLocationHoldersMap.get(i).getuPositionLocation(),
                    pls.getPosition().x, pls.getPosition().y, pls.getPosition().z);
            GLES20.glUniform1f(uPointLightLocationHoldersMap.get(i).getuConstantLocation(), pls.getConstant());
            GLES20.glUniform1f(uPointLightLocationHoldersMap.get(i).getuLinearLocation(), pls.getLinear());
            GLES20.glUniform1f(uPointLightLocationHoldersMap.get(i).getuQuadraticLocation(), pls.getQuadratic());
            GLES20.glUniform3f(uPointLightLocationHoldersMap.get(i).getuAmbientLocation(),
                    pls.getAmbient().x, pls.getAmbient().y, pls.getAmbient().z);
            GLES20.glUniform3f(uPointLightLocationHoldersMap.get(i).getuDiffuseLocation(),
                    pls.getDiffuse().x, pls.getDiffuse().y, pls.getDiffuse().z);
            GLES20.glUniform3f(uPointLightLocationHoldersMap.get(i).getuSpecularLocation(),
                    pls.getSpecular().x, pls.getSpecular().y, pls.getSpecular().z);
        }

        GLES20.glUniform3f(uSpotLightLocationHolder.getuPositionLocation(),
                mSpotLightSpec.getPosition().x, mSpotLightSpec.getPosition().y, mSpotLightSpec.getPosition().z);
        GLES20.glUniform3f(uSpotLightLocationHolder.getuDirectionLocation(),
                mSpotLightSpec.getDirection().x, mSpotLightSpec.getDirection().y, mSpotLightSpec.getDirection().z);
        GLES20.glUniform1f(uSpotLightLocationHolder.getuCutOffLocation(), mSpotLightSpec.getCutOff());
        GLES20.glUniform1f(uSpotLightLocationHolder.getuOuterCutOffLocation(), mSpotLightSpec.getOuterCutOff());
        GLES20.glUniform1f(uSpotLightLocationHolder.getuConstantLocation(), mSpotLightSpec.getConstant());
        GLES20.glUniform1f(uSpotLightLocationHolder.getuLinearLocation(), mSpotLightSpec.getLinear());
        GLES20.glUniform1f(uSpotLightLocationHolder.getuQuadraticLocation(), mSpotLightSpec.getQuadratic());

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
