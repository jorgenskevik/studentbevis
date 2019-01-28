package com.example.jorgenskevik.e_cardholders;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.jorgenskevik.e_cardholders.models.SessionManager;

import java.util.HashMap;

public class Contact_info extends Activity {
    TextView phone, email, kortfri;
    String phone_string, email_string;
    Button ok;
    SessionManager sessionManager;
    HashMap<String, String> unitDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contactschool);

        phone = findViewById(R.id.editText);
        email = findViewById(R.id.editText5);
        kortfri = findViewById(R.id.textView555);
        ok = findViewById(R.id.button2);

        sessionManager = new SessionManager(getApplicationContext());
        unitDetails = sessionManager.getUnitDetails();

        phone_string = unitDetails.get(SessionManager.KEY_PUBLIC_CONTACT_PHONE);
        email_string = unitDetails.get(SessionManager.KEY_PUBLIC_CONTACT_EMAIL);

        phone.setText(phone_string);
        email.setText(email_string);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Contact_info.this, UserActivity.class);
                startActivity(intent);
            }
        });

        kortfri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.kortfri.no/"));
                startActivity(browserIntent);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String helping_string = "tel:" + phone_string;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(helping_string));
                startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email_string, null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

    }
}
