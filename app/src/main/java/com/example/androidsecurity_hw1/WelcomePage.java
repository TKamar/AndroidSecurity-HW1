package com.example.androidsecurity_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class WelcomePage extends AppCompatActivity {

    private TextView welcome;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        findViews();

        Intent i = getIntent();
        String username = i.getStringExtra("currUsername");
        welcome.setText("Welcome " + username);
    }


    public void findViews() {
        welcome = findViewById(R.id.edtWelcome);
    }


}