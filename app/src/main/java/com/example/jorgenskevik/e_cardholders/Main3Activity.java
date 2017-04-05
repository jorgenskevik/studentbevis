package com.example.jorgenskevik.e_cardholders;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;
import com.example.jorgenskevik.e_cardholders.models.LoginModel;
import com.example.jorgenskevik.e_cardholders.models.PictureModel;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class Main3Activity extends AppCompatActivity implements ActionSheet.ActionSheetListener {
    public static final int CAM_REQUEST_CODE = 457843;
    private Main3Activity mContext;
    private static int IMAGE_GALLERY_REQUEST = 20;
    SessionManager sessionManager;
    private ImageView view2;
    private Uri imageUri;
    String mediaPath;



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
        TextView nameview1 = (TextView) findViewById(R.id.test1);
        TextView nameview2 = (TextView) findViewById(R.id.test2);
        TextView nameview3 = (TextView) findViewById(R.id.test3);
        TextView nameview4 = (TextView) findViewById(R.id.test4);

        HashMap<String, String> userDetails = sessionManager.getUserDetails();

        String name = userDetails.get(SessionManager.KEY_NAME);
        String name2 = userDetails.get(SessionManager.KEY_EXPERATIONDATE);
        String name3 = userDetails.get(SessionManager.KEY_EMAIL);
        String name4 = userDetails.get(SessionManager.KEY_STUDENTNUMBER);

        String path = userDetails.get(SessionManager.KEY_PATH);

        System.out.println("her skjer det noe " + path);

        if(path == null){
            view2.setImageResource(R.drawable.jogga2);
        }else{
            loadImageFromStorage(path);
        }


        nameview1.setText(name);
        nameview2.setText(name2);
        nameview3.setText(name3);
        nameview4.setText(name4);

        sessionManager = new SessionManager(getApplicationContext());


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
                .setCancelButtonTitle("Cancel")
                .setOtherButtonTitles("Logg ut", "Vilkår", "Sett Bilde", "Oppdater")
                .setCancelableOnTouchOutside(true).setListener(this).show();
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
        //Toast.makeText(getApplicationContext(), "dismissed isCancle = " + isCancle, Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(Main3Activity.this, Main2Activity.class);
            startActivity(intent);

        }
        //sett profilbilde
        if (index == 2) {

            HashMap<String, String> userDetails = sessionManager.getUserDetails();

            String fourDigits = userDetails.get(SessionManager.KEY_PICTURETOKEN);

            if (fourDigits.equals("BRUKT")) {
                Context context = getApplicationContext();
                CharSequence text = "Du har allerede lastet opp et bilde";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } else {
               /* Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), 1);
                startActivity(new Intent(Main3Activity.this, picturepopup.class));*/
                Intent photopickerintent = new Intent(Intent.ACTION_PICK);
                File pictureDire = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String pictureDirpath = pictureDire.getPath();

                Uri data = Uri.parse(pictureDirpath);

                photopickerintent.setDataAndType(data, "image/*");

                startActivityForResult(photopickerintent, IMAGE_GALLERY_REQUEST);
            }
        }
        //Oppdater appen
        if(index == 3){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(KVTVariables.getLocal_URL())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            HashMap<String, String> userDetails = sessionManager.getUserDetails();

            String version = KVTVariables.getAcceptVersion();
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

                        String picture = user.getPicture();
                        String role = user.getRole();
                        String pictureToken = user.getPictureToken();
                        java.util.Date juDate = user.getExpirationDate();
                        DateTime dt = new DateTime(juDate);

                        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                        String dtStr = fmt.print(dt);
                        System.out.println(username);
                        //sessionManager.logoutUser();

                        sessionManager.createUpdtaeLoginSession(username, email, studentNumber, id, role, pictureToken, dtStr);

                        TextView nameview1 = (TextView) findViewById(R.id.test1);
                        TextView nameview2 = (TextView) findViewById(R.id.test2);
                        TextView nameview3 = (TextView) findViewById(R.id.test3);
                        TextView nameview4 = (TextView) findViewById(R.id.test4);

                        nameview1.setText(username);
                        nameview2.setText(dtStr);
                        nameview3.setText(email);
                        nameview4.setText(studentNumber);


                    }else{
                        System.out.println(response.body());

                     }
                }


                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {

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

                        //view2.setImageBitmap(bitmap);


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Bilde ikke tilgjengelig", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(this, "Gi appen tilgang: settings/apps/Ecardholder/Storage", Toast.LENGTH_LONG).show();


            } else {
                Toast.makeText(this, "Gi appen tilgang: settings/apps/Ecardholder/Storage", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadImageFromStorage(String path) {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.window1);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}








