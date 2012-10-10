package com.huewu.alarme.view;

import com.huewu.alarme.R;
import com.huewu.alarme.db.AlarmePreference;
import com.huewu.alarme.model.AlarmInfo;
import com.huewu.alarme.model.UserInfo;
import com.huewu.alarme.util.Util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

/**
 * Fragment to represent new alarm setup screen.
 * User can do...
 * Check current time.
 * Choose private / group alarm.
 * Set new alarm time.
 * Add friends.
 * Request 'clock - app' sync.
 * Confirm a new alarm.
 * @author huewu.yang
 *
 */
public class SetAlarmFragment extends Fragment{
	
	private IAlarmeUIEvent mEventCallback = null;
	private TimePicker mPicker = null;
	private Button mSetAlarm = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.set_alarm_frag, null);
		
		mSetAlarm = (Button) view.findViewById(R.id.set_alarm);
		mPicker = (TimePicker) view.findViewById(R.id.set_time);		
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if( getActivity() instanceof IAlarmeUIEvent ){
			initAlarmUIEvent();
		}
	}
	
	private void initAlarmUIEvent() {
		mEventCallback = (IAlarmeUIEvent) getActivity();
		mSetAlarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//create AlarmInfo instance here? yes.
				
				Integer hour = mPicker.getCurrentHour();
				Integer min = mPicker.getCurrentMinute();
				
				//conver to ms.
				int ms = (hour * 60 + min) * 60 * 1000;

				//get user
				UserInfo user = AlarmePreference.getUser(getActivity());
				
				//how to get current user here?
				AlarmInfo newAlarm = new AlarmInfo(user, ms);
				mEventCallback.onSetAlarm(newAlarm);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mEventCallback = null;
	}

}//end of class
