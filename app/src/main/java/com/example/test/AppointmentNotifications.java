package com.example.test;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;



public class AppointmentNotifications extends AppCompatActivity{
    TextView appRequestOne, appRequestTwo, appRequestThree, appRequestFour, appRequestFive, appRequestSix, appRequestSeven, appRequestEight;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_app);
    }
}



