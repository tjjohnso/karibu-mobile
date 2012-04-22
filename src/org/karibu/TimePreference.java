package org.karibu;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

public class TimePreference extends DialogPreference {
	private int lastHour = 0;
	private int lastMinute = 0;
	private TimePicker picker = null;
	private String defaultTime;
	
	private String curTime;
	
	public TimePreference(Context ctxt, AttributeSet attrs) {
		super(ctxt, attrs);

        TypedArray a = ctxt.obtainStyledAttributes(attrs, R.styleable.TimeRangePreference);
        defaultTime = a.getString( R.styleable.TimeRangePreference_hour) + ":" + a.getString(R.styleable.TimeRangePreference_min);
        
		setPersistent(true);
		setPositiveButtonText("Set");
		setNegativeButtonText("Cancel");
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		curTime = preferences.getString("time_range_from", defaultTime);
	}

	public static int getHour(String time) {
		String[] pieces = time.split(":");

		return (Integer.parseInt(pieces[0]));
	}

	public static int getMinute(String time) {
		String[] pieces = time.split(":");

		return (Integer.parseInt(pieces[1]));
	}

	@Override
	protected View onCreateDialogView() {
		picker = new TimePicker(getContext());
		picker.setIs24HourView(true);
		picker.setCurrentHour(getHour(curTime));
		picker.setCurrentMinute(getMinute(curTime));
		setSummary(picker.getCurrentHour() + ":" + picker.getCurrentMinute());
		return (picker);
	}

	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);

		picker.setCurrentHour(lastHour);
		picker.setCurrentMinute(lastMinute);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult) {
			lastHour = picker.getCurrentHour();
			lastMinute = picker.getCurrentMinute();

			curTime = String.valueOf(lastHour) + ":"
					+ String.valueOf(lastMinute);

			if (callChangeListener(curTime)) {
				persistString(curTime);
				setSummary(picker.getCurrentHour() + ":" + picker.getCurrentMinute());
				notifyChanged();
			}
			//send new time to server
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
        defaultTime = a.getString( R.styleable.TimeRangePreference_hour) + ":" + a.getString(R.styleable.TimeRangePreference_min);
        return defaultTime;
//		return (a.getString(index));
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		String time = null;

		if (restoreValue) {
			if (defaultValue == null) {
				time = getPersistedString("00:00");
			} else {
				time = getPersistedString(defaultValue.toString());
			}
		} else {
			time = defaultValue.toString();
			persistString(time);
		}

		lastHour = getHour(time);
		lastMinute = getMinute(time);
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// TODO Auto-generated method stub
	      if (!state.getClass().equals(SavedState.class)) {
	            // Didn't save state for us in onSaveInstanceState
	            super.onRestoreInstanceState(state);
	            return;
	        }

	        // Restore the instance state
	        SavedState myState = (SavedState) state;
	        super.onRestoreInstanceState(myState.getSuperState());
	        curTime = myState.time;
	}

	@Override
	/*
     * Suppose a client uses this preference type without persisting. We
     * must save the instance state so it is able to, for example, survive
     * orientation changes.
     */
	protected Parcelable onSaveInstanceState() {
		// TODO Auto-generated method stub
		final Parcelable superState = super.onSaveInstanceState();
		if (isPersistent()) {
			// No need to save instance state since it's persistent
			return superState;
		}
		//save the instance state
		final SavedState myState = new SavedState(superState);
		myState.time = curTime;
		return myState;
	}
	
	/**
     * SavedState, a subclass of {@link BaseSavedState}, will store the state
     * of MyPreference, a subclass of Preference.
     * <p>
     * It is important to always call through to super methods.
     */
    private static class SavedState extends BaseSavedState {
        String time;

        public SavedState(Parcel source) {
            super(source);

            // Restore the click counter
            time = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);

            // Save the click counter
            dest.writeString(time);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
