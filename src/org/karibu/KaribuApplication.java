package org.karibu;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.karibu.net.EasySSLSocketFactory;

import android.app.Application;
import android.util.Log;

public class KaribuApplication extends Application {	
	/*
	 * The following URL is used when Trenton's test server (laptop) is
	 * connected to the CMU network. This will need to be changed when the server
	 * moves to a static machine.
	 */
	public static final String HOME_URL = "https://tjjohnso2.wv.cc.cmu.edu:3000/";
	
	/* The following fields are used for SSL connection. These have default 
	 * visibility so DefaultHttpClients can be instantiated in other Activities 
	 * in this package with the following objects used as arguments. */
	static ClientConnectionManager clientConnectionManager;
	static HttpParams params;
	
	@Override
    public void onCreate() {
		
		// Set up network configuration for SSL connections.
		networkSetup();
		
        super.onCreate();
    }
	
	/*
	 * Special thanks to Tobias Knell at
	 * http://blog.synyx.de/2010/06/android-and-self-signed-ssl-certificates/
	 * The following method prepares for the HTTPS connection.
	 */
	private void networkSetup() {
		SchemeRegistry schemeRegistry = new SchemeRegistry();

		// HTTP scheme
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		// HTTPS scheme
		schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(),
				443));

		params = new BasicHttpParams();
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 1);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,
				new ConnPerRouteBean(1));
		params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf8");
		clientConnectionManager = new ThreadSafeClientConnManager(params,
				schemeRegistry);
	}
	
	/*
	 * Thanks to Fabrizio Chami at JavaCodeGeeks.com
	 * http://www.javacodegeeks.com
	 * /2011/01/android-json-parsing-gson-tutorial.html The following method
	 * simply takes a URL, sends a GET request to the server and returns an 
	 * input stream, which can be read and parsed.
	 */
	public static InputStream getData(String url) {

		DefaultHttpClient client = new DefaultHttpClient(
				KaribuApplication.clientConnectionManager, KaribuApplication.params);

		HttpGet getRequest = new HttpGet(url);

		try {

			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w("KaribuApplication", "Error " + statusCode
						+ " for URL " + url);
				return null;
			}

			return getResponse.getEntity().getContent();

		} catch (IOException e) {
			getRequest.abort();
			Log.w("KaribuApplication", "Error for URL " + url, e);
		}

		return null;
	}	
	
	/*
	 * The following method simply takes a URL, sends a POST request to the 
	 * server and returns an input stream, which can be read and parsed.
	 */
	public static InputStream postData(String url, String jsonData) {
		
		DefaultHttpClient client = new DefaultHttpClient(
				KaribuApplication.clientConnectionManager, KaribuApplication.params);

		HttpPost postRequest = new HttpPost(url);

		try {
			postRequest.setEntity(new StringEntity(jsonData));
			postRequest.setHeader("Accept", "application/json");
			postRequest.setHeader("Content-type", "application/json");
			
			HttpResponse postResponse = client.execute(postRequest);
			final int statusCode = postResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w("KaribuApplication", "Error " + statusCode
						+ " for URL " + url);
				return null;
			}

			return postResponse.getEntity().getContent();
		
		} catch (IOException e) {
			postRequest.abort();
			Log.w("KaribuApplication", "Error for URL " + url, e);
		}

		return null;
	}
}
