package com.mt.bell;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class SetTimeFragment extends Fragment implements View.OnClickListener {

	LinearLayout layoutFullday, layoutHalfday;
	Button btnSetCurrentTime, btnSetBellTime;
	View rootView;
	Bluetooth bt;
	private Handler bluetoothHandler;

	private StringBuilder recDataString;

	
	public SetTimeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.set_time_fragment, container, false);
		setHasOptionsMenu(false);
		
		setLayout();
		return rootView;
	}

	public void setLayout() {
		// TODO Auto-generated method stub
		bt = new Bluetooth(getActivity());
		createBluetoothHandler();
		
		btnSetCurrentTime = (Button) rootView.findViewById(R.id.btnSetCurrentTime);
		btnSetCurrentTime.setOnClickListener(this);

		btnSetBellTime = (Button) rootView.findViewById(R.id.btnSetBellTime);
		btnSetBellTime.setOnClickListener(this);


	}
	
	private void createBluetoothHandler() {
		// TODO Auto-generated method stub
		recDataString = new StringBuilder();
		bluetoothHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				super.handleMessage(msg);
				if (msg.what == Utils.SET_CURRENT_DEVICE_TIME) {					//if message is what we want
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
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId = v.getId();

		switch (viewId) {
		case R.id.btnSetCurrentTime:
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm.dd/MM/yy", Locale.getDefault());
			Date currentDateTime = new Date();
			String dateTime = dateFormat.format(currentDateTime);
			bt.sendMessageToBTDevice("*SET TIME:" + dateTime + "#", 0);
//			Toast.makeText(getActivity(), "Current device time Sent "+dateTime, Toast.LENGTH_SHORT).show();
			break;

		case R.id.btnSetBellTime:
//			setBellTime();
			displayOptionDailog();
			break;

//		case R.id.btnEmergencyAlarm:
//
//			break;
		default:
			break;
		}
	}

	private void displayOptionDailog() {
		// TODO Auto-generated method stub
		final String options[] = {"Full Day", "Half Day"};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Make your selection");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int itemIndex) {
                // Do something with the selection
            	String alarmType="";
            	int totalAlarms = 0;
            	Intent intent = new Intent(getActivity(), SetBellTimeActivity.class);
//        		Bundle bundle = new Bundle();
        		
                if(options[itemIndex].equalsIgnoreCase("Full Day")){
                	alarmType = "FULL";
                	totalAlarms = Utils.totalFulldayAlarms;
                }else{
                	alarmType = "HALF";
                	totalAlarms = Utils.totalHalfdayAlarms;
                }
                intent.putExtra("bell_time", "BELL TIME ");
                intent.putExtra("alarm_type", alarmType);
                intent.putExtra("total_alarms", totalAlarms);
    			startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
		
	}

	private void setBellTime() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.custom_dialog);
		dialog.setTitle("Bell Time");
//		builder.setMessage("Enter Time:");

		// Set up the input
		final EditText input = (EditText) dialog.findViewById(R.id.etUserInput);
		final Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
		
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
//		dialog.setView(input);

		// Set up the buttons
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String bellTime = input.getText().toString();
				
				if(bellTime.length() > 0 ){ 
					int value = Integer.valueOf(bellTime);
					if(value < 0 || value > 99){
						Toast.makeText(getActivity(), "Enter time value between 0-99. ", Toast.LENGTH_SHORT).show();
						input.setError("Time must be between 0-99.");
						input.requestFocus();
					}else{
						bellTime = String.valueOf(value);
						while(bellTime.length() < 3){
							bellTime = "0" + bellTime;
						}
						String msg = "*BELL TIME:" + bellTime + "#";
						bt.sendMessageToBTDevice(msg, 0);
						Toast.makeText(getActivity(), "Bell Time Sent "+bellTime, Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}						
				}else{
//					Toast.makeText(getActivity(), "Enter valid time. ", Toast.LENGTH_SHORT).show();
					input.setError("Invalid time.");
					input.requestFocus();
				}
				

			}
		}); 
		
		dialog.show();

	}

}
