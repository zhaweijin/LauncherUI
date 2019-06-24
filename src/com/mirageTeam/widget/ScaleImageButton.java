package com.mirageTeam.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;

import com.mirageTeam.launcherui.R;

public class ScaleImageButton extends ImageButton {

	public boolean pressed_focus_flag = false;
	Context mContext;
	private String text;

	private boolean hasFocusFlag;

	private boolean operation_flag = false;

	public ScaleImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;

		TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleImageButton);

		int N = typeArray.getIndexCount();
		for (int i = 0; i < N; i++) {
			int attr = typeArray.getIndex(i);
			switch (attr) {
			case R.styleable.ScaleImageButton_text:
				int resouceId = typeArray.getResourceId(R.styleable.ScaleImageButton_text, 0);
				text = resouceId > 0 ? typeArray.getResources().getText(resouceId).toString() : typeArray.getString(R.styleable.ScaleImageButton_text);
				break;

			default:
				break;
			}
		}

		typeArray.recycle();
		//
		// final ScaleAnimation zoomOutAnimation = new ScaleAnimation(1.6f,
		// 1.9f, 1.6f, 1.9f, Animation.RELATIVE_TO_SELF, 0.5f,
		// Animation.RELATIVE_TO_SELF, 0.5f);
		// final ScaleAnimation ZoomInAnimation = new ScaleAnimation(1.2f, 1.0f,
		// 1.2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
		// Animation.RELATIVE_TO_SELF, 0.5f);
		//
		// zoomOutAnimation.setDuration(150);
		// zoomOutAnimation.setFillAfter(true);
		//
		// ZoomInAnimation.setDuration(150);
		// ZoomInAnimation.setFillAfter(true);
		//
		// setOnFocusChangeListener(new OnFocusChangeListener() {
		//
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// // TODO Auto-generated method stub
		// if (hasFocus) {
		// v.bringToFront();
		// startAnimation(zoomOutAnimation);
		// pressed_focus_flag = true;
		// postInvalidate();
		// } else {
		// startAnimation(ZoomInAnimation);
		// pressed_focus_flag = false;
		// postInvalidate();
		// }
		// }
		// });
		scaleAnimationFunc();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (text == null) {
			return;
		}
		//int canvas_width = canvas.getWidth();
		int canvas_width = getWidth();

		TextPaint mPaint = new TextPaint();
		mPaint.setARGB(255, 255, 255, 255);
		mPaint.setTextAlign(Paint.Align.CENTER);

		mPaint.setFlags(TextPaint.ANTI_ALIAS_FLAG | TextPaint.SUBPIXEL_TEXT_FLAG);
		mPaint.setAntiAlias(true);
		mPaint.setSubpixelText(true);

		int viewid = this.getId();
		
		int width = canvas_width >> 1;

		if (pressed_focus_flag) {
			mPaint.setTextSize(18);

			// 电子市场
			if (viewid == R.id.btn_appstore) {
				canvas.drawText(text, width, 270, mPaint);

			} else if (viewid == R.id.btn_televison) {
				// 电视
				canvas.drawText(text, width, 135, mPaint);

			} else if (viewid == R.id.btn_browser) {
				// 浏览器
				canvas.drawText(text, width, 135, mPaint);

			} else {
				// 其他
				canvas.drawText(text, width, 130, mPaint);

			}

		} else {
			mPaint.setTextSize(25);

			Log.e("ScaleImageButton", "pressed_focus_flag == false" + mPaint.measureText(text));

			// 电子市场
			if (viewid == R.id.btn_appstore) {
				canvas.drawText(text, width, 340, mPaint);

			} else if (viewid == R.id.btn_televison) {
				// 电视
				canvas.drawText(text, width, 155, mPaint);

			} else if (viewid == R.id.btn_browser) {
				// 浏览器
				canvas.drawText(text, width, 160, mPaint);

			} else {
				// 其他
				canvas.drawText(text, width, 160, mPaint);

			}

		}

	}

	public void setOperationFlag(boolean flag) {
		this.operation_flag = flag;
	}

	public boolean getOperationFlag() {
		return operation_flag;
	}

	public void rename(String name) {
		if (!name.equals("")) {
			this.text = name;
			// postInvalidate();
			scaleAnimationFunc();
		}

	}

	public void scaleAnimationFunc() {
		final ScaleAnimation zoomOutAnimation = new ScaleAnimation(1.6f, 1.9f, 1.6f, 1.9f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		final ScaleAnimation ZoomInAnimation = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

		zoomOutAnimation.setDuration(150);
		zoomOutAnimation.setFillAfter(true);

		ZoomInAnimation.setDuration(150);
		ZoomInAnimation.setFillAfter(true);

		if (hasFocusFlag) {
			this.bringToFront();
			startAnimation(zoomOutAnimation);
			pressed_focus_flag = true;
			postInvalidate();
		} else {
			startAnimation(ZoomInAnimation);
			pressed_focus_flag = false;
			postInvalidate();
		}

		setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					hasFocusFlag = true;
					v.bringToFront();
					startAnimation(zoomOutAnimation);
					pressed_focus_flag = true;
					postInvalidate();
				} else {
					hasFocusFlag = false;
					startAnimation(ZoomInAnimation);
					pressed_focus_flag = false;
					postInvalidate();
				}
			}
		});

	}
}
