package com.shoperino.starter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.shoperino.shoperino.R;

public class LocationTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        TextView tw = (TextView) findViewById(R.id.location);

        while (true)
        {
            ParseUser curr = ParseUser.getCurrentUser();
            ParseGeoPoint gp = curr.getParseGeoPoint("location");
            tw.setText(gp.getLatitude() + " " + gp.getLongitude());
        }
    }

}
