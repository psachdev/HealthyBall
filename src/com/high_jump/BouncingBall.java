package com.high_jump;


// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.speech.tts.TextToSpeech;

public class BouncingBall implements SensorEventListener  {

	BouncingBallView mbb;
	
	float m_f__acc_x;
	float m_f__acc_y;
	float m_f__acc_z;
	
	private long mSensorTimeStamp;
	public SensorManager mSensorManager;
	private Sensor mAccelerometer;
		
	//BluetoothChat   mBluetoothChat;
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	
	public BouncingBall () {}
	
	public void stopSensing () {
		mSensorManager.unregisterListener(this);
	}
	public void startSensing (BouncingBallView bb) {
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
		
		mbb = bb;
    }
	
    // SensorListner Interface - Start
	public void onSensorChanged(SensorEvent event) {
		if( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER )
		{
			m_f__acc_x = event.values[0];
			m_f__acc_y = event.values[1];
			m_f__acc_z = event.values[2];

			// Obtain MotionEvent object
			//long downTime = SystemClock.uptimeMillis();
			//long eventTime = SystemClock.uptimeMillis() + 100;
			float x = m_f__acc_x;
			float y = m_f__acc_y;
            mSensorTimeStamp = event.timestamp;
            mbb.updatePositions(x, m_f__acc_z, mSensorTimeStamp);
		}		
	}

	//@Override
	protected void onStop()
	{
		// unregister listener
		mSensorManager.unregisterListener(this);
		return;
	} //method
}
	