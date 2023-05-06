/**
 * @(#)PalioWebViewClient.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/5/13
 */
package palio.diclegis;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PalioWebViewClient extends WebViewClient
{
	private PalioWebViewClientCallback callback;
	public PalioWebViewClient(PalioWebViewClientCallback callback)
	{
		this.callback = callback;	
	}
	
	@Override
	public void onReceivedError (WebView view, int errorCode, String description, String failingUrl)
	{
		super.onReceivedError (view, errorCode, description, failingUrl);
		callback.onPageLoadError();
	}
	
	@Override
	public void onPageFinished(WebView view, String url)
	{
	    super.onPageFinished(view, url);
		if (callback!=null)callback.onPageLoadFinish();
	}
	
	public interface PalioWebViewClientCallback
	{
		public void onPageLoadError();
		public void onPageLoadFinish();
	}
}