package com.example.jorgenskevik.e_cardholders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * The type Terms 1 activity.
 */
public class Terms1Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.terms1_view);

    }

    /**
     * Open card.
     *
     * @param view the view
     */
    public void openCard(View view){
        Intent intent = new Intent(Terms1Activity.this, UserActivity.class);
        startActivity(intent);
    }
}
