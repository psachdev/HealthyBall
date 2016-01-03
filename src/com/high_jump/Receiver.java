package com.high_jump;

import android.app.Activity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class Receiver {
	ServerSocket ss = null;
	String mClientMsg = "";
	Thread myCommsThread = null;
	String tag;
	//protected static final int MSG_ID = 0x1337;
	public static final int SERVERPORT = 6000;
	public BouncingBallView mView;
	
	public Receiver (BouncingBallView view) {
		tag = "Receiver : ";
		mView = view;
		this.myCommsThread = new Thread(new CommsThread());
		this.myCommsThread.start();
	}

	public void handleMessage(String msg) {
			Log.d(tag, msg);
			String newMsg;
			if (msg.contains("-")){
				newMsg = msg.replace("null-", "-");
			}
			else {
			newMsg = msg.replace("null", "0");
			}
			Log.d(tag, newMsg);
			Float yc  = new Float (newMsg);
			try {
				mView.updatePositions(0, yc.floatValue(), -1);
			}catch (Exception e) {
				Log.e (tag, "formatting error ....");
			}
		}
	
	class CommsThread implements Runnable {
		public void run() {
			Socket s = null;
			try {
				ss = new ServerSocket(SERVERPORT );
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (true) {
				try {
					if (s == null)
						s = ss.accept();
						//if (MainActivity.mSender == null) {
						//	MainActivity.mSender = new Sender ();
						//	MainActivity.mSender.init(s.getInetAddress());
						//}
					BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
					handleMessage(input.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
