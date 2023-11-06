package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText password, email;
    TextView goToPasswordReset;
    Button loginBtn, goToRegister;
    boolean valid = true;
    FirebaseAuth fAuth;                             //variables
    FirebaseFirestore fStore;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);        //onCreate method to set content view

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        password = findViewById(R.id.loginPassword);    //set variables
        email = findViewById(R.id.loginEmail);
        loginBtn = findViewById(R.id.loginBtn);
        goToRegister = findViewById(R.id.goToRegister);
        goToPasswordReset = findViewById(R.id.tvForgotPass);

        database = FirebaseDatabase.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(email);      //check fields
                checkField(password);

                if(valid){
                    fAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {                                      //authenticates as a known user
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified()){
                                checkUserLevel();
                                Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                            }else{
                                user.sendEmailVerification();       //first time user must verify before logging in
                                Toast.makeText(Login.this, "You must verify your account to be granted access, please check your email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        goToPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                               //navigates to password reset
                startActivity(new Intent(Login.this, ForgottenPassword.class));
            }
        });

        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override                                                       //navigates to data protection form
            public void onClick(View view) {
                startActivity(new Intent(Login.this, DataProtectionForm.class));
            }
        });
    }

    private void checkUserLevel() {
        DatabaseReference myRef = database.getReference("ApprovedUsers");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {                  //checks the type of user logging in
                if(snapshot.getValue().toString().equals("Dentist")){
                    startActivity(new Intent(Login.this, DentistActivity.class));
                    finish();
                }
                else if(snapshot.getValue().toString().equals("Patient")){
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }
                else if(snapshot.getValue().toString().equals("Admin")){
                    startActivity(new Intent(Login.this, AdminActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DocumentReference df = fStore.collection("approvedUsers").document(email.getText().toString());
        //extract data
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                //user access level
                if (documentSnapshot.getString("UserType").equals("Dentist")) {
                    startActivity(new Intent(getApplicationContext(), DentistActivity.class));
                    finish();
                }                                                                                       //displays the relevant dashboard to the user

                else if (documentSnapshot.getString("UserType").equals("Patient")) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                else if (documentSnapshot.getString("UserType").equals("Admin")) {
                    startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                    finish();
                    }
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
        }                                                               //check field
        return valid;
    }


    }


