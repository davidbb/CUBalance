package ca.carleton.ccsl.cubalance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.Socket;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.os.AsyncTask;
import android.widget.TextView;

public class CUBalanceFetcher extends AsyncTask<Void, Void, String>
{
  private static final String FF_USER_AGENT         = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:11.0) Gecko/20100101 Firefox/11.0";
  private static final String CARLETON_LOGIN_URL    = "https://central.carleton.ca/prod/twbkwbis.P_ValLogin";
  private static final String CARLETON_LOGIN_REF    = "https://central.carleton.ca/prod/twbkwbis.P_WWWLogin";
  private static final String CARLETON_BALANCE_URL  = "https://central.carleton.ca/prod/pkg_campuscard_actrevsys.get_balances";

  private static final String CARLETON_LOGIN_COOKIE = "TESTID=set";
  private static final String CARLETON_USER_PARAM   = "sid";
  private static final String CARLETON_PIN_PARAM    = "PIN";
  
  private static final String  HTML_BALANCE_REGEXP  = "<TD ALIGN=\"right\">&nbsp; ([\\d]+.[\\d]{2})&nbsp;&nbsp;</TD>";
  private static final Pattern HTML_BALANCE_PATTERN = Pattern.compile(HTML_BALANCE_REGEXP);
    
  private final CookieStore cookieStore   = new BasicCookieStore();
  private final HttpContext localContext  = new BasicHttpContext();
  private final HttpClient  httpClient;
  private final String      user;
  private final String      pin;
  
  private final TextView    textView;

  /** Called when the activity is first created. */
  public CUBalanceFetcher(String user, String pin, TextView toUpdate)
  {    
    this.httpClient = setupClient();
    this.user       = user;
    this.pin        = pin;
    this.textView   = toUpdate;
  }
  
  @Override
  protected String doInBackground(Void... params)
  {
    //Called from within a separate thread.
    try
    {
      submitLogin(user, pin);
      return getBalance();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return "ERROR";
  }
  
  protected void onPostExecute (String result) 
  {
    //Called from within UI thread.
    textView.setText("Balance: "+ result);
  }
  
  private HttpClient setupClient()
  {
    final SchemeRegistry schemeRegistry = new SchemeRegistry();
   
    try
    {
      schemeRegistry.register(new Scheme("https", new DefaultKeyStoresSSLSocketFactory(), 443));
    } catch (KeyManagementException e1) {
      e1.printStackTrace();
    } catch (UnrecoverableKeyException e1) {
      e1.printStackTrace();
    } catch (NoSuchAlgorithmException e1) {
      e1.printStackTrace();
    } catch (KeyStoreException e1) {
      e1.printStackTrace();
    }

    final HttpParams params = new BasicHttpParams();
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
           
    final ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
    localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    
    return new DefaultHttpClient(cm, params);
  }

  public void submitLogin(final String sid, final String pin) throws ClientProtocolException, IOException
  {       
    HttpPost loginPost = new HttpPost(CARLETON_LOGIN_URL);
    
    loginPost.setHeader("Referer",     CARLETON_LOGIN_REF);
    loginPost.setHeader("Cookie",      CARLETON_LOGIN_COOKIE); 
    loginPost.setHeader("User-Agent",  FF_USER_AGENT);

    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
    nameValuePairs.add(new BasicNameValuePair(CARLETON_USER_PARAM, sid));
    nameValuePairs.add(new BasicNameValuePair(CARLETON_PIN_PARAM, pin));
    loginPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    
    httpClient.execute(loginPost, localContext);      
  }
  
  public String getBalance() throws ClientProtocolException, IOException
  {
    String balance     = "";
    HttpGet balanceGet = new HttpGet(CARLETON_BALANCE_URL);
    
    balanceGet.setHeader("Referer",     CARLETON_LOGIN_REF);
    balanceGet.setHeader("User-Agent",  FF_USER_AGENT);

    HttpResponse response = httpClient.execute(balanceGet, localContext);
    
    InputStream       input = response.getEntity().getContent();
    InputStreamReader isr   = new InputStreamReader(input);
    BufferedReader    br    = new BufferedReader(isr);
    String            line  = null;
    
    while((line = br.readLine()) != null) 
    {
      Matcher m = HTML_BALANCE_PATTERN.matcher(line);
      
      if(m.matches())
        balance = m.group(1);
    }
    
    return balance;
  }

  /**
   * An SSLSocketFactory that relies on the default trust store.
   */
  private class DefaultKeyStoresSSLSocketFactory extends SSLSocketFactory
  {
    protected SSLContext sslContext = SSLContext.getInstance("SSLv3");

    public DefaultKeyStoresSSLSocketFactory() throws NoSuchAlgorithmException,
        KeyManagementException, KeyStoreException, UnrecoverableKeyException
    {
      super(null, null, null, null, null, null);
      
      final TrustManagerFactory original = TrustManagerFactory.getInstance(
                                              TrustManagerFactory.getDefaultAlgorithm()
                                           );
      original.init((KeyStore) null);
      
      X509TrustManager defaultManager = null;
      
      for (TrustManager tm : original.getTrustManagers())
        if (tm instanceof X509TrustManager)
          defaultManager = (X509TrustManager) tm;
      
      sslContext.init(null, new TrustManager[] { defaultManager }, null);   
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException
    {
      SSLSocket s = (SSLSocket) sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
      
      //TODO: Figure out a way to not rely on this hack to disable TLS handshake.
      s.setEnabledProtocols(new String[] { "SSLv3" });
      
      return s;
    }

    @Override
    public Socket createSocket() throws IOException
    {
      SSLSocket s = (SSLSocket) sslContext.getSocketFactory().createSocket();
      
      //TODO: Figure out a way to not rely on this hack to disable TLS handshake.
      s.setEnabledProtocols(new String[] { "SSLv3" });

      return s;
    }
  }
}