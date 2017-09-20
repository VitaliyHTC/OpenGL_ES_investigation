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
 * http://startandroid.ru/ru/uroki/vse-uroki-spiskom/400-urok-171-opengl-tsvet.html
 */

public class OpenGLRenderer171 implements GLSurfaceView.Renderer {

    private Context context;

    private int programId;

    private FloatBuffer vertexData;
    private int aColorLocation;
    private int aPositionLocation;

    public OpenGLRenderer171(Context context) {
        this.context = context;
        prepareData();
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        glClearColor(0f, 0f, 0f, 1f);
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader171);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader171);
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
                // линия 1
                -0.4f, 0.6f, 1.0f, 0.0f, 0.0f,
                0.4f, 0.6f, 0.0f, 1.0f, 0.0f,

                // линия 2
                0.6f, 0.4f, 0.0f, 0.0f, 1.0f,
                0.6f, -0.4f, 1.0f, 1.0f, 1.0f,

                // линия 3
                0.4f, -0.6f, 1.0f, 1.0f, 0.0f,
                -0.4f, -0.6f, 1.0f, 0.0f, 1.0f,


                -0.5f, -0.2f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.2f, 0.0f, 1.0f, 0.0f,
                0.5f, -0.2f, 0.0f, 0.0f, 1.0f,
        };

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);
    }

    private void bindData() {
        // координаты
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 20, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        // цвет
        aColorLocation = glGetAttribLocation(programId, "a_Color");
        vertexData.position(2);
        glVertexAttribPointer(aColorLocation, 3, GL_FLOAT, false, 20, vertexData);
        glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onDrawFrame(GL10 arg0) {
        glLineWidth(5);
        glDrawArrays(GL_LINE_LOOP, 0, 6);
        glDrawArrays(GL_TRIANGLES, 6, 3);
    }


}
