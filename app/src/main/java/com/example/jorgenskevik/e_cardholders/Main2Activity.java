package com.example.jorgenskevik.e_cardholders;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class Main2Activity extends Activity {
    public static final int CAM_REQUEST_CODE = 4545;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main2);

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void opencard(View view) {
        String n = Build.VERSION.RELEASE;
        String firstLetter = String.valueOf(n.charAt(0));
        int number = Integer.parseInt(firstLetter);
        if(number < 6){
            Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
            startActivity(intent);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
            startActivity(intent);
        }else{
            String[] permissionRequest = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissionRequest, CAM_REQUEST_CODE);
            Toast.makeText(this, R.string.GiveAccess, Toast.LENGTH_SHORT).show();

        }
    }
}
