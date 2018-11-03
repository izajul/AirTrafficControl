package com.softfounder.apps.airtrafficcontrol.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.Random;

public class Plane {

    private int WIDTH,HEIGHT;
    private Bitmap mResource;


    private Path mPath = new Path();
    private Paint mSolidPathPaint,mDashedPathPaint;
    private float mPos[]=new float[2];
    private float mTan[]=new float[2];
    private Matrix mMatrix;
    private float off_set_x=0,off_set_y=0;

    private boolean isFirstTime = false,isUpdated=false;
    private float targetAngle=0;

    private float sin=0,cos=0;


    public Plane(Bitmap bitmap,int width,int height) {
        this.mResource = bitmap;
        isFirstTime = true;
        WIDTH = width;
        HEIGHT=height;

        off_set_x = mResource.getWidth()/2;
        off_set_y = mResource.getHeight()/2;

        mMatrix = new Matrix();

        mSolidPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSolidPathPaint.setColor(Color.RED);
        mSolidPathPaint.setStyle(Paint.Style.STROKE);
        mSolidPathPaint.setStrokeWidth(1);
        mSolidPathPaint.setTextSize(20);

        mDashedPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDashedPathPaint.setColor(Color.WHITE);
        mDashedPathPaint.setStyle(Paint.Style.STROKE);
        mDashedPathPaint.setStrokeWidth(5);
        mDashedPathPaint.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));

        //initialized start stage in random
        flying_itself();
    }

    public void update(){
        float current_x=0,current_y=0;
        current_x = mPos[0]-off_set_x;
        current_y = mPos[1]-off_set_y;
        mMatrix.reset();
        mMatrix.postRotate(targetAngle+90,off_set_x,off_set_y);
        mMatrix.postTranslate(current_x,current_y);
        if (!isFirstTime)
            targetAngle = getAngle(targetAngle,mPos);
        mPos = getNextTanOfXY(targetAngle,mPos,2);
        if (!isUpdated)isUpdated=true;
    }


    public void draw(Canvas canvas){
        if (isUpdated)   canvas.drawBitmap(mResource,mMatrix,null);
    }

    // flying itself while Path are not created
    private void flying_itself(){
        if (isFirstTime){
            mPos = getNewPos();
        }
    }

    //assigning starting position for new airplane
    private float[] getNewPos() {
        Random random = new Random();
        float posi[] = new float[2];
        int corner = random.nextInt(3)+1;
        if (corner==1){
            posi[0] = - 300;
            posi[1] = random.nextInt(HEIGHT-20)+10;
            targetAngle = getTargetAngel(corner,posi[1]);
        }
        if (corner==2){
            posi[1] = - 300;
            posi[0] = random.nextInt(WIDTH-20)+10;
            targetAngle = getTargetAngel(corner,posi[0]);
        }
        if (corner==3){
            posi[0] = WIDTH+300;
            posi[1] = random.nextInt(HEIGHT-20)+10;
            targetAngle = getTargetAngel(corner,posi[1]);
        }
        if (corner==4){
            posi[1] = HEIGHT+300;
            posi[0] = random.nextInt(WIDTH-20)+10;
            targetAngle = getTargetAngel(corner,posi[0]);
        }
        return posi;
    }

    //Getting Angel form the begin
    private float getTargetAngel(int corner, float range){
        float angel =0;

        if (corner==1){
            if ((HEIGHT/2)>=range){
                angel = (float) (new Random().nextDouble()*60)+10;
            }else{
                angel = -(float) (new Random().nextDouble()*60)+10;
            }
        }
        if (corner==2){
            if ((WIDTH/2)>=range){
                angel = (float) (new Random().nextDouble()*60)+10;
            }else{
                angel = (float) (new Random().nextDouble()*70)+100;
            }
        }
        if (corner==3){
            if ((HEIGHT/2)>=range){
                angel = (float) (new Random().nextDouble()*70)+100;
            }else{
                angel = -(float) (new Random().nextDouble()*70)+100;
            }
        }
        if (corner==4){
            if ((WIDTH/2)>=range){
                angel = -(float) (new Random().nextDouble()*60)+10;
            }else{
                angel = -(float) (new Random().nextDouble()*70)+100;
            }
        }
        return angel;
    }

    //Find Next X & Y position accordance Angle
    private float[] getNextTanOfXY (float angle,float[] currentPos,float speed){
        if ((currentPos[0]>0&&currentPos[0]<WIDTH)||(currentPos[1]<HEIGHT&&currentPos[1]>0))isFirstTime=false;
        double angleinRadian = Math.toRadians(angle);
        cos = (float) Math.cos(angleinRadian);
        sin = (float) Math.sin(angleinRadian);
        return new float[]{(cos*speed+currentPos[0]),(sin*speed+currentPos[1])};
    }

    //get angle while plane at the end
    private float getAngle(float angle, float[] nextXY){
        if ((angle<0 && angle>=-180) && (nextXY[1]<=0)){
            angle = Math.abs(angle);
        }
        if ((angle<180 && angle>0) && (nextXY[1])>=HEIGHT ){
            angle = -angle;
        }
        if ((angle<0 && angle>=-90) && (nextXY[0])>=WIDTH ){
            angle = -(angle+180);
        }
        if ((angle>0 && angle<=90) && (nextXY[0])>=WIDTH ){
            angle = 180-angle;
        }
        if ((angle<-90 && angle>=-180) && (nextXY[0])<=0 ){
            angle = -(angle+180);
        }
        if ((angle>90 && angle<=180) && (nextXY[0])<=0 ){
            angle = 180-angle;
        }
        return angle;
    }

    //
    public void setPath(Path mPath) {
        this.mPath = mPath;
    }

}
