package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class DataProtectionForm extends AppCompatActivity {

    private Button agreeButton, declineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_protection_form);                 //onCreate method to set content view
        agreeButton = findViewById(R.id.agreeBtn);
        declineButton = findViewById(R.id.declineBtn);

        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                        //agree and decline buttons for data protection form
                startActivity(new Intent(   DataProtectionForm.this, Register.class));
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(   DataProtectionForm.this, Login.class));
            }
        });


    }
}