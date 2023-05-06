/**
 * @(#)AboutActivity.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/4/24
 */
package palio.diclegis;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class AboutActivity extends PatientInfoActivity implements PalioWebView.ScrollListener, View.OnClickListener
{
    Button next;
    PalioWebView wv;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_screen);
        setTitle(getResources().getString(R.string.about));
        wv = (PalioWebView) findViewById(R.id.about_content);
        wv.setDefaultContent(R.raw.default_about);
        next = (Button)findViewById(R.id.about_next);
        wv.loadUrl("http://sector7studio.com/lipsum_remote/lipsum_remote.html");// need real content
        next.setOnClickListener(this);
        wv.addScrollListener(this);
    }
    
    public void onScrollToBottom()
    {
    	next.setEnabled(true);
    }
    
    public void onClick(View v) 
    {
        startActivity("PrivacyActivity");
    }
}
