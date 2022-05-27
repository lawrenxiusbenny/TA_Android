package com.lawrenxiusbenny.roemah_soto_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.android.volley.Request.Method.GET;

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShowPesananRecyclerViewAdapter adapter;
    private List<Pesanan> listPesanan;

    private TextView txtTotalHarga;

    private AutoCompleteTextView exposedDropDownPayment;
    private String[] ddPayment = new String[] {"Cash","Cashless"};
    private String payment = "Cash";
    private ConstraintLayout layoutCheckout;
    private ImageButton btnBack;

    ShimmerFrameLayout shimmerFrameLayout;

    private SharedPreferences sPreferences;
    public static final String KEY_ID = "id_customer";
//    private SharedPreferences.Editor editor;
    private int id_customer = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        //cek sudah login atau belum
        sPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        id_customer = sPreferences.getInt(KEY_ID,Context.MODE_PRIVATE);

        txtTotalHarga = findViewById(R.id.totalHargaCheckout);

        shimmerFrameLayout = findViewById(R.id.shimmer_layout_checkout);
        layoutCheckout = findViewById(R.id.layoutViewCheckout);

        setDropDown();
        loadPesanan();
        btnBack = findViewById(R.id.btn_back_checkout);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void setDropDown(){
        exposedDropDownPayment = findViewById(R.id.ddPayment);
        exposedDropDownPayment.setText(payment);
        ArrayAdapter<String> adapterPayment = new ArrayAdapter<>(Objects.requireNonNull(this),
                R.layout.list_item, R.id.item_list, ddPayment);
        exposedDropDownPayment.setAdapter(adapterPayment);
        exposedDropDownPayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                payment = ddPayment[i];
            }
        });
    }

    public void loadPesanan(){
        shimmerFrameLayout.startShimmer();
        setAdapter();
        getPesanan();
    }

    public void setAdapter(){
        listPesanan = new ArrayList<Pesanan>();
        adapter = new ShowPesananRecyclerViewAdapter(this,this,listPesanan);
        recyclerView = findViewById(R.id.recycler_view_checkout);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getPesanan(){
        RequestQueue queue = Volley.newRequestQueue(this);

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, PesananApi.ROOT_SELECT_ALL+id_customer, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    layoutCheckout.setVisibility(View.VISIBLE);


                    JSONArray jsonArray = response.getJSONArray("OUT_DATA");
                    if(!listPesanan.isEmpty())
                        listPesanan.clear();

                    int total =0;

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
                        total = total+jumlah_pesanan;
                    }

                    double harga = response.getDouble("TOTAL_HARGA");

                    NumberFormat formatter = new DecimalFormat("#,###");

                    txtTotalHarga.setText("IDR "+ formatter.format(harga));

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