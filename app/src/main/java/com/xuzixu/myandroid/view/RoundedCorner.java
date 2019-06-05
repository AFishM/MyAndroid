package com.xuzixu.myandroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class RoundedCorner extends RelativeLayout {
    Path path=new Path();
    RectF rectF=new RectF();

    public RoundedCorner(Context context) {
        super(context);
    }

    public RoundedCorner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedCorner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rectF.set(getLeft(),getTop(),getRight(),getBottom());
        path.addRoundRect(rectF,80,80, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
