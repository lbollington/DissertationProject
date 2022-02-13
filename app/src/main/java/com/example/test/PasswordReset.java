package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordReset extends AppCompatActivity {

    private boolean isAtLeast8 = false, valid = false, hasUpperCase = false, hasNumber = false, passwordMatch = false, emptyNameCheck = false , validPassword = false, emptyNewPasswordCheck = false, emptyEmailCheck = false, emptyConfirmPasswordCheck = false, hasSymbol = false, noEmptyFieldsExist = false, isSpace = false;
    Button requestPasswordReset, btnBack;
    EditText password, email, newPass, name;
    CardView cardOne, cardTwo, cardThree, cardFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        requestPasswordReset = findViewById(R.id.requestPassBtn);
        btnBack = findViewById(R.id.backToLogin);
        password = findViewById(R.id.confirmPassword);
        newPass = findViewById(R.id.newPassword);
        email = findViewById(R.id.resetEmail);
        name = findViewById(R.id.resetName);

        cardOne = findViewById(R.id.cardOne);
        cardTwo = findViewById(R.id.cardTwo);
        cardThree = findViewById(R.id.cardThree);
        cardFour = findViewById(R.id.cardFour);

        inputChange();

        DocumentReference passwordRequestsRef = FirebaseFirestore.getInstance().collection("passwordResetRequestsNumber").document("qhm5HzQYgG897w9EVuqZ");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordReset.this, Login.class));
            }
        });

        requestPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyNameCheck = checkField(name);
                emptyEmailCheck = checkField(email);
                emptyNewPasswordCheck = checkField(newPass);
                emptyConfirmPasswordCheck = checkField(password);

                if(validPassword && noEmptyFieldsExist) {
                    Toast.makeText(PasswordReset.this, "Password reset request submitted, password will be changed subject to admin approval", Toast.LENGTH_SHORT).show();
                    DocumentReference df = FirebaseFirestore.getInstance().collection("passwordResetRequests").document(email.getText().toString());
                    passwordRequestsRef.update("NumOfPassRequests", FieldValue.increment(1));

                    Map<String, Object> passInfo = new HashMap<>();

                    passInfo.put("ResetName", name.getText().toString());
                    passInfo.put("UserEmail", email.getText().toString());
                    passInfo.put("PassRequest", password.getText().toString());

                    startActivity(new Intent(PasswordReset.this, Login.class));
                    df.set(passInfo);
                }
                else {
                    Toast.makeText(PasswordReset.this, "Error passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @SuppressLint("ResourceType")
    public void passwordCheck() {
        String Password = password.getText().toString();
        int PassLength = Password.length();
        //8 characters
        if (PassLength >= 8 && PassLength <= 15 && !isSpace) {
            isAtLeast8 = true;
            cardOne.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            isAtLeast8 = false;
            cardOne.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));

        }
        //check for whitespace
        for (char currentChar : Password.toCharArray()) {
            isSpace = Character.isWhitespace(currentChar);
        }

        //for uppercase
        Pattern upper = Pattern.compile("(.*[A-Z].*)");
        Matcher upper_m = upper.matcher(Password);
        if (upper_m.matches()) {
            hasUpperCase = true;
            cardTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            hasUpperCase = false;
            cardTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        //for number
        Pattern number = Pattern.compile("(.*[0-9].*)");
        Matcher number_m = number.matcher(Password);
        if (number_m.matches()) {
            hasNumber = true;
            cardThree.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            hasNumber = false;
            cardThree.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        //for symbol

        Pattern symbol = Pattern.compile("[a-zA-Z0-9]*");
        Matcher symbol_m = symbol.matcher(Password);
        System.out.println(symbol_m);
        if (!symbol_m.matches() && !isSpace) {
            hasSymbol = true;
            cardFour.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            hasSymbol = false;
            cardFour.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        /*for password confirmation
        if (newPass.equals(password)){
            passwordMatch = true;
        }else{
            passwordMatch = false;
        }*/

        if (isAtLeast8 && hasUpperCase && hasNumber && hasSymbol && !isSpace) {
            validPassword = true;
        } else {
            validPassword = false;
        }

    }

    public boolean checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            noEmptyFieldsExist = false;
        } else {
            noEmptyFieldsExist = true;
        }
        return noEmptyFieldsExist;
    }

    private void inputChange() {
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                passwordCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
