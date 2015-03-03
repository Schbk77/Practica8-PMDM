package com.example.serj.eattheburger;

import android.content.Intent;
import android.graphics.Canvas;
import android.util.Log;

public class HebraJuego extends Thread {

    private VistaJuego vista;
    private boolean funcionando = false;
    private static final long FPS = 20;

    public HebraJuego(VistaJuego vj) {
        this.vista = vj;
    }

    public void setFuncionando(boolean f) {
        funcionando = f;
    }

    @Override
    public void run() {
        long inicio;
        long ticksPS = 1000 / FPS; //Cada 50 milisegundos se dibuja una imagen nueva por ser 20FPS
        long tiempoEspera;
        while (funcionando) {
            Canvas canvas = null;
            inicio = System.currentTimeMillis(); //Se calcula el tiempo de inicio
            try {
                canvas = vista.getHolder().lockCanvas(); //Se bloquea el canvas
                synchronized (vista.getHolder()) { //Garantiza que nadie mas se meta en la hebra
                    if(vista.getVidas() < 0 || vista.getComidasLength() <= 0){
                        funcionando = false;
                        Intent intent = new Intent(vista.getContext(), GameOver.class);
                        intent.putExtra("puntuacion", vista.getPuntuacion());
                        vista.getContext().startActivity(intent);
                    } /*else if(vista.getComidasLength() <= 0){
                        funcionando = false;
                    } else if(){
                        Log.v("NIVEL", vista.getNivel() + " terminado");
                        vista.setNivel(vista.getNivel()+1);
                        // Siguiente nivel..........................................
                        funcionando = false;
                    }*/ else {
                        vista.draw(canvas);
                    }
                }

            } catch (NullPointerException e){
                funcionando =false;
            }
            finally {
                if (canvas != null) {
                    vista.getHolder().unlockCanvasAndPost(canvas); //Se desbloquea el canvas
                }
            }
            tiempoEspera = ticksPS - (System.currentTimeMillis()-inicio);
            try {
                if (tiempoEspera > 0)
                    sleep(tiempoEspera);
                else
                    sleep(10);
            } catch (InterruptedException e) {
            }
        }
    }
}
