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

public class DentalHistory extends AppCompatActivity {
    String data;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    DentalHistoryAdapter documentAdapter;
    ArrayList<DentalHistoryData> list_of_documents;
    Button docBack;
    AlertDialog.Builder builder;
    AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dental_history);

        progressDialog = getDialogProgressBar().create();
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(true);

        data = DentistUpload.getInstance().getData();

        recyclerView = findViewById(R.id.documentList);
        docBack = findViewById(R.id.docBack);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        list_of_documents = new ArrayList<>();

        documentAdapter = new DentalHistoryAdapter(DentalHistory.this, list_of_documents);

        recyclerView.setAdapter(documentAdapter);

        EventChangeListener();

        docBack.setOnClickListener(v -> {
            FirebaseAuth.getInstance();
            startActivity(new Intent(recyclerView.getContext(), MainActivity.class));
            finish();
        });

    }
    public AlertDialog.Builder getDialogProgressBar(){
        if (builder == null) {
            builder = new AlertDialog.Builder(this);

            builder.setTitle("No documents double-click to dismiss and exit...");

            ProgressBar progressBar = new ProgressBar(DentalHistory.this);
            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(lp);
            builder.setView(progressBar);
        }
        return builder;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void EventChangeListener() {
        db.collection(data).orderBy("AppName", Query.Direction.ASCENDING)
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

                            list_of_documents.add(dc.getDocument().toObject(DentalHistoryData.class));

                        }

                        documentAdapter.notifyDataSetChanged();
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();

                    }
                });


    }


}

