package com.lawrenxiusbenny.roemah_soto_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegistrationActivity extends AppCompatActivity {

    private Button btnSignUp,btnBackToSignIn;

    //text input
    private TextInputEditText txtInputName,txtInputPhone,txtInputDate,txtInputEmail,txtInputPassword;

    //text layout
    private TextInputLayout twName, twPhone, twDate, twEmail, twPassword;

    private String getName, getPhone, getDate, getEmail, getPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnBackToSignIn = findViewById(R.id.btnBackToSignIn);

        //txt input
        txtInputName = findViewById(R.id.txtInputFullNameSignUp);
        txtInputPhone = findViewById(R.id.txtInputPhoneSignUp);
        txtInputDate = findViewById(R.id.txtInputDateOfBirthSignUp);
        txtInputEmail = findViewById(R.id.txtInputEmailSignUp);
        txtInputPassword = findViewById(R.id.txtInputPasswordSignUp);

        //txt layout
        twName = findViewById(R.id.twName);
        twPhone = findViewById(R.id.twPhone);
        twDate = findViewById(R.id.twDate);
        twEmail = findViewById(R.id.twEmailSignUp);
        twPassword = findViewById(R.id.twPasswordSignUp);

        btnBackToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekDataSignUp();
            }
        });

        txtInputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                twName.setError(null);
            }
        });

        txtInputPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                twPhone.setError(null);
            }
        });

        txtInputDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                twDate.setError(null);
            }
        });

        txtInputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                twEmail.setError(null);
            }
        });

        txtInputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                twPassword.setError(null);
            }
        });
    }

    public void cekDataSignUp(){
        getName = txtInputName.getText().toString();
        getPhone = txtInputPhone.getText().toString();
        getDate = txtInputDate.getText().toString();
        getEmail = txtInputEmail.getText().toString();
        getPassword = txtInputPassword.getText().toString();

        if(getName.isEmpty()){
            twName.setError("Full name should not be empty");
        }

        if(getPhone.length()<11 || getPhone.length()>13){
            twPhone.setError("Phone number should be between 11-13 characters");
        }

        if(getDate.isEmpty()){
            twDate.setError("Birth of Date should not be empty");
        }

        if(getEmail.isEmpty()){
            twEmail.setError("Email should not be empty");
        }else if(!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()){
            twEmail.setError("Invalid Email");
        }

        if(getPassword.isEmpty()){
            twPassword.setError("Password should not be empty");
        }else if(getPassword.length()<6){
            twPassword.setError("Password should be at least 6 characters");
        }

    }
}