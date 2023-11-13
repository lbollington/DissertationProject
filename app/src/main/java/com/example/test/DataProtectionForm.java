package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class DataProtectionForm extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_protection_form);
        Button agreeButton = findViewById(R.id.agreeBtn);
        Button declineButton = findViewById(R.id.declineBtn);

        agreeButton.setOnClickListener(view -> startActivity(new Intent(   DataProtectionForm.this, Register.class)));
        declineButton.setOnClickListener(view -> startActivity(new Intent(   DataProtectionForm.this, Login.class)));

    }
}