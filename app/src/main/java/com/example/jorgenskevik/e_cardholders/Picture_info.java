package com.example.jorgenskevik.e_cardholders;

import android.*;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.system.ErrnoException;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;
import com.example.jorgenskevik.e_cardholders.models.SessionManager;
import com.example.jorgenskevik.e_cardholders.models.User;
import com.example.jorgenskevik.e_cardholders.remote.UserAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jorgenskevik on 22.07.2018.
 */


public class Picture_info extends Activity {
    TextView button_back, continue_picture, pick_photo, information_picture, textview_crop;
    ImageView profil_picture;
    private CropImageView mCropImageView;
    private Uri mCropImageUri;
    String authToken, fourDigits;
    HashMap<String, String> userDetails;
    SessionManager sessionManager;
    int user_id;
    Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_photo);

        mCropImageView = (CropImageView) findViewById(R.id.crop123);
        button_back = (TextView) findViewById(R.id.back_button);
        continue_picture = (TextView) findViewById(R.id.done_button);
        profil_picture = (ImageView) findViewById(R.id.sircle);
        pick_photo = (TextView) findViewById(R.id.pick_photo);
        information_picture = (TextView) findViewById(R.id.this_is_how);
        textview_crop = (TextView) findViewById(R.id.textview_crop);

            button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(Picture_info.this, BarCodeActivity.class);
                startActivity(back);
            }
        });

        pick_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(getPickImageChooserIntent(), 200);
            }
        });

        continue_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profil_picture.getDrawable() == null){
                    Toast.makeText(getApplicationContext(), R.string.set_picture, Toast.LENGTH_LONG).show();
                    return;
                }
                sessionManager = new SessionManager(getApplicationContext());
                userDetails = sessionManager.getUserDetails();
                authToken = "token " + userDetails.get(SessionManager.KEY_TOKEN);
                user_id = Integer.parseInt(userDetails.get(SessionManager.KEY_UNIT_ID));
                fourDigits = userDetails.get(SessionManager.KEY_PICTURETOKEN);
                String helping_string = imageUri.toString();
                final File file = new File(helping_string);

                String mimeType = getMimeType(file);

                RequestBody reqFile = RequestBody.create(MediaType.parse(mimeType), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), reqFile);
                RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), fourDigits);
                //RequestBody name_id = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(user_id));

                  Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(KVTVariables.getBaseUrl())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                UserAPI userapi = retrofit.create(UserAPI.class);

                userapi.postPicture(authToken, body, name).enqueue(new Callback<User>() {

                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        System.out.println("1");
                        if (response.isSuccessful()){
                            System.out.println("2");

                        }else{
                            System.out.println("3");

                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        System.out.println("4");                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            imageUri = getPickImageResultUri(data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(imageUri)) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (!requirePermissions) {
                mCropImageView.setImageUriAsync(imageUri);
                profil_picture.setImageURI(imageUri);
                information_picture.setText(R.string.your_picture);
                continue_picture.setTextColor(ContextCompat.getColor(this, R.color.logobluecolor));
                textview_crop.setTextColor(ContextCompat.getColor(this, R.color.logobluecolor));
                pick_photo.setTextColor(ContextCompat.getColor(this, R.color.line_color));

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mCropImageView.setImageUriAsync(mCropImageUri);
            profil_picture.setImageURI(mCropImageUri);
            information_picture.setText(R.string.your_picture);
            continue_picture.setTextColor(ContextCompat.getColor(this, R.color.logobluecolor));
            textview_crop.setTextColor(ContextCompat.getColor(this, R.color.logobluecolor));
            pick_photo.setTextColor(ContextCompat.getColor(this, R.color.line_color));

        } else {
            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    public void onCropImageClick(View view) {
        Bitmap cropped = mCropImageView.getCroppedImage(500, 500);
        if (cropped != null)
            profil_picture.setImageBitmap(cropped);
    }

    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    public boolean isUriRequiresPermissions(Uri uri) {
        try {
            ContentResolver resolver = getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (FileNotFoundException e) {
            if (e.getCause() instanceof ErrnoException) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public String getMimeType(File url) {
        String type = null;
        String test = String.valueOf(url);
        test = test.toLowerCase();
        String extension = MimeTypeMap.getFileExtensionFromUrl(test);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }if(type == null){
            type = "image/*";
        }
        return type;
    }

}
