package com.example.jorgenskevik.e_cardholders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


import java.util.EnumMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorgenskevik.e_cardholders.models.SessionManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;
import com.theartofdev.edmodo.cropper.CropImageView;


/**
 * The type Bar code activity.
 */
public class BarCodeActivity extends Activity{
    TextView button_back;
    TextView continue_picture;
    EditText picture_token, codeString;
    HashMap<String, String> userDetails;
    SessionManager sessionManager;
    String fourDigits;
    Context context;
    int duration;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.picture_info);

        button_back = (TextView) findViewById(R.id.button_back);
        continue_picture = (TextView) findViewById(R.id.button_ok);
        codeString = (EditText) findViewById(R.id.code_picture);

        sessionManager = new SessionManager(getApplicationContext());
        userDetails = sessionManager.getUserDetails();
        fourDigits = userDetails.get(SessionManager.KEY_PICTURETOKEN);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);



        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(BarCodeActivity.this, UserActivity.class);
                startActivity(back);
            }
        });

    }
    public void open_picture_view(View v){
        if (fourDigits.trim().equals(codeString.getText().toString())){
            Intent back = new Intent(BarCodeActivity.this, Picture_info.class);
            startActivity(back);
        }else {
            context = getApplicationContext();
            duration = Toast.LENGTH_SHORT;
            toast = Toast.makeText(context, R.string.wrongCode, duration);
            toast.show();
        }

    }
}


