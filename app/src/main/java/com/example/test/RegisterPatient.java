package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterPatient extends AppCompatActivity
{
    EditText patientfullName, patientemail, patientpassword, patientphone;
    Button patientregBtn, goToAdmin;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        patientfullName = findViewById(R.id.patientregisterName);
        patientemail = findViewById(R.id.patientregisterEmail);
        patientpassword = findViewById(R.id.patientregisterPassword);
        patientphone = findViewById(R.id.patientregisterPhone);
        patientregBtn = findViewById(R.id.patientregisterBtn);
        goToAdmin = findViewById(R.id.goToAdmin);

        patientregBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(patientfullName);
                checkField(patientemail);
                checkField(patientpassword);
                checkField(patientphone);

                if(valid){
                    System.out.println("Is valid!");
                    fAuth.createUserWithEmailAndPassword(patientemail.getText().toString(), patientpassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            userID = fAuth.getCurrentUser().getUid();
                            Toast.makeText(RegisterPatient.this, "Account Created", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("Users").document(userID);
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("Full Name", patientfullName.getText().toString());
                            userInfo.put("User Email", patientemail.getText().toString());
                            userInfo.put("Phone Number", patientphone.getText().toString());
                            userInfo.put("isPatient", "1");
                            df.set(userInfo);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterPatient.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        goToAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AdminActivity.class));
            }
        });

    }

    public boolean checkField(EditText textField)
    {
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else{
            valid = true;
        }
        return valid;
    }
}


