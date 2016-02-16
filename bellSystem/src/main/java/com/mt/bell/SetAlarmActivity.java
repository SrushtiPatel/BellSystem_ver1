package com.mt.bell;

import java.lang.Override;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SetAlarmActivity extends AppCompatActivity {

	private ArrayList<AlarmBell> alarms;
	private CustomArrayAdapter adapter;
	String alarmType;
	ListView list;
	Bluetooth bt;
	final int handlerState = 0;        				 //used to identify handler message
	static Handler bluetoothHandler;
	StringBuilder recDataString;
	String rowString ="";
	String strBellTime="";
	int totalAlarms;
	public final int INPUT_TYPE_ALARM = 0;
	public final int INPUT_TYPE_BELL = 1;
	private int pHour;
	private int pMinute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_activity);

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		toolbar.setTitle(getResources().getString(R.string.title_activity_belltime));
		setSupportActionBar(toolbar);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		Intent bundle = getIntent();
		if(bundle != null){
			alarmType = bundle.getStringExtra("alarm_type");

			totalAlarms = bundle.getIntExtra("total_alarms", 0);

			if(bundle.hasExtra("bell_time")){
				strBellTime = bundle.getStringExtra("bell_time"); 
			}
		}
		setAlarmView();


	}

	@Override
	public void onResume(){
		super.onResume();

		setAlarmView();

		createBluetoothHandler();

		bt.setBluetoothHandler(bluetoothHandler);
	}

	private void setAlarmView() {
		// TODO Auto-generated method stub
		alarms = new ArrayList<AlarmBell>();

		for(int i=0; i< totalAlarms; i++){
			AlarmBell alarm = new AlarmBell();
			if(String.valueOf(i).length() < 2){
				alarm.setSerialNo("0"+i);
			}else{
				alarm.setSerialNo(i+"");
			}
			alarm.setAlarmLabel("Alarm "+ String.valueOf(i+1));
			alarms.add(alarm);
		}
		list = (ListView)findViewById(R.id.list);

		adapter = new CustomArrayAdapter(this, alarms);

		list.setAdapter(adapter);
		bt = new Bluetooth(SetAlarmActivity.this);

	}

	private void createBluetoothHandler() {
		// TODO Auto-generated method stub
		recDataString = new StringBuilder();
		bluetoothHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				super.handleMessage(msg);

				String readMessage = "";
				int endOfStringIndex =0;
				switch (msg.what) {
				case Utils.READ_FULLDAY_ALARM :				//if message is what we want
					readMessage = (String) msg.obj;                    // msg.arg1 = bytes from connect thread
					recDataString.append(readMessage);      	//keep appending to string until #
					//					rowString += readMessage;
					//					int endOfLineIndex = recDataString.indexOf(Bluetooth.END_OF_MSG_CHAR);                    // determine the end-of-line
					endOfStringIndex = recDataString.indexOf(Bluetooth.END_OF_MSG_CHAR);
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

							//							rowString = recDataString.toString();
							rowString = dataInPrint;
							Toast.makeText(SetAlarmActivity.this, "Read String-> "+ dataInPrint, Toast.LENGTH_SHORT).show();
							//							Toast.makeText(SetAlarmActivity.this, "Response Message-> "+ responseMsg, Toast.LENGTH_SHORT).show();
							//							parseReadAlarmString(dataInPrint);
							//							bt.resetBuffer();
							//							bluetoothHandler.post(updateRunnable);
						}
						recDataString.delete(0, recDataString.length()); 					//clear all string data 
						// strIncom =" ";
						dataInPrint = " ";
						rowString = "";
						System.out.println("String after process--"+ recDataString);


					}            
					break;

				case Utils.SET_ALARMS:

					readMessage = (String) msg.obj;                    // msg.arg1 = bytes from connect thread
					recDataString.append(readMessage);      	//keep appending to string until #
					//					rowString += readMessage;
					//					int endOfLineIndex = recDataString.indexOf(Bluetooth.END_OF_MSG_CHAR);                    // determine the end-of-line
					endOfStringIndex = recDataString.indexOf(Bluetooth.END_OF_MSG_CHAR);
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
							rowString = dataInPrint;
							Toast.makeText(SetAlarmActivity.this, "Response-> "+ dataInPrint, Toast.LENGTH_SHORT).show();
							//							Toast.makeText(SetAlarmActivity.this, "Response Message-> "+ responseMsg, Toast.LENGTH_SHORT).show();
							//							parseReadAlarmString(dataInPrint);
							//							bt.resetBuffer();
							//							bluetoothHandler.post(updateRunnable);
						}
						recDataString.delete(0, recDataString.length()); 					//clear all string data 
						// strIncom =" ";
						dataInPrint = " ";
						rowString = "";
						System.out.println("String after process--"+ recDataString);

					}
					break;

				default:
					break;
				}
			}
		};

	}

	final Runnable updateRunnable = new Runnable() {
		public void run() {
			//call the activity method that updates the UI
			parseReadAlarmString(rowString);
		}
	};


	public class CustomArrayAdapter extends ArrayAdapter<AlarmBell> {
		private final Context context;
		private final ArrayList<AlarmBell> values;
		//		private ArrayList<String> alarmTimes;
		//		  customButtonListener customListner;  

		public CustomArrayAdapter(Context context, ArrayList<AlarmBell> values) {
			super(context, R.layout.custom_list_dialog, values);
			this.context = context;
			this.values = values;
			//			this.alarmTimes = alarmTime;
		}

		//		    public interface customButtonListener {  
		//		        public void onButtonClickListner(int position,String value);  
		//		    }
		//		    
		//		    public void setCustomButtonListner(customButtonListener listener) {  
		//		        this.customListner = listener;  
		//		    }  

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;  
			if (convertView == null) {  
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.custom_list_dialog, null);
				viewHolder = new ViewHolder();
				viewHolder.position = (TextView)  convertView.findViewById(R.id.tvPosition);
				viewHolder.alarmLable = (TextView) convertView.findViewById(R.id.tvAlarmLable);  
				viewHolder.alarmTime = (TextView) convertView.findViewById(R.id.etAlarmTime);  
				viewHolder.bellTime = (TextView) convertView.findViewById(R.id.etBellTime);
//				viewHolder.alarmSet = (Button) convertView.findViewById(R.id.btnSet);
//				viewHolder.alarmReset = (Button) convertView.findViewById(R.id.btnReset);

				convertView.setTag(viewHolder);

			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}

			AlarmBell alarm = getItem(position); 

			viewHolder.position.setText(alarm.getSerialNo());
			viewHolder.alarmLable.setText(alarm.getAlarmLabel());

			viewHolder.alarmTime.setText(alarm.getAlarmTime());
			viewHolder.alarmTime.setTag(alarm);

//			viewHolder.alarmSet.setTag(alarm);
//			viewHolder.alarmReset.setTag(alarm);

			viewHolder.alarmTime.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlarmBell alarm = (AlarmBell) viewHolder.alarmTime.getTag();
					createInputDialog(alarm);
//					createInputDialog(alarm, INPUT_TYPE_ALARM);
//					createTimePickerDialog(alarm);
//					displayBelltimeInputDialog(alarm);

				}
			});

			viewHolder.bellTime.setText(alarm.getBellTime());
			viewHolder.bellTime.setTag(alarm);

//			viewHolder.alarmSet.setTag(alarm);
//			viewHolder.alarmReset.setTag(alarm);

			viewHolder.bellTime.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlarmBell alarm = (AlarmBell) viewHolder.alarmTime.getTag();
//					createInputDialog(alarm, INPUT_TYPE_BELL);
//					createTimePickerDialog(alarm);
					displayBelltimeInputDialog(alarm);

				}
			});

			//			viewHolder.alarmTime.addTextChangedListener(new TextWatcher() {
			//
			//				@Override
			//				public void onTextChanged(CharSequence s, int start, int before, int count) {
			//					// TODO Auto-generated method stub
			//
			//				}
			//
			//				@Override
			//				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			//					// TODO Auto-generated method stub
			//
			//				}
			//
			//				@Override
			//				public void afterTextChanged(Editable s) {
			//					// TODO Auto-generated method stub
			//					if(s != null){ 
			//						AlarmBell alarm = (AlarmBell) viewHolder.alarmTime.getTag();
			//						String str = s.toString();
			//						alarm.setAlarmTime(str);
			//					}
			//				}
			//			});

//			viewHolder.alarmSet.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					//		                if (customListner != null) {
//					//		                    customListner.onButtonClickListner(position,temp);
//					//		                }
//
//					AlarmBell alarm = (AlarmBell) viewHolder.alarmSet.getTag();
//
//					viewHolder.alarmTime.setText(alarm.getAlarmTime());
//					createPasswordDailogbox(alarm, 1);
//				}
//			});
//
//			viewHolder.alarmReset.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					//		                if (customListner != null) {
//					//		                    customListner.onButtonClickListner(position,temp);
//					//		                }
//					AlarmBell alarm = (AlarmBell) viewHolder.alarmReset.getTag();
//
//					viewHolder.alarmTime.setText(alarm.getAlarmTime());
//
//					createPasswordDailogbox(alarm, 0);
//
//				}
//			});

			return convertView;  
		}  


		public class ViewHolder {  
			TextView position;
			TextView alarmLable;
			TextView alarmTime;
			TextView bellTime;
			Button alarmSet;
			Button alarmReset;
		}

	}

	public void createInputDialog(final AlarmBell alarm){
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.custom_time_dialog);
//		if(type == INPUT_TYPE_ALARM){
			dialog.setTitle("Alarm Time");
//		}else{
//			dialog.setTitle("Bell Time");
//		}
//		builder.setMessage("Enter Time:");

		// Set up the input
		final EditText input = (EditText) dialog.findViewById(R.id.etUserInput);
		final Button btnSet = (Button) dialog.findViewById(R.id.btnSet);
		final Button btnReset = (Button) dialog.findViewById(R.id.btnReset);

		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
//		dialog.setView(input);

		input.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				if(type == INPUT_TYPE_ALARM){
					createTimePickerDialog(input);
//				}
//				else{
//					displayBelltimeInputDialog(alarm);
//				}
			}
		});
		// Set up the buttons
		btnSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if(type == INPUT_TYPE_ALARM){
					alarm.setAlarmTime(input.getText().toString());
//				}else{
//					alarm.setBellTime(input.getText().toString());
//				}

//				bt.sendMessageToBTDevice("", REQ);
			}
		});

		btnReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		dialog.show();
	}


//	public void createTimePickerDialog(final AlarmBell alarm) {
	public void createTimePickerDialog(final EditText input) {
		// TODO Auto-generated method stub
		final Calendar cal = Calendar.getInstance();
		pHour = cal.get(Calendar.HOUR_OF_DAY);
		pMinute = cal.get(Calendar.MINUTE);

		TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				pHour = hourOfDay;
				pMinute = minute;

				String hour = pHour+"";
				String sMinute = pMinute+"";
				if(hour.length() < 2){
					hour = "0" + hour;
				}
				if(sMinute.length() < 2){
					sMinute = "0" + sMinute;
				}

				input.setText(hour + ":" + sMinute);
//				alarm.setAlarmTime(hour + ":" + sMinute);
//				adapter.notifyDataSetChanged();
			}
		};

		TimePickerDialog timePickerDialog = new TimePickerDialog(SetAlarmActivity.this, timeSetListener, pHour, pMinute, true);
		timePickerDialog.show();
	}

	private void displayBelltimeInputDialog(final AlarmBell alarm) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.custom_time_dialog);
		dialog.setTitle("Bell Time");
//		builder.setMessage("Enter Time:");

		// Set up the input
		final EditText input = (EditText) dialog.findViewById(R.id.etUserInput);
		final Button btnSet = (Button) dialog.findViewById(R.id.btnSet);
		final Button btnReset = (Button) dialog.findViewById(R.id.btnReset);

		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		int maxLength = 2;
		InputFilter[] fArray = new InputFilter[1];
		fArray[0] = new InputFilter.LengthFilter(maxLength);
		input.setFilters(fArray);
//		dialog.setView(input);

		// Set up the buttons
		btnSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(isValidBellTime(input)){
					dialog.dismiss();
				}

			}
		});

		btnReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				bt.sendMessageToBTDevice();

			}
		});

		dialog.show();

	}

	private boolean isValidBellTime(EditText input) {
		String bellTime = input.getText().toString();
		boolean isValid = false;

		if (bellTime.length() > 0) {
			int value = Integer.valueOf(bellTime);
			if (value < 0 || value > 99) {
				Toast.makeText(SetAlarmActivity.this, "Enter time value between 0-99. ", Toast.LENGTH_SHORT).show();
				input.setError("Time must be between 0-99.");
				input.requestFocus();
			} else {
				bellTime = String.valueOf(value);
				while (bellTime.length() < 2) {
					bellTime = "0" + bellTime;
				}

//						alarm.setAlarmTime(bellTime);
//						String msg = "*BELL TIME:" + bellTime + "#";
//						bt.sendMessageToBTDevice(msg);
//						Toast.makeText(SetBellTimeActivity.this, "Bell Time Sent "+bellTime, Toast.LENGTH_SHORT).show();

				isValid = true;
			}
		} else {
//					Toast.makeText(getActivity(), "Enter valid time. ", Toast.LENGTH_SHORT).show();
			input.setError("Enter bell time.");
			input.requestFocus();
		}

		return isValid;

	}

	public void createPasswordDailogbox(final AlarmBell alarm, final int action) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Security");
		builder.setMessage("Enter Password:");

		// Set up the input
		final EditText input = new EditText(SetAlarmActivity.this);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String userInput = input.getText().toString();

				Utils utils = new Utils(SetAlarmActivity.this);
				if(utils.varifyPassword(userInput)){
					if(action == 1){	// action == 1 == Set
						String pos = alarm.getSerialNo();				
						String time = alarm.getAlarmTime();

						String msg = "*" + strBellTime + alarmType + pos + ":" + time + "#";
//						Toast.makeText(SetAlarmActivity.this, msg, Toast.LENGTH_SHORT).show();
						bt.sendMessageToBTDevice(msg, Utils.SET_ALARMS);

					}else if(action == 0){	// action == 1 == reset
						String pos = alarm.getSerialNo();				

						String msg = "*RESET " + strBellTime + alarmType + " ALARM:" + pos +"#";
//						Toast.makeText(SetAlarmActivity.this, msg, Toast.LENGTH_SHORT).show();						
						bt.sendMessageToBTDevice(msg, Utils.SET_ALARMS);
					}
				}else{
					Toast.makeText(SetAlarmActivity.this, "Invalid Password!!", Toast.LENGTH_SHORT).show();
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



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.alarm_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title

		// Handle action bar actions click
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			break;
//		case R.id.action_read:
//			readAlarm();
//			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void readAlarm() {
		// TODO Auto-generated method stub

		bt.sendMessageToBTDevice("*READ "+ alarmType +" ALARM#", Utils.READ_FULLDAY_ALARM);


	}

	private void parseReadAlarmString(String rowString) {
		String readResponse = "";
		//		int seconds = new Date().getSeconds(); 
		//		while(!rowString.endsWith("#")){
		////			readResponse = readResponse + bt.readMessage();
		//			int currentSeconds = new Date().getSeconds(); 
		//			if(currentSeconds > (seconds+5)){
		//				break;
		//			}
		//		}

		//		Toast.makeText(this, "Read String-> "+ rowString, Toast.LENGTH_SHORT).show();

		//		if(rowString.endsWith("#")){
		if(rowString.startsWith("*")){
			rowString = rowString.substring(1);
		}
		if(rowString.endsWith("#")){
			rowString = rowString.substring(0, rowString.length()-1);
		}
		//			readResponse = rowString.substring(1,rowString.length()-1);
		System.out.println("Alarms " + rowString);
		readResponse = rowString;
		Toast.makeText(this, "String-> "+ readResponse, Toast.LENGTH_SHORT).show();

		String alarm[] = readResponse.split(",");
		for(int i=0; i< alarm.length; i++){
			String strAlarmToken = alarm[i];
			try{
				int position = Integer.parseInt(strAlarmToken.substring(4, 6));
				String time = strAlarmToken.substring(7);
				alarms.get(position).setAlarmTime(time);
			}catch(Exception e){
				System.out.println("Exception while parsing alarm time "+i);
			}
		}
		adapter.notifyDataSetChanged();
		//		}
	}

}
