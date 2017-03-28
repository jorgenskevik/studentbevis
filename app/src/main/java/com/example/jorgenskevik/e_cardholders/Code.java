package com.example.jorgenskevik.e_cardholders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.example.jorgenskevik.e_cardholders.models.SessionManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;

import static com.example.jorgenskevik.e_cardholders.R.layout.codepopup;

/**
 * Created by jorgenskevik on 24.03.2017.
 */

public class Code extends Activity{

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Session class instance
        session = new SessionManager(getApplicationContext());


        setContentView(R.layout.codepopup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int height = dm.heightPixels;
        TextView tv1 = (TextView)findViewById(R.id.textView6);
        //tv1.setText(userIdPrefs.getString("id",null));

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // id
        String id = user.get(SessionManager.KEY_ID);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        // displaying user data
        tv1.setText(name);



        getWindow().setLayout((int) (width*.9) ,(int) (height*.7));
    }

    public void close(View v){
        Intent intent = new Intent(Code.this, Main3Activity.class);
        startActivity(intent);
    }
}

