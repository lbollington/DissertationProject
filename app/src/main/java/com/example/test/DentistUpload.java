package com.example.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class DentistUpload extends AppCompatActivity {

    EditText dentistEmail, appReason, patientEmail, day, month, year;
    ImageView imageView;
    Button button;
    FirebaseStorage storage;                //variables
    FirebaseFirestore fStore;
    Uri imageUri;
    String userID;
    FirebaseAuth fAuth;

    static DentistUpload INSTANCE;
    String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dentist_upload);       //onCreate method to set content view

        button = findViewById(R.id.uploadImageBtn);
        imageView = findViewById(R.id.imageView);
        storage = FirebaseStorage.getInstance();        //initialise variables
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        dentistEmail = findViewById(R.id.etDentistEmail);
        appReason = findViewById(R.id.etAppTitle);
        patientEmail = findViewById(R.id.etPatientEmail);
        day = findViewById(R.id.etDay);
        month = findViewById(R.id.etMonth);
        year = findViewById(R.id.etYear);

        INSTANCE=this;

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }      //launches gallery
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
                DocumentReference df = fStore.collection(patientEmail.getText().toString()).document(day.getText().toString() + month.getText().toString() + year.getText().toString());
                Map<String, Object> Documents = new HashMap<>();
                Documents.put("AppName", appReason.getText().toString());
                Documents.put("DentistName", dentistEmail.getText().toString());
                Documents.put("PatientName", patientEmail.getText().toString());        //writes to firebase with unique user collection
                Documents.put("Day", day.getText().toString());
                Documents.put("Month", month.getText().toString());
                Documents.put("Year", year.getText().toString());

                data = patientEmail.getText().toString();

                df.set(Documents);

            }
        });
    }

    public static DentistUpload getInstance(){
        return INSTANCE;
    }
                                                                        //get and setter to obtain patient email
    public String getData(){
        return this.data;
    }

    private void uploadImage() {
        ProgressDialog dialog = new ProgressDialog(this);       //process dialog
        dialog.setMessage("Uploading...");
        dialog.show();

        if(imageUri != null){
            StorageReference reference = storage.getReference().child("images/" + patientEmail.getText().toString()); //+ day.getText().toString(); merge the date/time variables to make a unique identifier
                                                                                                    //checks where to store image
            reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        dialog.dismiss();
                        Toast.makeText(DentistUpload.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(DentistUpload.this, DentistActivity.class));             //image uploaded to firebase storage
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DocumentReference df = fStore.collection(patientEmail.getText().toString()).document(day.getText().toString() + month.getText().toString() + year.getText().toString());
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("imageurl", String.valueOf(uri));       //url stored to patient collection for reference in dental history function
                                df.set(hashMap, SetOptions.merge());

                                FirebaseAuth.getInstance();
                                startActivity(new Intent(DentistUpload.this, DentistActivity.class));   //navigates back to dentist dashboard
                                finish();
                            }
                        });

                    }else{
                        dialog.dismiss();
                        Toast.makeText(DentistUpload.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {

                    if(result != null){
                        imageView.setImageURI(result);
                        imageUri = result;
                    }
                }

                });
}
