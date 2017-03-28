package com.example.jorgenskevik.e_cardholders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsOAuthSigning;
import com.digits.sdk.android.DigitsSession;
import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;
import com.example.jorgenskevik.e_cardholders.models.LoginModel;
import com.example.jorgenskevik.e_cardholders.models.SessionManager;
import com.example.jorgenskevik.e_cardholders.models.User;
import com.example.jorgenskevik.e_cardholders.remote.UserAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {
    //private AuthCallback authCallback;

    public TwitterAuthConfig authConfig = new TwitterAuthConfig(KVTVariables.getTwitterKey(), KVTVariables.getTwitterSecret());



    // Session Manager Class
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // id
        String id = user.get(SessionManager.KEY_ID);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        //token
        String token = user.get(SessionManager.KEY_TOKEN);


        if(name == null || id == null || email == null || token == null){
            super.onCreate(savedInstanceState);


            //Digits logged in setup
            Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build(), new Crashlytics());

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_main);

            //setting style for the digits button
            DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
            digitsButton.setAuthTheme(android.R.style.Theme_Material);

           // AuthConfig.Builder authConfigBuilder = new AuthConfig.Builder()
             //       .withAuthCallBack(callback)
              //      .withPhoneNumber("+34111111111")

          //  Digits.authenticate(authConfigBuilder.build());

            digitsButton.setCallback(new AuthCallback() {


                @Override
                public void success(DigitsSession session, String phoneNumber) {
                    TwitterAuthToken authToken = session.getAuthToken();
                    DigitsOAuthSigning oAuthSigning = new DigitsOAuthSigning(authConfig, authToken);
                    final Map<String, String> authHeader = oAuthSigning.getOAuthEchoHeadersForVerifyCredentials();
                    authHeader.put("phoneNumber", session.getPhoneNumber());
                    authHeader.put("client_key", KVTVariables.getAppkey());
                    authHeader.put("accept-version", KVTVariables.getAcceptVersion());

                    Gson gson = new GsonBuilder()
                           .setLenient()
                          .create();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(KVTVariables.getBaseUrl())
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    UserAPI userapi = retrofit.create(UserAPI.class);
                    userapi.userLogin(authHeader.get("X-Verify-Credentials-Authorization"), authHeader.get("X-Auth-Service-Provider"), authHeader.get("client_key"), authHeader.get("phoneNumber"), authHeader.get("accept-version")).enqueue(new Callback<LoginModel>() {
                        @Override
                        public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                            if (response.isSuccessful()) {

                                LoginModel LoginList = response.body();


                                // Session Manager
                                sessionManager = new SessionManager(getApplicationContext());

                                String username = LoginList.user.getName();
                                String email = LoginList.user.getEmail();
                                String token = LoginList.token;
                                String id = LoginList.user.getId();
                                String picture = LoginList.user.getPicture();
                                String role = LoginList.user.getRole();
                                String pictureToken = LoginList.user.getPictureToken();
                                Date dateExperatonDate = LoginList.user.getExpirationDate();
                                long mills = dateExperatonDate.getTime();
                                //DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                //String SexpirationDate = df.format(DexpirationDate);
                                //System.out.println(SexpirationDate);


                                sessionManager.createLoginSession(username,email,token,id,role, pictureToken, mills);//SexpirationDate);



                                if (role.equals("admin")) {

                                Context context = getApplicationContext();
                                CharSequence text = "Du er admin bruker, bruk web-appen";
                                int duration = Toast.LENGTH_LONG;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();


                                } else if (email.trim().equals("") || id.trim().equals("") || username.trim().equals("") || role.trim().equals("") || pictureToken.trim().equals("")) {
                                    Context context = getApplicationContext();
                                    CharSequence text = "Ta kontakt med IT-gutta";
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                                    startActivity(intent);

                                } else {
                                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                                    startActivity(intent);
                                }
                            } else {
                                Context context1 = getApplicationContext();
                                CharSequence text1 = response.message();
                                int duration1 = Toast.LENGTH_SHORT;
                                Toast toast1 = Toast.makeText(context1, text1, duration1);
                                toast1.show();

                            }
                        }

                        @Override
                        public void onFailure(Call<LoginModel> call, Throwable t) {
                            Context context1 = getApplicationContext();
                            CharSequence text1 = t.getMessage();
                            int duration1 = Toast.LENGTH_SHORT;
                            Toast toast1 = Toast.makeText(context1, text1, duration1);
                            toast1.show();
                        }
                    });
                }

                @Override
                public void failure(DigitsException exception) {
                    Context context1 = getApplicationContext();
                    CharSequence text1 = "Sign in with Digits failure";
                    int duration1 = Toast.LENGTH_SHORT;
                    Toast toast1 = Toast.makeText(context1, text1, duration1);
                    toast1.show();
                }
            });
        } else{
            Intent intent = new Intent(MainActivity.this, Main3Activity.class);
            startActivity(intent);
        }
    }


    public void sendToSecondActivity(View view) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }

   // public AuthCallback getAuthCallback(){
    //    return authCallback;
    //}

}
