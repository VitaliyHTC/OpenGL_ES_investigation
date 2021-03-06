package com.vitaliyhtc.opengl_es_investigation.renderer;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.vitaliyhtc.opengl_es_investigation.R;
import com.vitaliyhtc.opengl_es_investigation.util.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_LINE_LOOP;
import static android.opengl.GLES20.GL_LINE_STRIP;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLineWidth;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/**
 * Created by vitaliyhtc on 14.08.17.
 * tutorial:
 * http://startandroid.ru/ru/uroki/vse-uroki-spiskom/399-urok-170-opengl-graficheskie-primitivy.html
 */

public class OpenGLRenderer170 implements GLSurfaceView.Renderer {
    private Context context;
    private int programId;
    private FloatBuffer vertexData;
    private int uColorLocation;
    private int aPositionLocation;

    public OpenGLRenderer170(Context context) {
        this.context = context;
        prepareData();
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        glClearColor(0f, 0f, 0f, 1f);
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        programId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
        glUseProgram(programId);
        bindData();
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        glViewport(0, 0, width, height);
    }

    private void prepareData() {
        float[] vertices = {
                // треугольник 1
                -0.9f, 0.8f, -0.9f, 0.2f, -0.5f, 0.8f,

                // треугольник 2
                -0.6f, 0.2f, -0.2f, 0.2f, -0.2f, 0.8f,

                // треугольник 3
                0.1f, 0.8f, 0.1f, 0.2f, 0.5f, 0.8f,

                // треугольник 4
                0.1f, 0.2f, 0.5f, 0.2f, 0.5f, 0.8f,

                // линия 1
                -0.7f, -0.1f, 0.7f, -0.1f,

                // линия 2
                -0.6f, -0.2f, 0.6f, -0.2f,

                // точка 1
                -0.5f, -0.3f,

                // точка 2
                0.0f, -0.3f,

                // точка 3
                0.5f, -0.3f,
        };
        vertexData = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(vertices);
    }

    private void bindData() {
        uColorLocation = glGetUniformLocation(programId, "u_Color");
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onDrawFrame(GL10 arg0) {
        glClear(GL_COLOR_BUFFER_BIT);
        //glDrawArrays(GL_TRIANGLES, 0, 12);
        glLineWidth(5);
        glDrawArrays(GL_TRIANGLES, 0, 12);
        glDrawArrays(GL_LINES, 12, 4);
        glDrawArrays(GL_POINTS, 16, 3);
    }
}
