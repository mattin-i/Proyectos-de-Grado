package com.alumno.proyectopizzascebanc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // En las siguientes 3 líneas adaptamos la pantalla para que solo se vea nuestro layoutn ha pantalla completa.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        TimerTask task = new TimerTask(){
            @Override
            public void run() {

                // Creamos un intent para lanzar una nueva actividad después de 2 segundos.
                Intent mainIntent = new Intent(SplashActivity.this, InfoActivity.class);
                startActivity(mainIntent);

                // Cerramos la actividad para que no podamos volver a esta, ya que es un splash y solo lo queremos al abrir la app.
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 2000);
    }
}
