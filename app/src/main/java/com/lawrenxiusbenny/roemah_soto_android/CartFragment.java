package com.lawrenxiusbenny.roemah_soto_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
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
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.lawrenxiusbenny.roemah_soto_android.adapter.PesananRecyclerViewAdapter;
import com.lawrenxiusbenny.roemah_soto_android.api.PesananApi;
import com.lawrenxiusbenny.roemah_soto_android.model.Pesanan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private PesananRecyclerViewAdapter adapter;
    private List<Pesanan> listPesanan;

    private View view;
    private SearchView editSearch;
    private SwipeRefreshLayout swipeRefresh;
    private CardView viewTotalHarga;

    ShimmerFrameLayout shimmerFrameLayout;

    private Button btnCheckout, btnGoLogin;
    private TextView txtTotalHarga;

    private ConstraintLayout layoutKosong, layoutNotLoginYet;
    MeowBottomNavigation meowBottomNavigation;

    private SharedPreferences sPreferences;
    public static final String KEY_ID = "id_customer";
    private SharedPreferences.Editor editor;
    private int id_customer = 0;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cart, container, false);

        editSearch = (SearchView) view.findViewById(R.id.searchViewCart);

        swipeRefresh = view.findViewById(R.id.refreshPesanan);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout_pesanan);
        layoutKosong = view.findViewById(R.id.layoutKosongCart);
        layoutNotLoginYet = view.findViewById(R.id.layoutNotLoginCart);

        btnCheckout = view.findViewById(R.id.btnCheckOut);
        txtTotalHarga = view.findViewById(R.id.totalHargaCart);
        viewTotalHarga = view.findViewById(R.id.total_harga_view);

        meowBottomNavigation = getActivity().findViewById(R.id.bottom_navigation);

        //cek sudah login atau belum
        sPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        id_customer = sPreferences.getInt(KEY_ID,Context.MODE_PRIVATE);

        btnGoLogin = view.findViewById(R.id.btnGoLoginCart);

        btnGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(getContext(),LoginActivity.class);
                startActivity(i);
            }
        });

        loadPesanan();

        return view;
    }

    public void loadPesanan(){
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
            getPesanan();
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getPesanan();
                }
            });
        }
    }

    public void setAdapter(){
        listPesanan = new ArrayList<Pesanan>();
        adapter = new PesananRecyclerViewAdapter(this.getActivity(),view.getContext(), listPesanan);
        recyclerView = view.findViewById(R.id.recycler_view_pesanan);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        editSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    public void getPesanan(){
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, PesananApi.ROOT_SELECT_ALL+id_customer, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    JSONArray jsonArray = response.getJSONArray("OUT_DATA");
                    if(!listPesanan.isEmpty())
                        listPesanan.clear();

                    int total =0;

                    if(jsonArray.length() == 0){
                        layoutKosong.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        viewTotalHarga.setVisibility(View.GONE);
                    }else{
                        swipeRefresh.setVisibility(View.VISIBLE);
                        viewTotalHarga.setVisibility(View.VISIBLE);
                    }

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

                    meowBottomNavigation.setCount(2,String.valueOf(total));

                    double harga = response.getDouble("TOTAL_HARGA");

                    NumberFormat formatter = new DecimalFormat("#,###");

                    txtTotalHarga.setText("IDR "+ formatter.format(harga));

                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                }catch (JSONException e){
                    e.printStackTrace();
                    swipeRefresh.setRefreshing(false);
                    layoutKosong.setVisibility(View.VISIBLE);
                    meowBottomNavigation.clearAllCounts();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefresh.setRefreshing(false);
                Log.e("error",error.getMessage());
                meowBottomNavigation.clearAllCounts();
            }
        });
        queue.add(stringRequest);
    }
}