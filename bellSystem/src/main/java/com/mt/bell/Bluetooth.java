package com.mt.bell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Bluetooth{

	final int handlerState = 0;        				 //used to identify handler message

	Handler bluetoothHandler;
	
	private static BluetoothAdapter btAdapter = null;
	private static BluetoothSocket btSocket = null;
	private ConnectedThread mConnectedThread;
	public static final String START_OF_MSG_CHAR = "*";
	public static final String END_OF_MSG_CHAR = "#";
	
	// SPP UUID service - this should work for most devices
	private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// String for MAC address
	private static String address;
	int REQUEST_CODE=0;
	Context context;

	public Bluetooth(Context ctx){
		context = ctx;
	}
	public void setBluetoothHandler(Handler bluetoothHandler) {
		this.bluetoothHandler = bluetoothHandler;
	}

	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		Bluetooth.address = address;
		try 
		{
			if(btSocket != null){
				btSocket.close();
				btSocket = null;
//				btAdapter.disable();
//				btAdapter = null;
				
			}
		} catch (IOException e2) 
		{
			System.out.println("BluetootbhSocket Close: "+e2.getMessage());
			//insert code to deal with this 
		}
	}


	public BluetoothAdapter getBtAdapter() {
		return btAdapter;
	}



	public void setBtAdapter(BluetoothAdapter btAdapter) {
		Bluetooth.btAdapter = btAdapter;
	}



	public BluetoothSocket getBtSocket() {
		return btSocket;
	}



	public void setBtSocket(BluetoothSocket btSocket) {
		Bluetooth.btSocket = btSocket;
	}

	public boolean isBluetoothConnected(){
		if(btSocket != null && btSocket.isConnected()){
			return true;
		}else{
			return false;
		}
	}

	public void createBluetoothSocketConnection() {
		// TODO Auto-generated method stub
		//Get MAC address from DeviceListActivity via intent

		//create device and set the MAC address
		BluetoothDevice device = btAdapter.getRemoteDevice(address);

		try {
			if(btSocket == null){	// connect to device only if it is null.
				btSocket = createBluetoothSocket(device);
			}
		} catch (IOException e) {
			//			Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
		}
		// Establish the Bluetooth socket connection.
		try 
		{
			btSocket.connect();
		} catch (IOException e) {
			System.out.println("BluetootbhSocket Connection: "+e.getMessage());
			try 
			{
				btSocket.close();
			} catch (IOException e2) 
			{
				System.out.println("BluetootbhSocket Close: "+e2.getMessage());
				//insert code to deal with this 
			}
		} 
//		mConnectedThread = new ConnectedThread(btSocket);
//		mConnectedThread.start();

		//I send a character when resuming.beginning transmission to check device is connected
		//If it is not an exception will be thrown in the write method and finish() will be called
		//		mConnectedThread.write("x");
	}

	private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

		return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
		//creates secure outgoing connection with BT device using UUID
	}

	public void connectToDevice(String address) {
		setAddress(address);
		createBluetoothSocketConnection();
//		mConnectedThread = new ConnectedThread(btSocket);
//		mConnectedThread.start();
	}
	
	public void startConnectionThread() {
		mConnectedThread = new ConnectedThread(btSocket);
		mConnectedThread.start();
	}
	
	public void sendMessageToBTDevice(String message, int requestCode) {
		REQUEST_CODE = requestCode;
		if(isBluetoothConnected()) {
			try {
				if (mConnectedThread == null || !mConnectedThread.isAlive()) {
					startConnectionThread();
				}
//		startConnectionThread();
				mConnectedThread.write(message);
//		mConnectedThread.destroy();
			} catch (Exception e) {
				Log.e(getClass().getName(), "Exception @sendMessageToBTDevice(): " + e.getMessage());
				REQUEST_CODE = 0;
			}
		}else {
			Utils.showToast(context, "Your device is not connected.");
		}
	}
	
	public void resetBuffer() {
		try {
			mConnectedThread.resetBuffer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	//create new class for connect thread
	class ConnectedThread extends Thread {
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		String rowString;

		//creation of the connect thread
		public ConnectedThread(BluetoothSocket socket) {
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			try {
				//Create I/O streams for connection
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) { }

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}


		public void run() {
			int bytes; 

			// Keep looping to listen for received messages
			while (true) {
				try {
					int available = mmInStream.available();
					if(available > 0){
						byte[] buffer = new byte[available];  
						
					bytes = mmInStream.read(buffer);        	//read bytes from input buffer
					String readMessage = new String(buffer, 0, bytes);
//					rowString = readMessage;
					System.out.println("Incoming String= "+ readMessage);
//					// Send the obtained bytes to the UI Activity via handler
					Message msg = bluetoothHandler.obtainMessage(REQUEST_CODE, bytes, -1, readMessage);
//					bluetoothHandler.obtainMessage(Utils.READ_FULLDAY_ALARM, readMessage).sendToTarget();
//					bluetoothHandler.obtainMessage(Utils.READ_FULLDAY_ALARM, bytes, -1, buffer).sendToTarget();
//					Message msg = new Message();
//					msg.obj = bytes;
//					msg.what = bluetoothHandler.obtainMessage().what;
					bluetoothHandler.sendMessage(msg);
//					bluetoothHandler.removeMessages(REQUEST_CODE);
					
					}
					
				} catch (IOException e) {
					break;
				}
			}
		}
		
		public void resetBuffer() {
			try {
				mmInStream.reset();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		
		//write method
		public void write(String input) {
			byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
			try {
				mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
				Log.v(this.getName(), "Message to Device: "+ input);
			} catch (IOException e) {  
				//if you cannot write, close the application
				Log.e(this.getName(), e.getMessage());
				//				Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
				//				finish();

			}
		}
	}

}