/**
 * @(#)GraphAcvtivity.java
 *
 *
 * @author Mark Kahrl
 * @version 1.00 2013/5/16
 */
package palio.diclegis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.series.XYSeries;
import com.androidplot.xy.*;

public class GraphAcvtivity extends PatientInfoActivity implements View.OnClickListener
{

	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_screen);
        XYPlot mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
    }
    
    public void onClick(View v) 
    {
    	
    }
    
    
}