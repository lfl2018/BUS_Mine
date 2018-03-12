package com.mogu.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class ViewPagerIndicator extends View {
	private static final int RADIUS = 10;
	private static final int CY = 30;
	private Paint paint;
	private Paint paint2;
	private Paint paint3;
	private int offset;
	private int num = 5;

	@Override
	protected void onDraw(Canvas canvas) {
		int cx = (int) (ScreenUtils.getScreenWidth(getContext()) / 2 - (num - 1)
				* 1.5 * RADIUS);
		for (int i = 0; i < num; i++) {
			canvas.drawCircle(cx + i * RADIUS * 3, CY, RADIUS, paint);
			canvas.drawCircle(cx + i * RADIUS * 3, CY, RADIUS, paint2);
		}
		canvas.drawCircle(cx + offset, CY, RADIUS, paint3);
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void move(int position, float perc) {
		offset = position * 3 * RADIUS;
		if (position != num - 1) {
			offset += (int) (perc * 3 * RADIUS);
		}
		invalidate();
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.WHITE);
		paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint2.setColor(Color.GRAY);
		paint2.setStyle(Style.STROKE);
		paint2.setStrokeWidth(2);
		paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint3.setColor(Color.RED);

	}

}
