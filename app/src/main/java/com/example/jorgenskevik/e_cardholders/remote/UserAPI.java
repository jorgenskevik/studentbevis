package com.example.jorgenskevik.e_cardholders.remote;

/**
 * Created by jorgenskevik on 20.03.2017.
 */

import android.content.Context;
import android.util.Log;

import com.example.jorgenskevik.e_cardholders.BuildConfig;
import com.example.jorgenskevik.e_cardholders.models.LoginModel;
import com.example.jorgenskevik.e_cardholders.models.PictureModel;
import com.example.jorgenskevik.e_cardholders.models.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

import static android.content.ContentValues.TAG;

public interface UserAPI {


    @POST("Users/{id}/changePicture")
    Call<User> postPicture(@Path("id") String id, @Header("Authorization") String auth, @Header("accept-version") String version, @Header("client_key") String clientkey, @Body PictureModel pictureModel);

    @GET("Users/me")
    Call<User> getUser();

    @POST("auth")
    Call<LoginModel> userLogin(@Header("X-Verify-Credentials-Authorization") String auth, @Header("X-Auth-Service-Provider") String link,
                                     @Header("client_key") String clientkey, @Header("phoneNumber") String number,
                                     @Header("accept-version") String version);

}
