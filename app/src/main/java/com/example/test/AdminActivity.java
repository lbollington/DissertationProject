package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminActivity extends AppCompatActivity {
    TextView FullName, Email, notificationAppNum, notificationAccNum;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        FullName = findViewById(R.id.nameDisplay);
        Email = findViewById(R.id.emailDisplay);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        notificationAppNum = findViewById(R.id.notificationAppNumber);
        notificationAccNum = findViewById(R.id.notificationAccNumber);

        DocumentReference accRequests = firestore.collection("accountRequestsNumber").document("Lo8vvjq2m97ySrs2L1HA");
        accRequests.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
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
                if(documentSnapshot != null){
                    notificationAppNum.setText(documentSnapshot.getLong("NumOfAppRequests").toString());
                }
            }
        });

        DocumentReference documentReference= firestore.collection("approvedUsers").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null){
                    FullName.setText(documentSnapshot.getString("FullName"));
                    Email.setText(documentSnapshot.getString("UserEmail"));
                }
            }
        });

        Button addPatient = findViewById(R.id.btnAddPatient);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance();
                startActivity(new Intent(getApplicationContext(), AccountNotifications.class));
            }
        });

        Button schedule = findViewById(R.id.btnSchedule);
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance();
                startActivity(new Intent(getApplicationContext(), AppointmentNotifications.class));
            }
        });

        Button logout = findViewById(R.id.logoutAdminBtn);
        logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
    });
}
}
