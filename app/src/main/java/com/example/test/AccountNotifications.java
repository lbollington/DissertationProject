package com.example.test;

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


public class AccountNotifications extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseFirestore db;
    AccountNotificationsAdapter myAdapter;
    ArrayList<UsersData> list;
    AlertDialog.Builder builder;
    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        Button backToAdmin = findViewById(R.id.docBackAcc);
        backToAdmin.setOnClickListener(view -> {
            FirebaseAuth.getInstance();
            startActivity(new Intent(AccountNotifications.this, AdminActivity.class));
            finish();
        });

        progressDialog = getDialogProgressBar().create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(true);

        recyclerView = findViewById(R.id.userList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();

        myAdapter = new AccountNotificationsAdapter(AccountNotifications.this, list);

        recyclerView.setAdapter(myAdapter);

        EventChangeListener();
    }
    public AlertDialog.Builder getDialogProgressBar(){
        if (builder == null) {
            builder = new AlertDialog.Builder(this);

            builder.setTitle("No requests double-click to dismiss and exit...");

            ProgressBar progressBar = new ProgressBar(AccountNotifications.this);
            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(lp);
            builder.setView(progressBar);
        }
        return builder;
    }
    private void EventChangeListener() {
        db.collection("Users").orderBy("FullName", Query.Direction.ASCENDING)
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

                            list.add(dc.getDocument().toObject(UsersData.class));

                        }
                        //relevant for this use case
                        myAdapter.notifyDataSetChanged();
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();

                    }
                });

    }
}

