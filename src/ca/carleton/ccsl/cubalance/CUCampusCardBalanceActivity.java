package ca.carleton.ccsl.cubalance;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Context;

public class CUCampusCardBalanceActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
		@Override
			public void onClick(View v) {
				postData(v,getApplicationContext());
				
			}
		});
    }
    public void postData(View view, Context context) {
    	Log.v("POST", "Inside postData");
    	final KeyStore ks;
    	try {
    		ks = KeyStore.getInstance("BKS");
    		final InputStream in = context.getResources().openRawResource( R.raw.curoot);
    		ks.load(in,context.getString(R.string.mystore_password).toCharArray());
    		TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
    		tmf.init(ks);
		
    		SSLContext sslcontext = SSLContext.getInstance("TLS");
    		sslcontext.init(null, tmf.getTrustManagers(), null);
    		URL url = new URL("https://central.carleton.ca/prod/twbkwbis.P_ValLogin");
    		HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
    		urlConnection.setSSLSocketFactory(sslcontext.getSocketFactory());
    		InputStream ins = urlConnection.getInputStream();
    		Log.v("POST", "inputstreeeeem");
    		// Create a new HttpClient and Post Header
    		}
    		catch (KeyStoreException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		} catch (NoSuchAlgorithmException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (KeyManagementException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (MalformedURLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (CertificateException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		catch (SSLHandshakeException e){
    			e.printStackTrace();
    		}
    		catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
		   HttpClient httpclient = new DefaultHttpClient();
		   HttpPost httppost = new HttpPost("https://central.carleton.ca/prod/twbkwbis.P_ValLogin");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("sid", "1009999"));
            nameValuePairs.add(new BasicNameValuePair("PIN", "99999"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            Header[] header=response.getHeaders("Set-Cookie");
            for(int i=0;i<header.length;i++){ 
            	Log.v("Header:",header[i].toString());
            }
            
        } catch (ClientProtocolException e) {
            Log.v("Exception",e.toString());
        } catch (IOException e) {
        	Log.v("Exception",e.toString());
        }
    }
}
