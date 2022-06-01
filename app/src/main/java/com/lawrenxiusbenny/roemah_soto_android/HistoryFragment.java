package com.lawrenxiusbenny.roemah_soto_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.lawrenxiusbenny.roemah_soto_android.adapter.HistoryRecyclerViewAdapter;
import com.lawrenxiusbenny.roemah_soto_android.adapter.PesananRecyclerViewAdapter;
import com.lawrenxiusbenny.roemah_soto_android.api.PesananApi;
import com.lawrenxiusbenny.roemah_soto_android.api.TransactionApi;
import com.lawrenxiusbenny.roemah_soto_android.model.Pesanan;
import com.lawrenxiusbenny.roemah_soto_android.model.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private HistoryRecyclerViewAdapter adapter;
    private List<Transaction> listHistory;

    private View view;

    private ScrollView layoutRecycler;
    private ConstraintLayout layoutKosong, layoutNotLoginYet;

    ShimmerFrameLayout shimmerFrameLayout;

    private SharedPreferences sPreferences;
    public static final String KEY_ID = "id_customer";
    private int id_customer = 0;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout_history);

        layoutKosong = view.findViewById(R.id.layoutKosongHistory);
        layoutNotLoginYet = view.findViewById(R.id.layoutNotLoginHistory);
        layoutRecycler = view.findViewById(R.id.layoutRecyclerHistory);

        //cek sudah login atau belum
        sPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        id_customer = sPreferences.getInt(KEY_ID,Context.MODE_PRIVATE);

        loadHistory();

        return view;
    }

    public void loadHistory(){
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
            setAdapter();
            getHistory();
        }
    }

    public void setAdapter(){
        listHistory = new ArrayList<Transaction>();
        adapter = new HistoryRecyclerViewAdapter(view.getContext(), listHistory);
        recyclerView = view.findViewById(R.id.recycler_view_history);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getHistory(){
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, TransactionApi.ROOT_SELECT_ALL_BY_ID_CUSTOMER+id_customer, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    JSONArray jsonArray = response.getJSONArray("OUT_DATA");
                    if(!listHistory.isEmpty())
                        listHistory.clear();

                    int queue = 1;

                    if(jsonArray.length() == 0){
                        layoutKosong.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else{
                        layoutRecycler.setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String id_transaksi          = jsonObject.optString("id_transaksi");
                        int id_customer           = jsonObject.optInt("id_customer");
                        int id_karyawan           = jsonObject.optInt("id_karyawan");
                        int id_kupon_customer           = jsonObject.optInt("id_kupon_customer");
                        double total_harga     = jsonObject.optDouble("total_harga");
                        String metode_pembayaran    = jsonObject.optString("metode_pembayaran");
                        String nama_metode    = jsonObject.optString("nama_metode");
                        String status_transaksi    = jsonObject.optString("status_transaksi");
                        String va_number_or_link_payment = jsonObject.optString("va_number_or_link_payment");
                        String created_at    = jsonObject.optString("created_at");

                        Transaction transaction = new Transaction(id_transaksi,id_customer,id_karyawan,id_kupon_customer,
                                total_harga,metode_pembayaran,nama_metode,status_transaksi,va_number_or_link_payment,created_at,queue);
                        listHistory.add(transaction);
                        queue++;
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                    layoutKosong.setVisibility(View.VISIBLE);
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

