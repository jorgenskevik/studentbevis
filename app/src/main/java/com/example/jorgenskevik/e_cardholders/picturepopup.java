package com.example.jorgenskevik.e_cardholders;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;
import com.example.jorgenskevik.e_cardholders.models.PictureModel;
import com.example.jorgenskevik.e_cardholders.models.SessionManager;
import com.example.jorgenskevik.e_cardholders.models.User;
import com.example.jorgenskevik.e_cardholders.remote.UserAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jorgenskevik on 31.03.2017.
 */

public class picturepopup extends Activity {
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picturepopup);
        sessionManager = new SessionManager(getApplicationContext());


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if(getIntent().hasExtra("byteArray")) {
            ImageView previewThumbnail = new ImageView(this);
            Bitmap b = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
            previewThumbnail.setImageBitmap(b);
            System.out.println("dette gikk ikke så bra");
            
        }
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.5));
    }

    public void addPictureButton(View v) {

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

            PictureModel pictureModel = new PictureModel(fourDigits, "");

            /*userapi.postPicture(id, bearertoken, KVTVariables.getAcceptVersion(), KVTVariables.getAppkey(), pictureModel).enqueue(new Callback<User>() {
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
    }*/
        }
    }
}


