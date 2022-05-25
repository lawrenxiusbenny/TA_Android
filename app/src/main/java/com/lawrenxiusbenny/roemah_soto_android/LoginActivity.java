package com.lawrenxiusbenny.roemah_soto_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lawrenxiusbenny.roemah_soto_android.api.CustomerApi;
import com.lawrenxiusbenny.roemah_soto_android.api.MenuApi;
import com.lawrenxiusbenny.roemah_soto_android.dialog.LoadingDialog;
import com.lawrenxiusbenny.roemah_soto_android.model.Menu;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin,btnHome;

    private TextView txtSignUp;
    private TextInputEditText txtInputEmail,txtInputPass;
    private TextInputLayout twEmail,twPassword;

    private String getEmail, getPassword;

    private int id_customer;
    private SharedPreferences.Editor editor;
    private SharedPreferences sPreferences;
    public static final String KEY_ID = "id_customer";

    final LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);

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
        boolean cekEmail,cekPassword;

        cekEmail = false;
        cekPassword = false;

        getEmail = txtInputEmail.getText().toString();
        getPassword = txtInputPass.getText().toString();

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

        if(cekEmail && cekPassword){
            login(txtInputEmail.getText().toString(),txtInputPass.getText().toString());
        }
    }

    public void login(final String email, final String password){
        RequestQueue queue = Volley.newRequestQueue(this);

        loadingDialog.startLoadingDialog();

        StringRequest stringRequest = new StringRequest(POST, CustomerApi.ROOT_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loadingDialog.dismissDialog();
                    JSONObject obj = new JSONObject(response);
                    String status;
                    status = obj.getString("OUT_STAT");
                    if(status.equalsIgnoreCase("T")){
                        id_customer = obj.getJSONObject("OUT_DATA").getInt("id_customer");
                        FancyToast.makeText(LoginActivity.this, obj.getString("OUT_MESSAGE"),FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        getDataToPreference(id_customer);
                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        FancyToast.makeText(LoginActivity.this, obj.getString("OUT_MESSAGE"),FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                } catch (JSONException e) {
                    loadingDialog.dismissDialog();
                    e.printStackTrace();
                    FancyToast.makeText(LoginActivity.this, "Network unstable, please try again",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FancyToast.makeText(LoginActivity.this, "Network unstable, please try again",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                error.printStackTrace();
                loadingDialog.dismissDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email_customer", String.valueOf(email));
                params.put("password_customer", String.valueOf(password));

                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void getDataToPreference(int id){
        sPreferences = this.getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sPreferences.edit();
        editor.putInt(KEY_ID,id);
        editor.commit();
    }
}