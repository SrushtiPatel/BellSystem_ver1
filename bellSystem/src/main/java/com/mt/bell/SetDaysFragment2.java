package com.mt.bell;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

public class SetDaysFragment2 extends PreferenceFragment {
	ListPreference prefHalfday ;
    ListPreference prefHoliday;
    Bluetooth bt;
    
    
 @Override
 public void onCreate(Bundle savedInstanceState) {
  // TODO Auto-generated method stub
  super.onCreate(savedInstanceState);
  
  // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_general);
        bt = new Bluetooth(getActivity());
		
        prefHalfday = (ListPreference)findPreference("prefhalfdaySelection");
        prefHoliday = (ListPreference)findPreference("prefholidaySelection");
        
        Preference prefButton = (Preference)findPreference("prefButtonSave");
        Log.v(getTag(), prefButton.getLayoutResource()+"");
        prefButton.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				int halfday = prefHalfday.findIndexOfValue((String) prefHalfday.getEntry());
				int holiday = prefHoliday.findIndexOfValue((String) prefHoliday.getEntry());
				
				String message = "*HALFDAY:" + halfday + "#";	
				bt.startConnectionThread();
				bt.sendMessageToBTDevice(message, 0);
				
				message = "*HOLIDAY:" + holiday + "#";
				bt.sendMessageToBTDevice(message, 0);

				Toast.makeText(getActivity(), halfday +" ~ "+holiday , Toast.LENGTH_SHORT).show();
				
				return false;
			}
		});
 }

}
