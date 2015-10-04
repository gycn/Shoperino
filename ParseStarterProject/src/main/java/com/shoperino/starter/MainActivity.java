/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.shoperino.starter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.shoperino.shoperino.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;


public class MainActivity extends ActionBarActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
    ParseUser curr = ParseUser.getCurrentUser();

    showToast(getApplicationContext(),"start");
    //if (curr == null) {
      final Button button = (Button) findViewById(R.id.login_button);

      button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Collection<String> permissions = new ArrayList<String>();
          ParseFacebookUtils.initialize(v.getContext());
          ParseFacebookUtils.logInWithReadPermissionsInBackground((Activity) v.getContext(), permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
              if (user == null) {
                showToast(getApplicationContext(),"canceled");
              } else if (user.isNew()) {
                user.saveInBackground();
                showToast(getApplicationContext(),"successful login");
              } else {
                showToast(getApplicationContext(),"successful login");
              }
            }
          });
        }
      });
    /*}
    else
    {

      showToast(getApplicationContext(),"already logged in "+curr.toString());
    }*/

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void showToast(Context context, String msg)
  {

    int duration = Toast.LENGTH_SHORT;

    Toast toast = Toast.makeText(context, msg, duration);
    toast.show();
  }
}
