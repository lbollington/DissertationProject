package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class AdminActivity extends AppCompatActivity {
    TextView notificationAppNum, notificationAccNum, FullName, Email;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userId, fullName, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        notificationAppNum = findViewById(R.id.notificationAppNumber);
        notificationAccNum = findViewById(R.id.notificationAccNumber);

        FullName = findViewById(R.id.nameDisplay);
        Email = findViewById(R.id.emailDisplay);
        //notificationPassNum = findViewById(R.id.notificationPassNumber);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            fullName = Objects.requireNonNull(extras.getString("FullName"));
            userEmail = Objects.requireNonNull(extras.getString("UserEmail"));
        }

        FullName.setText(fullName);
        Email.setText(userEmail);

        DocumentReference accRequests = firestore.collection("accountRequestsNumber").document("Lo8vvjq2m97ySrs2L1HA");
        accRequests.addSnapshotListener(this, (documentSnapshot, e) -> {
            if(documentSnapshot != null){
                notificationAccNum.setText(String.format(String.valueOf(documentSnapshot.getLong("NumOfAccRequests"))));
            }
        });

        DocumentReference appRequests = firestore.collection("appointmentRequestsNumber").document("WZMyCwUdyT6TXLvcU28Q");
        appRequests.addSnapshotListener(this, (documentSnapshot, e) -> {
            if(documentSnapshot != null){
                notificationAppNum.setText(String.format(String.valueOf(documentSnapshot.getLong("NumOfAppRequests"))));
            }
        });

        Button addPatient = findViewById(R.id.btnAddPatient);
        addPatient.setOnClickListener(view -> {
            FirebaseAuth.getInstance();
            Intent accountNotifyActivity = new Intent(AdminActivity.this, AccountNotifications.class);
            accountNotifyActivity.putExtra("FullName", fullName);
            accountNotifyActivity.putExtra("UserEmail", userEmail);
            startActivity(accountNotifyActivity);
        });

        Button schedule = findViewById(R.id.btnSchedule);
        schedule.setOnClickListener(view -> {
            FirebaseAuth.getInstance();
            startActivity(new Intent(AdminActivity.this, AppointmentNotifications.class));
        });

        Button logout = findViewById(R.id.logoutAdminBtn);
        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AdminActivity.this, Login.class));
            finish();
        });
}
}
