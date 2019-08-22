package com.example.jorgenskevik.e_cardholders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;
import com.example.jorgenskevik.e_cardholders.models.FirebaseLoginModel;
import com.example.jorgenskevik.e_cardholders.models.LoginModel;
import com.example.jorgenskevik.e_cardholders.models.SessionManager;
import com.example.jorgenskevik.e_cardholders.models.Token;
import com.example.jorgenskevik.e_cardholders.models.Unit;
import com.example.jorgenskevik.e_cardholders.models.UnitMembership;
import com.example.jorgenskevik.e_cardholders.models.User;
import com.example.jorgenskevik.e_cardholders.models.UserDevice;
import com.example.jorgenskevik.e_cardholders.remote.UserAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

import in.myinnos.library.AppIconNameChanger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    private static float one_degree_just_beacause = 0;
    private static ArrayList<String> mProductArrayList = new ArrayList<String>();


    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private ViewGroup mPhoneNumberViews;
    private ViewGroup mSignedInViews;

    private TextView mStatusText;
    private TextView mDetailText;

    private EditText mPhoneNumberField;
    private EditText mVerificationField;
    private EditText landskode;

    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;

    private ImageView picture_view;

    ProgressBar progressBar;
    SessionManager sessionManager;

    private int unit_id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_login_page);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        Intent mIntent = getIntent();
        unit_id = mIntent.getIntExtra("unit_id_i_need", 0);

        JodaTimeAndroid.init(this);
        sessionManager = new SessionManager(getApplicationContext());

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Assign views
        mPhoneNumberViews = (ViewGroup) findViewById(R.id.phone_auth_fields);
        mSignedInViews = (ViewGroup) findViewById(R.id.signed_in_buttons);

        mStatusText = (TextView) findViewById(R.id.status);
        mDetailText = (TextView) findViewById(R.id.detail);

        mPhoneNumberField = (EditText) findViewById(R.id.field_phone_number);
        mVerificationField = (EditText) findViewById(R.id.field_verification_code);
        landskode = (EditText) findViewById(R.id.picker);

        mStartButton = (Button) findViewById(R.id.button_start_verification);
        mVerifyButton = (Button) findViewById(R.id.button_verify_phone);
        mResendButton = (Button) findViewById(R.id.button_resend);
        Button mSignOutButton = (Button) findViewById(R.id.sign_out_button);

        freeMemory();

        // Assign click listeners
        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);

        mStartButton.setTextColor(ContextCompat.getColor(this, R.color.black));

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    int selectedColor = Color.rgb(254, 0, 0);
                    if(!hasActiveInternetConnection()){
                        mDetailText.setText(R.string.nonet);
                        mDetailText.setTextColor(selectedColor);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }



            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(mPhoneNumberField.getText().toString());
        }
        // [END_EXCLUDE]
    }
    // [END on_start_check_user]

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mVerificationId == null && savedInstanceState != null) {
            mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
        }
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                landskode.getText().toString() + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
        mStatusText.setVisibility(View.INVISIBLE);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        freeMemory();
        // [START verify_with_code]
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        }catch (Exception e){
            Toast.makeText(this, R.string.Skrivinn, Toast.LENGTH_LONG).show();
        }
        // [END verify_with_code]
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        if(!phoneNumber.equals("")){
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    landskode.getText().toString() + phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks,         // OnVerificationStateChangedCallbacks
                    token);             // ForceResendingToken from callbacks
        }else{
            Toast.makeText(this, R.string.Skrivinn, Toast.LENGTH_LONG).show();
        }
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    private void signOut() {
        mAuth.signOut();
        updateUI(STATE_INITIALIZED);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public boolean hasActiveInternetConnection() {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                enableViews(mStartButton, mPhoneNumberField);
                disableViews(mVerifyButton, mResendButton, mVerificationField);
                mDetailText.setText(null);
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the
                enableViews(mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                disableViews(mStartButton);
                mDetailText.setText(R.string.status_code_sent);
                mDetailText.setTextColor(Color.parseColor("#43a047"));
                mVerifyButton.setTextColor(ContextCompat.getColor(this, R.color.black));
                mStartButton.setTextColor(ContextCompat.getColor(this, R.color.line_color));
                mResendButton.setTextColor(ContextCompat.getColor(this, R.color.black));

                break;
            case STATE_VERIFY_FAILED:
                // Verification has failed, show all options
                enableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,
                        mVerificationField);
                mDetailText.setText(R.string.status_verification_failed);
                mDetailText.setTextColor(Color.parseColor("#dd2c00"));
                progressBar.setVisibility(View.INVISIBLE);
                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
                disableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,
                        mVerificationField);
                mDetailText.setText(R.string.status_verification_succeeded);
                mDetailText.setTextColor(Color.parseColor("#43a047"));
                progressBar.setVisibility(View.INVISIBLE);

                // Set the verification text based on the credential
                if (cred != null) {

                    if (cred.getSmsCode() != null) {
                        mVerificationField.setText(cred.getSmsCode());
                    } else {
                        mVerificationField.setText(R.string.instant_validation);
                        mVerificationField.setTextColor(Color.parseColor("#4bacb8"));
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:

                // No-op, handled by sign-in check
                mDetailText.setText(R.string.status_sign_in_failed);
                mDetailText.setTextColor(Color.parseColor("#dd2c00"));
                progressBar.setVisibility(View.INVISIBLE);
                break;
            case STATE_SIGNIN_SUCCESS:

                // Np-op, handled by sign-in check
                mStatusText.setText(R.string.signed_in);
                break;
        }

        if (user == null) {
            // Signed out
            mPhoneNumberViews.setVisibility(View.VISIBLE);
            mSignedInViews.setVisibility(View.GONE);

            //mStatusText.setText(R.string.sign_out);;
        } else {

            // Signed in
            mPhoneNumberViews.setVisibility(View.GONE);



            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            mUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();

                                Gson gson = new GsonBuilder()
                                        .setLenient()
                                        .create();

                                //local eller base
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(KVTVariables.getBaseUrl())
                                        .addConverterFactory(GsonConverterFactory.create(gson))
                                        .build();

                                UserAPI userapi = retrofit.create(UserAPI.class);
                                Intent mIntent = getIntent();
                                int intValue = mIntent.getIntExtra("Unit_ID", unit_id);
                                final FirebaseLoginModel firebaseLoginModel = new FirebaseLoginModel(mPhoneNumberField.getText().toString(), idToken);

                                userapi.userLogin(firebaseLoginModel, String.valueOf(intValue)).enqueue(new Callback<LoginModel>() {

                                    private void storeInSession(SessionManager sessionManager, User user, String token, Unit unit, UnitMembership unitMembership){
                                        String full_name = user.getFullName();
                                        String emailString = user.getEmail();
                                        String picture = user.getPicture();
                                        String user_id = user.getId();
                                        int role = user.getUser_role();
                                        String pictureToken = user.getPicture_token();
                                        String phone = user.getPhone();

                                        int unitMembershipId = unitMembership.getId();
                                        String student_class = unitMembership.getStudent_class();
                                        String student_number = unitMembership.getStudent_number();
                                        String membership_number = unitMembership.getMembership_number();
                                        String membership_type = unitMembership.getMembership_type();


                                        String card_type = unit.getCard_type();
                                        String unit_name = unit.getName();
                                        String unit_short_name = unit.getShort_name();
                                        String public_contact_phone = unit.getPublic_contact_phone();
                                        String public_contact_email = unit.getPublic_contact_email();
                                        String unit_logo = unit.getUnit_logo();
                                        String unit_logo_short = unit.getSmall_unit_logo();
                                        int unit_id = unit.getId();
                                        String primary_color = unit.getPrimary_color_rgba();
                                        String secondary_color = unit.getSecondary_color_rgba();

                                        java.util.Date dateToExpiration = unitMembership.getExpiration_date();
                                        java.util.Date birthdayDate = user.getDate_of_birth();

                                        DateTime timeToExpiration = new DateTime(dateToExpiration);
                                        DateTime timeBirthday = new DateTime(birthdayDate);

                                        DateTimeFormatter dateTimeFormatter2 = DateTimeFormat.forPattern("yyyy-MM-dd");

                                        String birthDateString = dateTimeFormatter2.print(timeBirthday);
                                        String expirationString = dateTimeFormatter2.print(timeToExpiration);

                                        sessionManager.create_login_session_user(full_name, emailString,
                                                token, user_id, role, pictureToken, birthDateString, picture, one_degree_just_beacause, phone);

                                        sessionManager.create_login_session_unit(unit_name, unit_short_name, unit_logo, unit_logo_short, unit_id,
                                                public_contact_email, public_contact_phone, card_type, unit.getPrimary_color_rgba(), unit.getSecondary_color_rgba());

                                        sessionManager.create_login_session_unit_member(expirationString, student_class, student_number, unitMembershipId
                                        , membership_number,membership_type );

                                    }
                                    @Override
                                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                                        if (!response.isSuccessful()) {
                                            Context context = getApplicationContext();
                                            int duration = Toast.LENGTH_SHORT;
                                            Toast toast = Toast.makeText(context,R.string.notmember , duration);
                                            toast.show();
                                            return;
                                        }


                                        String fcm_token = FirebaseInstanceId.getInstance().getToken();
                                        LoginModel LoginList = response.body();

                                        sessionManager = new SessionManager(getApplicationContext());

                                        User user = LoginList.getUser();
                                        String token = LoginList.getAuth_token();
                                        Unit unit  = LoginList.getUnit();
                                        UnitMembership unit_membership = LoginList.getUnitMembership();
                                        String bearToken = "token " + token;

                                        storeInSession(sessionManager, user, token, unit, unit_membership);
                                        sendRegistrationToServer(fcm_token, bearToken);

                                        if (LoginList.getUser().getUser_role() == 1 || LoginList.getUser().getUser_role() == 2) {
                                            Context context = getApplicationContext();
                                            int duration = Toast.LENGTH_LONG;
                                            Toast toast = Toast.makeText(context, R.string.youareadmin, duration);
                                            toast.show();
                                            return;
                                        }

                                        switch (unit.getCard_type()) {
                                            case "student_card": {
                                                Intent intent = new Intent(LoginActivity.this, UserActivity1.class);
                                                startActivity(intent);
                                                break;
                                            }
                                            case "school_card": {
                                                Intent intent = new Intent(LoginActivity.this, UserActivity1.class);
                                                startActivity(intent);
                                                break;
                                            }
                                            case "membership_card": {
                                                Intent intent = new Intent(LoginActivity.this, MemberActivity.class);
                                                startActivity(intent);
                                                break;
                                            }
                                            default:
                                                Context context = getApplicationContext();
                                                int duration = Toast.LENGTH_SHORT;
                                                Toast toast = Toast.makeText(context, R.string.wrongCode, duration);
                                                toast.show();
                                                break;
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
                                // Send token to your backend via HTTPS
                                // ...
                            }
                        }
                    });

            //final Map<String, String> authHeader;
            //finish();
        }
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            //mPhoneNumberField.setTextColor(Color.parseColor("#ff1744"));
            return false;
        }

        return true;
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }
    private void sendRegistrationToServer(String fcm_token, String auth_token) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //local eller base
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KVTVariables.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserAPI userapi = retrofit.create(UserAPI.class);

        UserDevice userDevice = new UserDevice(fcm_token);
        userapi.postToken(userDevice, auth_token).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });

    }

    public void changeLogo(final String logo_name){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KVTVariables.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        UserAPI userapi = retrofit.create(UserAPI.class);
        userapi.getSchools().enqueue(new Callback<List<Unit>>() {
            @Override
            public void onResponse(Call<List<Unit>> call, Response<List<Unit>> response) {
                List<Unit> unit = response.body();
                for (int i = 0; i < unit.size() ; i++) {
                    //mProductArrayList.add(unit.get(i).getShort_name());
                    if (!logo_name.equals(unit.get(i).getShort_name())){
                        mProductArrayList.add(unit.get(i).getShort_name());
                    }
                }
                new AppIconNameChanger.Builder(LoginActivity.this)
                        .activeName(logo_name) // String
                        .disableNames(mProductArrayList) // List<String>
                        .packageName(BuildConfig.APPLICATION_ID)
                        .build()
                        .setNow();
            }

            @Override
            public void onFailure(Call<List<Unit>> call, Throwable t) {

            }
        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start_verification:
                if (!validatePhoneNumber()) {
                    return;
                }

                ///////hide keyboard start
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);


                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                mStatusText.setText(R.string.Autoriserer);
                progressBar.setVisibility(View.VISIBLE);
                startPhoneNumberVerification(mPhoneNumberField.getText().toString());

                break;
            case R.id.button_verify_phone:
                String code = mVerificationField.getText().toString();
                int selectedWhite = Color.rgb(0, 0, 0);
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setTextColor(selectedWhite);
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                try{
                    verifyPhoneNumberWithCode(mVerificationId, code);
                }catch (NullPointerException e){
                    Toast.makeText(this, R.string.Skrivinn, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.button_resend:
                resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
                break;
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }

    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

}