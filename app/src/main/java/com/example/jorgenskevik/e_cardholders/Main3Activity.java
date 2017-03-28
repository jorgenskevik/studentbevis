package com.example.jorgenskevik.e_cardholders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.example.jorgenskevik.e_cardholders.Variables.KVTVariables;
import com.example.jorgenskevik.e_cardholders.models.SessionManager;
import com.example.jorgenskevik.e_cardholders.models.User;
import com.example.jorgenskevik.e_cardholders.remote.UserAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class Main3Activity extends AppCompatActivity implements ActionSheet.ActionSheetListener {
    private Main3Activity mContext;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main3);
        mContext = Main3Activity.this;

        //View barcode
        ImageButton codebutton = (ImageButton)findViewById(R.id.imageButton3);
        TextView nameview = (TextView)findViewById(R.id.textView8);
        nameview.setText(KVTVariables.getUserName());
        sessionManager = new SessionManager(getApplicationContext());


        codebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent (Main3Activity.this, Code.class));
            }
        });

       /* ImageButton Settingsbutton = (ImageButton)findViewById(R.id.imageButton);
        Settingsbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent (Main3Activity.this, Settings.class));
            }
        });*/

    }

    public void testCam(View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }

    public void openSettings(View v){
        setTheme(R.style.ActionSheetStyleiOS7);
        showActionSheet();
    }

    public void showActionSheet() {
        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle("Cancel")
                .setOtherButtonTitles("Logg ut", "Vilkår", "", "Item4")
                .setCancelableOnTouchOutside(true).setListener(this).show();
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
        //Toast.makeText(getApplicationContext(), "dismissed isCancle = " + isCancle, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        Toast.makeText(getApplicationContext(), "click item index = " + index,
                Toast.LENGTH_SHORT).show();

        //Log out
        if(index == 0){
            sessionManager.logoutUser();
            Intent intent = new Intent(Main3Activity.this, MainActivity.class);
            startActivity(intent);
        }

        //terms
        if(index == 1){
            Intent intent = new Intent(Main3Activity.this, Main2Activity.class);
            startActivity(intent);

        }

    }
}







