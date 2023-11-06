package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DocumentView extends AppCompatActivity {
    Button documentBack;
    ImageView imageView;
    FirebaseFirestore fStore;
    String uid;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_view);

        documentBack = findViewById(R.id.documentViewBackBtn);
        imageView = findViewById(R.id.imageViewDoc);
        fStore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference df = fStore.collection("patientDocuments").document(uid);


        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/test-4132f.appspot.com/o/images%2Flukejosephbollington%40gmail.com?alt=media&token=4491dea9-cbcf-4f50-929e-cc5c7e734071").into(imageView);


        documentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance();
                startActivity(new Intent(DocumentView.this, DentalHistory.class));
                finish();
            }
        });

    }
}
