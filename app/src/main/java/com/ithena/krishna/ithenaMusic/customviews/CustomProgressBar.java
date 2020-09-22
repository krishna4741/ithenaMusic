package com.ithena.krishna.ithenaMusic.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ithena.krishna.ithenaMusic.activities.HomeActivity;
import com.ithena.krishna.ithenaMusic.fragments.PlayerFragment.PlayerFragment;


public class CustomProgressBar extends View {

    Paint forePaint;

    public CustomProgressBar(Context context) {
        super(context);
        init();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        forePaint = new Paint();
        forePaint.setStrokeWidth(1.0f);
        forePaint.setAntiAlias(true);
        forePaint.setColor(Color.rgb(0, 128, 255));
    }

    public void update() {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        forePaint.setColor(HomeActivity.themeColor);
        forePaint.setAlpha(248);
        float right = ((float) canvas.getWidth() / (float) PlayerFragment.durationInMilliSec) * (float) PlayerFragment.mMediaPlayer.getCurrentPosition();
        canvas.drawRect(0, 0, right, canvas.getHeight(), forePaint);
    }
}
