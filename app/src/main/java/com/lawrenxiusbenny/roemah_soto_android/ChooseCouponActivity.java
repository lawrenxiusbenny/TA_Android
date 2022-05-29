package com.lawrenxiusbenny.roemah_soto_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.facebook.shimmer.ShimmerFrameLayout;

public class ChooseCouponActivity extends AppCompatActivity {

    private ScrollView layoutViewChooseCoupon;
    ShimmerFrameLayout shimmerFrameLayout;

    private ImageButton btnBack;

    private SharedPreferences sPreferences;
    public static final String KEY_ID = "id_customer";
    public static final String KEY_ID_KUPON = "id_kupon_diskon";
    public static final String KEY_NAMA_KUPON = "nama_kupon";
    public static final String KEY_PERSENTASE_POTONGAN = "persentase_potongan";
    private SharedPreferences.Editor editor;
    private int id_customer = 0;
    private int id_kupon_diskon = 0;
    private String nama_kupon = "";
    private int persentase_potongan = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_coupon);
        layoutViewChooseCoupon = findViewById(R.id.layoutViewChooseCoupon);
        shimmerFrameLayout = findViewById(R.id.shimmer_layout_choose_coupon);
        btnBack = findViewById(R.id.btn_back_choose_coupon);
        getDataPreferences();
        loadCoupon();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(view.getContext(),CheckoutActivity.class);
                startActivity(i);
            }
        });
    }

    public void getDataPreferences(){
        sPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        id_customer = sPreferences.getInt(KEY_ID,Context.MODE_PRIVATE);
        id_kupon_diskon = sPreferences.getInt(KEY_ID_KUPON,Context.MODE_PRIVATE);
        nama_kupon = sPreferences.getString(KEY_NAMA_KUPON,String.valueOf(MODE_PRIVATE));
        persentase_potongan = sPreferences.getInt(KEY_PERSENTASE_POTONGAN,Context.MODE_PRIVATE);
    }

    public void loadCoupon(){
        shimmerFrameLayout.startShimmer();
        setAdapter();
        getCoupon();
    }

    public void setAdapter(){

    }

    public void getCoupon(){

    }
}