package com.lawrenxiusbenny.roemah_soto_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lawrenxiusbenny.roemah_soto_android.api.MenuApi;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class RegistrationActivity extends AppCompatActivity {

    private Button btnSignUp,btnBackToSignIn;

    DatePickerDialog.OnDateSetListener onDateSetListener;
    //text input
    private TextInputEditText txtInputName,txtInputPhone,txtInputDate,txtInputEmail,txtInputPassword;

    //text layout
    private TextInputLayout twName, twPhone, twDate, twEmail, twPassword;

    private String getName, getPhone, getDate, getEmail, getPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

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

        txtInputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RegistrationActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,onDateSetListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        onDateSetListener  = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = dayOfMonth+"/"+month+"/"+year;
                txtInputDate.setText(date);
            }
        };

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
        boolean cekName, cekPhone, cekDate, cekEmail, cekPassword;

        cekName = false;
        cekPhone = false;
        cekDate = false;
        cekEmail = false;
        cekPassword = false;

        getName = txtInputName.getText().toString();
        getPhone = txtInputPhone.getText().toString();
        getDate = txtInputDate.getText().toString();
        getEmail = txtInputEmail.getText().toString();
        getPassword = txtInputPassword.getText().toString();

        if(getName.isEmpty()){
            twName.setError("Full name should not be empty");
        }else{
            cekName = true;
        }

        if(getPhone.length()<11 || getPhone.length()>13){
            twPhone.setError("Phone number should be between 11-13 characters");
        }else{
            cekPhone = true;
        }

        if(getDate.isEmpty()){
            twDate.setError("Birth of Date should not be empty");
        }else{
            cekDate = true;
        }

        if(getEmail.isEmpty()){
            twEmail.setError("Email should not be empty");
        }else if(!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()){
            twEmail.setError("Invalid Email");
        }else{
            cekEmail = true;
        }

        if(getPassword.isEmpty()){
            twPassword.setError("Password should not be empty");
        }else if(getPassword.length()<6){
            twPassword.setError("Password should be at least 6 characters");
        }else{
            cekPassword = true;
        }

        if(cekName && cekPhone && cekDate && cekEmail && cekPassword){
            signUp(getName,getPhone,getDate,getEmail,getPassword);
        }
    }

    public void signUp(final String name, final String phone, final String date, final String email, final String pass){
        RequestQueue queue = Volley.newRequestQueue(this);

        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        StringRequest stringRequest = new StringRequest(POST, MenuApi.ROOT_REGISTRATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progress.dismiss();
                    JSONObject obj = new JSONObject(response);
                    String status;
                    status = obj.getString("OUT_STAT");

                    if(status.equalsIgnoreCase("T")){
                        FancyToast.makeText(RegistrationActivity.this, obj.getString("OUT_MESSAGE"),FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        Intent i = new Intent(RegistrationActivity.this,LoginActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        FancyToast.makeText(RegistrationActivity.this, obj.getString("OUT_MESSAGE"),FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                } catch (JSONException e) {
                    progress.dismiss();
                    e.printStackTrace();
                    FancyToast.makeText(RegistrationActivity.this, "Network unstable, please try again",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FancyToast.makeText(RegistrationActivity.this, "Network unstable, please try again",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nama_customer", name);
                params.put("telepon_customer", phone);
                params.put("tanggal_lahir_customer", date);
                params.put("email_customer", email);
                params.put("password_customer", pass);

                return params;
            }
        };
        queue.add(stringRequest);
    }
}