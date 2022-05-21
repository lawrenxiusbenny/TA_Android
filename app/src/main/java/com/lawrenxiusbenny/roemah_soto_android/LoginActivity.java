package com.lawrenxiusbenny.roemah_soto_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin,btnHome;

    private TextView txtSignUp;
    private TextInputEditText txtInputEmail,txtInputPass;
    private TextInputLayout twEmail,twPassword;

    private String getEmail, getPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnHome = findViewById(R.id.btnHome);

        //Text Input
        txtInputEmail = findViewById(R.id.txtInputEmail);
        txtInputPass = findViewById(R.id.txtInputPassword);
        txtSignUp = findViewById(R.id.txtSignUp);

        //Text Layout
        twEmail = findViewById(R.id.twEmail);
        twPassword = findViewById(R.id.twPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekDataSignIn();
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
                finish();
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

        txtInputPass.addTextChangedListener(new TextWatcher() {
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

    public void cekDataSignIn(){

        getEmail = txtInputEmail.getText().toString();
        getPassword = txtInputPass.getText().toString();

        if(getEmail.isEmpty()){
//            txtInputEmail.setError("Email should not be empty");
            twEmail.setError("Email should not be empty");
        }else if(!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()){
//            txtInputEmail.setError("Invalid Email");
            twEmail.setError("Invalid Email");
        }

        if(getPassword.isEmpty()){
//            txtInputPass.setError("Password should not be empty");
            twPassword.setError("Password should not be empty");
        }else if(getPassword.length()<6){
//            txtInputPass.setError("Password should be at least 6 characters");
            twPassword.setError("Password should be at least 6 characters");
        }

    }
}