package com.example.jorgenskevik.e_cardholders;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import static android.R.id.message;

/**
 * The type Second activity.
 */
public class SecondActivity extends Activity {
    /**
     * The constant phoneNumber.
     */
    public static final String phoneNumber = "tel:72894940";
    /**
     * The constant web.
     */
    public static final String web = "http://www.kortfri.no";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_second);
    }

    /**
     * Open phone.
     *
     * @param view the view
     */
    public void OpenPhone(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(phoneNumber));
        startActivity(intent);
    }

    /**
     * Open mail.
     *
     * @param view the view
     */
    public void OpenMail(View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_TEXT, message);
        Intent mailer = Intent.createChooser(intent, null);
        startActivity(mailer);
    }

    /**
     * Open browser.
     *
     * @param view the view
     */
    public void openBrowser(View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(web)));
    }
}
