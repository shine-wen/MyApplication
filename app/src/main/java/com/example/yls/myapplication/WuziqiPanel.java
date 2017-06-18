package com.example.yls.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yls on 2017/6/12.
 */

public class WuziqiPanel extends View{
    private  int mpanelWidth;
    private float mLineHeight;
    private  int MAX_LINE=10;

    private  int MAX_COUNT_IN_LINE=5;

    private Paint mPaint=new Paint();
    private Bitmap mWhitePiece;
    private  Bitmap mBlackPiece;
    private  float ratioPieceOfLineHeight=3*1.0f/4;

    private  boolean mIswhite=true;
    private List<Point> mWhiteArry=new ArrayList<>();
    private List<Point> mBlackArry=new ArrayList<>();
    private  boolean mIsGameOver;
    private  boolean mIsWhiteWinner;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    public WuziqiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0x44ff0000);
         init();
    }

    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mWhitePiece= BitmapFactory.decodeResource(getResources(),R.drawable.bai);
        mBlackPiece= BitmapFactory.decodeResource(getResources(),R.drawable.hei);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getSize(widthMeasureSpec);

        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getSize(heightMeasureSpec);

        int width=Math.min(widthSize,heightSize);
        if(widthMode==MeasureSpec.UNSPECIFIED){
            width =heightSize;
        }else if (heightMode==MeasureSpec.UNSPECIFIED){
            width =widthSize;
        }
        setMeasuredDimension(width,width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mpanelWidth=w;
        mLineHeight=mpanelWidth*1.0f /MAX_LINE;
        int pieceWidth= (int) (mLineHeight*ratioPieceOfLineHeight);
       mWhitePiece=Bitmap.createScaledBitmap(mWhitePiece,pieceWidth,pieceWidth,false);
        mBlackPiece=Bitmap.createScaledBitmap(mBlackPiece,pieceWidth,pieceWidth,false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
         if(mIsGameOver) return  false;

          int action=event.getAction();
        if(action==MotionEvent.ACTION_UP){

            int x= (int) event.getX();
            int y= (int) event.getY();

            Point p=getValidPoint(x,y);
            if(mWhiteArry.contains(p)||mBlackArry.contains(p)){
                return  false;
            }

            if(mIswhite){
                mWhiteArry.add(p);
            }else {
                mBlackArry.add(p);
            }
            invalidate();
            mIswhite=!mIswhite;

//            return  true;
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return  new Point((int)(x/mLineHeight),(int)(y/mLineHeight));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPiece(canvas);
        checkGameOver();
    }

    private void checkGameOver() {

        boolean whiteWin=checkFiveInLine(mWhiteArry);
        boolean blackWin=checkFiveInLine(mBlackArry);
        if(whiteWin||blackWin){
            mIsGameOver=true;
            mIsWhiteWinner=whiteWin;
            String text=mIsWhiteWinner ?"恭喜，棋获得胜利":"恭喜白棋获得胜利";
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFiveInLine(List<Point> points) {
        for(Point p: points){
            int x=p.x;
            int y=p.y;
            boolean win =checkHorizontal(x,y,points);
            if(win) return  true;
            win =checkVertical(x,y,points);
            if(win) return  true;

            win =checkLeftDiagonal(x,y,points);
            if(win) return  true;

            win =checkRightDiagonal(x,y,points);
            if(win) return  true;



        }

        return  false;
    }

    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count=1;
        for(int i=1;i<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x-i,y))){
                count++;
            }else {
                break;
            }
        }
          if(count==MAX_COUNT_IN_LINE) return  true;

        for(int i=1;i<MAX_COUNT_IN_LINE;i++){

            if(points.contains(new Point(x+i,y))){
                count++;
            }else {
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return  true;

        return  false;
    }

    private boolean checkVertical(int x, int y, List<Point> points) {
        int count=1;
        for(int i=1;i<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x,y-i))){
                count++;
            }else {
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return  true;

        for(int i=1;i<MAX_COUNT_IN_LINE;i++){

            if(points.contains(new Point(x,y+i))){
                count++;
            }else {
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return  true;

        return  false;
    }


    private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        int count=1;
        for(int i=1;i<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x-i,y+i))){
                count++;
            }else {
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return  true;

        for(int i=1;i<MAX_COUNT_IN_LINE;i++){

            if(points.contains(new Point(x+i,y+i))){
                count++;
            }else {
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return  true;

        return  false;
    }

    private boolean checkRightDiagonal(int x, int y, List<Point> points) {
        int count=1;
        for(int i=1;i<MAX_COUNT_IN_LINE;i++){
            if(points.contains(new Point(x-i,y-i))){
                count++;
            }else {
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return  true;

        for(int i=1;i<MAX_COUNT_IN_LINE;i++){

            if(points.contains(new Point(x+i,y+i))){
                count++;
            }else {
                break;
            }
        }
        if(count==MAX_COUNT_IN_LINE) return  true;

        return  false;
    }

    private void drawPiece(Canvas canvas) {
        for(int i=0,n=mWhiteArry.size();i<n;i++){
            Point whitePoint=mWhiteArry.get(i);
            canvas.drawBitmap(mWhitePiece,(whitePoint.x+(1-ratioPieceOfLineHeight)/2)*mLineHeight,
                    (whitePoint.y+(1-ratioPieceOfLineHeight)/2)*mLineHeight,null);
        }

        for(int i=0,n=mBlackArry.size();i<n;i++){
            Point blackPoint=mBlackArry.get(i);
            canvas.drawBitmap(mBlackPiece,(blackPoint.x+(1-ratioPieceOfLineHeight)/2)*mLineHeight,
                    (blackPoint.y+(1-ratioPieceOfLineHeight)/2)*mLineHeight,null);
        }
    }

    private void drawBoard(Canvas canvas) {
        int w=mpanelWidth;
        float lineHeight=mLineHeight;
        for(int i=0;i<MAX_LINE;i++){
            int startx= (int) (lineHeight/2);
            int endx= (int) (w-lineHeight/2);
            int y= (int) ((0.5+i)*lineHeight);
            canvas.drawLine(startx,y,endx,y,mPaint);
            canvas.drawLine(y,startx,y,endx,mPaint);
        }
    }
}
