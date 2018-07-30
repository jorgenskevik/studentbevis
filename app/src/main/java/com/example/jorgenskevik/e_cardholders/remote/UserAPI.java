package com.example.jorgenskevik.e_cardholders.remote;

/**
 * Created by jorgenskevik on 20.03.2017.
 */

import com.example.jorgenskevik.e_cardholders.models.LoginModel;
import com.example.jorgenskevik.e_cardholders.models.Login_model;
import com.example.jorgenskevik.e_cardholders.models.Token;
import com.example.jorgenskevik.e_cardholders.models.Unit;
import com.example.jorgenskevik.e_cardholders.models.User;
import com.example.jorgenskevik.e_cardholders.models.FirebaseLoginModel;
import com.example.jorgenskevik.e_cardholders.models.UserDevice;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * The interface User api.
 */
public interface UserAPI {



   /* @Multipart
    @PUT("users/change-picture/")
    Call<User> postPicture(@Header("Authorization") String auth,
                           @Part MultipartBody.Part photo,
                           @Part("id") RequestBody id,
                           @Part("picture_token") RequestBody pictureToken);
*/



    @Multipart
    @PUT("users/change-picture/")
    Call<User> postPicture(@Header("Authorization") String auth, @Part MultipartBody.Part picture, @Part("picture_token") RequestBody pictureToken);

    /*@Multipart
    @PUT("users/changePicture")
    Call<User> postPicture(@Path("id") String id,
                           @Header("Authorization") String auth,
                           @Header("accept-version") String version,
                           @Header("client_key") String clientKey,
                           @Part MultipartBody.Part photo,
                           @Part("pictureToken") RequestBody pictureToken);
*/
    /**
     * Gets user.
     *

     */
    @GET("units/{unit_pk}/membership/")
    Call<Login_model> getUser(@Header("Authorization")  String auth, @Path("unit_pk") String unit_pk);

    /**
     * User login call.
     *
     * @return the call
     */
    @POST("firebase/{unit_id}/firebase-api-token-auth/")
    Call<LoginModel> userLogin(@Body FirebaseLoginModel firebaseLoginModel, @Path("unit_id") String unit_id);

    @GET("units/search/")
    Call<List<Unit>> getSchools();

    @POST("users/user-device/")
    Call<Token> postToken(@Body UserDevice userDevice , @Header("Authorization") String authorization);

}
