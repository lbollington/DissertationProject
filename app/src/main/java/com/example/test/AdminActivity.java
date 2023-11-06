package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class AdminActivity extends AppCompatActivity {
    TextView notificationAppNum, notificationAccNum;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;            //variables
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);        //onCreate method to set content view
        //FullName = findViewById(R.id.nameDisplay);
        //Email = findViewById(R.id.emailDisplay);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();            //initialise variables
        userId = firebaseAuth.getCurrentUser().getUid();
        notificationAppNum = findViewById(R.id.notificationAppNumber);
        notificationAccNum = findViewById(R.id.notificationAccNumber);
        //notificationPassNum = findViewById(R.id.notificationPassNumber);

        DocumentReference accRequests = firestore.collection("accountRequestsNumber").document("Lo8vvjq2m97ySrs2L1HA");
        accRequests.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override                                                                                               //reference to counter
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null){
                    notificationAccNum.setText(documentSnapshot.getLong("NumOfAccRequests").toString());
                }
            }
        });

        DocumentReference appRequests = firestore.collection("appointmentRequestsNumber").document("WZMyCwUdyT6TXLvcU28Q");
        appRequests.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null){                                                                       //reference to counter
                    notificationAppNum.setText(documentSnapshot.getLong("NumOfAppRequests").toString());
                }
            }
        });


        //name + email
        /*DocumentReference documentReference= firestore.collection("approvedUsers").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null){
                    FullName.setText(documentSnapshot.getString("FullName"));
                    Email.setText(documentSnapshot.getString("UserEmail"));
                }
            }
        });*/

        Button addPatient = findViewById(R.id.btnAddPatient);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                    //on-click listener takes the admin to account notifications
                FirebaseAuth.getInstance();
                startActivity(new Intent(AdminActivity.this, AccountNotifications.class));
            }
        });

        Button schedule = findViewById(R.id.btnSchedule);
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                //on-click listener takes the admin to appointment notifications
                FirebaseAuth.getInstance();
                startActivity(new Intent(AdminActivity.this, AppointmentNotifications.class));
            }
        });


        Button logout = findViewById(R.id.logoutAdminBtn);
        logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {                    //on-click listener logs out the admin
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AdminActivity.this, Login.class));
            finish();
        }
    });
}
}
