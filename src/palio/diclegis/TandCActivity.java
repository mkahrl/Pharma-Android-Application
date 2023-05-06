/**
 * @(#)TandCActivity.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/5/2
 */
package palio.diclegis;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class TandCActivity extends PatientInfoActivity implements PalioWebView.ScrollListener , View.OnClickListener
{
	Button next;
	PalioWebView wv;
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tandc_screen);
        setTitle(getResources().getString(R.string.tandc));
        wv = (PalioWebView) findViewById(R.id.tandc_content);
        next = (Button)findViewById(R.id.tandc_accept);
        next.setOnClickListener(this);
        wv.addScrollListener(this);
        wv.loadUrl("http://rockandroadrealty.com/");// need real content
    }
    
    public void onScrollToBottom()
    {
    	next.setEnabled(true);
    }
    
    public void onClick(View v) 
    {
        startActivity("ISIActivity");
    }
}