package com.high_jump;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {


	public class AccHandler extends Thread {
		public BouncingBall mbb;
		public BouncingBallView mView;

		public AccHandler(BouncingBallView view) {
			mView = view;

		}

		public void run () {
			mbb.startSensing(view);
			/*
			while (true) {
				MainActivity.sendMessage("TEST");
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 */
		}
	}


	static private TextToSpeech mTts;
	private AccHandler mAccHandler; 
	BouncingBallView view;
	static Boolean mFirstTime;
	static private LinearLayout container;

	//public static Receiver mReceiver;
	//public static Sender mSender;
	//private String IP1;
	//private String IP2;
	// Local Bluetooth adapter
	private static BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private static BluetoothChatService mChatService = null;
	private String tag;

	static MainActivity mContext, mOldContext;

	// This code can be any value you want, its just a checksum.
	private static final int MY_DATA_CHECK_CODE = 1234;
	public static Boolean canSend;

	/** Called when the activity is first created. */
	OnClickListener radio_listener = new OnClickListener() {
		public void onClick (View v) {
			initialize (v);
			//sendMessage ("HELLO");
		}
	};

	private void initialize (View v) {
		setContentView(R.layout.bouncing_ball);
		container = (LinearLayout) findViewById(R.id.container);
		container.addView(view);

		mAccHandler.mbb.mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccHandler.start();
	}
	/*
	public void InitSender ()
	{	
		try {
			if(mSender== null) mSender = new Sender ();
			mSender.init(InetAddress.getByName(IP1));
			canSend = true;
			Log.d(tag, "InitSender:CanSend true");

		} catch (UnknownHostException e) {
			canSend = false;
			// TODO Auto-generated catch block
			String msg = IP1 + " host not known";
			Log.e(tag, msg);
			e.printStackTrace();
		} catch (Exception e) {
			canSend =false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 */
	/*
	public void customConnect () {
		//CHANGE
		// Get the BLuetoothDevice object	
		//	F4:FC:32:78:CA:EE
		String selfMac=mBluetoothAdapter.getAddress();
		BluetoothDevice device;
		if (selfMac != "F4:FC:32:72:90:65") {
			device = mBluetoothAdapter.getRemoteDevice("F4:FC:32:72:90:65");
		}else {
		    device = mBluetoothAdapter.getRemoteDevice("F4:FC:32:78:CA:EE");
		}

		// Attempt to connect to the device
		mChatService.connect(device);	
	}
	 */

	public void customConnect () {
		//CHANGE
		// Get the BLuetoothDevice object						//	F4:FC:32:78:CA:EE
		//BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("F4:FC:32:72:90:65");
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("F4:FC:32:78:CA:EE");
		// Attempt to connect to the device
		mChatService.connect(device);	
	}

	public static void sendMessage (String message) {
		byte[] send = message.getBytes();
		mChatService.write(send);
		//Log.d("MESS SENT : ", message);
	}
	public static void handleMessage(String msg) {
		Log.d("HANDLE MESS : ", msg);

		String breakup[] = msg.split("null");
		System.out.println("BREAKUPS\n");
		for (int i = 0; i < breakup.length; i++)
		{
			String newMsg;
			Log.d("Breakup : ", breakup[i]);
			/*

			if (breakup[i].contains("-")){
				newMsg = breakup[i].replace("null-", "-");
			}
			else {
				newMsg = breakup[i].replace("null", "0");
			}
			Log.d("HANDLE MESS : ", newMsg);
			 */
			Float yc;
			try {
				yc  = new Float (breakup[i]);
			}catch (Exception e){
				Log.e("Breakup ERROR : ", breakup[i]);
				continue;
			}
			try {
				if (mContext != null) { 
					//if (yc == new Float(0)) {
					//	mContext.declareWinner("Him");
					//}
					//else {
						mContext.mAccHandler.mView.updatePositions(0, yc, -1);
					//}
				}
			}catch (Exception e) {
				Log.e ("HANDLE MESS : ", "formatting error ....");
				
			}
		}
	}

	public static void readMessage (byte[] readBuf, int len){
		// construct a string from the valid bytes in the buffer
		String readMessage = new String(readBuf, 0, len);
		Log.d("MESS READ : ", readMessage);
		handleMessage(readMessage);
	}

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);

		setContentView (R.layout.main);
		view = new BouncingBallView (this);
		mAccHandler = new AccHandler (view);
		mAccHandler.mbb = new BouncingBall ();

		mContext = this;
		canSend = false;

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE); 

		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothChatService(this, null);
		mChatService.start();
		customConnect ();
		/*
		IP1 = "10.0.9.122";//122//154
		IP2 = "10.0.9.122";//122//154
		tag = "Main Activity";

		if (mReceiver == null) {
			mReceiver = new Receiver (view);

		}
		 */
		final RadioButton radio_me = (RadioButton) findViewById (R.id.radioButton1);
		radio_me.setOnClickListener (radio_listener);
	}

	public void onInit(int i)
	{
		if (mTts != null ){
			mTts.speak("Please select a game",
					TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
					null);
		}
	}

	/**
	 * This is the callback from the TTS engine check, if a TTS is installed we
	 * create a new TTS instance (which in turn calls onInit), if not then we will
	 * create an intent to go off and install a TTS engine
	 * @param requestCode int Request code returned from the check for TTS engine.
	 * @param resultCode int Result code returned from the check for TTS engine.
	 * @param data Intent Intent returned from the TTS check.
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == MY_DATA_CHECK_CODE)
		{
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
			{
				mTts = new TextToSpeech(this, this);
				view.setTts (mTts);
			}
			else
			{
				// missing data, install it
				mTts = null;
				Log.e(tag, "TTS not present");
			}
		}
	}


	/**
	 * Be kind, once you've finished with the TTS engine, shut it down so other
	 * applications can use it without us interfering with it :)
	 */

	@Override
	public void onDestroy()
	{
		// Don't forget to shutdown!
		if (mTts != null)
		{
			mTts.stop();
			mTts.shutdown();
		}
		//unregisterReceiver(mReceiver);
		//container.removeAllViews();
		container.removeAllViews();
		mAccHandler.mbb.stopSensing ();
		mChatService.stop();
		declareWinner ("You");
		super.onDestroy();
	} 

	void declareWinner (String playerName) {
		mAccHandler.mbb.stopSensing ();
		mChatService.stop();
		container.removeView(mAccHandler.mView);
		container.refreshDrawableState();
		
		if (mTts != null ){
			mTts.speak("Nice Job",
					TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
					null);
		}
		setContentView(R.layout.youwinner);
	}
}