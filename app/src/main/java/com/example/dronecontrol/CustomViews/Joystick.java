package com.example.dronecontrol.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Joystick extends View {
    private float[] mCirceleX;
    private float[] mCirceleY;
    private Paint mPaintCircle;
    private static final float mCircleRadius = 100;

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
        mCirceleX = new float[2];
        mCirceleY =new float[2];
        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCircle.setColor(Color.parseColor("#FF0000"));
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas)
    {
        if(0f == mCirceleX[0] && 0f == mCirceleY[0]) {
            mCirceleX[0] = getWidth() / 2;
            mCirceleY[0] = getHeight() / 2;
        }
        mCirceleX[1] = 0;
        mCirceleY[1] = 0;
        canvas.drawCircle(mCirceleX[0],mCirceleY[0],mCircleRadius,mPaintCircle);

    }

    public float getXDistance()
    {
        return mCirceleX[0]-mCirceleX[1];
    }

    public float getYDistance()
    {
        return mCirceleY[0]-mCirceleY[1];
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean value = super.onTouchEvent(event);

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mCirceleX[1] = mCirceleX[0];
                mCirceleY[1] = mCirceleY[0];
                return true;

            case MotionEvent.ACTION_MOVE:
                if(Math.pow(event.getX() - mCirceleX[0],2) + Math.pow(event.getY() - mCirceleY[0],2) < Math.pow(mCircleRadius,2)) //check if the touched part is inside my joystickCenter
                {
                    mCirceleY[0] = event.getY(); // set new position X of joystickCenter
                    mCirceleX[0] = event.getX(); // set new position X of joystickCenter

                    postInvalidate(); // update the UI of changes
                    return true;
                }
                return value;

        }
        return value;
    }
}
