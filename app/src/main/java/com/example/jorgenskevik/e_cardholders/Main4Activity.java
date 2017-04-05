package com.example.jorgenskevik.e_cardholders;

import android.content.Context;
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

public class Main4Activity extends AppCompatActivity {
    private ImageView view2;
    SessionManager sessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        view2 = (ImageView) findViewById(R.id.imageView4);
        sessionManager = new SessionManager(getApplicationContext());


        byte[] bytes = getIntent().getByteArrayExtra("bitmapbytes");

        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        view2.setImageBitmap(bmp);

    }

    public void addPictureButton(View v) {

        byte[] bytes = getIntent().getByteArrayExtra("bitmapbytes");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        HashMap<String, String> userDetails = sessionManager.getUserDetails();

        String fourDigits = userDetails.get(SessionManager.KEY_PICTURETOKEN);

        String authToken = userDetails.get(SessionManager.KEY_TOKEN);

        EditText code = (EditText) findViewById(R.id.editText1);

        String codeString = code.getText().toString();


        if (fourDigits.trim().equals("BRUKT")) {
            Context context = getApplicationContext();
            CharSequence text = "Du har allerede lastet opp et bilde";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        } else if (fourDigits.trim().equals(codeString)) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(KVTVariables.getLocal_URL())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            String id = userDetails.get(SessionManager.KEY_ID);

            UserAPI userapi = retrofit.create(UserAPI.class);
            String bearertoken = "Bearer " + authToken.toString();

            //File file = new File(String.valueOf(bmp));
            Uri tempUri = getImageUri(getApplicationContext(), bmp);

            File finalFile = new File(getRealPathFromURI(tempUri));

            System.out.println(finalFile);

            System.out.println(getMimeType(finalFile));

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), finalFile);

            System.out.println(requestFile);

            //multipartFormData.append(imageDataAndMimeType.0, withName: "photo", fileName: "profile_picture"+NSUUID().uuidString, mimeType: imageDataAndMimeType.1)



            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("photo", finalFile.getName(), requestFile);

            RequestBody CodeToken =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), fourDigits);



            userapi.postPicture(id, bearertoken, KVTVariables.getAcceptVersion(), KVTVariables.getAppkey(), body, CodeToken).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()){
                        System.out.println("This is good");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    System.out.println("This is nicht good");

                }
            });



            //File file1 = new File("/storage/emulated/0/Download/Corrections 6.jpg");
            //System.out.println("start " + String.valueOf(bmp) + " slutt");
            //RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), finalFile);


           // MultipartBody.Part imagePart = MultipartBody.Part.createFormData("photo", finalFile.getName(), RequestBody.create(MediaType.parse("image/*"), finalFile));
            //Call<ResponseBody> call = UserAPI.getmInstance().getService().postPicture(imagePart,RequestBody.create(MediaType.parse("text/plain"), UserAPI.class.getUserID()));


            //System.out.println("hei " + encodeToBase64(bmp, Bitmap.CompressFormat.JPEG, 100) + " hade");

           // PictureModel pictureModel = new PictureModel(fourDigits, encodeToBase64(bmp, Bitmap.CompressFormat.JPEG, 100));

           /* userapi.postPicture(id, bearertoken, KVTVariables.getAcceptVersion(), KVTVariables.getAppkey(),).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    System.out.println("dette gikk bra");


                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    System.out.println("dette gikk ikke så bra");

                }
            });

        } else if (!fourDigits.trim().equals(codeString)) {
            Context context = getApplicationContext();
            CharSequence text = "Feil kode, kontakt IKT";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }*/
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static String getMimeType(File url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(String.valueOf(url));
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
