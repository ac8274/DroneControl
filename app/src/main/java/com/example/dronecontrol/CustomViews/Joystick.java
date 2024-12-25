package com.example.dronecontrol.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dronecontrol.R;

public class Joystick extends View {

    private float mCirceleX;
    private float mCirceleY;
    private Paint mPaintCircle;
    private int mRadius;
    private double containment_height;
    private double containment_width;


    public Joystick(Context context)
    {
        super(context);
        init(null);
    }

    public Joystick(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
    }

    public Joystick(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    public Joystick(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void init(@Nullable AttributeSet set)
    {
        mCirceleX = (float) 0.0;
        mCirceleY =(float) 0.0;
        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCircle.setColor(Color.parseColor("#FF0000"));
        mRadius = 100;
        containment_height = 0.0;
        containment_width = 0.0;
        if(set == null)
        {
            return;
        }

        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.Joystick);

        mPaintCircle.setColor(ta.getColor(R.styleable.Joystick_circle_color,Color.parseColor("#FF0000")));

        ta.recycle();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas)
    {
        mRadius = (int) (getHeight()/3);
        if(0f == mCirceleX && 0f == mCirceleY) {
            mCirceleX = getWidth() / 2;
            mCirceleY = getHeight() / 2;
        }
        canvas.drawCircle(mCirceleX,mCirceleY,mRadius,mPaintCircle);
        containment_height = getHeight();
        containment_width = getWidth();
    }

    public float getXDistance()
    {
        return Math.abs(mCirceleY - (float) containment_height/2);
    }

    public float getYDistance()
    {
        return Math.abs(mCirceleX - (float)containment_width/2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean value = super.onTouchEvent(event);

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                return true;

            case MotionEvent.ACTION_MOVE:
                Log.println(Log.DEBUG,"Position", "circle X:"+ String.valueOf(event.getX()) + "\n circle Y" + String.valueOf(event.getY()));
                if((event.getY() <= (int) containment_height) && (event.getY() >=0) && (event.getX() <= (int) containment_width) && (event.getX() >=0))
                {
                    mCirceleY = event.getY(); // set new position X of joystickCenter
                    mCirceleX = event.getX(); // set new position X of joystickCenter
                    postInvalidate();
                } // update the UI of changes
                return value;

            case MotionEvent.ACTION_UP:
                mCirceleY = (float)containment_height/2;
                mCirceleX = (float)containment_width/2;
                postInvalidate();
                return true;
        }
        return value;
    }
}
