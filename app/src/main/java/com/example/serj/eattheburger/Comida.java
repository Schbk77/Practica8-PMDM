package com.example.serj.eattheburger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.Random;

public class Comida {

    private Context context;
    private Bitmap bmp;
    private int tipo;
    private boolean comida;
    private int ancho, alto;
    private int ejeY = 0;
    private static int direccionY;
    private int ejeX = 0;
    private int puntos;

    public Comida(Context context, int tipo){
        this.tipo = tipo;
        this.context = context;
        setTipo();
    }

    private void caer(){
        ejeY = ejeY + direccionY;
    }

    public boolean colisiona(Comida c, int delta) {
        if(this.ejeX + this.ancho - delta < c.ejeX){
            return false;
        }
        if(this.ejeY + this.alto - delta < c.ejeY){
            return false;
        }
        if(this.ejeX > c.ejeX +  c.ancho - delta){
            return false;
        }
        if(this.ejeY > c.ejeY + c.alto - delta){
            return false;
        }
        return true;
    }

    public void dibujar(Canvas canvas){
        caer();
        // Dibujar Comida
        canvas.drawBitmap(bmp, ejeX, ejeY, null);
    }

    public boolean isComida() {
        return comida;
    }

    public int getEjeY() {
        return ejeY;
    }

    public static int getDireccionY() {
        return direccionY;
    }

    public int getPuntos() {
        return puntos;
    }

    public boolean recogido(Bandeja bandeja) {
        // Para saber si he recogido una comida al caer
        if(this.ejeX + this.ancho < bandeja.getEjeX()){
            return false;
        }
        if(this.ejeY + this.alto < bandeja.getEjeY()){
            return false;
        }
        if(this.ejeX > bandeja.getEjeX() +  bandeja.getAncho()){
            return false;
        }
        if(this.ejeY > bandeja.getEjeY()+ bandeja.getAlto()){
            return false;
        }
        return true;
    }

    private void setDimensiones(Bitmap bmp){
        this.ancho = bmp.getWidth();
        this.alto = bmp.getHeight();
    }

    public void setColumna(int ejeX){
        this.ejeX = ejeX;
        this.ejeY = 0;
    }

    private void setTipo(){
        // Establece el comportamiento
        switch (tipo){
            case 0:
                //Hamburguesa
                this.comida = true;
                this.puntos = 50;
                this.bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.hamburguesa);
                setDimensiones(this.bmp);
                break;
            case 1:
                // Naranja
                this.comida = false;
                this.puntos = 0;
                this.bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.naranja);
                setDimensiones(this.bmp);
                break;
            case 2:
                // Patatas
                this.comida = true;
                this.puntos = 10;
                this.bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.patatas);
                setDimensiones(this.bmp);
                break;
            case 3:
                // Pollo
                this.comida = true;
                this.puntos = 20;
                this.bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.pollo);
                setDimensiones(this.bmp);
                break;
            case 4:
                // Platano
                this.comida = false;
                this.puntos = 0;
                this.bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.platano);
                setDimensiones(this.bmp);
                break;
            case 5:
                // Refresco
                this.comida = true;
                this.puntos = 5;
                this.bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.refresco);
                setDimensiones(this.bmp);
                break;
            case 6:
                // Bacon
                this.comida = true;
                this.puntos = 100;
                this.bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.bacon);
                setDimensiones(this.bmp);
                break;
        }
    }

    public static void setDireccionY(int direccionY) {
        Comida.direccionY = direccionY;
    }

    public void setVelocidad(int direccionY){
        //Random velocidad = new Random();
        //this.direccionY = velocidad.nextInt(5+(direccionY*2))+3;
        this.direccionY = direccionY;
    }

    public void setPosicionY(int distancia) {
        //Random y = new Random();
        //this.ejeY = -(y.nextInt(distancia)+(y.nextInt(distancia)*distancia));
        this.ejeY = -distancia;
    }
}
