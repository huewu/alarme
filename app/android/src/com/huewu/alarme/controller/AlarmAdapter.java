package com.huewu.alarme.controller;

import com.huewu.alarme.model.AlarmInfo;

import android.content.Context;
import android.widget.ArrayAdapter;

public class AlarmAdapter extends ArrayAdapter<AlarmInfo>{

	public AlarmAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

}//end of class
