package com.lawrenxiusbenny.roemah_soto_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.lawrenxiusbenny.roemah_soto_android.adapter.ChooseCouponRecyclerViewAdapter;
import com.lawrenxiusbenny.roemah_soto_android.adapter.MyCouponRecyclerViewAdapter;
import com.lawrenxiusbenny.roemah_soto_android.api.CouponApi;
import com.lawrenxiusbenny.roemah_soto_android.model.Coupon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class ChooseCouponActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChooseCouponRecyclerViewAdapter adapter;
    private List<Coupon> listCoupon;

    private ScrollView layoutViewChooseCoupon;
    ShimmerFrameLayout shimmerFrameLayout;
    private ConstraintLayout layoutKosong;

    private ImageButton btnBack;

    private SharedPreferences sPreferences;
    public static final String KEY_ID = "id_customer";
    private int id_customer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_coupon);
        layoutViewChooseCoupon = findViewById(R.id.layoutViewChooseCoupon);
        shimmerFrameLayout = findViewById(R.id.shimmer_layout_choose_coupon);
        layoutKosong = findViewById(R.id.layoutKosongChooseCoupon);
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
    }

    public void loadCoupon(){
        shimmerFrameLayout.startShimmer();
        setAdapter();
        getCoupon();
    }

    public void setAdapter(){
        listCoupon = new ArrayList<Coupon>();
        adapter = new ChooseCouponRecyclerViewAdapter(this,this,listCoupon);
        recyclerView = findViewById(R.id.recycler_view_choose_coupon);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getCoupon(){
        RequestQueue queue = Volley.newRequestQueue(this);

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, CouponApi.ROOT_SELECT_ALL_MY_COUPON+id_customer, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();

                    if(response.getString("OUT_STAT").equalsIgnoreCase("T")){
                        JSONArray jsonArray = response.getJSONArray("OUT_DATA");

                        if(jsonArray.length()!=0){
                            layoutViewChooseCoupon.setVisibility(View.VISIBLE);
                            layoutKosong.setVisibility(View.GONE);
                        }else{
                            layoutViewChooseCoupon.setVisibility(View.GONE);
                            layoutKosong.setVisibility(View.VISIBLE);
                        }

                        if(!listCoupon.isEmpty())
                            listCoupon.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                            String created_at          = jsonObject.optString("created_at");
                            int id_kupon_diskon           = jsonObject.optInt("id_kupon_diskon");
                            int id_kupon_customer           = jsonObject.optInt("id_kupon_customer");
                            String nama_kupon           = jsonObject.optString("nama_kupon");
                            int persentase_potongan     = jsonObject.optInt("persentase_potongan");
                            int jumlah_point_tukar      = jsonObject.optInt("jumlah_point_tukar");
                            String deskripsi_kupon      = jsonObject.optString("deskripsi_kupon");

                            Coupon coupon = new Coupon(id_kupon_customer,id_customer,id_kupon_diskon,nama_kupon,persentase_potongan,
                                    jumlah_point_tukar,deskripsi_kupon,created_at);
                            listCoupon.add(coupon);
                        }
                        adapter.notifyDataSetChanged();
                    }else{
                        layoutViewChooseCoupon.setVisibility(View.GONE);
                        layoutKosong.setVisibility(View.VISIBLE);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.getMessage());
            }
        });
        queue.add(stringRequest);
    }
}