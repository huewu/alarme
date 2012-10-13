package com.huewu.alarme.controller;

import java.util.Date;

import android.R.color;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huewu.alarme.R;
import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.util.Util;

public class AlarmInfoAdapter extends ArrayAdapter<AlarmInfo>{
	
	
	public AlarmInfoAdapter(Context context) {
		super(context, -1);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if( convertView == null ){
			convertView = initView();
		}
		setUpView(convertView, getItem(position));
		return convertView;
	}

	private void setUpView(View v, AlarmInfo alarm) {
		View alarm_pane = v.findViewById(R.id.pane);
		ImageView alarm_icon = (ImageView) v.findViewById(R.id.alarm_type_icon);
		TextView alarm_time = (TextView) v.findViewById(R.id.alarm_time);
		TextView alarm_status = (TextView) v.findViewById(R.id.alarm_status);
		TextView alarm_off = (TextView) v.findViewById(R.id.alarm_off_time);

		//group / private alarm.
		if( alarm.type.equals( AlarmInfo.PRIVATE_ALARM ) ){
			alarm_icon.setImageResource(R.drawable.private_alarm);
		}else{
			alarm_icon.setImageResource(R.drawable.group_alarm);
		}
	
		//alarm time
		{
			Date time = new Date(alarm.getTime());
			alarm_time.setText(Util.getSimpleTimeString(time));
		}
		
		//alarm status
		alarm_status.setText(alarm.status);
		if(alarm.status.equals(AlarmInfo.STATUS_OFF)){
			//already gone alarm.
			alarm_pane.setBackgroundColor(getContext().getResources().getColor(R.color.alarme_lighter_grey));
		}else{
			alarm_pane.setBackgroundColor(getContext().getResources().getColor(R.color.alarme_darker_white));
		}
		
		//alarm off time
		{
			Date time = new Date(alarm.getTime());
			alarm_off.setText(Util.getSimpleTimeString(time));
		}
		
		//TODO if already passed alarm. disable it.
	}

	private View initView() {

		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.alarm_item, null);
		
		return view;
	}

}//end of class
