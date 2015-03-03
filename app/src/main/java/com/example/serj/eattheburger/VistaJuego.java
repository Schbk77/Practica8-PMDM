package com.example.serj.eattheburger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Marco on 26/2/15.
 */
public class VistaJuego extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {

    private SensorManager sensorManager = null;
    private HebraJuego hebraJuego;
    private Bandeja bandeja;
    private Comida[] comidas;
    private int screenWidth, screenHeight;
    private int alto, ancho;
    private final int COLUMNAS = 4;
    private int nivel = 1;
    private int puntuacion;
    private int vidas;
    private int ejeXColumna;
    private int posXColumna;
    private Bitmap fondo;
    private MediaPlayer backgroundMusic;
    private SoundPool sp;
    private int[] soundIds;
    private Vibrator v;

    public int getVidas() {
        return vidas;
    }

    public int getComidasLength(){
        return comidas.length;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public VistaJuego(Context context) {
        super(context);
        getHolder().addCallback(this);
        sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Ancho pantalla
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        screenWidth = p.x;
        screenHeight = p.y;
        // Inicializar hebra
        //hebraJuego = new HebraJuego(this);
        reproducirAudio(context);
        initSoundPool(context);
        // Puntuacion/vidas/nivel
        this.nivel = nivel;
        puntuacion = 0;
        vidas = 5;
        fondo = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondo);
        // Generar bandeja
        bandeja = new Bandeja(getContext());
        bandeja.setPosicion(screenWidth / 2, screenHeight); //<-- generar en la mitad
        //generar comidas por columnas
        ejeXColumna = screenWidth / COLUMNAS;
        // si el nivel es 1...
        switch (nivel) {
            case 1:
                comidas = new Comida[250];
                for (int i = 0; i < comidas.length; i++) {
                    // Tipo de comida
                    Random tipo = new Random();
                    comidas[i] = new Comida(getContext(), tipo.nextInt(7)); // <-- SUSTITUIR POR NUM MAX DE TIPOS
                    // Columna en la que aparece
                    Random columna = new Random();
                    posXColumna = (ejeXColumna * columna.nextInt(COLUMNAS)) + 65;
                    comidas[i].setColumna(posXColumna);
                    // Para que aparezcan alternos y separados
                    comidas[i].setPosicionY(100);
                    for (int j = 0; j < i - 1; j++) {
                        while (comidas[i].colisiona(comidas[j], 1000)) {
                            comidas[i].setPosicionY(100);
                        }
                    }
                    // Velocidad con la que cae(FACIL)
                    comidas[i].setVelocidad(10);
                }
                break;
        }
    }

    private void initSoundPool(Context context) {
        sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        soundIds = new int[2];
        soundIds[0] = sp.load(context, R.raw.bite, 1);
        soundIds[1] = sp.load(context, R.raw.boing, 2);
    }

    private void reproducirAudio(Context context) {
        backgroundMusic = MediaPlayer.create(context, R.raw.bg);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(10.0f, 3.0f);
        backgroundMusic.start();
    }

    // SurfaceView

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
        // Pintar fondo
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        Rect dest = new Rect(0, 0, screenWidth, screenHeight);
        canvas.drawBitmap(fondo, null, dest, paint);
        // Dibujar bandeja
        bandeja.dibujar(canvas);
        // Subir dificultad segun puntuacion
        if(puntuacion > 500 && puntuacion < 1000){
            // MEDIO
            for(Comida c : comidas){
                c.setVelocidad(20);
            }
        } else if(puntuacion > 1000){
            // DIFICIL
            for(Comida c : comidas){
                //Random velocidad = new Random();
                //c.setVelocidad(velocidad.nextInt(3 + (c.getDireccionY() * 2)));
                c.setVelocidad(30);
            }
        }
        // Dibujar comidas
        for (int i = 0; i < comidas.length; i++) {
            comidas[i].dibujar(canvas);
            if (comidas[i].getEjeY() >= screenHeight) {
                // Si cae en el fondo eliminar del array
                comidas = ArrayUtils.removeElement(comidas, comidas[i]);
                continue;
            }

            if (comidas[i].recogido(bandeja)) {
                // Si la bandeja recoge sumar o restar puntuacion dependiendo del tipo de objeto recogido
                if (comidas[i].isComida()) {
                    v.vibrate(100);
                    sp.play(soundIds[0], 1, 1, 1, 0, (float) 1.0);
                    puntuacion = puntuacion + comidas[i].getPuntos();
                } else {
                    v.vibrate(100);
                    sp.play(soundIds[1], 1, 1, 1, 0, (float) 1.0);
                    puntuacion = puntuacion + comidas[i].getPuntos();
                    vidas--;
                }
                comidas = ArrayUtils.removeElement(comidas, comidas[i]);
            }
        }
        paint.setTextSize(30);
        Typeface fipps = Typeface.createFromAsset(getContext().getAssets(), "fipps.otf");
        paint.setTypeface(fipps);
        // Dibujar marco puntuaciones
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(242,242,242));
        Rect marco = new Rect(20, 50, screenWidth-20, 120);
        canvas.drawRect(marco, paint);
        // Dibujar vidas en la pantalla
        paint.setColor(Color.BLUE);
        float text_x = 115;   // Coordenadas vidas
        float text_y = 100;
        canvas.drawText("Vidas: "+String.valueOf(vidas), text_x, text_y, paint);
        // Dibujar puntuación en la pantalla
        paint.setColor(Color.RED);
        text_x = 365; // Coordenadas puntuacion
        text_y = 100;
        canvas.drawText("Puntos: " + String.valueOf(puntuacion), text_x, text_y, paint);


    }

    // Callback

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        // Lanzar hebra si el juego está funcionando
        hebraJuego = new HebraJuego(this);
        hebraJuego.setFuncionando(true);
        hebraJuego.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        alto = height;
        ancho = width;
        Bandeja.setDimension(ancho, alto);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        if(backgroundMusic!=null){
            backgroundMusic.release();
            backgroundMusic = null;
        }
        boolean reintentar = true;
        hebraJuego.setFuncionando(false); //La hebra se para
        while (reintentar) {
            try {
                hebraJuego.join();
                reintentar = false;
            } catch (InterruptedException e) {
            }
        }
    }

    // SensorEventListener

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            bandeja.setDireccion(x);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
