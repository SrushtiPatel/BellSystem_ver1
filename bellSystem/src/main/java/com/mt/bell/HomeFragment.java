package com.mt.bell;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment {
	
	View rootView;
	private static final String TAG = "HomeFragment";
    private static final boolean D = true;
    
  
    // declare button for launching website and textview for connection status
    Button tlbutton;
    ProgressDialog progressDialog;
    // EXTRA string to send on to mainactivity
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
	public final int REQ_CODE_BLUETOOTH_ON = 100;
	Bluetooth bt;
    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	
    public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.device_list, container, false);
        
        progressDialog = new ProgressDialog(getActivity());
        setHasOptionsMenu(true);
        
        setupBluetooth();
        progressDialog.dismiss();
        return rootView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Handle action bar actions click
		switch (item.getItemId()) {
//		case R.id.action_refresh:
//			setupBluetooth();
//			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void setupBluetooth() {
		// TODO Auto-generated method stub
		
    	//*************** 
		bt = new Bluetooth(getActivity());
        
    	checkBTState();

//    	textView1 = (TextView) findViewById(R.id.connecting);
//    	textView1.setTextSize(40);
//    	textView1.setText(" ");

    	// Initialize array adapter for paired devices
    	mPairedDevicesArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_name);

    	// Find and set up the ListView for paired devices
    	ListView pairedListView = (ListView) rootView.findViewById(R.id.paired_devices);
    	pairedListView.setAdapter(mPairedDevicesArrayAdapter);
    	pairedListView.setOnItemClickListener(mDeviceClickListener);

    	// Get the local Bluetooth adapter
    	mBtAdapter = BluetoothAdapter.getDefaultAdapter();

    	// Get a set of currently paired devices and append to 'pairedDevices'
    	Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

    	// Add previosuly paired devices to the array
    	if (pairedDevices.size() > 0) {
//    		findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);//make title viewable
    		for (BluetoothDevice device : pairedDevices) {
    			mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
    		}
    	} else {
    		String noDevices = getResources().getText(R.string.none_paired).toString();
    		mPairedDevicesArrayAdapter.add(noDevices);
    	}
	}


	// Set up on-click listener for the list (nicked this - unsure)
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

//        	textView1.setText("Connecting...");
        	progressDialog.setTitle("Connecting...");
//        	progressDialog.setMessage("Connecting to BT Serial Device...");
            progressDialog.show();
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

          //Store Device address in SharedPreference
//            SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString(EXTRA_DEVICE_ADDRESS, address);
//            editor.commit();
//            Bluetooth.setAddress(address);
            checkBTState();
            bt.connectToDevice(address);
            
            if(bt.getBtSocket().isConnected()){
            	Toast.makeText(getActivity(), "Bluetooth Device is connected.", Toast.LENGTH_SHORT).show();
            }else{
            	Toast.makeText(getActivity(), "Connection Failure!!! Try again.", Toast.LENGTH_SHORT).show();
            	bt.setBtSocket(null);
    			
            }
            progressDialog.dismiss();
//            // Make an intent to start next activity while taking an extra which is the MAC address.
//			Intent i = new Intent(DeviceListActivity.this, SpeechToTextActivity.class);
//            i.putExtra(EXTRA_DEVICE_ADDRESS, address);
//			startActivity(i);   
        }
    };

    private void checkBTState() {
        // Check device has Bluetooth and that it is turned on
    	 mBtAdapter=BluetoothAdapter.getDefaultAdapter(); // CHECK THIS OUT THAT IT WORKS!!!
        if(mBtAdapter==null) { 
        	Toast.makeText(getActivity(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
          if (mBtAdapter.isEnabled()) {
            Log.d(TAG, "...Bluetooth ON...");
            //Prompt user to turn on Bluetooth
            bt.setBtAdapter(mBtAdapter);
          } else {
            Intent enableBtIntent = new
            		Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
 
            }
          }
        }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQ_CODE_BLUETOOTH_ON : {
			if (resultCode == Activity.RESULT_OK && null != data) {

				ArrayList<String> result = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				String text = result.get(0);
				Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
//				txtString.setText(text);
				try{
				if(!bt.isBluetoothConnected()){
					bt.createBluetoothSocketConnection();
				}
//				mConnectedThread.write("*"+text+"#");
				}
				catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}

		}
	}

}
