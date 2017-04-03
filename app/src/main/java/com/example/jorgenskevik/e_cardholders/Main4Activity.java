package com.example.jorgenskevik.e_cardholders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
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
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
            System.out.println("hei " + encodeToBase64(bmp, Bitmap.CompressFormat.JPEG, 100) + " hade");

            PictureModel pictureModel = new PictureModel(fourDigits, encodeToBase64(bmp, Bitmap.CompressFormat.JPEG, 100));

            userapi.postPicture(id, bearertoken, KVTVariables.getAcceptVersion(), KVTVariables.getAppkey(), pictureModel).enqueue(new Callback<User>() {
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
    }
}
