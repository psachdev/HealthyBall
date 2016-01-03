package com.high_jump;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class BouncingBallView extends View {
	private Ball ball1;
	private Box box1;
	private Ball ball2;

	private StatusMessage statusMsg1;
	private StatusMessage statusMsg2;

	// For touch inputs - previous touch (x, y)
	private float previousX;
	private float previous1Y;
	private float previous2Y;
	private long mLastT;
	private float mLastDeltaT;

	MainActivity mContext;
	// Constructor
	public BouncingBallView(MainActivity context) {
		super(context);
		mLastT = 0;
		mLastDeltaT = 0;

		mContext = context;
		box1 = new Box(Color.BLUE);  // ARGB
		ball1 = new Ball(this, Color.GREEN, 0);
		ball2 = new Ball(this, Color.RED, 200);

		statusMsg1 = new StatusMessage(Color.BLACK, 0, "johny");
		statusMsg2 = new StatusMessage(Color.BLACK, 175, "william");

		// To enable keypad
		this.setFocusable(true);
		this.requestFocus();
		// To enable touch mode
		this.setFocusableInTouchMode(true);
	}

	public void setTts (TextToSpeech tts) {
		ball1.setTts (tts);
		ball2.setTts (tts);
	}
	// Called back to draw the view. Also called after invalidate().
	@Override
	protected void onDraw(Canvas canvas) {
		// Draw the components
		box1.draw(canvas);
		ball1.draw(canvas);
		statusMsg1.draw(canvas);

		// Update the position of the ball, including collision detection and reaction.
		ball1.moveWithCollisionDetection(box1);
		statusMsg1.update(ball1, false);

		ball2.draw(canvas);
		statusMsg2.draw(canvas);

		// Update the position of the ball, including collision detection and reaction.
		ball2.moveWithCollisionDetection(box1);
		statusMsg2.update(ball2, true);

		// Delay
		try {  
			Thread.sleep(30);  
		} catch (InterruptedException e) { }

		invalidate();  // Force a re-draw
	}

	public int getOtherBallScore () {
		return ball2.numberOfHits; 
	}
	// Called back when the view is first created or its size changes.
	@Override
	public void onSizeChanged(int w, int h, int oldW, int oldH) {
		// Set the movement bounds for the ball
		box1.set(0, 0, w, h);
	}

	public void declareWinner (String name) {
		mContext.declareWinner(name);
	}

	public synchronized void  updatePositions(float sx, float sy, long timestamp) {
		//float currentX = sx;
		float currentY = sy;
		float deltaX, deltaY;
		float scalingFactor = 5.0f / ((box1.xMax > box1.yMax) ? box1.yMax : box1.xMax);
		//deltaX = currentX - previousX;
		//ball.speedX += deltaX * scalingFactor;
		//currentY += currentY * scalingFactor;

		float tmpSpeedY;
		if (timestamp != -1) {
			deltaY = currentY - previous1Y;
			tmpSpeedY = ball1.getY();
			tmpSpeedY -= deltaY;// * scalingFactor;
			ball1.setY(tmpSpeedY);
			previous1Y = currentY;

			String ycoord = null;
			Float yc = new Float (tmpSpeedY);
			ycoord = ycoord + yc.toString();
			MainActivity.sendMessage(ycoord);
			//statusMsg1.update(ball1);
		}
		else {
			//if (sy == 0.0) {
			//	declareWinner ("Him");
			//}
			//else {
				ball2.setY(sy);
				//statusMsg2.update(ball2);
			//}
			//previousX = currentX;
		}
		if ((ball1.numberOfHits - ball2.numberOfHits) >= 20) {
			String ycoord = null;
			Float yc = new Float (0);
			ycoord = ycoord + yc.toString();
			MainActivity.sendMessage(ycoord);
			declareWinner ("You");
		}
		//	if ((ball2.numberOfHits - ball1.numberOfHits) == 20) {
		//		declareWinner ("Him");

		//}

	}
}