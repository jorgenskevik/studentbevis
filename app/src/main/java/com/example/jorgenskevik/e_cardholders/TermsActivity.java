package com.example.jorgenskevik.e_cardholders;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.digits.sdk.android.Digits;
import com.example.jorgenskevik.e_cardholders.models.SessionManager;


/**
 * The type Terms activity.
 */
public class TermsActivity extends Activity {
    /**
     * The constant CAM_REQUEST_CODE.
     */
    public static final int CAM_REQUEST_CODE = 4545;
    /**
     * The constant maxBuildVersion.
     */
    public static final int maxBuildVersion = 6;

    /**
     * The Check box.
     */
    String checkBox = "";
    /**
     * The Session manager.
     */
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.terms_view);
        sessionManager = new SessionManager(getApplicationContext());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9), (int)(height*.8));

        Button cancelbutton = (Button) findViewById(R.id.cancelbutton);

        cancelbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermsActivity.this, LandingPage.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void openCard(View view) {
        String buildVersion = Build.VERSION.RELEASE;
        String firstLetter = String.valueOf(buildVersion.charAt(0));
        int number = Integer.parseInt(firstLetter);
        if(number < maxBuildVersion){
            Intent intent = new Intent(TermsActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(TermsActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            if(number < maxBuildVersion){

            }else{
                String[] permissionRequest = {Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissionRequest, CAM_REQUEST_CODE);
                Toast.makeText(this, R.string.GiveAccess, Toast.LENGTH_LONG).show();
            }

        }
    }
}
