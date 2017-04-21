package com.example.jorgenskevik.e_cardholders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * The type Main 5 activity.
 */
public class Main5Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main5);

    }

    /**
     * Open card.
     *
     * @param view the view
     */

    public void openCard(View view){
        Intent intent = new Intent(Main5Activity.this, Main3Activity.class);
        startActivity(intent);
    }
}
