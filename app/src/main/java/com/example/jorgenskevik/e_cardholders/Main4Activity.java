package com.example.jorgenskevik.e_cardholders;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;
import com.example.jorgenskevik.e_cardholders.models.PictureModel;
import com.example.jorgenskevik.e_cardholders.models.SessionManager;
import com.example.jorgenskevik.e_cardholders.models.User;
import com.example.jorgenskevik.e_cardholders.remote.UserAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

/**
 * The type Main 4 activity.
 */
public class Main4Activity extends AppCompatActivity {
    private ImageView imageView;
    /**
     * The Session manager.
     */
    SessionManager sessionManager;
    /**
     * The constant bitMapBytes.
     */
    public static final String bitMapBytes = "bitmapBytes";
    /**
     * The constant photoCodeToken.
     */
    public static final String photoCodeToken = "BRUKT";
    /**
     * The constant tokenBearer.
     */
    public static final String tokenBearer = "Bearer ";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main4);
        imageView = (ImageView) findViewById(R.id.imageView4);
        sessionManager = new SessionManager(getApplicationContext());
        byte[] bytes = getIntent().getByteArrayExtra(bitMapBytes);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bitmap);
    }

    /**
     * Add picture button.
     *
     * @param v the v
     */
    public void addPictureButton(View v) {
        byte[] bytes = getIntent().getByteArrayExtra(bitMapBytes);
        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        String fourDigits = userDetails.get(SessionManager.KEY_PICTURETOKEN);
        String authToken = userDetails.get(SessionManager.KEY_TOKEN);
        EditText code = (EditText) findViewById(R.id.editText1);
        String codeString = code.getText().toString();

        if (fourDigits.trim().equals(photoCodeToken)) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, R.string.DenyPicture, duration);
            toast.show();

        } else if (fourDigits.trim().equals(codeString)) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(KVTVariables.getBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            String id = userDetails.get(SessionManager.KEY_ID);
            UserAPI userapi = retrofit.create(UserAPI.class);
            String bearerToken = tokenBearer + authToken.toString();
            Uri temporaryUri = getImageUri(getApplicationContext(), bitmap);
            File finalFile = new File(getRealPathFromURI(temporaryUri));
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(temporaryUri)), finalFile);

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("photo", finalFile.getName(),requestFile);

            RequestBody CodeToken =
                    RequestBody.create(MediaType.parse("multipart/form-data"), fourDigits);

            userapi.postPicture(id, bearerToken, KVTVariables.getAcceptVersion(), KVTVariables.getAppkey(), body, CodeToken).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()){
                        saveToInternalStorage(bitmap);
                        String path = saveToInternalStorage(bitmap);
                        sessionManager.updatePath(path);
                        sessionManager.updatePictureToken(photoCodeToken);
                        Intent intent = new Intent(Main4Activity.this, Main3Activity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, R.string.PictureNotUpdated, duration);
                    toast.show();
                }
            });

        } else if (!fourDigits.trim().equals(codeString)) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, R.string.wrongCode, duration);
            toast.show();
        }
    }

    /**
     * Gets image uri.
     *
     * @param inContext the in context
     * @param inImage   the in image
     * @return the image uri
     */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * Gets real path from uri.
     *
     * @param uri the uri
     * @return the real path from uri
     */
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    /**
     * Gets mime type.
     *
     * @param url the url
     * @return the mime type
     */
    public static String getMimeType(File url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(url));
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath = new File(directory, "profile.jpg");

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
}
