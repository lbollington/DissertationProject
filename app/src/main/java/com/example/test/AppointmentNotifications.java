package com.example.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentChange;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class AppointmentNotifications extends AppCompatActivity {

    Button docBack;
    RecyclerView recyclerViewApp;
    FirebaseFirestore db;                               //set variables
    AppointmentNotificationsAdapter myAppAdapter;
    ArrayList<AppUsersData> userAppList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {        //onCreate method to set content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_app);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);                    //process dialog
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        docBack = findViewById(R.id.docBackApp);

        recyclerViewApp = findViewById(R.id.userListAppointments);          //initialise items
        recyclerViewApp.setHasFixedSize(true);
        recyclerViewApp.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        userAppList = new ArrayList<AppUsersData>();

        myAppAdapter = new AppointmentNotificationsAdapter(AppointmentNotifications.this, userAppList);

        recyclerViewApp.setAdapter(myAppAdapter);           //initialise adapter

        AppEventChangeListener();

        docBack.setOnClickListener(new View.OnClickListener() {
            @Override                                                           //on-click listener to go back to admin dashboard
            public void onClick(View v) {
                FirebaseAuth.getInstance();
                startActivity(new Intent(recyclerViewApp.getContext(), AdminActivity.class));
                finish();
            }
        });


    }

    private void AppEventChangeListener() {
        db.collection("appointmentRequests").orderBy("PatientEmail", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {                                                       //checks for documents changes and will display appointment requests
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {

                            if (dc.getType() == DocumentChange.Type.ADDED) {

                                userAppList.add(dc.getDocument().toObject(AppUsersData.class));

                            }

                            myAppAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())                  //process dialog
                                progressDialog.dismiss();

                        }
                    }
                });


    }


}



