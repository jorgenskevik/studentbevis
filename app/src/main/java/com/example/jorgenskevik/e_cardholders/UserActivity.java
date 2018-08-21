package com.example.jorgenskevik.e_cardholders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.os.StrictMode;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aromajoin.actionsheet.ActionSheet;
import com.aromajoin.actionsheet.OnActionListener;
import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;
import com.example.jorgenskevik.e_cardholders.models.FirebaseLoginModel;
import com.example.jorgenskevik.e_cardholders.models.LoginModel;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.crashlytics.android.Crashlytics.log;

/**
 * The type User activity.
 */
public class UserActivity extends AppCompatActivity {
    /**
     * The constant CAM_REQUEST_CODE.
     */
    public static final int CAM_REQUEST_CODE = 457843;
    private static int IMAGE_GALLERY_REQUEST = 20;
    /**
     * The Session manager.
     */
    SessionManager sessionManager;
    /**
     * The Expiration button.
     */
//Uri imageUri, data;
    /**
     * The View 2.
     */
    ImageView view2, /**
     * The Image.
     */
    short_logo_view;/**
     * The Code button.
     */
    /**
     * The First and sir name.
     */
    TextView firstAndSirName, /**
     * The Student id.
     */
    studentId,

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
    thisExpDate, /**
     * The Birthday string.
     */
    birthdayString, /**
     * The Student id string.
     */
    studentIDString, /**
     * The Path.
     */
    path, /**
     * The Picture.
     */
    picture, /**
     * The Extra student id.
     */
    extraStudentID, /**
     * The Date time birthday.
     */
     /**
     * The Date time expiration.
     */
     /**
     * The Build version.
     */
    buildVersion, /**
     * The First letter.
     */
    firstLetter;


    /**
     * The Expiration date string.
     */
    String expirationDateString, /**
     * The Media path.
     */
    mediaPath,

    short_school_name_string,
    /**
     * The Formatted date.
     */
    formattedDate, /**

     * The Expiration date.
     */
     /**
     * The Four digits.
     */
    fourDigits, /**
     * The Picture path.
     */
    picturePath, /**
     * The Authenticate string.
     */
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
    int selectedColor, /**
     * The Selected white.
     */
    selectedWhite, /**
     * The Duration.
     */
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
    Intent intent, /**
     * The Photo picker intent.
     */
    photoPickerIntent;
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
    /**
     * The Picture file.
     */
    File pictureFile;
    RelativeLayout r1;

    Float orientation;
    /**
     * The User date of birth date.
     */


    @SuppressLint("SetTextI18n")
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

        setContentView(R.layout.user_view2);

        sessionManager = new SessionManager(getApplicationContext());
        view2 = (ImageView) findViewById(R.id.sircle);
        short_logo_view = (ImageView) findViewById(R.id.short_logo);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);


        //View barcode

        TextView card_type = (TextView) findViewById(R.id.skolebevis);
        firstAndSirName = (TextView) findViewById(R.id.textView11);
        short_school_name = (TextView) findViewById(R.id.textView16);
        BirthDay = (TextView) findViewById(R.id.textView2);
        studentId = (TextView) findViewById(R.id.textView17);
        userDetails = sessionManager.getUserDetails();
        unit_details = sessionManager.getUnitDetails();
        unit_membership_details = sessionManager.getUnitMemberDetails();
        ImageView button = (ImageView) findViewById(R.id.imageView2);

        short_school_name_string = unit_details.get(SessionManager.KEY_UNIT_SHORT_NAME);

        String small_logo = unit_details.get(SessionManager.KEY_UNIT_LOGO_SHORT);
        String card_type_string = unit_details.get(SessionManager.KEY_CARD_TYPE);

        firstAndSirNameString = userDetails.get(SessionManager.KEY_FULL_NAME);
        birthdayString = userDetails.get(SessionManager.KEY_BIRTHDATE);
        targetFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.GERMANY);

        studentIDString = unit_membership_details.get(SessionManager.KEY_STUDENTNUMBER);
        path = userDetails.get(SessionManager.KEY_PATH);
        userDetails = sessionManager.getMedia_path();
        picture = userDetails.get(SessionManager.KEY_PICTURE);
        JodaTimeAndroid.init(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                showActionSheet(view);
            }
        });

        r1 = (RelativeLayout) findViewById(R.id.background);

        if (path == null) {
            view2.setImageResource(R.drawable.facebookgirl);

            if (picture != null && !picture.contains("/img/white_image.png") && !picture.contains("/img/avatar.png")) {
                //ContextWrapper cw = new ContextWrapper(this);
                //File directory = cw.getDir(studentIDString, Context.MODE_PRIVATE);
                //File myImageFile = new File(directory, "my_image.jpeg");
                //Picasso.with(this).load(myImageFile).into(view2);
                // Laster bare med picasso uten å cache..
                // Disk cache of 2% storage space up to 50MB but no less than 5MB.
                // (Note: this is only available on API 14+ or if you are using a standalone
                // library that provides a disk cache on all API levels like OkHttp)
                Picasso.with(getApplicationContext()).load(picture).into(view2);

            }
        } else {
            File f = new File(path);
            Picasso.with(getApplicationContext()).load(f).into(view2);
        }

        Picasso.with(this).load(small_logo).into(short_logo_view);

        if (card_type_string.equals("student_card")){
            card_type.setText(getResources().getString(R.string.student_sertificat));
        }

        firstAndSirName.setText(firstAndSirNameString);
        extraStudentID = studentIDString;
        short_school_name.setText(short_school_name_string);
        studentId.setText(extraStudentID);
        sessionManager = new SessionManager(getApplicationContext());
        expirationDateString = unit_membership_details.get(SessionManager.KEY_EXPERATIONDATE);

        startDate = null;
        try {
            startDate = simpleDateFormat.parse(expirationDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (System.currentTimeMillis() > startDate.getTime()) {
            //Ugyldig
            r1.setBackgroundColor(ContextCompat.getColor(this, R.color.invalid_backgroud));
            BirthDay.setText(R.string.expired);
        } else {
            //gyldig
            r1.setBackgroundColor(ContextCompat.getColor(this, R.color.valid_backgroud));

            targetFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.GERMANY);
            try {
                if(Calendar.getInstance().get(Calendar.MONTH) + 1 < 8){
                    r1.setBackgroundColor(ContextCompat.getColor(this, R.color.valid_backgroud));
                    BirthDay.setText(getResources().getString(R.string.spring) + " " + Calendar.getInstance().get(Calendar.YEAR));

                }else{
                    r1.setBackgroundColor(ContextCompat.getColor(this, R.color.valid_backgroud));
                    thisExpDate = unit_membership_details.get(SessionManager.KEY_EXPERATIONDATE);
                    date = simpleDateFormat.parse(thisExpDate);
                    formattedDate = targetFormat.format(date);
                    BirthDay.setText(getResources().getString(R.string.fall) + " " + Calendar.getInstance().get(Calendar.YEAR));
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
            window.setStatusBarColor(ContextCompat.getColor(UserActivity.this,R.color.black));
        }
    }
    private void showActionSheet(View anchor) {
        ActionSheet actionSheet = new ActionSheet(this);
        actionSheet.setTitle(getResources().getString(R.string.chooise));
        actionSheet.setSourceView(anchor);
        actionSheet.addAction(getResources().getString(R.string.barcode), ActionSheet.Style.DEFAULT, new OnActionListener() {
            @Override public void onSelected(ActionSheet actionSheet, String title) {
                performAction(title);
                Intent barcode = new Intent(UserActivity.this, Barcode_new.class);
                startActivity(barcode);
                actionSheet.dismiss();
            }
        });
        actionSheet.addAction(getResources().getString(R.string.setPic), ActionSheet.Style.DEFAULT, new OnActionListener() {
            @Override public void onSelected(ActionSheet actionSheet, String title) {
                performAction(title);

                //if (ActivityCompat.checkSelfPermission((Activity)context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                        Intent infoIntent = new Intent(UserActivity.this, BarCodeActivity.class);
                        startActivity(infoIntent);
                    }
                } else {
                   // Toast.makeText(this, getResources().getString(R.string.GiveAccess), Toast.LENGTH_LONG).show();
                }
                }
                actionSheet.dismiss();
            }
        });
        actionSheet.addAction(getResources().getString(R.string.updateProfile), ActionSheet.Style.DEFAULT, new OnActionListener() {
            @Override public void onSelected(ActionSheet actionSheet, String title) {
                performAction(title);
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
                    public void onResponse(Call<Login_model> call, Response<Login_model> response) {
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

                            int unitMembershipId = unitMembership.getId();
                            String student_class = unitMembership.getStudent_class();
                            String student_number = unitMembership.getStudent_number();

                            String card_type = unit.getCard_type();
                            String unit_name = unit.getName();
                            String unit_short_name = unit.getShort_name();
                            String public_contact_phone = unit.getPublic_contact_phone();
                            String public_contact_email = unit.getPublic_contact_email();
                            String unit_logo = unit.getUnit_logo();
                            String unit_logo_short = unit.getSmall_unit_logo();
                            int unit_id = unit.getId();

                            java.util.Date dateToExpiration = unitMembership.getExpiration_date();
                            java.util.Date birthdayDate = user.getDate_of_birth();

                            DateTime timeToExpiration = new DateTime(dateToExpiration);
                            DateTime timeBirthday = new DateTime(birthdayDate);

                            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd-MMM-yyyy");
                            DateTimeFormatter dateTimeFormatter2 = DateTimeFormat.forPattern("yyyy-MM-dd");

                            String birthDateString = dateTimeFormatter.print(timeBirthday);
                            String expirationString = dateTimeFormatter2.print(timeToExpiration);

                            sessionManager.update_user(full_name, emailString, user_id, role, pictureToken, birthDateString, picture, user.isHas_set_picture());

                            sessionManager.create_login_session_unit(unit_name, unit_short_name, unit_logo, unit_logo_short, unit_id,
                                    public_contact_email, public_contact_phone, card_type);

                            sessionManager.create_login_session_unit_member(expirationString, student_class, student_number, unitMembershipId);
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.userUpdated), Toast.LENGTH_SHORT).show();


                            if(picture == null) {
                                view2.setImageResource(R.drawable.facebookgirl);
                            } else if(picture.contains("/img/white_image.png") || picture.contains("/img/avatar.png")){
                                view2.setImageResource(R.drawable.facebookgirl);
                            }else{
                                Picasso.with(getApplicationContext()).load(picture).into(view2);
                            }

                            startDate = null;
                            try {
                                startDate = simpleDateFormat.parse(expirationString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (System.currentTimeMillis() > startDate.getTime()) {
                                //Ugyldig
                                r1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.invalid_backgroud));
                                BirthDay.setText(R.string.expired);

                            } else {
                                //gyldig
                                r1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.valid_backgroud));

                                targetFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.GERMANY);
                                try {
                                    if(Calendar.getInstance().get(Calendar.MONTH) + 1 < 8){
                                        r1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.valid_backgroud));
                                        BirthDay.setText(getResources().getString(R.string.spring) + " " + Calendar.getInstance().get(Calendar.YEAR));

                                    }else{
                                        r1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.valid_backgroud));
                                        thisExpDate = unit_membership_details.get(SessionManager.KEY_EXPERATIONDATE);
                                        date = simpleDateFormat.parse(thisExpDate);
                                        formattedDate = targetFormat.format(date);
                                        BirthDay.setText(getResources().getString(R.string.fall) + " " + Calendar.getInstance().get(Calendar.YEAR));
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            finish();
                            startActivity(getIntent());

                        }else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.userNotUpdated), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Login_model> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.userNotUpdated), Toast.LENGTH_SHORT).show();
                    }
                });
                actionSheet.dismiss();
            }
        });



        actionSheet.addAction(getResources().getString(R.string.Loggout), ActionSheet.Style.DESTRUCTIVE, new OnActionListener() {
            @Override public void onSelected(ActionSheet actionSheet, String title) {
                performAction(title);
                sessionManager.logoutUser();
                intent = new Intent(UserActivity.this, LandingPage.class);
                startActivity(intent);
                actionSheet.dismiss();
            }
        });

        actionSheet.show();
    }

    private void performAction(String title) {
        Snackbar.make(UserActivity.this.findViewById(android.R.id.content), title,
                Snackbar.LENGTH_SHORT).show();
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
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    public String hentString(Context context, String imageDir,String imageName){
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        File myImageFile = new File(directory, imageName);
        return myImageFile.getAbsolutePath();
    }

    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        return new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName);
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {
                }
            }
        };
    }

    public String getCameraPhotoOrientation(String imagePath) {
        String rotate = "0";
        try {
            File imageFile = new File(imagePath);
            android.support.media.ExifInterface exif = new android.support.media.ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    android.support.media.ExifInterface.TAG_ORIENTATION,
                    android.support.media.ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case android.support.media.ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = "270";
                    break;
                case android.support.media.ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = "180";
                    break;
                case android.support.media.ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = "90";
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }



    private void deleteTempFolder(String dir) {
        File myDir = new File(Environment.getExternalStorageDirectory() + "/"+dir);
        if (myDir.isDirectory()) {
            String[] children = myDir.list();
            for (String aChildren : children) {
                new File(myDir, aChildren).delete();
            }
        }
    }

}