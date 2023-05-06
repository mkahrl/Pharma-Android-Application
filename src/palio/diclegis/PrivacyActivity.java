/**
 * @(#)PrivacyActivity.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/4/26
 */
package palio.diclegis;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class PrivacyActivity extends PatientInfoActivity implements PalioWebView.ScrollListener, View.OnClickListener
{
	Button next;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_screen);
        setTitle(getResources().getString(R.string.privacy_agreement));
        PalioWebView wv = (PalioWebView) findViewById(R.id.privacy_content);
        next = (Button)findViewById(R.id.privacy_accept);
        next.setOnClickListener(this);
        wv.addScrollListener(this);
        wv.setDefaultContent(R.raw.default_privacy);
        wv.loadUrl("http://snowpilot.org/");// need real content
    }
    
    public void onScrollToBottom()
    {
    	next.setEnabled(true);
    }
    
    public void onClick(View v) 
    {
        startActivity("TandCActivity");
    }
}