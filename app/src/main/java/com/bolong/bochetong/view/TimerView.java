package com.bolong.bochetong.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.bolong.bochetong.activity.R;
import com.bolong.bochetong.utils.SharedPreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerView extends View {

    private final MyThread thread;

    /**
     * 第一圈的颜色
     */
    private int mFirstColor;
    /**
     * 第二圈的颜色
     */
    private int mSecondColor;
    /**
     * 圈的宽度
     */
    private int mCircleWidth;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 当前进度
     */
    public int mProgress;
    //int sj = (int) SharedPreferencesUtils.getData(this,"jindu",0);
    /**
     * 速度
     */
    private int mSpeed = 1000 * 60*60 / 360;//1 hour

    /**
     * 是否应该开始下一个
     */
    private boolean isNext = false;

    public boolean flag = true;

    public TimerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimerView(Context context) {
        this(context, null);
    }

    /**
     * 必要的初始化，获得一些自定义的值
     *
     * @param context
     * @param attrs
     * @param defStyle
     */

	/*绘制圆周白色分割线的画笔*/
    private Paint linePaint;

    public TimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyle, 0);
        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomProgressBar_firstColor:
                    mFirstColor = a.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.CustomProgressBar_secondColor:
                    mSecondColor = a.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.CustomProgressBar_circleWidth:
                    mCircleWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
            /*case R.styleable.CustomProgressBar_speed:
				mSpeed = a.getInt(attr, 20);//
				break;*/
            }
        }
        a.recycle();
        mPaint = new Paint();
        //添加
        linePaint = new Paint();
        //linePaint.setColor(Color.GRAY);
        //linePaint.setStrokeWidth(5);


        // 绘图线程
        /*new Thread() {
            public void run() {
                while (true) {
                    mProgress++;

                    if (mProgress == 360) {
                        mProgress = 0;
                        if (!isNext)
                            isNext = true;
                        else
                            isNext = false;
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(mSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            ;
        }.start();*/
        thread = new MyThread();
        //start();

    }

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date curDate,endDate;
    int diff=0;
    public void stop() {


        flag=false;
        thread.interrupt();


    }


    public void start(int diffTime) {

        flag=true;
        mProgress= diffTime;
        thread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = centre - mCircleWidth / 2;// 半径
        mPaint.setStrokeWidth(mCircleWidth / 5); // 设置圆环的宽度
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限
        if (!isNext) {// 第一颜色的圈完整，第二颜色跑
            mPaint.setColor(mFirstColor); // 设置圆环的颜色
            canvas.drawCircle(centre, centre, radius, mPaint); // 画出圆环
            mPaint.setColor(Color.parseColor("#5084ed")); // 设置圆环的颜色
            canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧
        } else {
            mPaint.setColor(mFirstColor); // 设置圆环的颜色
            canvas.drawCircle(centre, centre, radius, mPaint); // 画出圆环
            mPaint.setColor(Color.parseColor("#5084ed")); // 设置圆环的颜色
            canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧
        }
        //添加
        //半径
        //float radius = (getMeasuredWidth() - circlePadding * 3) / 2;
        //X轴中点坐标
        //int centerX = getMeasuredWidth() / 2;

        //3.绘制100份线段，切分空心圆弧
        for (float i = 0; i < 360; i += 6) {
            double rad = i * Math.PI / 180;//Math.PI / 180为1°
            float startX = (float) (centre + (radius - mCircleWidth) * Math.sin(rad));
            float startY = (float) (centre + (radius - mCircleWidth) * Math.cos(rad));

            float stopX = (float) (centre + radius * Math.sin(rad) + 1);
            float stopY = (float) (centre + radius * Math.cos(rad) + 1);

            float myCenterX = (float) (centre + (radius +mCircleWidth/4) * Math.sin(rad));
            float myCenterY = (float) (centre - (radius +mCircleWidth/4) * Math.cos(rad));
            //canvas.drawLine(startX, startY, stopX, stopY, linePaint);
            //Log.d("shushushu",mProgress+"");
            if ((mProgress) >= i) {
                linePaint.setColor(Color.parseColor("#5084ed"));
            } else {
                linePaint.setColor(Color.GRAY);
            }


            canvas.drawCircle(myCenterX, myCenterY, radius/40, linePaint);


        }



    }

    public int getProgress(){
        return mProgress;
    }

    private long newProgress;

    class MyThread extends  Thread{

        @Override
        public void run() {
           /* newProgress =  (SharedPreferenceUtil.getLong("diffTime",0)%60)*6;
            System.out.println(newProgress+"分钟对应度数");
            mProgress= (int) newProgress;*/
            System.out.println(mProgress+"long转int的进度");
            while (flag) {

                mProgress++;

                if (mProgress == 360) {
                    mProgress = 0;
                    if (!isNext)
                        isNext = true;
                    else
                        isNext = false;
                }
                postInvalidate();
                try {
                    Thread.sleep(mSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
