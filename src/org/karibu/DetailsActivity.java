package org.karibu;

import java.util.List;

import org.karibu.model.Announcement;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class DetailsActivity extends MapActivity implements LocationListener {
	//passed from other activity
	private Bundle bundle;
	private Announcement a;
	
	
	// layout resources
	MapView mapView;
	private TextView announcer, overview, details;
	Button refreshLocation;

	//for user location display
	private LocationManager lm;
	private String provider;
	private Location userLocation;
	List<Overlay> overlays; // 0: touchy, 1: compass, 2: store list, 3: user
							// location
	MyLocationOverlay compass;
	MapController controller; // animate to location
	// store locations
	CustomPinPoint stores;
	Drawable redPin, blueDot; // blueDot is for user location

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);

		bundle = this.getIntent().getExtras();
		 if (bundle != null)
			 a = (Announcement) bundle.getSerializable("announcement");
		// else {
		// a = new Announcement();
		// a.announcer = new Announcer();
		//
		// }
		configureView();
	}

	private void displayLocation() {
		// user location
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		provider = lm.getBestProvider(new Criteria(), false); // default
		userLocation = lm.getLastKnownLocation(provider); // update location for listener
		if (userLocation != null) {
			double lat = userLocation.getLatitude();
			double lng = userLocation.getLongitude();
			Toast.makeText(getBaseContext(), "lat " + lat + " lng " + lng,
					Toast.LENGTH_SHORT);
			GeoPoint userP = new GeoPoint((int) lat, (int) lng);
			CustomPinPoint myPinPoint = new CustomPinPoint(blueDot,
					DetailsActivity.this);
			OverlayItem overlayItem = new OverlayItem(userP, "You are Here",
					"You are Here");
			myPinPoint.addPinPoint(overlayItem);
			overlays.add(myPinPoint);
			controller.setCenter(userP);
			controller.animateTo(userP);
			controller.setZoom(17);
		} else 
			Toast.makeText(getBaseContext(), "no user location",
						Toast.LENGTH_SHORT);
		// announcer location
		if (a != null) {
			GeoPoint anP = new GeoPoint((int) (a.latitude * 1E6),
					(int) (a.longitude * 1E6));
			OverlayItem storeLocation;
			if (a.announcer != null)
				storeLocation = new OverlayItem(anP, a.announcer.name,a.announcer.name);
			else 
				storeLocation = new OverlayItem(anP, "", "");
			stores.addPinPoint(storeLocation);
			overlays.add(stores);
			
			if (userLocation == null) {
				controller.animateTo(anP);
				controller.setZoom(15);

			}
		}

	}

	private void configureView() {
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		if (a != null) {
			announcer = (TextView) findViewById(R.id.announcer);
			overview = (TextView) findViewById(R.id.overview);
			details = (TextView) findViewById(R.id.details);
			// refreshLocation = (Button) findViewById(R.id.refreshMyLocation);
			// deal info
			if (a.announcer != null)
				announcer.setText(a.announcer.name);
			overview.setText(a.overview);
			details.setText(a.details);
		}
		// map overlay
		overlays = mapView.getOverlays();
		// Touchy t = new Touchy(); // touch screen overlay
		// overlays.add(t);
		compass = new MyLocationOverlay(DetailsActivity.this, mapView);
		overlays.add(compass);
		controller = mapView.getController();

		redPin = this.getResources().getDrawable(R.drawable.marker_red);
		blueDot = this.getResources().getDrawable(R.drawable.blue_dot);

		// store locations
		stores = new CustomPinPoint(redPin, DetailsActivity.this);
		displayLocation();

		// back button
		// listener = new OnClickListener() {
		// public void onClick(View v) {
		// Intent intent = new Intent(DetailsActivity.this,
		// ShowMessageActivity.class);
		// startActivity(intent);
		// DetailsActivity.this.finish();
		// }
		// };
		// backBt = (Button) findViewById(R.id.back);
		// backBt.setOnClickListener(listener);

	}

	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onPause() {
		compass.disableCompass();
		super.onPause();
		lm.removeUpdates(this);
	}

	@Override
	protected void onResume() {
		compass.enableCompass();
		super.onResume();
		lm.requestLocationUpdates(provider, 35000, 100, this);
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
			Intent intent2 = new Intent(DetailsActivity.this, KaribuActivity.class);
			startActivity(intent2);
			break;
		case R.id.post:
			Intent intent3 = new Intent(DetailsActivity.this, PostNew.class);
			startActivity(intent3);
			break;
		case R.id.preferences:
			Intent intent5 = new Intent(DetailsActivity.this, Prefs.class);
			startActivity(intent5);
			break;
		case R.id.cur_loc:
			break;
		case R.id.aboutUs:
			Intent intent1 = new Intent(DetailsActivity.this, AboutUs.class);
			startActivity(intent1);
			break;
		case R.id.exit:
			finish();
			break;
		}
		return false;
	}

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
