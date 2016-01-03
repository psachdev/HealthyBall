package com.high_jump;

import java.util.Formatter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
 
public class StatusMessage {
   // Status message to show Ball's (x,y) position and speed.
   private StringBuilder statusMsg = new StringBuilder();
   private Formatter formatter = new Formatter(statusMsg);
   private Paint paint;
   private int offset;
   private String playerName;
   
   // Constructor
   public StatusMessage(int color, int off, String name) {
      paint = new Paint();
      // Set the font face and size of drawing text
      paint.setTypeface(Typeface.MONOSPACE);
      paint.setTextSize(16);
      paint.setColor(color);
      offset = off;
      playerName = name;
   }
  
   public void update(Ball ball, boolean second) {
      // Build status message
      statusMsg.delete(0, statusMsg.length());   // Empty buffer
      //formatter.format("Ball@(%3.0f,%3.0f),Speed=(%2.0f,%2.0f)", ball.x, ball.y,
        //    ball.speedX, ball.speedY);
      if (second == false){
          formatter.format("Score = %d", ball.numberOfHits);
      }
      else {
    	  formatter.format("Your Aim > %d", ball.numberOfHits + 20);
      }
  }
   
   public void draw(Canvas canvas) {
      canvas.drawText(statusMsg.toString(), offset + 10, 30, paint);
   }
}
