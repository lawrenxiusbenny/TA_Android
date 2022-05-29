package com.lawrenxiusbenny.roemah_soto_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.lawrenxiusbenny.roemah_soto_android.adapter.CouponListRecyclerViewAdapter;
import com.lawrenxiusbenny.roemah_soto_android.adapter.MyCouponRecyclerViewAdapter;
import com.lawrenxiusbenny.roemah_soto_android.api.CouponApi;
import com.lawrenxiusbenny.roemah_soto_android.model.Coupon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class CouponListFragment extends Fragment {

    private RecyclerView recyclerView;
    private CouponListRecyclerViewAdapter adapter;
    private List<Coupon> listCoupon;

    private ScrollView layoutRecycler;
    private ConstraintLayout layoutKosong;

    ShimmerFrameLayout shimmerFrameLayout;
    private View view;

    private SharedPreferences sPreferences;
    public static final String KEY_ID = "id_customer";
    private int id_customer = 0;

    public CouponListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_coupon_list, container, false);

        layoutRecycler = view.findViewById(R.id.layoutCouponList);
        layoutKosong = view.findViewById(R.id.layoutKosongCouponList);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout_coupon_list);
        //get id customer
        sPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        id_customer = sPreferences.getInt(KEY_ID,Context.MODE_PRIVATE);

        loadCoupon();

        return view;
    }

    public void loadCoupon(){
        setAdapter();
        getData();
    }

    public void setAdapter(){
        listCoupon = new ArrayList<Coupon>();
        adapter = new CouponListRecyclerViewAdapter(this.getActivity(),getContext(),listCoupon);
        recyclerView = view.findViewById(R.id.recycler_view_coupon_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getData(){
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, CouponApi.ROOT_SELECT_ALL_COUPON_LIST, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();

                    JSONArray jsonArray = response.getJSONArray("OUT_DATA");

                    if(jsonArray.length()!=0){
                        layoutRecycler.setVisibility(View.VISIBLE);
                    }else{
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