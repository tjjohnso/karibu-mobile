package org.karibu;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;


public class CustomPinPoint extends ItemizedOverlay {
	private ArrayList<OverlayItem> pinPoints = new ArrayList<OverlayItem>();
	private Context c;

	public CustomPinPoint(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));	//align
	}
	
	public CustomPinPoint(Drawable m, Context con) {
		this(m);
		c = con;	//relay the context
	}

	@Override
	protected OverlayItem createItem(int i) {
		return pinPoints.get(i);
	}

	@Override
	public int size() {

		return pinPoints.size();
	}
	
	public void addPinPoint(OverlayItem overlay) {
		pinPoints.add(overlay);
		this.populate();	//read each of out OverlayItems and prepare them to be drawn
	}

	
}
