package com.bk.beaconapplication;


import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import java.util.List;
import java.util.UUID;

/*

    Developed By Nuh Burak Karakaya
    Project was Licanced with GPL
    Available for Personal Usage
    Permission must be obtained for Commercial Usage
    for contact please send an email to burakkarakaya10@gmail.com

*/

public class MainActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private Region region;
    TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView img = (ImageView)findViewById(R.id.fullscreen);
        txtInfo = (TextView)findViewById(R.id.txtInfo);
        beaconManager = new BeaconManager(this);
        beaconManager.setForegroundScanPeriod(4000,4000);
        beaconManager.setBackgroundScanPeriod(4000,4000);
        beaconManager.setRangingListener(new BeaconManager.RangingListener()
        {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list)
            {
               if(!list.isEmpty())
               {
                   Beacon nearest = list.get(0);
                   String beaconKey = String.format("%d:%d", nearest.getMajor(), nearest.getMinor());

                   if (beaconKey.equals("0:0"))
                   {
                        img.setBackgroundColor(Color.BLUE);
                        txtInfo.setText(beaconKey);
                   }
                   else if(beaconKey.equals("1:1"))
                   {
                       img.setBackgroundColor(Color.RED);
                       txtInfo.setText(beaconKey);
                   }
                   else
                   {
                       txtInfo.setText("Foreign Beacon:"+beaconKey);
                       img.setBackgroundColor(Color.YELLOW);

                   }
               }
               else
               {
                   img.setBackgroundColor(Color.GRAY);
                   txtInfo.setText("We could not find any beacon!");
               }
            }
        });
        region = new Region("ranged region", UUID.fromString("YOUR_BEACON_UUID"), null, null);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause()
    {
        //beaconManager.stopRanging(region);
        super.onPause();
    }
}
