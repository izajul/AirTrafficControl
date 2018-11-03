package com.softfounder.apps.airtrafficcontrol.threads;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.softfounder.apps.airtrafficcontrol.controller.GameController;

public class GameMainThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private GameController gameController;
    private int FPS = 10;
    private double avgFPS;
    private boolean running;

    public static Canvas canvas;

    public GameMainThread(SurfaceHolder surfaceHolder, GameController gameController){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameController = gameController;
    }

    @Override
    public void run(){
        long startTime,timeMillSec,waitTime,totalTime=0,targetTime=1000/FPS;
        int frameCount=0;

        while (running){
            canvas = null;
            startTime = System.nanoTime();
            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gameController.update();
                    this.gameController.draw(canvas);
                }
                this.surfaceHolder.unlockCanvasAndPost(canvas);
            }
            catch (Exception e){}
            finally {
                if (canvas!=null){
                    try{
                        this.surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e){}
                }
            }

            timeMillSec = System.nanoTime()-startTime;
            waitTime = targetTime-timeMillSec;

            try{
                this.sleep(waitTime);
            }catch (Exception e){}
        }

    }

    public void setRunning(Boolean running){
        this.running = running;
    }
}
