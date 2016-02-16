package com.mt.bell;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff.Mode;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

public class Utils {

	public static String SYSTEM_PASSWORD = "1234";
	public static final String PREF_FILE_NAME = "shared_pref";
	public static final int totalFulldayAlarms = 30;
	public static final int totalHalfdayAlarms = 10;
	public static final int READ_FULLDAY_ALARM = 1;
	public static final int READ_FULLDAY_BELLTIME = 2;
	public static final int SET_DAYS = 3;
	public static final int SET_ALARMS = 4;
//	public static final int SET_HALFDAY_ALARM = 5;
	public static final int SET_BELLTIME = 6;
//	public static final int SET_HALFDAY_BELLTIME = 7;
	public static final int SET_EMERGENCY_ALARM = 8;
	public static final int SET_CURRENT_DEVICE_TIME = 9;
	
	static boolean flag = false;
	public Context mContext;

	public Utils(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public static AlertDialog.Builder createDailogbox(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Security");
		builder.setMessage("Enter PIN:");

		// Set up the input
		final EditText input = new EditText(context);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
		builder.setView(input);

		// Set up the buttons
//		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				String userInput = input.getText().toString();
//				
//				if(!varifyPassword(context, userInput)){
//					Toast.makeText(context, "Invalid Password!!", Toast.LENGTH_SHORT).show;
//				}
//
//			}
//		});
//		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				flag = false;
//				dialog.cancel();
//			}
//		});

		builder.show();
		return builder;


	}

	public boolean varifyPassword(String userInput) {
		SharedPreferences pref = mContext.getSharedPreferences(Utils.PREF_FILE_NAME, Context.MODE_PRIVATE);
		SYSTEM_PASSWORD = pref.getString("password", "");
		if(userInput.equals(SYSTEM_PASSWORD)){
			return true;
		}else{
			return false;
		}		
	}

	public static void showToast(Context context, String message){
		Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
	}
}
