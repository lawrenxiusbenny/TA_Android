package com.lawrenxiusbenny.roemah_soto_android;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.lawrenxiusbenny.roemah_soto_android.adapter.MenuRecyclerViewAdapter;
import com.lawrenxiusbenny.roemah_soto_android.adapter.RecommendMenuRecyclerViewAdapter;
import com.lawrenxiusbenny.roemah_soto_android.api.MenuApi;
import com.lawrenxiusbenny.roemah_soto_android.model.Menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class MenuFragment extends Fragment {

    private RecyclerView recyclerView, recyclerViewRecommend;
    private MenuRecyclerViewAdapter adapterMenu;
    private RecommendMenuRecyclerViewAdapter adapterMenuRecommend;
    private List<Menu> listMenu, listMenuRecommend;
    private ConstraintLayout layout;

    ShimmerFrameLayout shimmerFrameLayout;

    private SearchView editSearch;
    private View view;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu, container, false);

        editSearch = (SearchView)view.findViewById(R.id.searchViewMenu);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout_menu);
        layout = view.findViewById(R.id.ConstraintLayoutMenu);
        loadMenu();

        editSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                adapterMenu.getFilter().filter(s);
                return false;
            }
        });
        return view;
    }

    public void loadMenu(){
        setAdapter();
        setAdapterRecommend();
        getMenuRecommend();
        getMenu();
        shimmerFrameLayout.startShimmer();
    }

    public void setAdapter(){
        listMenu = new ArrayList<Menu>();
        recyclerView = view.findViewById(R.id.recycler_view_menu);
        adapterMenu = new MenuRecyclerViewAdapter(view.getContext(), listMenu);
        int gridData = 2;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(view.getContext(),gridData);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterMenu);
    }

    public void setAdapterRecommend(){
        listMenuRecommend = new ArrayList<Menu>();
        recyclerViewRecommend = view.findViewById(R.id.recycler_view_recommend);
        adapterMenuRecommend = new RecommendMenuRecyclerViewAdapter(view.getContext(), listMenuRecommend);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewRecommend.setLayoutManager(layoutManager);
        recyclerViewRecommend.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRecommend.setAdapter(adapterMenuRecommend);
    }

    public void getMenu(){
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, MenuApi.ROOT_SELECT_ALL, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    layout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    JSONArray jsonArray = response.getJSONArray("OUT_DATA");
                    if(!listMenu.isEmpty())
                        listMenu.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int id_menu           = jsonObject.optInt("id_menu");
                        int id_status_menu    = jsonObject.optInt("id_jenis_menu");
                        int id_jenis_menu     = jsonObject.optInt("id_status_menu");
                        String nama_menu      = jsonObject.optString("nama_menu");
                        Double harga_menu     = jsonObject.optDouble("harga_menu");
                        String deskripsi_menu = jsonObject.optString("deskripsi_menu");
                        String gambar_menu    = jsonObject.optString("gambar_menu");
                        Menu menu = new Menu(id_menu,id_status_menu,id_jenis_menu,nama_menu,harga_menu,deskripsi_menu,gambar_menu);
                        listMenu.add(menu);
                    }
                    adapterMenu.notifyDataSetChanged();
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

    public void getMenuRecommend(){
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, MenuApi.ROOT_SELECT_ALL_RECOMMEND, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("OUT_DATA");
                    if(!listMenuRecommend.isEmpty())
                        listMenuRecommend.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int id_menu           = jsonObject.optInt("id_menu");
                        int id_status_menu    = jsonObject.optInt("id_jenis_menu");
                        int id_jenis_menu     = jsonObject.optInt("id_status_menu");
                        String nama_menu      = jsonObject.optString("nama_menu");
                        Double harga_menu     = jsonObject.optDouble("harga_menu");
                        String deskripsi_menu = jsonObject.optString("deskripsi_menu");
                        String gambar_menu    = jsonObject.optString("gambar_menu");
                        Menu menu = new Menu(id_menu,id_status_menu,id_jenis_menu,nama_menu,harga_menu,deskripsi_menu,gambar_menu);
                        listMenuRecommend.add(menu);
                    }
                    adapterMenuRecommend.notifyDataSetChanged();
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