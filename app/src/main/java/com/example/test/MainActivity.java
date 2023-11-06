package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String userId;
    StorageReference storageReference;          //variables


    private CircleImageView profileImageView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     //onCreate method to set content view

        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");


        Button requestApp = findViewById(R.id.btnRequestApp);
        requestApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                //navigates to appointment request
                FirebaseAuth.getInstance();
                startActivity(new Intent(MainActivity.this, AppRequest.class));
                finish();
            }
        });

        Button viewDentalHistory = findViewById(R.id.btnViewDentalHistory);
        viewDentalHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance();                                                 //navigates to dental history
                startActivity(new Intent(MainActivity.this, DentalHistory.class));
                finish();
            }
        });

        Button logout = findViewById(R.id.logoutPatientBtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            //navigates to login page
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }
        });


    }
}
