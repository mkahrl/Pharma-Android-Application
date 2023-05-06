/**
 * @(#)DashboardActivity.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/5/7
 */
package palio.diclegis;

import android.app.TabActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TabHost;

public class DashboardActivity extends TabActivity /// TabActivity  Depreactes at API Level 13, need to replace with FragmentActivity at higer sdk level
/* Tabs and TabActivity need to be replaced with Fragments and FragmentActivity for tablets and ICS support.
 * ToDo: implement PatientInfoInterface since this Activity needs to extends 'Tab' or 'Fragment' activities.*/
{
    TabHost tabHost;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_screen);
        setTitle(getResources().getString(R.string.dashboard));
        tabHost = getTabHost();
        
        TabHost.TabSpec dashboard = tabHost.newTabSpec("dash");
        dashboard.setIndicator(getResources().getString(R.string.dash_board));
        Intent intent = new Intent();
    	intent.setClassName(getPackageName(), getPackageName()+".AboutActivity"); ///  set about activity as place holder for now.
        dashboard.setContent(intent);
        tabHost.addTab(dashboard);
        
        ////////////  Todo: need new activities for each below, need to set up TabWidget as well.
        TabHost.TabSpec faq = tabHost.newTabSpec("faq");
        faq.setIndicator(getResources().getString(R.string.faq));
        faq.setContent(intent);
        tabHost.addTab(faq);
        
        TabHost.TabSpec tips = tabHost.newTabSpec("tips");
        tips.setIndicator(getResources().getString(R.string.tips));
        tips.setContent(intent);
        tabHost.addTab(tips);
        
        TabHost.TabSpec copay = tabHost.newTabSpec("copay");
        copay.setIndicator(getResources().getString(R.string.copay));
        copay.setContent(intent);
        tabHost.addTab(copay);
        
        TabHost.TabSpec more = tabHost.newTabSpec("more");
        more.setIndicator(getResources().getString(R.string.more));
        more.setContent(intent);
        tabHost.addTab(more);
    }
}
