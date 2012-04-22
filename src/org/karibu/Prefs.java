package org.karibu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Prefs extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	private ListPreferenceMultiSelect multiCatePref;
	private CheckBoxPreference enablePref;
	private EditTextPreference welcomePref;
	private ListPreference distancePref;
	private TimePreference fromTimePref;
	private TimePreference toTimePref;
	
	private String url = "https://tjjohnso2.wv.cc.cmu.edu:3000/announcements.json";
	
	private enum Keys {
		ENABLE_MSG, ENABLE_CATES, DISTANCE, TIME_FROM, TIME_TO
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {     
	    super.onCreate(savedInstanceState);        
	    addPreferencesFromResource(R.xml.prefs);        
	    
	    enablePref = (CheckBoxPreference)getPreferenceScreen().findPreference("enable_msg");
	    multiCatePref = (ListPreferenceMultiSelect)getPreferenceScreen().findPreference("enable_cates");
	    welcomePref = (EditTextPreference)getPreferenceScreen().findPreference("welcome_message");
	    distancePref = (ListPreference)getPreferenceScreen().findPreference("distance");
	    fromTimePref = (TimePreference) getPreferenceScreen().findPreference("time_range_from");
	    toTimePref = (TimePreference) getPreferenceScreen().findPreference("time_range_to");
	}

	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		// TODO Auto-generated method stub
		String field;
		switch (Keys.valueOf(key)) {
		case ENABLE_MSG:
			if (prefs.getBoolean(key, true))
				field = "1";
			else
				field = "0";
			break;
		case ENABLE_CATES:
			String[] selected = ListPreferenceMultiSelect.parseStoredValue(prefs.getString("enable_cates", "#ALL#"));
			StringBuilder s = new StringBuilder();
			for (String cate : selected)
				s.append(cate+"_");
			field = s.toString();
			break;
		case DISTANCE:
			field = prefs.getString("distance", "3");
			break;
		case TIME_TO:
			Toast.makeText(getBaseContext(), "time change", Toast.LENGTH_SHORT).show();
			field = "";
			break;
		case TIME_FROM:
			Toast.makeText(getBaseContext(), "time change", Toast.LENGTH_SHORT).show();
		default:
			field = "";
		}
//		if (key.equals("enable_msg")) {
//			if (prefs.getBoolean(key, true))
//				field = "1";
//			else
//				field = "0";
//        } else if (key.equals("enable_cates")) {
//			String[] selected = ListPreferenceMultiSelect.parseStoredValue(prefs.getString("enable_cates", "#ALL#"));
//			StringBuilder s = new StringBuilder();
//			for (String cate : selected)
//				s.append(cate+"_");
//			field = s.toString();
//		} 
		
		Log.i("url", url + "?" + key + "=" + field);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater blowUp = getMenuInflater();
		blowUp.inflate(R.menu.our_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case R.id.main_list:	//back to main list
			Intent intent2 = new Intent(Prefs.this, KaribuActivity.class);
			startActivity(intent2);
			break;
		case R.id.post:
			Intent intent3 = new Intent(Prefs.this, PostNew.class);
			startActivity(intent3);
			break;
		case R.id.preferences:
			break;
		case R.id.cur_loc:
			Intent intent4 = new Intent(Prefs.this, DetailsActivity.class);
			startActivity(intent4);
			break;
		case R.id.aboutUs:
			Intent intent1 = new Intent(Prefs.this, AboutUs.class);
			startActivity(intent1);
			break;
		case R.id.exit:
			finish();
			break;
		}
		return false;
	}

}
