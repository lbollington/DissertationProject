package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Objects;


public class AppointmentNotifications extends AppCompatActivity {

    Button docBack;
    RecyclerView recyclerViewApp;
    FirebaseFirestore db;
    AppointmentNotificationsAdapter myAppAdapter;
    ArrayList<AppUsersData> userAppList;
    AlertDialog.Builder builder;
    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_app);

        progressDialog = getDialogProgressBar().create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(true);

        docBack = findViewById(R.id.docBackApp);

        recyclerViewApp = findViewById(R.id.userListAppointments);
        recyclerViewApp.setHasFixedSize(true);
        recyclerViewApp.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        userAppList = new ArrayList<>();

        myAppAdapter = new AppointmentNotificationsAdapter(AppointmentNotifications.this, userAppList);

        recyclerViewApp.setAdapter(myAppAdapter);

        AppEventChangeListener();

        docBack.setOnClickListener(v -> {
            FirebaseAuth.getInstance();
            startActivity(new Intent(recyclerViewApp.getContext(), AdminActivity.class));
            finish();
        });

    }
    public AlertDialog.Builder getDialogProgressBar(){
        if (builder == null) {
            builder = new AlertDialog.Builder(this);

            builder.setTitle("No appointments double-click to dismiss and exit...");

            ProgressBar progressBar = new ProgressBar(AppointmentNotifications.this);
            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(lp);
            builder.setView(progressBar);
        }
        return builder;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void AppEventChangeListener() {
        db.collection("appointmentRequests").orderBy("PatientEmail", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {

                    if (error != null) {
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        Log.e("Firestore error", Objects.requireNonNull(error.getMessage()));
                        return;
                    }

                    assert value != null;
                    for (DocumentChange dc : value.getDocumentChanges()) {

                        if (dc.getType() == DocumentChange.Type.ADDED) {

                            userAppList.add(dc.getDocument().toObject(AppUsersData.class));

                        }

                        myAppAdapter.notifyDataSetChanged();
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();

                    }
                });


    }


}



