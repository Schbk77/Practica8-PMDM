package com.example.serj.eattheburger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Bandeja {

    private Bitmap bmp;
    private int frameActual=0;
    private static final int COLUMNAS = 8;
    private static final int FILAS = 2;
    private int ancho, alto;
    private int ejeY = 0;
    private int ejeX = 0;
    private int direccionX;
    private static int anchoMax=0, altoMax=0;

    public Bandeja(Context contexto) {
        this.bmp = BitmapFactory.decodeResource(contexto.getResources(), R.drawable.scottpilgrim_multiple);
        this.ancho = bmp.getWidth()/COLUMNAS;
        this.alto = bmp.getHeight()/FILAS;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public int getEjeX() {
        return ejeX;
    }

    public int getEjeY() {
        return ejeY;
    }

    public void dibujar(Canvas canvas){
        movimiento();
        int origenx = frameActual * ancho;
        int origeny = 0;
        if(direccionX<0)
            origeny=alto;
        else
            origeny=0;
        // Centrar...
        Rect origen = new Rect(origenx, origeny, origenx + ancho, origeny +
                alto);
        Rect destino = new Rect(ejeX, ejeY, ejeX + ancho, ejeY + alto);
        canvas.drawBitmap(bmp, origen, destino, null);
        frameActual = ++frameActual % COLUMNAS;
    }

    private void movimiento(){
        if(ejeX > anchoMax - ancho - direccionX){
            direccionX = 0;
        }else if(ejeX + direccionX < 0){
            direccionX = 0;
        }
        ejeX = ejeX + direccionX;
        frameActual = ++frameActual % COLUMNAS;
    }

    public static void setDimension(int ancho, int alto){
        // Dimensiones de la bandeja
        anchoMax = ancho;
        altoMax = alto;
    }

    public void setDireccion(float aceleracion){
        // Mover izquierda o derecha dependiendo del acelerometro
        if((aceleracion > 0 && aceleracion < 1) || (aceleracion > -1 && aceleracion < 0)){
            // Quieto
            direccionX = 0;
        } else if(aceleracion > 1){
            // Ir hacia la izquierda
            direccionX = -20;
        } else if(aceleracion < -1){
            // Ir hacia la derecha
            direccionX = 20;
        }
    }

    public void setPosicion(float x, float y){
        // Situar bandeja en el eje X y en el eje Y
        this.ejeX = (int) x - ancho;
        this.ejeY = (int) (y * 0.82);
    }
}
