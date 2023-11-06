package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgottenPassword extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton, backButton;             //variables

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);       //onCreate method to set content view

        auth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.resetEmail);
        resetPasswordButton = findViewById(R.id.requestPassBtn);            //set variables
        backButton = findViewById(R.id.backToLogin);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                       //brings user back to login after submitting request
                resetPassword();
                startActivity(new Intent(ForgottenPassword.this, Login.class));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgottenPassword.this, Login.class));    //brings user back to login
            }
        });
    }



    private void resetPassword(){
        String email = emailEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("Email is required");            //ensures email is provided
            emailEditText.requestFocus();
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please provide in email format");   //ensures email is in the correct format
            emailEditText.requestFocus();
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {                                              //sends password reset email to provided email address
                if (task.isSuccessful()){
                    Toast.makeText(ForgottenPassword.this, "Check your email for the password reset email", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ForgottenPassword.this, "Error sending password reset email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
