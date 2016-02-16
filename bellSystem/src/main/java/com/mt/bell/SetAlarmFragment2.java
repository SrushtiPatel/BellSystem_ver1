package com.mt.bell;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class SetAlarmFragment2 extends Fragment implements View.OnClickListener {
	
	LinearLayout layoutFullday, layoutHalfday;
	Button btnFulldayAlarm,btnHalfdayAlarm;
	View rootView;
	
	public SetAlarmFragment2(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.set_alarm_fragment, container, false);
         
        setLayout();
        return rootView;
	}

	public void setLayout() {
		// TODO Auto-generated method stub
		btnFulldayAlarm = (Button)rootView.findViewById(R.id.btnFulldayAlarm);
		btnFulldayAlarm.setOnClickListener(this);
		
		btnHalfdayAlarm = (Button)rootView.findViewById(R.id.btnHalfdayAlarm);
		btnHalfdayAlarm.setOnClickListener(this);
		
		layoutFullday = (LinearLayout)rootView.findViewById(R.id.layoutFullday);
		
		LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		for(int i= 1; i <= 10; i++){
			LinearLayout layoutHorizontal = new LinearLayout(getActivity());
			layoutHorizontal.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			layoutHorizontal.setTag("fullday_alarm"+i);
			layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
			TextView textView = new TextView(getActivity());
			textView.setLayoutParams(params);
			textView.setText("Alarm"+i);
			
			EditText editText = new EditText(getActivity());
			editText.setLayoutParams(params);
			
			layoutHorizontal.addView(textView,0);
			layoutHorizontal.addView(editText,1);
			
			layoutFullday.addView(layoutHorizontal);
			
		}
		
		layoutHalfday = (LinearLayout)rootView.findViewById(R.id.layoutHalfday);
		for(int i= 1; i <= 5; i++){
			LinearLayout layoutHorizontal = new LinearLayout(getActivity());
			layoutHorizontal.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			layoutHorizontal.setTag("halfday_alarm"+i);
			layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
			TextView textView = new TextView(getActivity());
			textView.setLayoutParams(params);
			textView.setText("Alarm"+i);
			
			EditText editText = new EditText(getActivity());
			editText.setLayoutParams(params);
			
			layoutHorizontal.addView(textView,0);
			layoutHorizontal.addView(editText,1);
			
			layoutHalfday.addView(layoutHorizontal);
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId = v.getId();
		switch (viewId) {
		case R.id.btnFulldayAlarm:
			toggleView(layoutFullday);
			break;

		case R.id.btnHalfdayAlarm:
			toggleView(layoutHalfday);
			break;

		default:
			break;
		}
	}
	
	private void toggleView(View view) {
		int visibility = (view.getVisibility() == View.VISIBLE)? View.GONE: View.VISIBLE;
		view.setVisibility(visibility);
		// TODO Auto-generated method stub
		
	}

}
