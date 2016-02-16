package com.mt.bell;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ResetPasswordFragment extends Fragment {

	View rootView;
	private static final String TAG = "HomeFragment";
	private static final boolean D = true;


	// declare button for launching website and textview for connection status
	Button btnSave;
	EditText etNewPassword, etOldPassword, etConfirmPassword;

	public ResetPasswordFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.reset_password, container, false);
		setHasOptionsMenu(false);
		init();
		return rootView;
	}

	private void init() {
		// TODO Auto-generated method stub
		etOldPassword = (EditText) rootView.findViewById(R.id.etOldPassword);
		etNewPassword = (EditText) rootView.findViewById(R.id.etNewPassword);
		etConfirmPassword = (EditText) rootView.findViewById(R.id.etConfirmPassword);

		btnSave = (Button) rootView.findViewById(R.id.btnSave);

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				validatePassword();
			}
		});
	}

	protected void validatePassword() {
		// TODO Auto-generated method stub

		SharedPreferences pref = getActivity().getSharedPreferences(Utils.PREF_FILE_NAME, getActivity().MODE_PRIVATE);
		String savedPassword = pref.getString("password", "");
		boolean isValid = true;
		if(etOldPassword.getText() == null || etOldPassword.getText().toString().equals("")){
			etOldPassword.setError("Enter current PIN");
			isValid = false;
		}else if(!etOldPassword.getText().toString().equalsIgnoreCase(savedPassword)){
			etOldPassword.setError("Invalid PIN");
			isValid = false;
		}
		if(etNewPassword.getText() == null || etNewPassword.getText().toString().equals("")){
			etNewPassword.setError("Enter New PIN");
			isValid = false;
		}
		if(etConfirmPassword.getText() == null || etConfirmPassword.getText().toString().equals("")){
			etConfirmPassword.setError("Enter Confirm PIN");
			isValid = false;
		}else if(!etConfirmPassword.getText().toString().equalsIgnoreCase(etNewPassword.getText().toString())){
			etConfirmPassword.setError("New PIN and Confirm PIN do not match.");
			isValid = false;
		}
		if(isValid){
			String oldPassword = etOldPassword.getText().toString();
			String newPassword = etNewPassword.getText().toString();
			String confirmPassword = etConfirmPassword.getText().toString();
			Editor editor = pref.edit();
			editor.putString("password", newPassword);
			editor.commit();
			
			Toast.makeText(getActivity(), "PIN is changed successfully", Toast.LENGTH_SHORT).show();
			
		}


	}

}