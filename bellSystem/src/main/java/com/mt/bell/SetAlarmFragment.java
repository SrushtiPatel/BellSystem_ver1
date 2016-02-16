package com.mt.bell;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class SetAlarmFragment extends Fragment implements View.OnClickListener {

	LinearLayout layoutFullday, layoutHalfday;
	Button btnFulldayAlarm, btnHalfdayAlarm, btnEmergencyAlarm;
	View rootView;
	ListView listFulldayAlarm, listHalfdayAlarm;
//	final int totalFulldayAlarms = 30;
//	final int totalHalfdayAlarms = 10;

	private Handler bluetoothHandler;

	private StringBuilder recDataString;
	private Bluetooth bt;

	public SetAlarmFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.set_alarm_fragment, container, false);

		setLayout();
		// createDialog();
		return rootView;
	}

//	private void createDialog() {
//		// TODO Auto-generated method stub
//		AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
//		builderSingle.setIcon(R.drawable.ic_launcher);
//		builderSingle.setTitle("Select One Name:-");
//
//		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
//				android.R.layout.select_dialog_singlechoice);
//		arrayAdapter.add("Hardik");
//		arrayAdapter.add("Archit");
//		arrayAdapter.add("Jignesh");
//		arrayAdapter.add("Umang");
//		arrayAdapter.add("Gatti");
//
//		builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//
//		builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				String strName = arrayAdapter.getItem(which);
//				AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
//				builderInner.setMessage(strName);
//				builderInner.setTitle("Your Selected Item is");
//				builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				});
//				builderInner.show();
//			}
//		});
//		builderSingle.show();
//	}

	public void setLayout() {
		// TODO Auto-generated method stub
		btnFulldayAlarm = (Button) rootView.findViewById(R.id.btnFulldayAlarm);
		btnFulldayAlarm.setOnClickListener(this);

		btnHalfdayAlarm = (Button) rootView.findViewById(R.id.btnHalfdayAlarm);
		btnHalfdayAlarm.setOnClickListener(this);
		
		btnEmergencyAlarm = (Button) rootView.findViewById(R.id.btnEmergencyAlarm);
		btnEmergencyAlarm.setOnClickListener(this);
		
		bt = new Bluetooth(getActivity());
		createBluetoothHandler();
		
		
//		listFulldayAlarm = (ListView) rootView.findViewById(R.id.listFullDayAlarm);
//		listHalfdayAlarm = (ListView) rootView.findViewById(R.id.listHalfDayAlarm);

		// layoutFullday =
		// (LinearLayout)rootView.findViewById(R.id.layoutFullday);
		//
		// LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT,
		// 1);
		// for(int i= 1; i <= 10; i++){
		// LinearLayout layoutHorizontal = new LinearLayout(getActivity());
		// layoutHorizontal.setLayoutParams(new
		// LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		// layoutHorizontal.setTag("fullday_alarm"+i);
		// layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
		// TextView textView = new TextView(getActivity());
		// textView.setLayoutParams(params);
		// textView.setText("Alarm"+i);
		//
		// EditText editText = new EditText(getActivity());
		// editText.setLayoutParams(params);
		//
		// layoutHorizontal.addView(textView,0);
		// layoutHorizontal.addView(editText,1);
		//
		// layoutFullday.addView(layoutHorizontal);
		//
		// }
		//
		// layoutHalfday =
		// (LinearLayout)rootView.findViewById(R.id.layoutHalfday);
		// for(int i= 1; i <= 5; i++){
		// LinearLayout layoutHorizontal = new LinearLayout(getActivity());
		// layoutHorizontal.setLayoutParams(new
		// LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		// layoutHorizontal.setTag("halfday_alarm"+i);
		// layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
		// TextView textView = new TextView(getActivity());
		// textView.setLayoutParams(params);
		// textView.setText("Alarm"+i);
		//
		// EditText editText = new EditText(getActivity());
		// editText.setLayoutParams(params);
		//
		// layoutHorizontal.addView(textView,0);
		// layoutHorizontal.addView(editText,1);
		//
		// layoutHalfday.addView(layoutHorizontal);
		//
		// }
	}

	private void createBluetoothHandler() {
		// TODO Auto-generated method stub
		recDataString = new StringBuilder();
		bluetoothHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				super.handleMessage(msg);
				if (msg.what == Utils.SET_EMERGENCY_ALARM) {					//if message is what we want
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
		Intent intent = new Intent(getActivity(), SetAlarmActivity.class);
		Bundle bundle = new Bundle();
		
		switch (viewId) {
		case R.id.btnFulldayAlarm:
			// toggleView(layoutFullday);
//			bundle.putInt("total_alarms", totalFulldayAlarms);
			intent.putExtra("total_alarms", Utils.totalFulldayAlarms);
//			bundle.putString("alarm_type", "Fullday");
			intent.putExtra("alarm_type", "FULL");
			startActivity(intent);
			break;

		case R.id.btnHalfdayAlarm:
			// toggleView(layoutHalfday);

//			bundle.putInt("total_alarms", totalFulldayAlarms);
			intent.putExtra("total_alarms", Utils.totalHalfdayAlarms);
//			bundle.putString("alarm_type", "Halfday");
			intent.putExtra("alarm_type", "HALF");
			startActivity(intent);

			break;
			
		case R.id.btnEmergencyAlarm:
//			sendEmergencyAlarm();
			Bluetooth bt = new Bluetooth(getActivity());
			bt.sendMessageToBTDevice("*EMERGENCY ALARM#",Utils.SET_EMERGENCY_ALARM);
//			Toast.makeText(getActivity(), "Emergency Alarm sent", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	private void sendEmergencyAlarm() {
		// TODO Auto-generated method stub
		final Bluetooth bt = new Bluetooth(getActivity());
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.custom_dialog);
		dialog.setTitle("Emergency Alarm Time");
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

	private void toggleView(View view) {
		int visibility = (view.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE;
		view.setVisibility(visibility);
		// TODO Auto-generated method stub

	}

}
