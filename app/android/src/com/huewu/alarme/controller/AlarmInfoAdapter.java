package com.huewu.alarme.controller;

import com.huewu.alarme.R;
import com.huewu.alarme.model.AlarmInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class AlarmInfoAdapter extends ArrayAdapter<AlarmInfo>{
	
	
	public AlarmInfoAdapter(Context context) {
		super(context, -1);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if( convertView == null ){
			convertView = initView();
		}
		setUpView();
		return convertView;
	}

	private void setUpView() {
		
	}

	private View initView() {

		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.alarm_item, null);
		
		return view;
	}

}//end of class
