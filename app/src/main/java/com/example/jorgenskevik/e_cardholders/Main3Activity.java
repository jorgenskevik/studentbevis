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
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main3Activity extends AppCompatActivity implements ActionSheet.ActionSheetListener {
    public static final int CAM_REQUEST_CODE = 457843;
    private Main3Activity mContext;
    private static int IMAGE_GALLERY_REQUEST = 20;
    SessionManager sessionManager;
    private ImageView view2;
    private Uri imageUri;
    String mediaPath;
    Button expButton;
    private int state = 0;

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

        ImageButton codebutton = (ImageButton) findViewById(R.id.imageButton3);
        TextView fogenavn = (TextView) findViewById(R.id.navnstring);
        TextView fdato = (TextView) findViewById(R.id.stringfdato);
        TextView studentid = (TextView) findViewById(R.id.studentid);
        HashMap<String, String> userDetails = sessionManager.getUserDetails();

        String forandsirname = userDetails.get(SessionManager.KEY_NAME);
        String birthday = userDetails.get(SessionManager.KEY_BIRTHDATE);
        String studentIDString = userDetails.get(SessionManager.KEY_STUDENTNUMBER);

        String path = userDetails.get(SessionManager.KEY_PATH);

        if(path == null){
            view2.setImageResource(R.drawable.tommann);
        }else{
            loadImageFromStorage(path);
        }

        fogenavn.setText(forandsirname);
        fdato.setText(birthday);
        String extra = getResources().getString(R.string.studentnumber) + " " + studentIDString;
        studentid.setText(extra);

        sessionManager = new SessionManager(getApplicationContext());
        String expDate = userDetails.get(SessionManager.KEY_EXPERATIONDATE);
        expButton = (Button) findViewById(R.id.button11);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Date strDate = null;
        try {
            strDate = sdf1.parse(expDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
       // if (new Date().after(strDate)) {
        if(System.currentTimeMillis() > strDate.getTime()){
            //Ugyldig
            TextView valid = (TextView) findViewById(R.id.validTil);
            TextView experation = (TextView) findViewById(R.id.expDate);
            String empty = "";
            valid.setText(empty);
            experation.setText(empty);
            int selectedColor = Color.rgb(254, 56, 36);
            expButton.setText(R.string.expired);
            expButton.setTextSize(30);

            expButton.setBackgroundColor(selectedColor);
       // }if(path == null){
         //   TextView valid = (TextView) findViewById(R.id.validTil);
           // TextView experation = (TextView) findViewById(R.id.expDate);
            //String empty = "";
            //valid.setText(empty);
            //experation.setText(empty);
            //int selectedColor = Color.rgb(254, 56, 36);
            //expButton.setText(R.string.Not);
            //expButton.setTextSize(30);

           // expButton.setBackgroundColor(selectedColor);

        } else{
            //gyldig
            int selectedColor = Color.rgb(132, 205, 182);
            TextView experation = (TextView) findViewById(R.id.expDate);
            String experationDate = userDetails.get(SessionManager.KEY_EXPERATIONDATE);
            experation.setText(experationDate);
            expButton.setBackgroundColor(selectedColor);

        }

       /* expButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView view = (ImageView) findViewById(R.id.window1);
                   RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    //Setup anim with desired properties
                    anim.setInterpolator(new LinearInterpolator());
                    anim.setRepeatCount(1); //Repeat animation indefinitely
                    anim.setDuration(1000); //Put desired duration per anim cycle here, in milliseconds
                    //Start animation
                    view.startAnimation(anim);


            }
        });*/

        codebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main3Activity.this, Code.class));
            }
        });
    }

    public void openSettings(View v) {
        setTheme(R.style.ActionSheetStyleiOS7);
        showActionSheet();
    }

    public void showActionSheet() {
        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle(R.string.closeSettings)
                .setOtherButtonTitles(getResources().getString(R.string.Loggout), getResources().getString(R.string.Terms), getResources().getString(R.string.setPicture),
                        getResources().getString(R.string.updateProfile))
                .setCancelableOnTouchOutside(true).setListener(this).show();
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
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

            if (fourDigits.equals("BRUKT")) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, getResources().getString(R.string.DenyPicture), duration);
                toast.show();

            } else {

                Intent photopickerintent = new Intent(Intent.ACTION_PICK);
                File pictureDire = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String pictureDirpath = pictureDire.getPath();
                Uri data = Uri.parse(pictureDirpath);
                photopickerintent.setDataAndType(data, "image/*");
                System.out.println("kommer hit");
                startActivityForResult(photopickerintent, IMAGE_GALLERY_REQUEST);

            }
        }
        //Oppdater brukeren
        if(index == 3){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(KVTVariables.getLocal_URL())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            HashMap<String, String> userDetails = sessionManager.getUserDetails();

            String auth = userDetails.get(SessionManager.KEY_TOKEN);
            String auth1 = "Bearer " + auth;

            UserAPI userapi = retrofit.create(UserAPI.class);
            userapi.getUser(KVTVariables.getAcceptVersion(), auth1).enqueue(new Callback<User>() {
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


                        java.util.Date juDate = user.getDateOfBirth();
                        java.util.Date juDate2 = user.getExpirationDate();

                        DateTime dt = new DateTime(juDate);
                        DateTime dt2 = new DateTime(juDate2);

                        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MMM-yyyy");
                        DateTimeFormatter fmt1 = DateTimeFormat.forPattern("yyyy-MM-dd");
                        String dtStr = fmt.print(dt);
                        String dtStr2 = fmt1.print(dt2);

                        sessionManager.createUpdtaeLoginSession(username, email, studentNumber, id, role, pictureToken, dtStr, dtStr2);

                        TextView fogenavn = (TextView) findViewById(R.id.navnstring);
                        TextView fdato = (TextView) findViewById(R.id.stringfdato);
                        TextView studentid = (TextView) findViewById(R.id.studentid);
                        TextView exp = (TextView) findViewById(R.id.expDate);

                        expButton = (Button) findViewById(R.id.button11);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date strDate = null;
                        try {
                            strDate = sdf.parse(dtStr2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(System.currentTimeMillis() > strDate.getTime()){
                            //Ugyldig
                            TextView valid = (TextView) findViewById(R.id.validTil);
                            //TextView expDate = (TextView) findViewById(R.id.expDate);
                            String empty = "";
                            String empty1 = "";
                            valid.setText(empty);
                            exp.setText(empty1);
                            int selectedColor = Color.rgb(254, 56, 36);
                            expButton.setText(R.string.expired);
                            expButton.setTextSize(30);
                            expButton.setBackgroundColor(selectedColor);
                        }else{
                            //gyldig
                            int selectedColor = Color.rgb(132, 205, 182);
                            //TextView experation = (TextView) findViewById(R.id.expDate);
                            TextView valid = (TextView) findViewById(R.id.validTil);
                            valid.setText(R.string.GyldigTil);
                            exp.setText(dtStr2);
                            //Button mybutton = (Button) findViewById(R.id.button11);
                            expButton.setText("");
                            expButton.setBackgroundColor(selectedColor);

                        }

                        fogenavn.setText(username);
                        fdato.setText(dtStr);
                        String extra = getResources().getString(R.string.studentnumber) + " " + studentNumber;
                        studentid.setText(extra);

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        System.out.println("onAct");

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (resultcode == RESULT_OK) {
                if (requestcode == IMAGE_GALLERY_REQUEST) {
                    imageUri = data.getData();
                    String [] filePath = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    int columindex = cursor.getColumnIndex(filePath[0]);
                    mediaPath = cursor.getString(columindex);
                    InputStream inputStream;

                    try {
                        inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        Intent intent = new Intent(this, Main4Activity.class);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] bytes = stream.toByteArray();
                        intent.putExtra("bitmapbytes",bytes);
                        startActivity(intent);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, getResources().getString(R.string.pictureNotAvailable), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        } else {
            String[] permissionRequest = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissionRequest, CAM_REQUEST_CODE);
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
            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.window1);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void trykk(View v){
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.anime);
        findViewById(R.id.window1).startAnimation(shake);
    }
}








