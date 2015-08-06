package org.cos.watch;

import java.util.Calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class WatchView extends View {
	private String TAG = "WatchView";
	private float mPaddingLeft = 5;
	private float mPaddingRight = 5;
	private float mPaddingTop = 5;
	private float mPaddingBottom = 5;

	private float mWidth = 100;
	private float mHeight = 100;

	// 表盘的外环宽度
	private float mWatchCycleWidth = 3;
	// 表盘的半径
	private float mWatchRadius = 150;

	// 秒钟的长度
	private float mSecondCursorLength = 120;
	// 秒钟的形状
	private Path mSecondCursorPath = new Path();
	// 秒钟旋转的角度
	private float mSecendCursorDegrees = 0;

	// 分钟的长度
	private float mMinuteCursorLength = 90;
	// 分钟的形状
	private Path mMinuteCursorPath = new Path();
	// 分钟旋转的角度
	private float mMinuteCursorDegrees = 0;

	// 时钟的长度
	private float mHourCursorLength = 90;
	// 时钟的形状
	private Path mHourCursorPath = new Path();
	// 时钟旋转的角度
	private float mHourCursorDegrees = 0;

	// 画笔
	private Paint mPaint = new Paint();
	// 字体样式
	private Typeface mFace;

	private Rect mLogoRect;
	private Drawable mLogo;

	private Rect mBackgroundRect;
	private GradientDrawable mBackground;

	// 标记watch是否在走
	private boolean isRunning;
	private Handler handler = new Handler();

	public WatchView(Context context) {
		this(context, null);
	}

	public WatchView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WatchView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.HM_Watch, defStyleAttr, 0);
		mWatchCycleWidth = typedArray.getDimensionPixelSize(
				R.styleable.HM_Watch_cycleWidth, (int) mWatchCycleWidth);
		mWatchRadius = typedArray.getDimensionPixelSize(
				R.styleable.HM_Watch_watchRadius, (int) mWatchRadius);
		mSecondCursorLength = typedArray.getDimensionPixelSize(
				R.styleable.HM_Watch_secondLength, (int) mSecondCursorLength);
		mMinuteCursorLength = typedArray.getDimensionPixelSize(
				R.styleable.HM_Watch_minuteLength, (int) mMinuteCursorLength);
		mHourCursorLength = typedArray.getDimensionPixelSize(
				R.styleable.HM_Watch_hourLength, (int) mHourCursorLength);

		mLogo = typedArray.getDrawable(R.styleable.HM_Watch_logo);

		typedArray.recycle();

		mFace = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/samplefont.ttf");

		initSecondCursorPath();
		initMinuteCursorPath();
		initHourCursorPath();

		initBackground();
		initLogo();
	}

	private void initSecondCursorPath() {
		// 针尖
		float x1 = mPaddingLeft + mWatchRadius;
		float y1 = mPaddingTop + mWatchRadius - mSecondCursorLength;
		// 针左角
		float x2 = x1 - 3;
		float y2 = mPaddingTop + mWatchRadius + 8;
		// 针尾
		float x3 = x1;
		float y3 = mPaddingTop + mWatchRadius + 4;
		// 针右角
		float x4 = x1 + 3;
		float y4 = mPaddingTop + mWatchRadius + 8;

		mSecondCursorPath.moveTo(x1, y1);
		mSecondCursorPath.lineTo(x2, y2);
		mSecondCursorPath.lineTo(x3, y3);
		mSecondCursorPath.lineTo(x4, y4);
		mSecondCursorPath.lineTo(x1, y1);

		mSecondCursorPath.close();
	}

	private void initMinuteCursorPath() {
		// 针尖
		float x1 = mPaddingLeft + mWatchRadius;
		float y1 = mPaddingTop + mWatchRadius - mMinuteCursorLength;
		// 针左角
		float x2 = x1 - 3;
		float y2 = mPaddingTop + mWatchRadius + 8;
		// 针尾
		float x3 = x1;
		float y3 = mPaddingTop + mWatchRadius + 4;
		// 针右角
		float x4 = x1 + 3;
		float y4 = mPaddingTop + mWatchRadius + 8;

		mMinuteCursorPath.moveTo(x1, y1);
		mMinuteCursorPath.lineTo(x2, y2);
		mMinuteCursorPath.lineTo(x3, y3);
		mMinuteCursorPath.lineTo(x4, y4);
		mMinuteCursorPath.lineTo(x1, y1);

		mMinuteCursorPath.close();
	}

	private void initHourCursorPath() {
		// 针尖
		float x1 = mPaddingLeft + mWatchRadius;
		float y1 = mPaddingTop + mWatchRadius - mHourCursorLength;
		// 针左角
		float x2 = x1 - 3;
		float y2 = mPaddingTop + mWatchRadius + 8;
		// 针尾
		float x3 = x1;
		float y3 = mPaddingTop + mWatchRadius + 4;
		// 针右角
		float x4 = x1 + 3;
		float y4 = mPaddingTop + mWatchRadius + 8;

		mHourCursorPath.moveTo(x1, y1);
		mHourCursorPath.lineTo(x2, y2);
		mHourCursorPath.lineTo(x3, y3);
		mHourCursorPath.lineTo(x4, y4);
		mHourCursorPath.lineTo(x1, y1);

		mHourCursorPath.close();
	}

	private void initBackground() {

		mBackgroundRect = new Rect((int) mPaddingLeft, (int) mPaddingTop,
				(int) (mPaddingLeft + 2 * mWatchRadius),
				(int) (mPaddingTop + 2 * mWatchRadius));

		mBackground = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
				new int[] { Color.LTGRAY, Color.WHITE, Color.LTGRAY });
		mBackground.setShape(GradientDrawable.RECTANGLE);
		mBackground.setGradientRadius((float) (Math.sqrt(2) * mWatchRadius));
	}

	private void initLogo() {
		// int left = (int) (mPaddingLeft + mWatchRadius / 2);
		// int top = (int) (mPaddingTop + mWatchRadius / 2);
		// int right = (int) (mPaddingLeft + mWatchRadius * 1.5);
		// int bottom = (int) (mPaddingTop + mWatchRadius * 1.5);

		int left = (int) (mPaddingLeft + mWatchRadius * 5 / 6);
		int top = (int) (mPaddingTop + mWatchRadius * 2 / 4);
		int right = (int) (mPaddingLeft + mWatchRadius * 7 / 6);
		int bottom = (int) (mPaddingTop + mWatchRadius * 10 / 12);

		mLogoRect = new Rect(left, top, right, bottom);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(
				(int) (mPaddingLeft + mPaddingRight + mWatchRadius * 2f),
				(int) (mPaddingTop + mPaddingBottom + mWatchRadius * 2f));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawBackground(canvas);
		drawWatchCycle(canvas);
		drawTimeScale(canvas);
		drawLogo(canvas);

		drawTimeNum(canvas);
		drawHourCursor(canvas);
		drawMinuteCursor(canvas);
		drawSecendCursor(canvas);
	}

	// 画 表的外环
	private void drawWatchCycle(Canvas canvas) {
		float cx = getCenterX();
		float cy = getCenterY();
		float radius = mWatchRadius;

		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Style.STROKE);// 设置 是否为空心
		mPaint.setStrokeWidth(mWatchCycleWidth);
		mPaint.setAntiAlias(true);// 设置 抗锯齿
		canvas.drawCircle(cx, cy, radius, mPaint);
	}

	// 画 秒针
	private void drawSecendCursor(Canvas canvas) {

		mPaint.setColor(Color.RED);
		mPaint.setStyle(Style.FILL);
		mPaint.setAntiAlias(true);// 设置 抗锯齿

		canvas.save();
		// 将画布旋转
		canvas.rotate(mSecendCursorDegrees, getCenterX(), getCenterY());
		// 旋转完再画
		canvas.drawPath(mSecondCursorPath, mPaint);
		canvas.restore();
	}

	// 画 分针
	private void drawMinuteCursor(Canvas canvas) {
		mPaint.setColor(Color.GREEN);
		mPaint.setStyle(Style.FILL);
		mPaint.setAntiAlias(true);// 设置 抗锯齿

		canvas.save();
		// 将画布旋转
		canvas.rotate(mMinuteCursorDegrees, getCenterX(), getCenterY());
		// 旋转完再画
		canvas.drawPath(mMinuteCursorPath, mPaint);
		canvas.restore();
	}

	// 画 时针
	private void drawHourCursor(Canvas canvas) {
		mPaint.setColor(Color.BLUE);
		mPaint.setStyle(Style.FILL);
		mPaint.setAntiAlias(true);// 设置 抗锯齿

		canvas.save();
		// 将画布旋转
		canvas.rotate(mHourCursorDegrees, getCenterX(), getCenterY());
		// 旋转完再画
		canvas.drawPath(mHourCursorPath, mPaint);
		canvas.restore();
	}

	// 画 时间刻度
	private void drawTimeScale(Canvas canvas) {
		canvas.save();
		for (int i = 1; i <= 60; i++) {
			canvas.rotate(6, getCenterX(), getCenterY());

			float startX = mPaddingLeft + mWatchRadius;
			float startY = mPaddingTop;
			float stopX = startX;
			float stopY = mPaddingTop + 4;
			mPaint.setStrokeWidth(1);
			mPaint.setColor(Color.GRAY);
			if (i % 15 == 0) {
				stopY = mPaddingTop + 10;
				mPaint.setStrokeWidth(2);
				mPaint.setColor(Color.DKGRAY);
			} else if (i % 5 == 0) {
				stopY = mPaddingTop + 7;
				mPaint.setStrokeWidth(2);
				mPaint.setColor(Color.DKGRAY);
			}
			mPaint.setStrokeWidth(2);
			canvas.drawLine(startX, startY, stopX, stopY, mPaint);
		}
		canvas.restore();
	}

	// 时间数
	private void drawTimeNum(Canvas canvas) {
		canvas.save();
		for (int i = 1; i <= 12; i++) {

			canvas.rotate(30, getCenterX(), getCenterY());
			float x = mPaddingLeft + mWatchRadius;
			float y = mPaddingTop + mWatchRadius * 5 / 16;

			String text = i + "";
			mPaint.setColor(Color.BLACK);
//			mPaint.setTypeface(mFace);
			mPaint.setTextAlign(Paint.Align.CENTER);

			float textHeight = 16;
			mPaint.setTextSize(textHeight);
			float textWidth = mPaint.measureText(text);

			float textCenterX = x;
			float textCenterY = y - textHeight / 2;

			canvas.rotate(-30 * i, textCenterX, textCenterY);
			canvas.drawText(text, x, y, mPaint);
			canvas.rotate(30 * i, textCenterX, textCenterY);
		}
		canvas.restore();
	}

	private void drawBackground(Canvas canvas) {
		mBackground.setBounds(mBackgroundRect);

		canvas.save();
		canvas.translate(0, 0);
		mBackground.setGradientType(GradientDrawable.RADIAL_GRADIENT);
		mBackground.setCornerRadii(new float[] { mWatchRadius, mWatchRadius,
				mWatchRadius, mWatchRadius, mWatchRadius, mWatchRadius,
				mWatchRadius, mWatchRadius });
		mBackground.draw(canvas);
		canvas.restore();
	}

	private void drawLogo(Canvas canvas) {
		mLogo.setBounds(mLogoRect);
		canvas.save();
		mLogo.draw(canvas);
		canvas.restore();
	}

	private float getCenterX() {
		return mPaddingLeft + mWatchRadius;
	}

	private float getCenterY() {
		return mPaddingTop + mWatchRadius;
	}

	public void start() {
		if (isRunning) {
			return;
		}
		isRunning = true;
		doRunWatch();
	}

	public void stop() {
		isRunning = false;
	}

	private void doRunWatch() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (!isRunning) {
					return;
				}

				long currentTimeMillis = System.currentTimeMillis();
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(currentTimeMillis);

				int hour = calendar.get(Calendar.HOUR);
				int minute = calendar.get(Calendar.MINUTE);
				int second = calendar.get(Calendar.SECOND);

				mSecendCursorDegrees = second * 360f / 60;
				mMinuteCursorDegrees = minute * 360f / 60;
				mHourCursorDegrees = hour * 360f / 12;

				invalidate();

				doRunWatch();
			}
		}, 1000);
	}
}
