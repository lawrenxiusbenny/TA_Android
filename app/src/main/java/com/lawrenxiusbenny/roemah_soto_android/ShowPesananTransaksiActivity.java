package com.lawrenxiusbenny.roemah_soto_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.SearchView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.lawrenxiusbenny.roemah_soto_android.adapter.PesananRecyclerViewAdapter;
import com.lawrenxiusbenny.roemah_soto_android.adapter.ShowPesananRecyclerViewAdapter;
import com.lawrenxiusbenny.roemah_soto_android.api.PesananApi;
import com.lawrenxiusbenny.roemah_soto_android.model.Pesanan;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class ShowPesananTransaksiActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShowPesananRecyclerViewAdapter adapter;
    private List<Pesanan> listPesanan;

    ImageButton btnBack;

    ShimmerFrameLayout shimmerFrameLayout;
    ScrollView layoutRecycler;

    private SharedPreferences sPreferences;
    public static final String KEY_ID = "id_transaksi";
    private SharedPreferences.Editor editor;
    private String id_transaksi = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pesanan_transaksi);

        btnBack = findViewById(R.id.btn_back);
        shimmerFrameLayout = findViewById(R.id.shimmer_layout_show_pesanan);
        layoutRecycler = findViewById(R.id.scrollViewTampilPesanan);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sPreferences = getSharedPreferences("login", MODE_PRIVATE);
        id_transaksi = sPreferences.getString(KEY_ID,String.valueOf(MODE_PRIVATE));
        if(!id_transaksi.equalsIgnoreCase("")){
            loadPesanan();
        }else{
            FancyToast.makeText(this, id_transaksi,FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
        }

    }

    public void loadPesanan(){
        shimmerFrameLayout.startShimmer();
        setAdapter();
        getPesanan(id_transaksi);
    }

    public void setAdapter(){
        listPesanan = new ArrayList<Pesanan>();
        adapter = new ShowPesananRecyclerViewAdapter(this,this,listPesanan);
        recyclerView = findViewById(R.id.recycler_view_tampil_pesanan);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getPesanan(String id_transaksi){
        RequestQueue queue = Volley.newRequestQueue(this);

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, PesananApi.ROOT_SELECT_ALL_BY_ID_TRANSAKSI+id_transaksi, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    layoutRecycler.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = response.getJSONArray("OUT_DATA");
                    if(!listPesanan.isEmpty())
                        listPesanan.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int id_pesanan          = jsonObject.optInt("id_pesanan");
                        int id_menu           = jsonObject.optInt("id_menu");
                        int id_customer           = jsonObject.optInt("id_customer");
                        double sub_total     = jsonObject.optDouble("sub_total");
                        String catatan      = jsonObject.optString("catatan");

                        String nama_menu      = jsonObject.optString("nama_menu");
                        double harga_menu     = jsonObject.optDouble("harga_menu");
                        String gambar_menu    = jsonObject.optString("gambar_menu");

                        int jumlah_pesanan           = jsonObject.optInt("jumlah_pesanan");

                        Pesanan pesanan = new Pesanan(id_pesanan,id_menu,id_customer,sub_total,catatan,nama_menu,
                                gambar_menu,harga_menu,jumlah_pesanan);
                        listPesanan.add(pesanan);
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