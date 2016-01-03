package com.high_jump;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.speech.tts.TextToSpeech;

public class Ball {
	float radius = 80;      // Ball's radius
	float x = radius + 20;  // Ball's center (x,y)
	float y = radius + 40;
	float speedX = 5;       // Ball's speed (x,y)
	float speedY = 0;
	private RectF bounds;   // Needed for Canvas.drawOval
	private Paint paint;    // The paint style, color used for drawing
	private float mLastPosX;
	private float mLastPosY;
	private float mPosX;
	private float mPosY;
	private float mAccelX;
	private float mAccelY;
	private float mOneMinusFriction;
	private static final float sFriction = 0.1f;

	public int numberOfHits = 0;
	private BouncingBallView mCtx;

	private TextToSpeech mTts;
	private Canvas mCanvas;

	// Constructor
	public Ball(BouncingBallView context, int color, int xcord) {
		bounds = new RectF();
		paint = new Paint();
		paint.setColor(color);
		mTts = null;
		mCtx = context;

		x += xcord;

		final float r = ((float) Math.random() - 0.5f) * 0.2f;
		mOneMinusFriction = 1.0f - sFriction + r;

	}

	public void speak (String msg)
	{
		if (mTts != null){
			mTts.speak(msg,
					TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
					null);
		}
	}

	public synchronized void setTts (TextToSpeech tts) {
		mTts = tts;
	}
	public void moveWithCollisionDetection(Box box) {
		// Get new (x,y) position
		//x += speedX;
		//y += getY();
		y  += getY();
		
		// Detect collision and react 
		if (x + radius > box.xMax) {
			speedX = -speedX;
			x = box.xMax-radius;
		} else if (x - radius < box.xMin) {
			speedX = -speedX;
			x = box.xMin+radius;
		}
		if (y + radius > box.yMax) {
			float tmpY = speedY;
			tmpY = -tmpY;
			setY (tmpY);
			//speedY = -speedY;
			y = box.yMax - radius;

			//FeedBack
			numberOfHits++;

			if ((numberOfHits > mCtx.getOtherBallScore()) &&
				 ((numberOfHits % 10) == 0)	) { 
				this.sayHello(true);
				paint.setColor(getBallColor());
				mCanvas.drawOval(bounds, paint);
			}
			else if ((numberOfHits % 10) == 0){
				this.sayHello(false);
				paint.setColor(getBallColor());
				mCanvas.drawOval(bounds, paint);
			}
			else {;}
		} else if (y - radius < box.yMin) {
			float tmpY = speedY;
			tmpY = -tmpY;
			setY (tmpY);
			//speedY = -speedY;
			y = box.yMin + radius;
			//numberOfHits++;
		}
	}

	public void draw(Canvas canvas) {
		bounds.set(x-radius, y-radius, x+radius, y+radius);
		canvas.drawOval(bounds, paint);
		mCanvas = canvas;
	}

	private static final Random RANDOM = new Random();
	private static final String[] posHELLOS = {
		"GOOD",
		"KEEP GOING",
		"Awesome",
		"You can win",
		"SWEET",
		"Your score is better than him"
	};

	private static final String[] negHELLOS = {
		"PUSH HARDER",
		"You can win",
		"He is winning"
	};

	private void sayHello(boolean ispos) {
		if (mTts == null) return;
		// Select a random hello.

		String hello;
		if (ispos){
			int helloLength = posHELLOS.length;
			hello = posHELLOS[RANDOM.nextInt(helloLength)];
		}
		else {
			int helloLength = negHELLOS.length;
			hello = negHELLOS[RANDOM.nextInt(helloLength)];
		}
		mTts.speak(hello,
				TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
				null);
	}

	private static final int[] COLORS = {
		Color.MAGENTA,
		Color.WHITE,
		Color.CYAN,
		Color.DKGRAY,
		Color.RED,
		Color.YELLOW
	};

	private int getBallColor() {
		// Select a random hello.
		int helloLength = COLORS.length;
		int color = COLORS[RANDOM.nextInt(helloLength)];

		return color;
	}
	public synchronized void setY (float sy) {
		speedY = sy;
	}
	public synchronized float getY () {
		return speedY;
	}
}
