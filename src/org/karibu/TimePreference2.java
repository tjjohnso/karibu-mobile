package org.karibu;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimePreference2 extends TimePreference {
	private int lastHour = 0;
	private int lastMinute = 0;
	private String defaultTime;
	private String curTime;
	private TimePicker picker = null;
	
	public TimePreference2(Context ctxt, AttributeSet attrs) {
		super(ctxt, attrs);
		
        TypedArray a = ctxt.obtainStyledAttributes(attrs, R.styleable.TimeRangePreference);
        defaultTime = a.getString( R.styleable.TimeRangePreference_hour2) + ":" + a.getString(R.styleable.TimeRangePreference_min2);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		curTime = preferences.getString("time_range_to", defaultTime);
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
        defaultTime = a.getString( R.styleable.TimeRangePreference_hour2) + ":" + a.getString(R.styleable.TimeRangePreference_min2);
        return defaultTime;
//		return (a.getString(index));
	}
}
