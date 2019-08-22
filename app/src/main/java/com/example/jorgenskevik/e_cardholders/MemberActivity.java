package com.example.jorgenskevik.e_cardholders;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.aromajoin.actionsheet.ActionSheet;
import com.aromajoin.actionsheet.OnActionListener;
import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;
import com.example.jorgenskevik.e_cardholders.models.Login_model;
import com.example.jorgenskevik.e_cardholders.models.SessionManager;
import com.example.jorgenskevik.e_cardholders.models.Unit;
import com.example.jorgenskevik.e_cardholders.models.UnitLinks;
import com.example.jorgenskevik.e_cardholders.models.UnitMembership;
import com.example.jorgenskevik.e_cardholders.models.User;
import com.example.jorgenskevik.e_cardholders.remote.UserAPI;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MemberActivity extends AppCompatActivity {
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    SessionManager sessionManager;
    TextView company_name_tv, name_customer_tv, stilling_tv, membership_number_tv, membership, valid;
    View card;
    private static ArrayList<UnitLinks> mProductArrayList = new ArrayList<UnitLinks>();

    Bitmap bitmap;
    boolean dirY = true;
    Date startDate;
    int role_int, unit_membership_id_int;
    ImageView settings, swipe, comp_logo, not_valid, barcode;
    SimpleDateFormat simpleDateFormat;
    HashMap<String, String> userDetails, unit_details, unit_membership_details;
    String comp_name_string, comp_name_short_string, card_type_string, logo_string, small_logo_string, user_id_string,
            name_customer_string, expirationDateString, authenticateString, email_string, picture_token_string, birth_date_string,
            picture_string, phone_string, expiration_string, public_contact_phone_string, public_contact_email_string,
            student_class_string, student_number_string, membership_number_string_new, membership_type_string, help_string,
            contact_help_string, primary_color, secondary_color;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Setting black status bar
        this.changeStatusBarColor();
//        Window window = UserActivity.this.getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(ContextCompat.getColor(UserActivity.this,R.color.black));

        setContentView(R.layout.testmembership);

        sessionManager = new SessionManager(getApplicationContext());
        //Logo of club
        comp_logo = findViewById(R.id.logo);
        //The card
        card = findViewById(R.id.coloring);
        //the settings button
        settings = findViewById(R.id.settings);
        //Text membership
        membership = findViewById(R.id.medlemskort);
        //Text name of club
        company_name_tv = findViewById(R.id.compname);
        //Text member number
        membership_number_tv = findViewById(R.id.number);
        //name of user
        name_customer_tv = findViewById(R.id.name);
        //If the card is valid or not
        valid = findViewById(R.id.valid);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);

        userDetails = sessionManager.getUserDetails();
        unit_details = sessionManager.getUnitDetails();
        unit_membership_details = sessionManager.getUnitMemberDetails();
        comp_name_string = unit_details.get(SessionManager.KEY_UNIT_NAME);
        primary_color = unit_details.get(SessionManager.KEY_COLOR_PRIMARY);
        secondary_color = unit_details.get(SessionManager.KEY_COLOR_SECONDARY);
        small_logo_string = unit_details.get(SessionManager.KEY_UNIT_LOGO_SHORT);
        card_type_string = unit_details.get(SessionManager.KEY_CARD_TYPE);
        membership_number_string_new = unit_membership_details.get(SessionManager.KEY_MEMBERSHIP_NUMBER);
        name_customer_string = userDetails.get(SessionManager.KEY_FULL_NAME);
        expirationDateString = unit_membership_details.get(SessionManager.KEY_EXPERATIONDATE);
        logo_string = unit_details.get(SessionManager.KEY_UNIT_LOGO);
        membership_type_string = unit_membership_details.get(SessionManager.KEY_MEMBERSHIP_TYPE);


        if(membership_number_string_new == null){
            swipe.setImageBitmap(null);
            swipe.setOnClickListener(null);
        }

        JodaTimeAndroid.init(this);

        //setting all the data
        Picasso.get().load(logo_string).memoryPolicy(MemoryPolicy.NO_CACHE )
                .networkPolicy(NetworkPolicy.NO_CACHE).into(comp_logo);
        name_customer_tv.setText(name_customer_string);
        company_name_tv.setText(comp_name_string);
        help_string = "Nr: " + membership_number_string_new;
        membership_number_tv.setText(help_string);

        //setting the color
        try{
            name_customer_tv.setTextColor(Color.parseColor(secondary_color));
            company_name_tv.setTextColor(Color.parseColor(secondary_color));
            membership_number_tv.setTextColor(Color.parseColor(secondary_color));
            membership.setTextColor(Color.parseColor(secondary_color));
        } catch (NullPointerException e){
            name_customer_tv.setTextColor(BLACK);
            company_name_tv.setTextColor(BLACK);
            membership_number_tv.setTextColor(BLACK);
            membership.setTextColor(BLACK);
        }


        DrawableCompat.setTint(card.getBackground(), Color.parseColor(primary_color));


        //Drawable mDrawable = getApplicationContext().getResources().getDrawable(R.drawable.valid);
        //mDrawable.setColorFilter(new PorterDuffColorFilter(0xffff00,PorterDuff.Mode.MULTIPLY));


        startDate = null;
        try {
            startDate = simpleDateFormat.parse(expirationDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (System.currentTimeMillis() > startDate.getTime()) {
            //Ugyldig
            valid.setText(getResources().getString(R.string.Not));
            valid.setTextColor(ContextCompat.getColor(this, R.color.invalid_backgroud));
        }else{
            String helping = getResources().getString(R.string.ValidTil) + ": " + expirationDateString;
            valid.setText(helping);
        }
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KVTVariables.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        UserAPI userapi = retrofit.create(UserAPI.class);
        String super_token = "token " + userDetails.get(SessionManager.KEY_TOKEN);
        final int unit_id = Integer.parseInt(unit_details.get(SessionManager.KEY_UNIT_ID));
        userDetails = sessionManager.getUserDetails();
        userapi.unitLinks(super_token, String.valueOf(unit_id)).enqueue(new Callback<List<UnitLinks>>() {
            @Override
            public void onResponse(@NonNull Call<List<UnitLinks>> call, @NonNull Response<List<UnitLinks>> response) {
                List<UnitLinks> unit_links = response.body();
                mProductArrayList.clear();
                mProductArrayList.addAll(unit_links);
            }

            @Override
            public void onFailure(@NonNull Call<List<UnitLinks>> call, @NonNull Throwable t) {
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.press));
                showActionSheet(view);
            }
        });
    }

    private void changeStatusBarColor(){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(MemberActivity.this,R.color.black));
        }
    }

    private void showActionSheet(View anchor) {
        final ActionSheet actionSheet = new ActionSheet(this);
        actionSheet.setTitle(getResources().getString(R.string.chooise));
        actionSheet.setSourceView(anchor);

        for (int i = 0; i <mProductArrayList.size() ; i++) {
            final int finalI = i;
            actionSheet.addAction(mProductArrayList.get(i).getName(), ActionSheet.Style.DEFAULT, new OnActionListener() {
                @Override public void onSelected(ActionSheet actionSheet, String title) {
                    performAction(title);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mProductArrayList.get(finalI).getLink()));
                    startActivity(browserIntent);
                    actionSheet.dismiss();
                }
            });
        }

        actionSheet.addAction(getResources().getString(R.string.updateProfile), ActionSheet.Style.DEFAULT, new OnActionListener() {
            @Override public void onSelected(ActionSheet actionSheet, String title) {
                performAction(title);
                updateUser();
                actionSheet.dismiss();
            }
        });

        actionSheet.addAction(getResources().getString(R.string.Privacy), ActionSheet.Style.DEFAULT, new OnActionListener() {
            @Override public void onSelected(ActionSheet actionSheet, String title) {
                performAction(title);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.kortfri.no/privacypolicy.html"));
                startActivity(browserIntent);
                actionSheet.dismiss();
            }
        });

        contact_help_string = getResources().getString(R.string.contactschool) + " " + comp_name_string;
        actionSheet.addAction(contact_help_string, ActionSheet.Style.DEFAULT, new OnActionListener() {
            @Override public void onSelected(ActionSheet actionSheet, String title) {
                performAction(title);
                Intent contact = new Intent(MemberActivity.this, Contact_info.class);
                startActivity(contact);
                actionSheet.dismiss();
            }
        });

        if(!membership_number_string_new.equals("")){
            actionSheet.addAction(getResources().getString(R.string.barcode), ActionSheet.Style.DEFAULT, new OnActionListener() {
                @Override public void onSelected(ActionSheet actionSheet, String title) {
                    performAction(title);
                    Intent barcode = new Intent(MemberActivity.this, Barcode_new.class);
                    startActivity(barcode);
                    actionSheet.dismiss();
                }
            });
        }

        actionSheet.addAction(getResources().getString(R.string.Loggout), ActionSheet.Style.DESTRUCTIVE, new OnActionListener() {
            @Override public void onSelected(ActionSheet actionSheet, String title) {
                performAction(title);
                logout_user();
                actionSheet.dismiss();
            }
        });
        actionSheet.show();
    }

    private void performAction(String title) {
        Snackbar.make(MemberActivity.this.findViewById(android.R.id.content), title,
                Snackbar.LENGTH_SHORT).show();
    }

    public void logout_user(){
        sessionManager.logoutUser();
        Intent intent = new Intent(MemberActivity.this, LandingPage.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }



    public void updateUser(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KVTVariables.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        userDetails = sessionManager.getUserDetails();

        authenticateString = "Bearer " + userDetails.get(SessionManager.KEY_TOKEN);

        UserAPI userapi = retrofit.create(UserAPI.class);
        final int unit_id = Integer.parseInt(unit_details.get(SessionManager.KEY_UNIT_ID));
        String super_token = "token " + userDetails.get(SessionManager.KEY_TOKEN);
        userapi.getUser(super_token, String.valueOf(unit_id)).enqueue(new Callback<Login_model>() {
            @Override
            public void onResponse(@NonNull Call<Login_model> call, @NonNull Response<Login_model> response) {
                if (response.isSuccessful()) {
                    Login_model login_model = response.body();
                    User user = login_model.getUser();
                    Unit unit  = login_model.getUnit();
                    UnitMembership unitMembership = login_model.getUnitMembership();

                    name_customer_string = user.getFullName();
                    email_string = user.getEmail();
                    user_id_string = user.getId();
                    role_int = user.getUser_role();
                    picture_token_string = user.getPicture_token();
                    picture_string = user.getPicture();
                    phone_string = user.getPhone();

                    comp_name_string = unit.getName();
                    card_type_string = unit.getCard_type();
                    comp_name_short_string = unit.getShort_name();
                    public_contact_phone_string = unit.getPublic_contact_phone();
                    public_contact_email_string = unit.getPublic_contact_email();
                    logo_string = unit.getUnit_logo();
                    small_logo_string = unit.getSmall_unit_logo();
                    int unit_id = unit.getId();
                    primary_color = unit.getPrimary_color_rgba();
                    secondary_color = unit.getSecondary_color_rgba();

                    student_class_string = unitMembership.getStudent_class();
                    student_number_string = unitMembership.getStudent_number();
                    unit_membership_id_int = unitMembership.getId();
                    membership_number_string_new = unitMembership.getMembership_number();
                    membership_type_string = unitMembership.getMembership_type();

                    DateTimeFormatter dateTimeFormatter2 = DateTimeFormat.forPattern("yyyy-MM-dd");

                    java.util.Date birthdayDate = user.getDate_of_birth();
                    java.util.Date dateToExpiration = unitMembership.getExpiration_date();

                    DateTime timeBirthday = new DateTime(birthdayDate);
                    DateTime timeToExpiration = new DateTime(dateToExpiration);

                    birth_date_string = dateTimeFormatter2.print(timeBirthday);
                    expiration_string = dateTimeFormatter2.print(timeToExpiration);

                    sessionManager.update_user(name_customer_string, email_string, user_id_string, role_int,
                            picture_token_string, birth_date_string, picture_string, user.isHas_set_picture(), phone_string);

                    sessionManager.create_login_session_unit(comp_name_string, comp_name_short_string, logo_string,
                            small_logo_string, unit_id, public_contact_email_string, public_contact_phone_string, card_type_string, primary_color, secondary_color);

                    sessionManager.create_login_session_unit_member(expiration_string, student_class_string,
                            student_number_string, unit_membership_id_int, membership_number_string_new, membership_type_string);

                    startDate = null;
                    try {
                        startDate = simpleDateFormat.parse(expiration_string);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (System.currentTimeMillis() > startDate.getTime()) {
                        //Ugyldig
                        valid.setText(getResources().getString(R.string.Not));
                        valid.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.invalid_backgroud));
                    }else{
                        String helping = getResources().getString(R.string.ValidTil) + ": " + expirationDateString;
                        valid.setText(helping);
                        valid.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                        Picasso.get().load(logo_string).memoryPolicy(MemoryPolicy.NO_CACHE )
                                .networkPolicy(NetworkPolicy.NO_CACHE).into(comp_logo);
                        name_customer_tv.setText(name_customer_string);
                        company_name_tv.setText(comp_name_string);
                        help_string = "Nr: " + membership_number_string_new;
                        membership_number_tv.setText(help_string);
                    }
                    finish();
                    startActivity(getIntent());

                }else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.userNotUpdated), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Login_model> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.userNotUpdated), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean is_string_empty(String s){
        return s != null && !s.isEmpty() && !s.equals("null");
    }

    Bitmap encodeAsBitmap(String contentsToEncode) throws WriterException {
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, BarcodeFormat.CODE_39, 700, 200, hints);
        } catch (IllegalArgumentException iae) {
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
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

}