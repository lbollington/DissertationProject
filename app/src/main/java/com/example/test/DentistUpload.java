package com.example.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class DentistUpload extends AppCompatActivity {

    EditText patientEmail, dentistEmail, appReason, day, month, year;ImageView imageView;
    View selectImageBtn;
    Button uploadImageBtn, docBack;
    FirebaseStorage storage;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    LinearProgressIndicator progress;
    Uri image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentist_upload);

        storageReference = FirebaseStorage.getInstance().getReference();
        imageView = findViewById(R.id.imageView);
        selectImageBtn = findViewById(R.id.detailsContainer);
        uploadImageBtn = findViewById(R.id.uploadImageBtn);
        docBack = findViewById(R.id.btnBack);
        fStore = FirebaseFirestore.getInstance();

        dentistEmail = findViewById(R.id.etDentistEmail);
        appReason = findViewById(R.id.etAppTitle);
        patientEmail = findViewById(R.id.etPatientEmail);
        day = findViewById(R.id.etDay);
        month = findViewById(R.id.etMonth);
        year = findViewById(R.id.etYear);

        docBack.setOnClickListener(v -> {
            FirebaseAuth.getInstance();
            startActivity(new Intent(DentistUpload.this, DentistActivity.class));
            finish();
        });

        selectImageBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            mGetContent.launch(intent);
        });

        uploadImageBtn.setOnClickListener(view -> {
            try {
                uploadImage(image);
                DocumentReference df = fStore.collection(patientEmail.getText().toString()).document(day.getText().toString() + month.getText().toString() + year.getText().toString());
                Map<String, Object> Documents = new HashMap<>();
                Documents.put("AppName", appReason.getText().toString());
                Documents.put("DentistName", dentistEmail.getText().toString());
                Documents.put("PatientName", patientEmail.getText().toString());
                Documents.put("Day", day.getText().toString());
                Documents.put("Month", month.getText().toString());
                Documents.put("Year", year.getText().toString());

                df.set(Documents);
          } catch(Exception e) {
                Toast.makeText(DentistUpload.this,"No image selected ", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void uploadImage(Uri image){
        StorageReference reference = storage.getReference().child("images/" + patientEmail.getText().toString()); //+ day.getText().toString();
        reference.putFile(image).addOnSuccessListener(taskSnapshot -> Toast.makeText(DentistUpload.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(DentistUpload.this, "There was an error while uploading image", Toast.LENGTH_SHORT).show()).addOnProgressListener(snapshot -> {
            progress.setMax(Math.toIntExact(snapshot.getTotalByteCount()));
            progress.setProgress(Math.toIntExact(snapshot.getBytesTransferred()));
        });
    }

    private final ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK){
            if (result.getData() != null) {
                image = result.getData().getData();
                uploadImageBtn.setEnabled(true);
                Glide.with(getApplicationContext()).load(image).into(imageView);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }
});
}
