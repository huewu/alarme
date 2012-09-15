package com.huewu.alarme.view;

import com.huewu.alarme.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingFragment extends Fragment {
	
	//reference JB alarm setting activity UI.
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.setting_frag, null);
	}
	
	//ADD / EDIT / DELETE
	//Turn alarm on
	//Time
	//Repeat
	//Ringtone
	//Vibrate
	//Label

}//end of class
