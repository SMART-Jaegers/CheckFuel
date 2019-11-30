package com.example.something;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.RelativeLayout;


import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void refueling(@NotNull View view) {
        switch (view.getId()) {
            case R.id.refuling:
                Intent intent = new Intent(this, SetVolume.class);
                startActivity(intent);
                break;
            default:
                break;

        }


    }
    public void choosethegasstation(@NotNull View view) {
        switch (view.getId()) {
            case R.id.quality:
                Intent intent =new  Intent(this, ChooseTheGasSation.class);
                startActivity(intent);
                break;
            default:
                break;

        }
    }



}