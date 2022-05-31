package com.lawrenxiusbenny.roemah_soto_android;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lawrenxiusbenny.roemah_soto_android.api.CustomerApi;
import com.lawrenxiusbenny.roemah_soto_android.api.MenuApi;
import com.lawrenxiusbenny.roemah_soto_android.api.PesananApi;
import com.lawrenxiusbenny.roemah_soto_android.dialog.LoadingDialog;
import com.lawrenxiusbenny.roemah_soto_android.function.AESCrypt;
import com.lawrenxiusbenny.roemah_soto_android.model.Customer;
import com.lawrenxiusbenny.roemah_soto_android.model.Menu;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.PUT;

public class ProfileFragment extends Fragment {

    private View view;

    private ConstraintLayout layoutProfile, layoutNotLoginYet;
    private ShimmerFrameLayout shimmerFrameLayout;

    private Customer customer = new Customer();

    private ImageButton btnEditName, btnEditPhone, btnEditDate, btnEditPass;
    private Button btnLogout, btnGoLogin, btnClaimCoupon;

    private TextInputEditText txtNama,txtPhone, txtDate, txtEmail, txtPassword, txtRoyalty;

    private SharedPreferences sPreferences;
    private SharedPreferences.Editor editor;
    public static final String KEY_POINT = "jumlah_point";
    public static final String KEY_TRANSAKSI = "id_transaksi";
    public static final String KEY_ID = "id_customer";
    public static final String KEY_NAMA_CUSTOMER = "nama_customer";
    public static final String KEY_EMAIL_CUSTOMER = "email_customer";
    public static final String KEY_TELEPON_CUSTOMER = "telepon_customer";

    public static final String KEY_ID_KUPON = "id_kupon_customer";
    public static final String KEY_NAMA_KUPON = "nama_kupon";
    public static final String KEY_PERSENTASE_POTONGAN = "persentase_potongan";
    public static final String KEY_PAYMENT = "va_number_or_link_payment";
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

        btnEditName = view.findViewById(R.id.btnEdtName);
        btnEditPhone = view.findViewById(R.id.btnEdtPhone);
        btnEditDate = view.findViewById(R.id.btnEdtDate);
        btnEditPass = view.findViewById(R.id.btnEdtPassword);

        btnClaimCoupon = view.findViewById(R.id.btnClaimCoupon);

        btnClaimCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),CouponActivity.class);
                startActivity(i);
            }
        });


        btnEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog;
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_edit_name);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                Button btnCancel = dialog.findViewById(R.id.btnCancelEditName);
                Button btnSave = dialog.findViewById(R.id.btnSaveEditName);

                TextInputEditText txtEditName = dialog.findViewById(R.id.txtInputEditName);
                TextInputLayout twEditName = dialog.findViewById(R.id.twNameEdit);

                txtEditName.setText(customer.getNama_customer());
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cekEditName(dialog,twEditName, txtEditName);
                    }
                });

                txtEditName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        twEditName.setError(null);
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        btnEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog;
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_edit_phone);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                Button btnCancel = dialog.findViewById(R.id.btnCancelEditPhone);
                Button btnSave = dialog.findViewById(R.id.btnSaveEditPhone);

                TextInputEditText txtEditPhone = dialog.findViewById(R.id.txtInputEditPhone);
                TextInputLayout twEditPhone = dialog.findViewById(R.id.twPhoneEdit);

                txtEditPhone.setText(customer.getTelepon_customer());
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cekEditPhone(dialog, twEditPhone, txtEditPhone);
                    }
                });

                txtEditPhone.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        twEditPhone.setError(null);
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        btnEditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog;
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_edit_date);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                Button btnCancel = dialog.findViewById(R.id.btnCancelEditDate);
                Button btnSave = dialog.findViewById(R.id.btnSaveEditDate);

                TextInputEditText txtEditDate = dialog.findViewById(R.id.txtInputEditDate);
                TextInputLayout twEditDate = dialog.findViewById(R.id.twDateEdit);

                txtEditDate.setText(customer.getTanggal_lahir_customer());
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cekEditDate(dialog, twEditDate, txtEditDate);

                    }
                });

                txtEditDate.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        twEditDate.setError(null);
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                DatePickerDialog.OnDateSetListener onDateSetListener;

                onDateSetListener  = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String date = year +"-"+month+"-"+dayOfMonth;
                        txtEditDate.setText(date);
                    }
                };

                txtEditDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,onDateSetListener,year,month,day);
                        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                        datePickerDialog.show();
                    }
                });


            }
        });

        btnEditPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog;
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_edit_password);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                Button btnCancel = dialog.findViewById(R.id.btnCancelEditPassword);
                Button btnSave = dialog.findViewById(R.id.btnSaveEditPassword);

                TextInputEditText txtEditPasswordOld = dialog.findViewById(R.id.txtInputEditPasswordOld);
                TextInputLayout twEditPasswordOld = dialog.findViewById(R.id.twPasswordOldEdit);

                TextInputEditText txtEditPasswordNew = dialog.findViewById(R.id.txtInputEditPasswordNew);
                TextInputLayout twEditPasswordNew = dialog.findViewById(R.id.twPasswordNewEdit);

                TextInputEditText txtEditPasswordNewConf = dialog.findViewById(R.id.txtInputEditPasswordNewConf);
                TextInputLayout twEditPasswordNewConf = dialog.findViewById(R.id.twPasswordNewConfEdit);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cekEditPassword(dialog, twEditPasswordOld, txtEditPasswordOld, twEditPasswordNew,
                                txtEditPasswordNew, twEditPasswordNewConf, txtEditPasswordNewConf);
                    }
                });

                txtEditPasswordOld.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        twEditPasswordOld.setError(null);
                    }
                });

                txtEditPasswordNew.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        twEditPasswordNew.setError(null);
                    }
                });

                txtEditPasswordNewConf.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        twEditPasswordNewConf.setError(null);
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });



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
                        editor.putInt(KEY_POINT,0);
                        editor.putString(KEY_TRANSAKSI,"");
                        editor.putInt(KEY_ID_KUPON,0);
                        editor.putString(KEY_NAMA_KUPON,"");
                        editor.putInt(KEY_PERSENTASE_POTONGAN,0);
                        editor.putString(KEY_PAYMENT,"");
                        editor.putString(KEY_NAMA_CUSTOMER,"");
                        editor.putString(KEY_EMAIL_CUSTOMER,"");
                        editor.putString(KEY_TELEPON_CUSTOMER,"");
                        editor.commit();
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

    public void loadFragment(Fragment fragment) {
        FragmentManager manager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.fragment_profile, fragment)
                .addToBackStack(null)
                .commit();
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

    public void cekEditName(Dialog dialog, TextInputLayout twEdit, TextInputEditText txtEdit){
        String getName = txtEdit.getText().toString();

        if(getName.equalsIgnoreCase("")){
            twEdit.setError("Full name should not be empty");
        }else{
            saveName(dialog,txtEdit.getText().toString());
            Fragment fragment = new ProfileFragment();
            loadFragment(fragment);
        }
    }

    public void cekEditPhone(Dialog dialog, TextInputLayout twEdit, TextInputEditText txtEdit){
        String getPhone = txtEdit.getText().toString();

        if(getPhone.length()<11 || getPhone.length()>13){
            twEdit.setError("Phone number should be between 11-13 characters");
        }else{
            savePhone(dialog,txtEdit.getText().toString());
            Fragment fragment = new ProfileFragment();
            loadFragment(fragment);
        }
    }

    public void cekEditDate(Dialog dialog, TextInputLayout twEdit, TextInputEditText txtEdit){
        String getDate = txtEdit.getText().toString();

        if(getDate.isEmpty()){
            twEdit.setError("Birth of Date should not be empty");
        }else{
            saveDate(dialog,txtEdit.getText().toString());
            Fragment fragment = new ProfileFragment();
            loadFragment(fragment);
        }
    }

    public void cekEditPassword(Dialog dialog, TextInputLayout twOld, TextInputEditText txtOld, TextInputLayout twNew, TextInputEditText txtNew, TextInputLayout twConf, TextInputEditText txtConf){
        String getOld = txtOld.getText().toString();
        String getNew = txtNew.getText().toString();
        String getConf = txtConf.getText().toString();

        boolean cekOld, cekNew, cekConf;
        cekOld = false;
        cekNew = false;
        cekConf = false;

        if(getOld.isEmpty()){
            twOld.setError("Old password should not be empty");
        }else if(getOld.length()<6){
            twOld.setError("Old password should be at least 6 characters");
        }else{
            cekOld = true;
        }

        if(getNew.isEmpty()){
            twNew.setError("New password should not be empty");
        }else if(getNew.length()<6){
            twNew.setError("New password should be at least 6 characters");
        }else{
            cekNew = true;
        }

        if(getConf.isEmpty()){
            twConf.setError("Confirmation password should not be empty");
        }else if(getConf.length()<6){
            twConf.setError("Confirmation password should be at least 6 characters");
        }else if(!getConf.equalsIgnoreCase(getNew)){
            twConf.setError("Confirmation password should be same with new password");
        }else{
            cekConf = true;
        }

        if(cekOld && cekNew && cekConf){
            savePassword(dialog,txtOld.getText().toString(),txtNew.getText().toString());
            Fragment fragment = new ProfileFragment();
            loadFragment(fragment);
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
                        int jumlah_point = 0;

                        if(!jsonObject.getString("id_royalty_point").equalsIgnoreCase("null")){
                            jumlah_point = jsonObject.getInt("jumlah_point");
                        }

                        AESCrypt aesCrypt = new AESCrypt();

                        customer.setNama_customer(nama_customer);
                        customer.setTelepon_customer(telepon_customer);
                        customer.setPassword_customer(password_customer);
                        customer.setTanggal_lahir_customer(tanggal_lahir_customer);

                        String passDecrypt;
                        try {
                            passDecrypt = aesCrypt.decrypt(password_customer);
                            txtPassword.setText(passDecrypt);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        txtNama.setText(nama_customer);
                        txtPhone.setText(telepon_customer);
                        txtDate.setText(tanggal_lahir_customer);
                        txtEmail.setText(email_customer);
                        txtRoyalty.setText(String.valueOf(jumlah_point)+" point");

                        //SAVE JUMLAH ROYALTY POINT
                        editor = sPreferences.edit();
                        editor.putInt(KEY_POINT,jumlah_point);
                        editor.commit();
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

    public void saveName(Dialog dialog, String name){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());
        final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        dialog.dismiss();
        loadingDialog.startLoadingDialog();
        StringRequest stringRequest = new StringRequest(PUT, CustomerApi.ROOT_UPDATE_NAME + id_customer, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loadingDialog.dismissDialog();
                    JSONObject obj = new JSONObject(response);
                    FancyToast.makeText(getContext(), obj.getString("OUT_MESSAGE"), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                } catch (JSONException e) {
                    loadingDialog.dismissDialog();
                    e.printStackTrace();
                    FancyToast.makeText(getContext(), "Network unstable, please try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissDialog();
                FancyToast.makeText(getContext(), "Network unstable, please try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama_customer", String.valueOf(name));
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void saveDate(Dialog dialog, String date){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());
        final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        dialog.dismiss();
        loadingDialog.startLoadingDialog();
        StringRequest stringRequest = new StringRequest(PUT, CustomerApi.ROOT_UPDATE_DATE + id_customer, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loadingDialog.dismissDialog();
                    JSONObject obj = new JSONObject(response);
                    FancyToast.makeText(getContext(), obj.getString("OUT_MESSAGE"), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                } catch (JSONException e) {
                    loadingDialog.dismissDialog();
                    e.printStackTrace();
                    FancyToast.makeText(getContext(), "Network unstable, please try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissDialog();
                FancyToast.makeText(getContext(), "Network unstable, please try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tanggal_lahir_customer", String.valueOf(date));
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void savePhone(Dialog dialog, String phone){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());
        final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        dialog.dismiss();
        loadingDialog.startLoadingDialog();
        StringRequest stringRequest = new StringRequest(PUT, CustomerApi.ROOT_UPDATE_PHONE + id_customer, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loadingDialog.dismissDialog();
                    JSONObject obj = new JSONObject(response);
                    FancyToast.makeText(getContext(), obj.getString("OUT_MESSAGE"), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                } catch (JSONException e) {
                    loadingDialog.dismissDialog();
                    e.printStackTrace();
                    FancyToast.makeText(getContext(), "Network unstable, please try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissDialog();
                FancyToast.makeText(getContext(), "Network unstable, please try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("telepon_customer", String.valueOf(phone));
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void savePassword(Dialog dialog, String oldPassword, String newPassword){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        dialog.dismiss();
        loadingDialog.startLoadingDialog();

        StringRequest stringRequest = new StringRequest(PUT, CustomerApi.ROOT_UPDATE_PASSWORD + id_customer, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loadingDialog.dismissDialog();
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("OUT_STAT").equalsIgnoreCase("T")){
                        FancyToast.makeText(getContext(), obj.getString("OUT_MESSAGE"), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                    }else{
                        FancyToast.makeText(getContext(), obj.getString("OUT_MESSAGE"), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }

                } catch (JSONException e) {
                    loadingDialog.dismissDialog();
                    e.printStackTrace();
                    FancyToast.makeText(getContext(), "Network unstable, please try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissDialog();
                FancyToast.makeText(getContext(), "Network unstable, please try again", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("old_password", String.valueOf(oldPassword));
                params.put("new_password", String.valueOf(newPassword));
                return params;
            }
        };
        queue.add(stringRequest);
    }
}

