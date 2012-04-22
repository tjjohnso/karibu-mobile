package org.karibu;

import java.io.IOException;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PostNew extends Activity {
	private EditText overview, details;
	private String myOverview, myDetails;
	private DatePicker begin, end;
	private String newStart, newEnd;
	private Button postBtn;
	
	private int year, month, day;
	private TextView debug1, debug2;

	static final int SUMMARY_DIALOG = 0;
	static final int OVERVIEW_MISSING_DIALOG = 1;
	static final int DETAILS_MISSING_DIALOG = 2;
	
	private String url = "https://tjjohnso2.wv.cc.cmu.edu:3000/announcements.json";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_form);
		setCurrentDateOnView();
		addListenerOnButton();
	}
	
	private void setCurrentDateOnView() {	//system date	
		overview = (EditText) findViewById(R.id.post_overview);
		details = (EditText) findViewById(R.id.post_details);
		
		debug1 = (TextView) findViewById(R.id.begindate);
		debug2 = (TextView) findViewById(R.id.enddate);
		begin = (DatePicker) findViewById(R.id.post_begindate);
		end = (DatePicker) findViewById(R.id.post_enddate);
 
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		
		// set current date into datepicker
		begin.init(year, month, day, null);
		end.init(year, month, day, null);
 
	}

	private void addListenerOnButton() {
		postBtn = (Button) findViewById(R.id.post_button);
		postBtn.setOnClickListener(new View.OnClickListener() {
 
			public void onClick(View v) {
				newStart = new StringBuilder().append(begin.getMonth() + 1).append("-").append(begin.getDayOfMonth()).append("-").append(begin.getYear()).toString();
//				debug1.setText(newStart);
				newEnd = new StringBuilder().append(end.getMonth() + 1).append("-").append(end.getDayOfMonth()).append("-").append(end.getYear()).toString();
//				debug2.setText(newEnd);
				
				myOverview = overview.getText().toString();
				myDetails = details.getText().toString();
				if (myOverview.trim().equals("")) 
					showDialog(OVERVIEW_MISSING_DIALOG);
					
				else if (myDetails.trim().equals("")) 
					showDialog(DETAILS_MISSING_DIALOG);

				else 
					showDialog(SUMMARY_DIALOG);
			}
		});
 
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(PostNew.this);
		AlertDialog dialog;
		switch (id) {
		case SUMMARY_DIALOG:
			builder.setTitle("Summary");
			builder.setMessage("Overview:\n" + myOverview + "\n" + "Details:\n" + myDetails + 
					"\nStart On: " + newStart  + "\nExpired On: " + newEnd + "\nLocation: ");
			builder.setPositiveButton("Confirm and Post", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //send to server
                	
                	String resp = postNew(url + "?overview=" + myOverview.replace(" ", "%20") + "&details=" + myDetails.replace(" ", "%20") + "?start_date=" + newStart + "?end_date=" + newEnd);
                	if (resp == null) {
                		dialog.dismiss();
                		Toast.makeText(getBaseContext(), "Post Successfully!", Toast.LENGTH_SHORT).show();
                	} else
                		Toast.makeText(getBaseContext(), "Sorry, post failed:\n" + resp + "\nTry Again!", Toast.LENGTH_SHORT).show();
                }
            });
			builder.setNegativeButton("Return to Modify", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	dialog.dismiss();
                }
            });
			dialog = builder.create();
			break;
		case OVERVIEW_MISSING_DIALOG:
			builder.setTitle("Missing Overview");
			builder.setMessage("OVERVIEW of your annoucnement is required");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					overview.requestFocus();
				}
			});
			dialog = builder.create();
			break;
		case DETAILS_MISSING_DIALOG:
			builder.setTitle("Missing Details");
			builder.setMessage("Why not add a few lines of DETAILS?");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					details.requestFocus();
				}
			});
			builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					showDialog(SUMMARY_DIALOG);
				}
			});
			dialog = builder.create();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	private String postNew(String url) {
        Log.i("url", url);
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(new HttpPost(url));
			Log.i("response", EntityUtils.toString(response.getEntity()));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}
		return null;
	}
}
