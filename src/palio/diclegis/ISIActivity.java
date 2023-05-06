/**
 * @(#)ISIActivity.java
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

public class ISIActivity extends PatientInfoActivity implements PalioWebView.ScrollListener, View.OnClickListener
{
	Button next;
	PalioWebView wv;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.isi_complete_screen);
        setTitle(getResources().getString(R.string.isi));
        wv = (PalioWebView) findViewById(R.id.isi_complete_content);
        next = (Button)findViewById(R.id.isi_accept);
        next.setOnClickListener(this);
        wv.addScrollListener(this);
        wv.loadUrl("http://blog.kahrlconsulting.com/device-news/");// need real content
        
        if (!dayPassed) next.setEnabled(true);
        if (getPatientInfo().firstStart) next.setEnabled(false);
      
    }
    
    public void onScrollToBottom()
    {
    	next.setEnabled(true);
    }
    
    public void onClick(View v) 
    {
        startActivity("GetStartedActivity");
    }
    
}