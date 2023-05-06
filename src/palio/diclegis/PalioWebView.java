/**
 * @(#)PalioWebView.java
 *
 *
 * @author Marrk Kahrl
 * @version 1.00 2013/5/2
 */
package palio.diclegis;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class PalioWebView extends WebView implements PalioWebViewClient.PalioWebViewClientCallback 
/// extends WebView and adds support for a scroll listener, web client methods
{
	private long pageLoadTime = 60*1000;
	private PalioWebView.ScrollListener listener;
	private PageLoadListener pageLoadListener;
	private int defaultContent = R.raw.default_content;
	private Context ctx;
	
	public PalioWebView(Context ctx)
	{
		super(ctx);
		this.ctx=ctx;
		setWebViewClient(new PalioWebViewClient(this));
		
	}
	
	public PalioWebView(Context ctx, AttributeSet attrs)
	{
		super(ctx, attrs);
		this.ctx=ctx;
		setWebViewClient(new PalioWebViewClient(this));
	}
	
	public void addPageLoadListener(PageLoadListener listener)
	{
		pageLoadListener=listener;
	}
	
	public void addScrollListener(PalioWebView.ScrollListener listener)
	{
		this.listener=listener;
	}
	
	protected void onOverScrolled (int scrollX, int scrollY, boolean clampedX, boolean clampedY)
	{
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		if (clampedY && (scrollY > 0)) if (listener!=null) listener.onScrollToBottom();
	}
	
	public interface ScrollListener
	{
		public void onScrollToBottom();
	}
	
	public void setDefaultContent(int contentId)
	{
		defaultContent = contentId;
	}
	
	private void loadDefaultContent()
	{
		loadData(readRawTextFile(defaultContent), "text/html", "UTF-8");
	}
	
	public void onPageLoadFinish()
	{
		if (pageLoadListener!=null)  pageLoadListener.onPageLoadComplete();
	}
	
	public void onPageLoadError()
	{
		loadDefaultContent();
	}
	
	public String readRawTextFile(int resId)
    {
          InputStream inputStream = ctx.getResources().openRawResource(resId);
          InputStreamReader inputreader = new InputStreamReader(inputStream);
          BufferedReader buffreader = new BufferedReader(inputreader);
          String line;
          StringBuilder text = new StringBuilder();
		  try 
		  	{
                while (( line = buffreader.readLine()) != null) 
                {
                    text.append(line);
                    text.append('\n');
                }
            } 
            catch (Exception e) 
            {
                return null;
            }
            return text.toString();
     }
}
