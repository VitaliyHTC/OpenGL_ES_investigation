package com.vitaliyhtc.opengl_es_investigation;

import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.vitaliyhtc.opengl_es_investigation.renderer.OpenGLRenderer176;
import com.vitaliyhtc.opengl_es_investigation.util.OpenGLUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!OpenGLUtils.supportES2(this)) {
            Toast.makeText(this, "OpenGl ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        //glSurfaceView = new GLSurfaceView(this);
        glSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setZOrderOnTop(true);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        glSurfaceView.setRenderer(new OpenGLRenderer176(this));

        // Render the view only when there is a change in the drawing data
        // disable animation. So, not applicable in our context.
        //glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        //setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

}
