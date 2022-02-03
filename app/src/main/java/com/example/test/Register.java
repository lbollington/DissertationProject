package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Register extends AppCompatActivity {
    EditText fullName, email, password, phone;
    Button registerBtn, goToLogin;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CheckBox isDentistBox, isPatientBox;
    String userIDField;

    CardView cardOne, cardTwo, cardThree, cardFour;
    private boolean isAtLeast8 = false, hasUpperCase = false, hasNumber = false, hasSymbol = false, noEmptyFieldsExist =false, isSpace = false, validEmail = false, validPhoneNumber = false, validPassword = false, emptyNameCheck =false, emptyEmailCheck = false, emptyPasswordCheck =false, emptyPhoneNumberCheck =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        phone = findViewById(R.id.registerPhone);
        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.goToLogin);


        isDentistBox = findViewById(R.id.isDentist);
        isPatientBox = findViewById(R.id.isPatient);

        cardOne = findViewById(R.id.cardOne);
        cardTwo = findViewById(R.id.cardTwo);
        cardThree = findViewById(R.id.cardThree);
        cardFour = findViewById(R.id.cardFour);

        inputChange();

        isPatientBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    isDentistBox.setChecked(false);
                }
            }
        });

        isDentistBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    isPatientBox.setChecked(false);
                }
            }
        });

        DocumentReference accountRequestsRef = fStore.collection("accountRequestsNumber").document("Lo8vvjq2m97ySrs2L1HA");

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emptyNameCheck = checkField(fullName);
                emptyEmailCheck = checkField(email);
                emptyPasswordCheck = checkField(password);
                emptyPhoneNumberCheck = checkField(phone);

                if (emptyNameCheck && emptyEmailCheck && emptyPasswordCheck && emptyPhoneNumberCheck){
                    noEmptyFieldsExist = true;
                }else{
                    noEmptyFieldsExist = false;
                    Toast.makeText(Register.this, "Empty fields exist", Toast.LENGTH_SHORT).show();
                }

                if (!(isDentistBox.isChecked() || isPatientBox.isChecked())) {
                    Toast.makeText(Register.this, "Select an Account Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (validEmail && validPhoneNumber && validPassword && noEmptyFieldsExist) {
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userIDField = fAuth.getCurrentUser().getUid();
                                Toast.makeText(Register.this, "Registration form submitted, account will be created subject to admin approval", Toast.LENGTH_LONG).show();
                                DocumentReference df = fStore.collection("Users").document(userIDField);
                                accountRequestsRef.update("NumOfAccRequests", FieldValue.increment(1));
                                Map<String, Object> userInfo = new HashMap<>();
                                //access level
                                if (isDentistBox.isChecked()) {
                                    userInfo.put("FullName", fullName.getText().toString());
                                    userInfo.put("UserEmail", email.getText().toString());
                                    userInfo.put("Password", password.getText().toString());
                                    userInfo.put("PhoneNumber", phone.getText().toString());
                                    userInfo.put("UserType", "Dentist");
                                    //userInfo.put( "Consent", true);
                                    finish();
                                    df.set(userInfo);
                                }
                                if (isPatientBox.isChecked()) {
                                    userInfo.put("FullName", fullName.getText().toString());
                                    userInfo.put("UserEmail", email.getText().toString());
                                    userInfo.put("Password", password.getText().toString());
                                    userInfo.put("PhoneNumber", phone.getText().toString());
                                    userInfo.put("UserType", "Patient");
                                    //userInfo.put( "Consent", true);
                                    finish();
                                    df.set(userInfo);
                                }
                            }
                            else{
                                Toast.makeText(Register.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                //FirebaseAuth.getInstance().getCurrentUser().delete();

                            }
                        }

                    });
                }

            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }

    public boolean checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

    public void emailCheck() {
        String Email = email.getText().toString();
        String emailRegex = "[\\w-]{1,20}@\\w{2,20}\\.\\w{2,3}$";

        //check for whitespace
        for (char currentChar : Email.toCharArray()) {
            isSpace = Character.isWhitespace(currentChar);
        }
        Pattern email_chars = Pattern.compile(emailRegex);
        Matcher email_m = email_chars.matcher(Email);
        if (email_m.matches() && !isSpace) {
            validEmail = true;
        } else {
            validEmail = false;
        }

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

        if(isAtLeast8 && hasUpperCase && hasNumber && hasSymbol && !isSpace){
            validPassword = true;
        }else{
            validPassword = false;
        }

    }

    public void phoneNumberCheck() {
        String PhoneNumber = phone.getText().toString();
        int PhoneNumberLength = PhoneNumber.length();

        for (char currentChar : PhoneNumber.toCharArray()) {
            isSpace = Character.isWhitespace(currentChar);
        }

        if (PhoneNumberLength == 11 && !isSpace) {
            validPhoneNumber = true;

        } else {
            validPhoneNumber = false;

        }
    }

        private void inputChange () {
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

            email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int count, int after) {
                    emailCheck();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            phone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int count, int after) {
                    phoneNumberCheck();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }




