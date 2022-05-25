package com.lawrenxiusbenny.roemah_soto_android;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.lawrenxiusbenny.roemah_soto_android.api.CustomerApi;
import com.lawrenxiusbenny.roemah_soto_android.api.MenuApi;
import com.lawrenxiusbenny.roemah_soto_android.dialog.LoadingDialog;
import com.lawrenxiusbenny.roemah_soto_android.model.Customer;
import com.lawrenxiusbenny.roemah_soto_android.model.Menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.Request.Method.GET;

public class ProfileFragment extends Fragment {

    private View view;

    private ConstraintLayout layoutProfile, layoutNotLoginYet;
    private ShimmerFrameLayout shimmerFrameLayout;

    private Button btnLogout, btnGoLogin;

    private TextInputEditText txtNama,txtPhone, txtDate, txtEmail, txtPassword, txtRoyalty;

    private SharedPreferences sPreferences;
    private SharedPreferences.Editor editor;
    public static final String KEY_ID = "id_customer";
    int id_customer=0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        layoutProfile = view.findViewById(R.id.layoutProfile);
        layoutNotLoginYet = view.findViewById(R.id.layoutNotLoginProfile);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout_profile);

        sPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        id_customer = sPreferences.getInt(KEY_ID,Context.MODE_PRIVATE);

        txtNama = view.findViewById(R.id.txtInputNameProfile);
        txtPhone = view.findViewById(R.id.txtInputPhoneProfile);
        txtDate = view.findViewById(R.id.txtInputDateProfile);
        txtEmail = view.findViewById(R.id.txtInputEmailProfile);
        txtPassword = view.findViewById(R.id.txtInputPasswordProfile);
        txtRoyalty = view.findViewById(R.id.txtInputRoyaltyProfile);

        btnLogout = view.findViewById(R.id.btnLogout);
        btnGoLogin = view.findViewById(R.id.btnGoLoginProfile);

        btnGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(getContext(),LoginActivity.class);
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog;
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_logout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                Button btnCancel = dialog.findViewById(R.id.closeBtnLogout);
                Button btnLogoutDialog = dialog.findViewById(R.id.btnLogoutDialog);

                final LoadingDialog loadingDialog = new LoadingDialog(getActivity());

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnLogoutDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        loadingDialog.startLoadingDialog();

                        editor = sPreferences.edit();
                        editor.putInt(KEY_ID,0);
                        editor.commit();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                                Intent i;
                                i = new Intent(getContext(),LoginActivity.class);
                                startActivity(i);
                            }
                        },3500);

                    }
                });
            }
        });

        loadProfile();

        return view;
    }

    public void loadProfile(){
        shimmerFrameLayout.startShimmer();
        if(id_customer == 0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    layoutNotLoginYet.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }
            },1500);
        }else{
            getDataCustomer();
        }
    }

    public void getDataCustomer(){
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, CustomerApi.ROOT_GET_CUSTOMER_BY_ID+id_customer, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    layoutProfile.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    JSONArray jsonArray = response.getJSONArray("OUT_DATA");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        String nama_customer = jsonObject.getString("nama_customer");
                        String email_customer = jsonObject.getString("email_customer");
                        String password_customer = jsonObject.getString("password_customer");
                        String telepon_customer = jsonObject.getString("telepon_customer");
                        String tanggal_lahir_customer = jsonObject.getString("tanggal_lahir_customer");
                        int jumlah_point = jsonObject.getInt("jumlah_point");

                        txtNama.setText(nama_customer);
                        txtPhone.setText(telepon_customer);
                        txtDate.setText(tanggal_lahir_customer);
                        txtEmail.setText(email_customer);
                        txtPassword.setText(password_customer);
                        txtRoyalty.setText(String.valueOf(jumlah_point));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                Log.e("error",error.getMessage());
            }
        });
        queue.add(stringRequest);
    }
}
