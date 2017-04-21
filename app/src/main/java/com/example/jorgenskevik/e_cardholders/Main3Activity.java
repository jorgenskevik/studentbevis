package com.example.jorgenskevik.e_cardholders;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

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
    private static int IMAGE_GALLERY_REQUEST = 20;
    /**
     * The Session manager.
     */
    SessionManager sessionManager;
    //Uri imageUri, data;
    Button expirationButton;
    ImageView view2, image;
    ImageButton codeButton;
    TextView firstAndSirName, studentId, BirthDay;
    String firstAndSirNameString, thisExpDate, birthdayString, studentIDString, path, picture, extraStudentID, dateTimeBirthday, dateTimeExpiration, buildVersion, firstLetter;
    String expirationDateString, mediaPath, formattedDate, expirationDate, fourDigits, picturePath, authenticateString, username, email, id, studentNumber,role,pictureToken;
    SimpleDateFormat simpleDateFormat;
    Date startDate, date;
    int selectedColor, selectedWhite, duration, number, columnIndex;
    DateFormat targetFormat;
    Intent intent, photoPickerIntent;
    HashMap<String, String> userDetails;
    Context context;
    Toast toast;
    File pictureFile, directory, myPath;
    java.util.Date userDateOfBirthDate, userExpirationDate ;
    DateTime dateTime, dateTime2;
    DateTimeFormatter dateTimeFormatter, dateTimeFormatter2;
    InputStream inputStream;
    Bitmap bitmap;
    ByteArrayOutputStream stream;
    //Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main3);
        sessionManager = new SessionManager(getApplicationContext());
        view2 = (ImageView) findViewById(R.id.window1);

        //View barcode

        codeButton = (ImageButton) findViewById(R.id.imageButton3);
        firstAndSirName = (TextView) findViewById(R.id.navnstring);
        BirthDay = (TextView) findViewById(R.id.stringfdato);
        studentId = (TextView) findViewById(R.id.studentid);
        userDetails = sessionManager.getUserDetails();

        System.gc();

        firstAndSirNameString = userDetails.get(SessionManager.KEY_NAME);
        birthdayString = userDetails.get(SessionManager.KEY_BIRTHDATE);
        studentIDString = userDetails.get(SessionManager.KEY_STUDENTNUMBER);
        path = userDetails.get(SessionManager.KEY_PATH);
        picture = userDetails.get(SessionManager.KEY_PICTURE);

        if(path == null){
            view2.setImageResource(R.drawable.facebookgirl);

            if(!picture.equals("")){
                new DownloadImageTask((ImageView) findViewById(R.id.window1))
                        .execute(picture);
            }
        }else{
            loadImageFromStorage(path);
        }

        firstAndSirName.setText(firstAndSirNameString);
        BirthDay.setText(birthdayString);
        extraStudentID = getResources().getString(R.string.studentnumber) + " " + studentIDString;
        studentId.setText(extraStudentID);

        sessionManager = new SessionManager(getApplicationContext());
        expirationDateString = userDetails.get(SessionManager.KEY_EXPERATIONDATE);
        expirationButton = (Button) findViewById(R.id.button11);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);
        startDate = null;
        try {
            startDate = simpleDateFormat.parse(expirationDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(System.currentTimeMillis() > startDate.getTime()){
            //Ugyldig
            selectedColor = Color.rgb(254, 56, 36);
            expirationButton.setText(R.string.expired);
            expirationButton.setTextSize(30);
            expirationButton.setBackgroundColor(selectedColor);

        } else{
            //gyldig
            selectedColor = Color.rgb(132, 205, 182);
            selectedWhite = Color.rgb(255, 255, 255);
            targetFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.GERMANY);
            try {
                thisExpDate = userDetails.get(SessionManager.KEY_EXPERATIONDATE);
                date = simpleDateFormat.parse(thisExpDate);
                formattedDate = targetFormat.format(date);
                expirationDate = getResources().getString(R.string.GyldigTil) + " " + formattedDate;
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
            intent = new Intent(Main3Activity.this, MainActivity.class);
            startActivity(intent);
        }

        //terms
        if (index == 1) {
            intent = new Intent(Main3Activity.this, Main5Activity.class);
            startActivity(intent);

        }
        //sett profilbilde
        if (index == 2) {
            userDetails = sessionManager.getUserDetails();
            fourDigits = userDetails.get(SessionManager.KEY_PICTURETOKEN);

            if (fourDigits.equals("BRUKT")) {
                context = getApplicationContext();
                duration = Toast.LENGTH_SHORT;
                toast = Toast.makeText(context, getResources().getString(R.string.DenyPicture), duration);
                toast.show();

            } else {
                System.gc();
                photoPickerIntent = new Intent(Intent.ACTION_PICK);
                pictureFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                picturePath = pictureFile.getPath();
                Uri data = Uri.parse(picturePath);
                photoPickerIntent.setDataAndType(data, "image/*");
                startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
                System.out.println("åpner album");
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
            userDetails = sessionManager.getUserDetails();

            authenticateString = "Bearer " + userDetails.get(SessionManager.KEY_TOKEN);

            UserAPI userapi = retrofit.create(UserAPI.class);
            userapi.getUser(KVTVariables.getAcceptVersion(), authenticateString).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User user = response.body();

                        // Session Manager
                        sessionManager = new SessionManager(getApplicationContext());

                        username = user.getName();
                        email = user.getEmail();
                        studentNumber = user.getStudentNumber();
                        id = user.getId();
                        role = user.getRole();
                        pictureToken = user.getPictureToken();
                        picture = user.getPicture();

                        userDateOfBirthDate = user.getDateOfBirth();
                        userExpirationDate = user.getExpirationDate();

                        dateTime = new DateTime(userDateOfBirthDate);
                        dateTime2 = new DateTime(userExpirationDate);

                        dateTimeFormatter = DateTimeFormat.forPattern("dd-MMM-yyyy");
                        dateTimeFormatter2 = DateTimeFormat.forPattern("yyyy-MM-dd");
                        dateTimeBirthday = dateTimeFormatter.print(dateTime);
                        dateTimeExpiration = dateTimeFormatter2.print(dateTime2);

                        sessionManager.createUpdatedLoginSession(username, email, studentNumber, id, role, pictureToken, dateTimeBirthday, dateTimeExpiration, picture);

                        startDate = null;
                        try {
                            startDate = simpleDateFormat.parse(dateTimeExpiration);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(System.currentTimeMillis() > startDate.getTime()){
                            //Ugyldig
                            selectedColor = Color.rgb(254, 56, 36);
                            expirationButton.setText(R.string.expired);
                            expirationButton.setTextSize(30);
                            expirationButton.setBackgroundColor(selectedColor);
                        }else{
                            //gyldig
                            selectedColor = Color.rgb(132, 205, 182);
                            selectedWhite = Color.rgb(255, 255, 255);
                            targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
                            try {
                                date = simpleDateFormat.parse(dateTimeExpiration);
                                formattedDate = targetFormat.format(date);
                                expirationDate = getResources().getString(R.string.GyldigTil) + " " + formattedDate;
                                expirationButton.setText(expirationDate);
                                expirationButton.setTextColor(selectedWhite);
                                expirationButton.setBackgroundColor(selectedColor);
                                expirationButton.setTextSize(14);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        firstAndSirName.setText(username);
                        BirthDay.setText(dateTimeBirthday);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        buildVersion = Build.VERSION.RELEASE;
        firstLetter = String.valueOf(buildVersion.charAt(0));
        number = Integer.parseInt(firstLetter);
        if(number < 6){
            System.gc();
            System.out.println("fordi lav api, går inn her");
            if(data == null){
                return;
            }
            android.net.Uri imageUri = data.getData();
            String [] filePath = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            columnIndex = cursor.getColumnIndex(filePath[0]);
            mediaPath = cursor.getString(columnIndex);

            cursor.close();

           // try {

                System.gc();

                //inputStream = getContentResolver().openInputStream(imageUri);
                //bitmap = BitmapFactory.decodeStream(inputStream);

                //Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                //File finalFile = new File(getRealPathFromURI(tempUri));

                intent = new Intent(this, Main4Activity.class);
                intent.putExtra("picture", mediaPath);


                //File fi = new File(mediaPath);
                //intent.putExtra("picture", fi);


                //stream = new ByteArrayOutputStream();
                //System.out.println("før " + sizeOf(bitmap));
                //bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //bitmap = getResizedBitmap(bitmap, 120, 120);
                //System.out.println("etter " + sizeOf(bitmap));
                //System.out.println(getResizedBitmap(bitmap, 120, 120));
                //byte[] bytes = stream.toByteArray();
                //intent.putExtra("bitmapBytes", bytes);
                startActivity(intent);
           // } catch (FileNotFoundException e) {
             //   e.printStackTrace();
              //  Toast.makeText(this, getResources().getString(R.string.pictureNotAvailable), Toast.LENGTH_SHORT).show();
            //}
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (resultCode == RESULT_OK) {
                if (requestCode == IMAGE_GALLERY_REQUEST) {
                    android.net.Uri imageUri = data.getData();
                    String [] filePath = {MediaStore.Images.Media.DATA};
                    android.database.Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    columnIndex = cursor.getColumnIndex(filePath[0]);
                    mediaPath = cursor.getString(columnIndex);
                    cursor.close();

                   // try {
                     //   inputStream = getContentResolver().openInputStream(imageUri);
                       // bitmap = BitmapFactory.decodeStream(inputStream);
                        intent = new Intent(this, Main4Activity.class);
                        //stream = new ByteArrayOutputStream();
                        //bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        //byte[] bytes = stream.toByteArray();
                        intent.putExtra("picture", mediaPath);
                        startActivity(intent);
                        //cursor.close();
                        //inputStream.close();

                   // } catch (FileNotFoundException e) {
                     //   e.printStackTrace();
                      //  Toast.makeText(this, getResources().getString(R.string.pictureNotAvailable), Toast.LENGTH_SHORT).show();
                    //} catch (IOException e) {
                     //   e.printStackTrace();
                    //}
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
                System.out.println("her?");

                Toast.makeText(this, R.string.GiveAccess, Toast.LENGTH_LONG).show();

            } else {
                System.out.println("eller kanskje her?");
                Toast.makeText(this, getResources().getString(R.string.GiveAccess), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadImageFromStorage(String path) {
        try {
            pictureFile = new File(path, "profile.jpg");
            bitmap = BitmapFactory.decodeStream(new FileInputStream(pictureFile));
            image = (ImageView)findViewById(R.id.window1);
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            String picturePath = saveToInternalStorage(result);
            sessionManager.updatePath(picturePath);

        }
    }


    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        myPath = new File(directory, "picture.jpg");

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    protected int sizeOf(Bitmap data) {
        return data.getByteCount();
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String s = cursor.getString(idx);
        cursor.close();
        return s;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}








