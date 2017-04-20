package com.example.jorgenskevik.e_cardholders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;
import com.example.jorgenskevik.e_cardholders.models.SessionManager;
import com.example.jorgenskevik.e_cardholders.models.User;
import com.example.jorgenskevik.e_cardholders.remote.UserAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.animation.Animation.INFINITE;

/**
 * The type Main 3 activity.
 */
public class Main3Activity extends AppCompatActivity implements ActionSheet.ActionSheetListener {
    /**
     * The constant CAM_REQUEST_CODE.
     */
    public static final int CAM_REQUEST_CODE = 457843;
    /**
     * The constant buildVersionMin.
     */
    public static final int buildVersionMin = 6;
    /**
     * The constant compressBitmap.
     */
    public static final int compressBitmap = 100;
    /**
     * The constant photoCodeToken.
     */
    public static final String photoCodeToken = "BRUKT";
    /**
     * The constant bitMapBytes.
     */
    public static final String bitMapBytes = "bitmapBytes";
    /**
     * The constant tokenBearer.
     */
    public static final String tokenBearer = "Bearer ";
    private Main3Activity mContext;
    private static int IMAGE_GALLERY_REQUEST = 20;
    /**
     * The Session manager.
     */
    SessionManager sessionManager;
    private ImageView view2;
    private Uri imageUri;
    /**
     * The Media path.
     */
    String mediaPath;
    /**
     * The Expiration button.
     */
    Button expirationButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main3);
        mContext = Main3Activity.this;
        sessionManager = new SessionManager(getApplicationContext());
        view2 = (ImageView) findViewById(R.id.window1);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        view2.getLayoutParams().height = height;
        view2.getLayoutParams().width = width;

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //View barcode

        ImageButton codeButton = (ImageButton) findViewById(R.id.imageButton3);
        TextView firstAndSirName = (TextView) findViewById(R.id.navnstring);
        TextView BirthDay = (TextView) findViewById(R.id.stringfdato);
        TextView studentId = (TextView) findViewById(R.id.studentid);
        HashMap<String, String> userDetails = sessionManager.getUserDetails();

        String firstAndSirNameString = userDetails.get(SessionManager.KEY_NAME);
        String birthdayString = userDetails.get(SessionManager.KEY_BIRTHDATE);
        String studentIDString = userDetails.get(SessionManager.KEY_STUDENTNUMBER);

        String path = userDetails.get(SessionManager.KEY_PATH);

        if(path == null){
            view2.setImageResource(R.drawable.jogga2);
        }else{
            loadImageFromStorage(path);
        }

        firstAndSirName.setText(firstAndSirNameString);
        BirthDay.setText(birthdayString);
        String extraStudentID = getResources().getString(R.string.studentnumber) + " " + studentIDString;
        studentId.setText(extraStudentID);

        sessionManager = new SessionManager(getApplicationContext());
        String expirationDateString = userDetails.get(SessionManager.KEY_EXPERATIONDATE);
        expirationButton = (Button) findViewById(R.id.button11);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        try {
            startDate = simpleDateFormat.parse(expirationDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(System.currentTimeMillis() > startDate.getTime()){
            //Ugyldig
            int selectedColor = Color.rgb(254, 56, 36);
            expirationButton.setText(R.string.expired);
            expirationButton.setTextSize(30);
            expirationButton.setBackgroundColor(selectedColor);

        } else{
            //gyldig
            int selectedColor = Color.rgb(132, 205, 182);
            int selectedWhite = Color.rgb(255, 255, 255);
            DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
            try {
                String thisExpDate = userDetails.get(SessionManager.KEY_EXPERATIONDATE);
                Date date = simpleDateFormat.parse(thisExpDate);
                String formattedDate = targetFormat.format(date);
                String expirationDate = getResources().getString(R.string.GyldigTil) + " " + formattedDate;
                expirationButton.setText(expirationDate);
                expirationButton.setTextColor(selectedWhite);
                expirationButton.setBackgroundColor(selectedColor);
                expirationButton.setTextSize(14);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        codeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main3Activity.this, Code.class));
            }
        });
    }

    /**
     * Open settings.
     *
     * @param v the v
     */
    public void openSettings(View v) {
        setTheme(R.style.ActionSheetStyleiOS7);
        showActionSheet();
    }

    /**
     * Show action sheet.
     */
    public void showActionSheet() {
        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle(R.string.closeSettings)
                .setOtherButtonTitles(getResources().getString(R.string.Loggout), getResources().getString(R.string.Terms), getResources().getString(R.string.setPicture),
                        getResources().getString(R.string.updateProfile))
                .setCancelableOnTouchOutside(true).setListener(this).show();
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        //Log out
        if (index == 0) {
            sessionManager.logoutUser();
            Intent intent = new Intent(Main3Activity.this, MainActivity.class);
            startActivity(intent);
        }

        //terms
        if (index == 1) {
            Intent intent = new Intent(Main3Activity.this, Main5Activity.class);
            startActivity(intent);

        }
        //sett profilbilde
        if (index == 2) {
            HashMap<String, String> userDetails = sessionManager.getUserDetails();
            String fourDigits = userDetails.get(SessionManager.KEY_PICTURETOKEN);

            if (fourDigits.equals(photoCodeToken)) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, getResources().getString(R.string.DenyPicture), duration);
                toast.show();

            } else {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                File pictureFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String picturePath = pictureFile.getPath();
                Uri data = Uri.parse(picturePath);
                photoPickerIntent.setDataAndType(data, "image/*");
                startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
            }
        }
        //Oppdater brukeren
        if(index == 3){

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(KVTVariables.getBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            HashMap<String, String> userDetails = sessionManager.getUserDetails();

            String authenticateString = userDetails.get(SessionManager.KEY_TOKEN);
            String authenticateExtra = tokenBearer + authenticateString;

            UserAPI userapi = retrofit.create(UserAPI.class);
            userapi.getUser(KVTVariables.getAcceptVersion(), authenticateExtra).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User user = response.body();

                        // Session Manager
                        sessionManager = new SessionManager(getApplicationContext());

                        String username = user.getName();
                        String email = user.getEmail();
                        String studentNumber = user.getStudentNumber();
                        String id = user.getId();
                        String role = user.getRole();
                        String pictureToken = user.getPictureToken();

                        java.util.Date userDateOfBirthDate = user.getDateOfBirth();
                        java.util.Date userExpirationDate = user.getExpirationDate();

                        DateTime dateTime = new DateTime(userDateOfBirthDate);
                        DateTime dateTime2 = new DateTime(userExpirationDate);

                        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd-MMM-yyyy");
                        DateTimeFormatter dateTimeFormatter2 = DateTimeFormat.forPattern("yyyy-MM-dd");
                        String dateTimeBirthday = dateTimeFormatter.print(dateTime);
                        String dateTimeExpiration = dateTimeFormatter2.print(dateTime2);

                        sessionManager.createUpdatedLoginSession(username, email, studentNumber, id, role, pictureToken, dateTimeBirthday, dateTimeExpiration);

                        TextView firstAndSirName = (TextView) findViewById(R.id.navnstring);
                        TextView dateOfBirth = (TextView) findViewById(R.id.stringfdato);
                        TextView studentId = (TextView) findViewById(R.id.studentid);

                        expirationButton = (Button) findViewById(R.id.button11);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date startDate = null;
                        try {
                            startDate = simpleDateFormat.parse(dateTimeExpiration);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(System.currentTimeMillis() > startDate.getTime()){
                            //Ugyldig
                            int selectedColor = Color.rgb(254, 56, 36);
                            expirationButton.setText(R.string.expired);
                            expirationButton.setTextSize(30);
                            expirationButton.setBackgroundColor(selectedColor);
                        }else{
                            //gyldig
                            int selectedColor = Color.rgb(132, 205, 182);
                            int selectedWhite = Color.rgb(255, 255, 255);
                            DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
                            try {
                                Date date = simpleDateFormat.parse(dateTimeExpiration);
                                String formattedDate = targetFormat.format(date);
                                String ExpirationDate = getResources().getString(R.string.GyldigTil) + " " + formattedDate;
                                expirationButton.setText(ExpirationDate);
                                expirationButton.setTextColor(selectedWhite);
                                expirationButton.setBackgroundColor(selectedColor);
                                expirationButton.setTextSize(14);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        firstAndSirName.setText(username);
                        dateOfBirth.setText(dateTimeBirthday);
                        String extra = getResources().getString(R.string.studentnumber) + " " + studentNumber;
                        studentId.setText(extra);

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.userUpdated), Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.userNotUpdated), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.userNotUpdated), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String buildVersion = Build.VERSION.RELEASE;
        String firstLetter = String.valueOf(buildVersion.charAt(0));
        int number = Integer.parseInt(firstLetter);
        if(number < buildVersionMin){
            imageUri = data.getData();
            String [] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePath[0]);
            mediaPath = cursor.getString(columnIndex);
            InputStream inputStream;

            try {
                inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Intent intent = new Intent(this, Main4Activity.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, compressBitmap, stream);
                byte[] bytes = stream.toByteArray();
                intent.putExtra(bitMapBytes, bytes);
                startActivity(intent);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, getResources().getString(R.string.pictureNotAvailable), Toast.LENGTH_SHORT).show();
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (resultCode == RESULT_OK) {
                if (requestCode == IMAGE_GALLERY_REQUEST) {
                    imageUri = data.getData();
                    String [] filePath = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePath[0]);
                    mediaPath = cursor.getString(columnIndex);
                    InputStream inputStream;

                    try {
                        inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        Intent intent = new Intent(this, Main4Activity.class);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, compressBitmap, stream);
                        byte[] bytes = stream.toByteArray();
                        intent.putExtra(bitMapBytes,bytes);
                        startActivity(intent);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, getResources().getString(R.string.pictureNotAvailable), Toast.LENGTH_SHORT).show();
                    }
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

                Toast.makeText(this, R.string.GiveAccess, Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, getResources().getString(R.string.GiveAccess), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadImageFromStorage(String path) {
        try {
            File pictureFile = new File(path, "profile.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(pictureFile));
            ImageView image = (ImageView)findViewById(R.id.window1);
            image.setImageBitmap(bitmap);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shake.
     *
     * @param v the v
     */
    public void shake(View v){
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.anime);
        findViewById(R.id.window1).startAnimation(shake);
    }
}








