package com.example.serj.eattheburger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class Juego extends Activity {

    private VistaJuego vistaJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        vistaJuego = new VistaJuego(this);
        setContentView(vistaJuego);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
    }
}
