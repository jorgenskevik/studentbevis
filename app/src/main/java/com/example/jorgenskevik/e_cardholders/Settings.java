package com.example.jorgenskevik.e_cardholders;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;

import com.baoyz.actionsheet.ActionSheet;
import com.digits.sdk.android.Digits;
import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;
import com.example.jorgenskevik.e_cardholders.models.SessionManager;

import java.util.HashMap;

/**
 * Created by jorgenskevik on 27.03.2017.
 */
public class Settings extends Activity{
    /**
     * The Session manager.
     */
    SessionManager sessionManager;
    private static double widthSize = 0.9;
    private static double heightSize = 0.7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);
        int i = 0;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        sessionManager = new SessionManager(getApplicationContext());


        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width*widthSize) ,(int) (height*heightSize));


        Button termsButton = (Button)findViewById(R.id.button7);
        termsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, Main2Activity.class));
            }
        });
        Button logoutButton = (Button)findViewById(R.id.button8);
        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Close settings.
     *
     * @param v the v
     */
    public void closeSettings(View v){
        Intent intent = new Intent(Settings.this, Main3Activity.class);
        startActivity(intent);
    }
}
