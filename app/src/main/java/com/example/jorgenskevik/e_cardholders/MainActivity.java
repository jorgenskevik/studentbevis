package com.example.jorgenskevik.e_cardholders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
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


import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * The type Main activity.
 */
public class MainActivity extends Activity {
    private AuthCallback authCallback;
    /**
     * The Auth config.
     */

    public TwitterAuthConfig authConfig = new TwitterAuthConfig(KVTVariables.getTwitterKey(), KVTVariables.getTwitterSecret());
    /**
     * The Session manager.
     */
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JodaTimeAndroid.init(this);
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
            //digitsButton.setAuthTheme(android.R.style.Theme_Black);
            digitsButton.setText(R.string.LogginButton);
            digitsButton.setBackgroundColor(Color.TRANSPARENT);
            digitsButton.setTextSize(16);
            digitsButton.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));

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
                    userapi.userLogin(
                            authHeader.get("X-Verify-Credentials-Authorization"),
                            authHeader.get("X-Auth-Service-Provider"), authHeader.get("client_key"),
                            authHeader.get("phoneNumber"), authHeader.get("accept-version")).enqueue(new Callback<LoginModel>() {

                        @Override
                        public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                            if (response.isSuccessful()) {

                                LoginModel LoginList = response.body();

                                // Session Manager
                                sessionManager = new SessionManager(getApplicationContext());

                                String usernameString = LoginList.user.getName();
                                String emailString = LoginList.user.getEmail();
                                String tokenString = LoginList.token;
                                String picture = LoginList.user.getPicture();

                                String studentNumber = LoginList.user.getStudentNumber();
                                String id = LoginList.user.getId();

                                String role = LoginList.user.getRole();
                                String pictureToken = LoginList.user.getPictureToken();

                                java.util.Date dateToExpiration = LoginList.user.getExpirationDate();
                                java.util.Date birthdayDate = LoginList.user.getDateOfBirth();


                                DateTime timeToExpiration = new DateTime(dateToExpiration);
                                DateTime timeBirthday = new DateTime(birthdayDate);


                                DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd-MMM-yyyy");
                                DateTimeFormatter dateTimeFormatter2 = DateTimeFormat.forPattern("yyyy-MM-dd");

                                String birthDateString = dateTimeFormatter.print(timeBirthday);
                                String expirationString = dateTimeFormatter2.print(timeToExpiration);



                                sessionManager.createLoginSession(usernameString,emailString, tokenString, studentNumber, id, role, pictureToken, expirationString, birthDateString, picture);
                                Digits.logout();

                                if (role.equals("admin")) {
                                Context context = getApplicationContext();
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(context, R.string.youareadmin, duration);
                                toast.show();


                                } else if (emailString.trim().equals("") || id.trim().equals("") || usernameString.trim().equals("") || role.trim().equals("") || pictureToken.trim().equals("")) {
                                    Context context = getApplicationContext();
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, R.string.contactIT, duration);
                                    toast.show();
                                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                                    startActivity(intent);

                                } else {
                                    //Digits.logout();
                                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                                    startActivity(intent);

                                }
                            } else {
                                Context context = getApplicationContext();
                                CharSequence text = response.message();
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginModel> call, Throwable t) {
                            Context context = getApplicationContext();
                            CharSequence text = t.getMessage();
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    });
                }

                @Override
                public void failure(DigitsException exception) {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, R.string.signInDigiFail, duration);
                    toast.show();
                }
            });

        } else{
            Intent intent = new Intent(MainActivity.this, Main3Activity.class);
            startActivity(intent);
        }
    }

    /**
     * Send to second activity.
     *
     * @param view the view
     */
    public void sendToSecondActivity(View view) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }

    /**
     * Get auth callback auth callback.
     *
     * @return the auth callback
     */
    public AuthCallback getAuthCallback(){
        return authCallback;
    }

    /**
     * Logout.
     *
     * @param w the w
     */
    public void logout(View w){
        Digits.logout();
    }
}
