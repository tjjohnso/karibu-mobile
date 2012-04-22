package org.karibu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.karibu.model.Announcement;
import org.karibu.model.Announcer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ShowMessageActivity extends ListActivity {
	// private static final String TAG = "Activity02";
	private List<Announcement> announcements;
	private Bundle bundle;
	private int numOfMsg;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg_list);
		bundle = this.getIntent().getExtras();
		this.setTitle(bundle.getString("Category"));
		numOfMsg = bundle.getInt("numOfMsg");
		announcements = new ArrayList<Announcement>();
		// TextView cate = (TextView) findViewById(R.id.category);
		// cate.setText(cateName);
		// TextView msgN = (TextView) findViewById(R.id.msg_num);
		// msgN.setText((CharSequence) String.valueOf(msgNum));
		// display messages
		List<Map<String, Object>> myList = getData();
		SimpleAdapter adapter = new SimpleAdapter(this, myList,
				R.layout.msg_item, new String[] { "announcer", "street",
						"overview" }, new int[] { R.id.announcer, R.id.street,
						R.id.overview });
		setListAdapter(adapter);

	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> myList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < numOfMsg; i++) {
			Announcement a = (Announcement) bundle.getSerializable("msg" + i);
			announcements.add(a);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("img", R.drawable.starbucks);
			if (a.announcer != null)
				map.put("announcer", a.announcer.name);
			map.put("street", a.street);
			map.put("overview", a.overview);
			myList.add(map);
		}
		return myList;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent1 = new Intent(ShowMessageActivity.this,
				DetailsActivity.class);
		Bundle bundle2 = new Bundle();
		bundle2.putSerializable("announcement", announcements.get(position));
		intent1.putExtras(bundle2);
		startActivity(intent1);
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
			Intent intent2 = new Intent(ShowMessageActivity.this, KaribuActivity.class);
			startActivity(intent2);
			break;
		case R.id.post:
			Intent intent3 = new Intent(ShowMessageActivity.this, PostNew.class);
			startActivity(intent3);
			break;
		case R.id.preferences:
			Intent intent5 = new Intent(ShowMessageActivity.this, Prefs.class);
			startActivity(intent5);
			break;
		case R.id.cur_loc:
			Intent intent4 = new Intent(ShowMessageActivity.this, DetailsActivity.class);
			startActivity(intent4);
			break;
		case R.id.aboutUs:
			Intent intent1 = new Intent(ShowMessageActivity.this, AboutUs.class);
			startActivity(intent1);
			break;
		case R.id.exit:
			finish();
			break;
		}
		return false;
	}
}