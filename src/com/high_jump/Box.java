package com.high_jump;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.speech.tts.TextToSpeech;
import android.util.Log;
  
public class Box {
   int xMin, xMax, yMin, yMax;
   private Paint paint;  // paint style and color
   private Rect bounds;

   private TextToSpeech mTts;
   
   public Box(int color) {
      paint = new Paint();
      paint.setColor(color);
      bounds = new Rect();
 
      Log.d("BOX - bottom", new Integer(bounds.bottom).toString());
      Log.d("BOX - left ", new Integer(bounds.left).toString());
      Log.d("BOX - right ", new Integer (bounds.right).toString() );
      Log.d("BOX - top ", new Integer (bounds.top).toString() );
    }
  
   public void set(int x, int y, int width, int height) {
      xMin = x;
      xMax = x + width - 1;
      yMin = y;
      yMax = y + height - 1;
      // The box's bounds do not change unless the view's size changes
      bounds.set(xMin, yMin, xMax, yMax);
   }
   
   public void draw(Canvas canvas) {
      canvas.drawRect(bounds, paint);
   }
}
