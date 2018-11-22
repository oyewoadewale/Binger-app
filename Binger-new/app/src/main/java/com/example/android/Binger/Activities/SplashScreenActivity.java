package com.example.android.Binger.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.Binger.R;


public class SplashScreenActivity extends AppCompatActivity {

    TextView refresh;
    Context context;
    TextView dialogBox;
    private int FAVOURITE_INTENT;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        refresh = findViewById(R.id.refresh);
        dialogBox = findViewById(R.id.dialog);
        relativeLayout = findViewById(R.id.relative);
        FAVOURITE_INTENT = 1;


        //check to see if network connection is available
        if (!isNetworkAvailable()){
            networkDialog(dialogBox);
        } else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //refresh.setVisibility(View.VISIBLE);
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    Log.i("Act", "ActivityX could not start");
                }
            }, 2350);
        }

        //set onClck on the refresh text
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()){
                    refresh.setVisibility(View.VISIBLE);
                    networkDialog(dialogBox);
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dialogBox.setVisibility(View.GONE);
        refresh.setVisibility(View.VISIBLE);
    }

    //this method returns true if there is active network connection
    private boolean isNetworkAvailable() {
        ConnectivityManager con = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = con.getActiveNetworkInfo();
        return  activeNetwork != null && activeNetwork.isConnected();
    }

    private void networkDialog (View view) {
        dialogBox.setVisibility(View.VISIBLE );
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Network unavailable. Do you want to view saved movie?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (FAVOURITE_INTENT != 1 ) {
                    FAVOURITE_INTENT = 1;
                }
                refresh.setVisibility(View.VISIBLE);
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class)
                        .putExtra("Favourite", FAVOURITE_INTENT));
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                refresh.setVisibility(View.VISIBLE);
                Toast.makeText(SplashScreenActivity.this,
                        "Please turn on WI-FI or mobile network", Toast.LENGTH_SHORT).show();
                dialogBox.setVisibility(View.INVISIBLE);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }
}