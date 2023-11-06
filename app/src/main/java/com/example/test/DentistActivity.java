package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;

public class DentistActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;        //variables
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentist); //onCreate method to set content view

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();        //initialise variables
        userId = firebaseAuth.getCurrentUser().getUid();

        Button upload = findViewById(R.id.btnUploadPatientInfo);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override                                                   //button for upload
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DentistUpload.class));
                finish();
            }
        });

    Button logout = findViewById(R.id.logoutDentistBtn);
        logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {                            //button to logout
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
    });
}
}