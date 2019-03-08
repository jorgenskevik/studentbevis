package com.example.jorgenskevik.e_cardholders;

import android.Manifest;
import android.annotation.SuppressLint;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.aromajoin.actionsheet.ActionSheet;
import com.aromajoin.actionsheet.OnActionListener;
import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;
import com.example.jorgenskevik.e_cardholders.models.Login_model;
import com.example.jorgenskevik.e_cardholders.models.SessionManager;
import com.example.jorgenskevik.e_cardholders.models.Unit;
import com.example.jorgenskevik.e_cardholders.models.UnitMembership;
import com.example.jorgenskevik.e_cardholders.models.User;
import com.example.jorgenskevik.e_cardholders.remote.UserAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import net.danlew.android.joda.JodaTimeAndroid;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type User activity.
 */
public class UserActivity1 extends AppCompatActivity {
    /**
     * The constant CAM_REQUEST_CODE.
     */
    public static final int CAM_REQUEST_CODE = 457843;
    String STUDENT_CARD;
    /**
     * The Session manager.
     */
    SessionManager sessionManager;

    ImageView profile_picture,
    short_logo_view,

    settings_button,

    info_button;
    TextView firstAndSirName, /**
     * The Student id.
     */

    card_type,
    studentId,

    birthToId,

    short_school_name,
    /**
     * The Birth day.
     */
    BirthDay;
    /**
     * The First and sir name string.
     */
    String firstAndSirNameString, /**
     * The This exp date.
     */
    thisExpDate,

    help_string1,

    help_string2,

    small_logo,

    card_type_string,


    birthdayString, /**
     * The Student id string.
     */
    studentIDString, /**
     * The Path.
     */
    path, /**
     * The Picture.
     */
    picture,

    buildVersion,
    firstLetter;
    String expirationDateString,
    mediaPath,

    short_school_name_string,
    /**
     * The Formatted date.
     */
    formattedDate,
    authenticateString;
    /**
     * The Simple date format.
     */
    SimpleDateFormat simpleDateFormat;
    /**
     * The Start date.
     */
    Date startDate, /**
     * The Date.
     */
    date;
    /**
     * The Selected color.
     */
    int
    duration, /**
     * The Number.
     */
    number, /**
     * The Column index.
     */
    columnIndex;
    /**
     * The Target format.
     */
    DateFormat targetFormat;
    /**
     * The Intent.
     */
    Intent intent;
    /**
     * The User details.
     */
    HashMap<String, String> userDetails, unit_details, unit_membership_details;
    /**
     * The Context.
     */
    Context context;
    /**
     * The Toast.
     */
    Toast toast;
    RelativeLayout r1;

    String date_of_birth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.changeStatusBarColor();
        setContentView(R.layout.user_view2);

        sessionManager = new SessionManager(getApplicationContext());
        userDetails = sessionManager.getUserDetails();
        unit_details = sessionManager.getUnitDetails();
        unit_membership_details = sessionManager.getUnitMemberDetails();

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);
        targetFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.GERMANY);

        JodaTimeAndroid.init(this);

        r1 = findViewById(R.id.background);
        profile_picture = findViewById(R.id.sircle);
        short_logo_view = findViewById(R.id.short_logo);
        card_type = findViewById(R.id.skolebevis);
        firstAndSirName = findViewById(R.id.textView11);
        short_school_name = findViewById(R.id.textView16);
        BirthDay = findViewById(R.id.textView2);
        studentId = findViewById(R.id.textView17);
        birthToId = findViewById(R.id.textView13);
        settings_button = findViewById(R.id.settings);
        info_button = findViewById(R.id.imageView5);

        //Get values from session manager
        short_school_name_string = unit_details.get(SessionManager.KEY_UNIT_SHORT_NAME);
        small_logo = unit_details.get(SessionManager.KEY_UNIT_LOGO_SHORT);
        card_type_string = unit_details.get(SessionManager.KEY_CARD_TYPE);
        date_of_birth = userDetails.get(SessionManager.KEY_BIRTHDATE);
        studentIDString = unit_membership_details.get(SessionManager.KEY_STUDENTNUMBER);
        firstAndSirNameString = userDetails.get(SessionManager.KEY_FULL_NAME);
        birthdayString = userDetails.get(SessionManager.KEY_BIRTHDATE);
        picture = userDetails.get(SessionManager.KEY_PICTURE);
        path = userDetails.get(SessionManager.KEY_PATH);
        expirationDateString = unit_membership_details.get(SessionManager.KEY_EXPERATIONDATE);
        STUDENT_CARD = unit_details.get(SessionManager.KEY_CARD_TYPE);

        startDate = null;


        set_attributes(date_of_birth, studentIDString, STUDENT_CARD, picture, path,
                small_logo, firstAndSirNameString, short_school_name_string, startDate, expirationDateString, simpleDateFormat, r1);

        info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent send_to_information = new Intent(UserActivity1.this, FlipActivity.class);
                startActivity(send_to_information);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_in);
            }
        });

        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                showActionSheet(view);
            }
        });
    }

    private void changeStatusBarColor(){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(UserActivity1.this,R.color.black));
        }
    }
    private void showActionSheet(View anchor) {
        ActionSheet actionSheet = new ActionSheet(this);
        actionSheet.setTitle(getResources().getString(R.string.chooise));
        actionSheet.setSourceView(anchor);

        userDetails = sessionManager.getUserDetails();
        picture = userDetails.get(SessionManager.KEY_PICTURE);

        if (!(picture != null && !picture.contains("/img/white_image.png") && !picture.contains("/img/avatar.png"))) {
            actionSheet.addAction(getResources().getString(R.string.setPic), ActionSheet.Style.DEFAULT, new OnActionListener() {
                @Override public void onSelected(ActionSheet actionSheet, String title) {
                    performAction(title);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            userDetails = sessionManager.getUserDetails();
                            picture = userDetails.get(SessionManager.KEY_PICTURE);
                            if (picture != null && !picture.contains("/img/white_image.png") && !picture.contains("/img/avatar.png")) {
                                context = getApplicationContext();
                                duration = Toast.LENGTH_SHORT;
                                toast = Toast.makeText(context, getResources().getString(R.string.DenyPicture), duration);
                                toast.show();
                            } else {
                                Intent infoIntent = new Intent(UserActivity1.this, BarCodeActivity.class);
                                startActivity(infoIntent);
                            }
                        }
                    }
                    actionSheet.dismiss();
                }
            });
        }

        if(!studentIDString.equals("")){
            actionSheet.addAction(getResources().getString(R.string.barcode), ActionSheet.Style.DEFAULT, new OnActionListener() {
                @Override public void onSelected(ActionSheet actionSheet, String title) {
                    performAction(title);
                    Intent barcode = new Intent(UserActivity1.this, Barcode_new.class);
                    startActivity(barcode);
                    actionSheet.dismiss();
                }
            });
        }

        actionSheet.addAction(getResources().getString(R.string.contactschool), ActionSheet.Style.DEFAULT, new OnActionListener() {
            @Override public void onSelected(ActionSheet actionSheet, String title) {
                performAction(title);
                Intent contact = new Intent(UserActivity1.this, Contact_info.class);
                startActivity(contact);
                actionSheet.dismiss();
            }
        });
        actionSheet.addAction(getResources().getString(R.string.updateProfile), ActionSheet.Style.DEFAULT, new OnActionListener() {
            @Override public void onSelected(ActionSheet actionSheet, String title) {
                performAction(title);
                updateUser();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.userUpdated), Toast.LENGTH_SHORT).show();
                actionSheet.dismiss();
            }
        });

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
        Snackbar.make(UserActivity1.this.findViewById(android.R.id.content), title,
                Snackbar.LENGTH_SHORT).show();
    }

    public void logout_user(){
        sessionManager.logoutUser();
        intent = new Intent(UserActivity1.this, LandingPage.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        buildVersion = Build.VERSION.RELEASE;
        firstLetter = String.valueOf(buildVersion.charAt(0));
        number = Integer.parseInt(firstLetter);
        if (number < 6) {
            System.gc();
            if (data == null) {
                return;
            }
            android.net.Uri imageUri = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            columnIndex = cursor.getColumnIndex(filePath[0]);
            mediaPath = cursor.getString(columnIndex);
            cursor.close();

            intent.putExtra("picture", mediaPath);
            startActivity(intent);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (resultCode == RESULT_OK) {
                int IMAGE_GALLERY_REQUEST = 20;
                if (requestCode == IMAGE_GALLERY_REQUEST) {
                    android.net.Uri imageUri = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    android.database.Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    columnIndex = cursor.getColumnIndex(filePath[0]);
                    mediaPath = cursor.getString(columnIndex);
                    cursor.close();
                    sessionManager.updatePicture(mediaPath);

                    intent.putExtra("picture", mediaPath);
                    startActivity(intent);
                }
            }

        } else {
            String[] permissionRequest = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissionRequest, CAM_REQUEST_CODE);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAM_REQUEST_CODE) {
        }
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
        int unit_id = Integer.parseInt(unit_details.get(SessionManager.KEY_UNIT_ID));
        String super_token = "token " + userDetails.get(SessionManager.KEY_TOKEN);
        userapi.getUser(super_token, String.valueOf(unit_id)).enqueue(new Callback<Login_model>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Login_model> call, @NonNull Response<Login_model> response) {
                if (response.isSuccessful()) {
                    Login_model login_model = response.body();
                    User user = login_model.getUser();
                    Unit unit  = login_model.getUnit();
                    UnitMembership unitMembership = login_model.getUnitMembership();

                    String full_name = user.getFullName();
                    String emailString = user.getEmail();
                    String picture = user.getPicture();
                    String user_id = user.getId();
                    int role = user.getUser_role();
                    String pictureToken = user.getPicture_token();
                    String phone = user.getPhone();

                    int unitMembershipId = unitMembership.getId();
                    String student_class = unitMembership.getStudent_class();
                    String student_number = unitMembership.getStudent_number();
                    String membership_number_string_new = unitMembership.getMembership_number();
                    String membership_type_string = unitMembership.getMembership_type();

                    String card_type = unit.getCard_type();
                    String unit_name = unit.getName();
                    String unit_short_name = unit.getShort_name();
                    String public_contact_phone = unit.getPublic_contact_phone();
                    String public_contact_email = unit.getPublic_contact_email();
                    String unit_logo = unit.getUnit_logo();
                    String unit_logo_short = unit.getSmall_unit_logo();
                    int unit_id = unit.getId();
                    STUDENT_CARD = unit.getCard_type();

                    java.util.Date dateToExpiration = unitMembership.getExpiration_date();
                    java.util.Date birthdayDate = user.getDate_of_birth();

                    DateTime timeToExpiration = new DateTime(dateToExpiration);
                    DateTime timeBirthday = new DateTime(birthdayDate);

                    DateTimeFormatter dateTimeFormatter2 = DateTimeFormat.forPattern("yyyy-MM-dd");

                    String birthDateString = dateTimeFormatter2.print(timeBirthday);
                    String expirationString = dateTimeFormatter2.print(timeToExpiration);


                    sessionManager.update_user(full_name, emailString, user_id, role, pictureToken, birthDateString, picture, user.isHas_set_picture(), phone);

                    sessionManager.create_login_session_unit(unit_name, unit_short_name, unit_logo, unit_logo_short, unit_id,
                            public_contact_email, public_contact_phone, card_type);

                    sessionManager.create_login_session_unit_member(expirationString, student_class, student_number, unitMembershipId,
                            membership_number_string_new, membership_type_string);

                    if(picture == null) {
                        profile_picture.setImageResource(R.drawable.facebookgirl);
                    }else{

                        Picasso.get().load(user.getPicture()).into(picassoImageTarget(getApplicationContext(), student_number));
                        Picasso.get().load(picture).into(profile_picture);

                    }

                    set_attributes(birthDateString, student_number, STUDENT_CARD, picture, path,
                            unit_logo_short, full_name, unit_short_name, startDate, expirationString, simpleDateFormat, r1);

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

    private Target picassoImageTarget(Context context, final String imageDir) {
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        return new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, "my_image.jpeg");
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                Objects.requireNonNull(fos).close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
    }

    public boolean is_string_empty(String s){
        return s != null && !s.isEmpty() && !s.equals("null");
    }

    private void set_attributes(String date_of_birth, String student_id, String set_card_type_String,
                                String set_picture, String set_path, String set_small_logo, String set_name,
                                String set_short_school_name, Date set_start_date, String set_experation,
                                SimpleDateFormat s2, RelativeLayout set_r2){

        if(set_picture != null){
            SessionManager.set_has_set_picture(getApplicationContext(), true);
        }else{
            SessionManager.set_has_set_picture(getApplicationContext(), false);
        }

        if (!SessionManager.get_has_set_picture_sttus(getApplicationContext())) {
            profile_picture.setImageResource(R.drawable.facebookgirl);
        }else{
            if(set_path == null){
                Picasso.get().load(set_picture).into(picassoImageTarget(getApplicationContext(), student_id));
                Picasso.get().load(set_picture).into(profile_picture);
            }else{
                File picture_file = new File(set_path);
                Picasso.get().load(picture_file).into(profile_picture);
            }
        }

        Picasso.get().load(set_small_logo).into(short_logo_view);
        firstAndSirName.setText(set_name);
        short_school_name.setText(set_short_school_name);
        card_type.setText(set_card_type_String);

        set_start_date = null;
        try {
            set_start_date = s2.parse(set_experation);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (System.currentTimeMillis() > set_start_date.getTime()) {
            //Ugyldig
            set_r2.setBackgroundColor(ContextCompat.getColor(this, R.color.invalid_backgroud));
            BirthDay.setText(R.string.expired);
        } else {
            //gyldig
            //gyldig
            set_r2.setBackgroundColor(ContextCompat.getColor(this, R.color.valid_backgroud));

            targetFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.GERMANY);
            try {
                if(Calendar.getInstance().get(Calendar.MONTH) + 1 < 8){
                    set_r2.setBackgroundColor(ContextCompat.getColor(this, R.color.valid_backgroud));
                    help_string1 = getResources().getString(R.string.spring) + " " + Calendar.getInstance().get(Calendar.YEAR);
                    BirthDay.setText(help_string1);

                }else{
                    set_r2.setBackgroundColor(ContextCompat.getColor(this, R.color.valid_backgroud));
                    thisExpDate = unit_membership_details.get(SessionManager.KEY_EXPERATIONDATE);
                    date = simpleDateFormat.parse(thisExpDate);
                    formattedDate = targetFormat.format(date);
                    help_string2 = getResources().getString(R.string.fall) + " " + Calendar.getInstance().get(Calendar.YEAR);
                    BirthDay.setText(help_string2);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        try{
            String first_four_numbers = date_of_birth.substring(0,4);
            int first_four_numbers_integer = Integer.parseInt(first_four_numbers);
            if(first_four_numbers_integer > 2015){
                if(!is_string_empty(student_id)){
                    studentId.setText("");
                    birthToId.setText("");
                }else{
                    birthToId.setText(getResources().getString(R.string.Student_number));
                    studentId.setText(student_id);
                }
            }else{
                studentId.setText(date_of_birth);
            }
        }catch (Exception e) {
            updateUser();
            Intent i = new Intent(UserActivity1.this, LandingPage.class);
            startActivity(i);
        }
    }
}