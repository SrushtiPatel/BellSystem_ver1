package com.mt.bell;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SetDaysFragment extends Fragment {

	private static final String TAG = "SetDaysFragment";

	Bluetooth bt;
	View rootView;
	Button btnSendHoliday, btnSendHalfday;
	Spinner spHalfday, spHoliday;
	String message ="";
	private Handler bluetoothHandler;

	private StringBuilder recDataString;

	public SetDaysFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		rootView = inflater.inflate(R.layout.set_days_fragment, container, false);
		setHasOptionsMenu(false);
		init();
		return rootView;
	}


	private void init() {
		// TODO Auto-generated method stub
		bt = new Bluetooth(getActivity());
		
		createBluetoothHandler();

		spHalfday = (Spinner)rootView.findViewById(R.id.spHalfday);
		spHoliday= (Spinner)rootView.findViewById(R.id.spHoliday);
		

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice);

		adapter.addAll(getActivity().getResources().getStringArray(R.array.pref_set_day_titles));
		spHoliday.setAdapter(adapter);
		
		adapter1.addAll(getActivity().getResources().getStringArray(R.array.pref_set_day_titles));
		spHalfday.setAdapter(adapter1);
		
//		spHoliday.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				// TODO Auto-generated method stub
//				int holiday = spHoliday.getSelectedItemPosition();
//
//				if(holiday == 7){
//					holiday = 9;
//				}
//				String message = "*HOLIDAY:" + holiday + "#";
//				bt.sendMessageToBTDevice(message, 0);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
//		spHalfday.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				// TODO Auto-generated method stub
//				int halfday = spHalfday.getSelectedItemPosition();
//				
//				if(halfday == 7){
//					halfday = 9;
//				}
//				
//				String message = "*HALFDAY:" + halfday + "#";	
//				bt.sendMessageToBTDevice(message, 0);
//
//				
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		
		btnSendHoliday= (Button)rootView.findViewById(R.id.btnSendHoliday);
		btnSendHoliday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				int halfday = spHalfday.getSelectedItemPosition();
				int holiday = spHoliday.getSelectedItemPosition();

				if(holiday > -1 && holiday < 7){
//					String message = "*HALFDAY:" + halfday + "#";	
//					bt.sendMessageToBTDevice(message);

					message = "*HOLIDAY:" + holiday + "#";
//					bt.sendMessageToBTDevice(message, Utils.SET_DAYS);
//					Toast.makeText(getActivity(), "Holiday " +  holiday+" sent" , Toast.LENGTH_SHORT).show();
					createDailogbox();
				}

			}
		});
		
		btnSendHalfday = (Button)rootView.findViewById(R.id.btnSendHalfday);
		btnSendHalfday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int halfday = spHalfday.getSelectedItemPosition();
//				int holiday = spHoliday.getSelectedItemPosition();

				if(halfday > -1 && halfday < 7){
					message = "*HALFDAY:" + halfday + "#";
//					bt.sendMessageToBTDevice(message, Utils.SET_DAYS);

//					message = "*HOLIDAY:" + holiday + "#";
//					bt.sendMessageToBTDevice(message);
//					Toast.makeText(getActivity(), "Halfday= " + halfday + "sent.", Toast.LENGTH_SHORT).show();
					createDailogbox();
				}

			}
		});

	}

	private void createBluetoothHandler() {
		// TODO Auto-generated method stub
		recDataString = new StringBuilder();
		bluetoothHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				super.handleMessage(msg);
				if (msg.what == Utils.SET_DAYS) {					//if message is what we want
					String readMessage = (String) msg.obj;                    // msg.arg1 = bytes from connect thread
					recDataString.append(readMessage);      	//keep appending to string until #
//					rowString += readMessage;
//					int endOfLineIndex = recDataString.indexOf(Bluetooth.END_OF_MSG_CHAR);                    // determine the end-of-line
					int endOfStringIndex = recDataString.indexOf(Bluetooth.END_OF_MSG_CHAR);
					System.out.println("msg.obj = " + msg.obj);
					System.out.println("msg.arg1 = " + msg.arg1);
//					System.out.println("rowString = " + rowString);
					System.out.println("recDataString = " + recDataString);
					System.out.println("# index = " + endOfStringIndex);    
					
					if (endOfStringIndex >= 0) {                                           // make sure there data before ~
						String dataInPrint = recDataString.substring(0, endOfStringIndex+1);    // extract string
						System.out.println("Data Received = " + dataInPrint);
//						String responseMsg = recDataString.substring(endOfStringIndex);
						int dataLength = dataInPrint.length();							//get length of data received
						System.out.println("String Length = " + String.valueOf(dataLength));

						if (dataInPrint.charAt(0) == Bluetooth.START_OF_MSG_CHAR.toCharArray()[0])								//if it starts with # we know it is what we are looking for
						{
							//                    	String sensor0 = recDataString.substring(1, 5);             //get sensor value from string between indices 1-5       	
							//                    	String sensor1 = recDataString.substring(6, 10);            //same again...        	
							//                    	String sensor2 = recDataString.substring(11, 15);
							//                    	String sensor3 = recDataString.substring(16, 20);

							//                    	sensorView0.setText(" Sensor 0 Voltage = " + sensor0 + "V");	//update the textviews with sensor values
							//                    	sensorView1.setText(" Sensor 1 Voltage = " + sensor1 + "V");
							//                    	sensorView2.setText(" Sensor 2 Voltage = " + sensor2 + "V");
							//                    	sensorView3.setText(" Sensor 3 Voltage = " + sensor3 + "V");

							Toast.makeText(getActivity(), ""+ dataInPrint, Toast.LENGTH_SHORT).show();
							bt.REQUEST_CODE = 0;
						}
						recDataString.delete(0, recDataString.length()); 					//clear all string data 
						// strIncom =" ";
						dataInPrint = " ";
						System.out.println("String after process--"+ recDataString);
						
						
					}            
				}
			}
		};
		
		bt.setBluetoothHandler(bluetoothHandler);
		
	}
	
	public void createDailogbox() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Security");
		builder.setMessage("Enter Password:");

		// Set up the input
		final EditText input = new EditText(getActivity());
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String userInput = input.getText().toString();

				Utils utils = new Utils(getActivity());
				if(utils.varifyPassword(userInput)){
//					if(action == 1){	// action == 1 == Set
//						String pos = alarm.getSerialNo();				
//						String time = alarm.getAlarmTime();
//
//						String msg = "*" + strBellTime + alarmType + pos + ":" + time + "#";
//						Toast.makeText(SetAlarmActivity.this, msg, Toast.LENGTH_SHORT).show();
//						bt.sendMessageToBTDevice(msg, Utils.SET_ALARMS);
//
//					}else if(action == 0){	// action == 1 == reset
//						String pos = alarm.getSerialNo();				
//
//						String msg = "*RESET " + strBellTime + alarmType + " ALARM:" + pos +"#";
//						Toast.makeText(SetAlarmActivity.this, msg, Toast.LENGTH_SHORT).show();						
//						bt.sendMessageToBTDevice(msg, Utils.SET_ALARMS);
//					}
					bt.sendMessageToBTDevice(message, Utils.SET_DAYS);
				}else{
					Toast.makeText(getActivity(), "Invalid Password!!", Toast.LENGTH_SHORT).show();
				}

			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.cancel();
			}
		});

		builder.show();


	}

}
