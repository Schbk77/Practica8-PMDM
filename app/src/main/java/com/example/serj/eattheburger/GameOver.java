package com.example.serj.eattheburger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.logging.Handler;

public class GameOver extends Activity{

    private TextView tvPuntuacion, tvPuntuacionMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_game_over);
        tvPuntuacionMax = (TextView)findViewById(R.id.tvPuntuacionMax);
        tvPuntuacion = (TextView)findViewById(R.id.tvPuntuacion);
        Bundle b = getIntent().getExtras();
        int puntuacion = b.getInt("puntuacion");
        if(puntuacion > getMaxPuntuacion()){
            setMaxPuntuacion(puntuacion);
        }
        tvPuntuacionMax.setText("Máxima: "+getMaxPuntuacion());
        tvPuntuacion.setText("Actual: "+puntuacion);
    }

    public void reintentar(View v){
        Intent intent = new Intent(this, Juego.class);
        startActivity(intent);
    }

    private int getMaxPuntuacion() {
        SharedPreferences sp = getSharedPreferences("puntuacionMax", Context.MODE_PRIVATE);
        return sp.getInt("puntuacionMax", 0);
    }

    private void setMaxPuntuacion(int puntuacionMax) {
        SharedPreferences sp = getSharedPreferences("puntuacionMax", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("puntuacionMax", puntuacionMax);
        ed.apply();
    }
}
