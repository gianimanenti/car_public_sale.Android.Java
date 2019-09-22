package com.moondu.leilao.view.others;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.moondu.leilao.R;
import com.moondu.leilao.view.others.Print;
import com.moondu.leilao.view.others.Screenshot;

import java.io.File;

public class PrintScreenshot extends AppCompatActivity {

    private View main;
    private File imageFile;
    //private ImageView imageView;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_screenshot);

        main = findViewById(R.id.main);
        //imageView = findViewById(R.id.ivScreenshot);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI();

        handler = new Handler();
    }

    private Runnable takeSreenshotRunnable = new Runnable() {
        public void run() {
            takeSreenshotAction();
        }
    };

    public void takeSreenshot(View view){

        handler.postDelayed(takeSreenshotRunnable, 500);
    }

    private void takeSreenshotAction(){

        Bitmap b = Screenshot.takescreenshotOfRootView(main);
        //imageView.setImageBitmap(b);
        //main.setBackgroundColor(Color.parseColor("#999999"));
        //imageFile = Screenshot.storeScreenshot(b, "teste.jpg");

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        imageFile = Screenshot.storeScreenshot(b, "teste", storageDir);

        showSystemUI();

        Toast.makeText(this, "File Created", Toast.LENGTH_LONG).show();
    }

    public void openScreenshot(View view){

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    public void printSreenshot(View view){

        //Print.printHtml(this);
        Print.printImage(this, imageFile.getPath());
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}
