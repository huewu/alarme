package com.huewu.alarme.view;

import com.huewu.alarme.controller.AlarmInfoAdapter;
import com.huewu.alarme.db.AlarmeProvider;
import com.huewu.alarme.model.AlarmInfo;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;

public class AlarmsListFragment extends ListFragment{

	//	@Override
	//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	//			Bundle savedInstanceState) {
	//
	//		View view = inflater.inflate(R.layout.alarm_list_frag, null);
	//		return view;
	//	}
	
	private AlarmInfoAdapter mAdapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAdapter = new AlarmInfoAdapter(getActivity());
		
		AlarmInfo[] alarms = AlarmeProvider.loadAlarms();
		mAdapter.addAll(alarms);
		
		//load stored alarm info list.
		setListAdapter(mAdapter);
	}

}//end of class
