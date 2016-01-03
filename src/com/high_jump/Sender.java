package com.high_jump;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class Sender {
	private Socket socket;
	private String tag;
	private PrintWriter out;

	private static final int SERVERPORT = 6000;

	public Sender () {}

	public void init (InetAddress ip) throws IOException {
		
		//InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
		socket = new Socket(ip, SERVERPORT);
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
	}

	public  void sendMessage (String str) {
		try {
			out.println(str);
			//String msg = "Client Sent - " + str;
			//Log.d("Client", msg);
		} catch (Exception e) {
			//Log.d(tag, "Unable to send");
			//e.printStackTrace();
		}
	}
}

