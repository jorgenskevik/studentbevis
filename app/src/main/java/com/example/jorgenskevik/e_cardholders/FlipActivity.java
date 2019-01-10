package com.example.jorgenskevik.e_cardholders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jorgenskevik.e_cardholders.models.SessionManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class FlipActivity extends AppCompatActivity {

    TextView Name, School, Student_number, Class, Phone, Email, Exp_date, Valid_card, date_of_birth;
    String Name_string, School_string, Student_number_string, Class_string,
            Phone_string, Email_string, Exp_date_string, Valid_date_string, formattedDate, date_of_birth_string, expirationDateString;
    SessionManager sessionManager;
    HashMap<String, String> userDetails, unit_details, unit_membership_details;
    Date start_date, date;
    RelativeLayout r1;
    SimpleDateFormat simpleDateFormat;
    DateFormat targetFormat;
    ImageView cancel_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.changeStatusBarColor();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        Name = findViewById(R.id.textView23);

        School = findViewById(R.id.textView8);
        Student_number = findViewById(R.id.textView21);
        Class = findViewById(R.id.textView25);
        Phone = findViewById(R.id.textView28);
        Email = findViewById(R.id.textView31);
        Exp_date = findViewById(R.id.textView24);
        Valid_card = findViewById(R.id.textView2);
        cancel_button = findViewById(R.id.imageView3);
        date_of_birth = findViewById(R.id.date_of_birth);
        r1 = findViewById(R.id.background);

        sessionManager = new SessionManager(getApplicationContext());

        userDetails = sessionManager.getUserDetails();
        unit_details = sessionManager.getUnitDetails();
        unit_membership_details = sessionManager.getUnitMemberDetails();

        Name_string = userDetails.get(SessionManager.KEY_FULL_NAME);
        School_string = unit_details.get(SessionManager.KEY_UNIT_NAME);
        Student_number_string = unit_membership_details.get(SessionManager.KEY_STUDENTNUMBER);
        Class_string = unit_membership_details.get(SessionManager.KEY_STUDENT_CLASS);
        Phone_string = userDetails.get(SessionManager.KEY_PHONE_NUMBER);
        Email_string = userDetails.get(SessionManager.KEY_EMAIL);
        Exp_date_string = unit_membership_details.get(SessionManager.KEY_EXPERATIONDATE);
        date_of_birth_string = userDetails.get(SessionManager.KEY_STUDENTNUMBER);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);

        Name.setText(Name_string);
        School.setText(School_string);
        Student_number.setText(Student_number_string);
        Class.setText(Class_string);
        Phone.setText(Phone_string);
        Email.setText(Email_string);
        Exp_date.setText(Exp_date_string);
        date_of_birth.setText(date_of_birth_string);

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_card_view = new Intent(FlipActivity.this, UserActivity.class);
                startActivity(to_card_view);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_up);
            }
        });

        expirationDateString = unit_membership_details.get(SessionManager.KEY_EXPERATIONDATE);

        start_date = null;
        try {
            start_date = simpleDateFormat.parse(expirationDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (System.currentTimeMillis() > start_date.getTime()) {
            //Ugyldig
            r1.setBackgroundColor(ContextCompat.getColor(this, R.color.invalid_backgroud));
            Valid_card.setText(R.string.expired);
        } else {
            //gyldig
            r1.setBackgroundColor(ContextCompat.getColor(this, R.color.valid_backgroud));

            targetFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.GERMANY);
            try {
                if(Calendar.getInstance().get(Calendar.MONTH) + 1 < 8){
                    r1.setBackgroundColor(ContextCompat.getColor(this, R.color.valid_backgroud));
                    Valid_card.setText(getResources().getString(R.string.spring) + " " + Calendar.getInstance().get(Calendar.YEAR));

                }else{
                    r1.setBackgroundColor(ContextCompat.getColor(this, R.color.valid_backgroud));
                    Valid_date_string = unit_membership_details.get(SessionManager.KEY_EXPERATIONDATE);
                    date = simpleDateFormat.parse(Valid_date_string);
                    formattedDate = targetFormat.format(date);
                    Valid_card.setText(getResources().getString(R.string.fall) + " " + Calendar.getInstance().get(Calendar.YEAR));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    private void changeStatusBarColor(){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(FlipActivity.this,R.color.black));
        }
    }
}
