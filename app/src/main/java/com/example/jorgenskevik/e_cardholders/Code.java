package com.example.jorgenskevik.e_cardholders;

import android.app.Activity;
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.codepopup);

        LinearLayout l = new LinearLayout(this);
        l.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        l.setOrientation(LinearLayout.VERTICAL);

        setContentView(l);
        session = new SessionManager(getApplicationContext());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        TextView tv1 = new TextView(this);
        tv1.setText(R.string.barcode);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextSize(32);
        tv1.setTextColor(BLACK);



        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String id = user.get(SessionManager.KEY_STUDENTNUMBER);


        // barcode image
        Bitmap bitmap = null;
        ImageView iv = new ImageView(this);
        try {

            bitmap = encodeAsBitmap(id, BarcodeFormat.CODE_39, 700, 200);
            iv.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }




        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1000, 1000);
        iv.setLayoutParams(layoutParams);
        layoutParams.gravity=Gravity.CENTER;
        l.addView(tv1);
        l.addView(iv);


        Button cancel = new Button(this);
        cancel.setText(R.string.CancelBarCode);

        cancel.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                Intent intent = new Intent(Code.this, Main3Activity.class);
                startActivity(intent);
            }
        });

        l.addView(cancel);

        getWindow().setLayout((int) (width*.9) ,(int) (height*.7));
    }

    public void close(View v){
        Intent intent = new Intent(Code.this, Main3Activity.class);
        startActivity(intent);
    }

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

}


