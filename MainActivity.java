package com.thaina.filmesdahora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Login");
    }

    public void onclick(View view) {
        Intent intent;
        intent = new Intent(MainActivity.this, Movies.class);
        startActivity(intent);
    }
}