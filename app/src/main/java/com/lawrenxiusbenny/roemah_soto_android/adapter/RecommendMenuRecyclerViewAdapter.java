package com.lawrenxiusbenny.roemah_soto_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.textview.MaterialTextView;
import com.lawrenxiusbenny.roemah_soto_android.R;
import com.lawrenxiusbenny.roemah_soto_android.model.Menu;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class RecommendMenuRecyclerViewAdapter extends RecyclerView.Adapter<RecommendMenuRecyclerViewAdapter.RecommendMenuViewHolder>{
    private List<Menu> RecommendMenuList;
    private List<Menu> RecommendMenuListFiltered;
    private Context context;
    private Context mainContext;
    private View view;
    MeowBottomNavigation bottomNavigation;

//    private SharedPreferences sPreferences;
//    public static final String KEY_ID = "id_reservasi";
//    int id_reservasi;

    public RecommendMenuRecyclerViewAdapter(Context context, List<Menu> productList) {

        this.context = context;
        this.RecommendMenuList = productList;
        this.RecommendMenuListFiltered = productList;

    }

    @NonNull
    @Override
    public RecommendMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_menu, parent, false);
        View view2 = layoutInflater.inflate(R.layout.activity_main,parent,false);
        bottomNavigation = view2.findViewById(R.id.bottom_navigation);

        return new RecommendMenuRecyclerViewAdapter.RecommendMenuViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecommendMenuRecyclerViewAdapter.RecommendMenuViewHolder holder, int position) {
        final Menu menu = RecommendMenuListFiltered.get(position);
//
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(Color.parseColor("#F3F3F3"))
                .setBaseAlpha(1)
                .setHighlightColor(Color.parseColor("#E7E7E7"))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .build();

        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);

        NumberFormat formatter = new DecimalFormat("#,###");
        holder.txtNama.setText(menu.getNama_menu());
        holder.txtdeskripsi.setText(menu.getDeskripsi_menu());
        if(menu.getId_status_menu() == 0) {
            holder.txtNot.setVisibility(View.VISIBLE);
        }
//            holder.btnOrder.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Dialog dialog;
//                    dialog = new Dialog(context);
//                    dialog.setContentView(R.layout.dialog_kosong);
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    dialog.show();
//                    MaterialButton btnClose = dialog.findViewById(R.id.closeBtnKosong);
//                    btnClose.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();
//                        }
//                    });
//                }
//            });
//        }else{
//            holder.btnOrder.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    sPreferences = context.getSharedPreferences("scan", Context.MODE_PRIVATE);
//                    id_reservasi = sPreferences.getInt(KEY_ID,Context.MODE_PRIVATE);
//                    if(id_reservasi==0){
//                        Dialog dialog;
//                        dialog = new Dialog(context);
//                        dialog.setContentView(R.layout.dialog_scanning);
//                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        dialog.show();
//                        Button btnClose = dialog.findViewById(R.id.btnCloseScanning);
//                        btnClose.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog.dismiss();
//                            }
//                        });
//                    }else{
//                        Dialog dialog;
//                        dialog = new Dialog(context);
//                        dialog.setContentView(R.layout.dialog_tambah_edit);
//                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        dialog.show();
//                        MaterialButton btnCancel = dialog.findViewById(R.id.btnCancel);
//                        MaterialButton btnAdd = dialog.findViewById(R.id.btnAddEdit);
//                        TextInputEditText txtInput = dialog.findViewById(R.id.txtInputEdtTambah);
//                        ImageView imgAdd = dialog.findViewById(R.id.ivTambahEdit);
//                        TextView txtAvailable = dialog.findViewById(R.id.jumlahTersediaAdd);
//                        TextView txtNamaAdd = dialog.findViewById(R.id.namaMenuAdd);
//                        TextView txtHargaAdd = dialog.findViewById(R.id.HargaMenuAdd);
//
//                        double stok = menu.getStok_bahan();
//                        double serving = menu.getServing_size();
//
//                        double available = Math.floor(stok/serving);
//                        txtNamaAdd.setText(menu.getNama_menu());
//                        if(menu.getHarga_menu()==0){
//                            txtHargaAdd.setText("Free");
//                        }else{
//                            txtHargaAdd.setText("IDR "+ formatter.format(menu.getHarga_menu()));
//                        }
//                        Glide.with(context)
//                                .load("http://be.atmabbq.xyz/menus/"+menu.getGambar_menu())
//                                .placeholder(shimmerDrawable)
//                                .into(imgAdd);
//                        txtAvailable.setText(String.valueOf(available));
//
//                        btnCancel.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                        btnAdd.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                if(txtInput.getText().toString().length() == 0){
//                                    txtInput.setError("cannot be null");
//                                }else{
//                                    int input = Integer.parseInt(txtInput.getText().toString());
//                                    if(input < 1){
//                                        txtInput.setError("should be at least 1");
//                                    } else if(input>available) {
//                                        txtInput.setError("should be less than available number");
//                                    }else{
//                                        addPesanan(menu.getId_menu(),id_reservasi,Integer.parseInt(txtInput.getText().toString()));
//                                        dialog.dismiss();
//                                        Intent i = new Intent(context,MainActivity.class);
//                                        context.startActivity(i);
//                                    }
//                                }
//                            }
//                        });
//                    }
//                }
//            });
//        }


        if(menu.getHarga_menu() == 0){
            holder.txtHarga.setText("Free");
        }else{
            holder.txtHarga.setText("IDR "+ formatter.format(menu.getHarga_menu()));
        }

        Glide.with(context)
                .load("https://api.roemahsoto.xyz/Gambar_menu/"+menu.getGambar_menu())
                .placeholder(shimmerDrawable)
                .into(holder.ivGambar);


//
//        holder.mParent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return (RecommendMenuListFiltered != null) ? RecommendMenuListFiltered.size() : 0;
    }

    public class RecommendMenuViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNama, txtHarga, txtdeskripsi;
        private ImageView ivGambar;
        private LinearLayout mParent;
        private Button btnOrder;
        private MaterialTextView txtNot;

        public RecommendMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.txtNamaMenu);
            txtdeskripsi = itemView.findViewById(R.id.txtDeskripsiMenu);
            txtHarga = itemView.findViewById(R.id.txtHargaMenu);
            ivGambar = itemView.findViewById(R.id.imageMenu);
            mParent = itemView.findViewById(R.id.linear_layout_menu);
            btnOrder = itemView.findViewById(R.id.btnOrder);
            txtNot = itemView.findViewById(R.id.txtNot);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString();
                if (userInput.isEmpty()) {
                    RecommendMenuListFiltered = RecommendMenuList;
                }
                else {
                    List<Menu> filteredList = new ArrayList<>();
                    for(Menu menu : RecommendMenuList) {
                        if(String.valueOf(menu.getNama_menu()).toLowerCase().contains(userInput) ||
                                menu.getStringHarga().toLowerCase().contains(userInput) ||
                                menu.getDeskripsi_menu().toLowerCase().contains(userInput)) {
                            filteredList.add(menu);
                        }
                    }
                    RecommendMenuListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = RecommendMenuListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                RecommendMenuListFiltered = (ArrayList<Menu>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

//    public void addPesanan(final int id_menu, final int id_reservasi, final int jumlah){
//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        StringRequest stringRequest = new StringRequest(POST, PesananApi.ROOT_INSERT, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject obj = new JSONObject(response);
//                    FancyToast.makeText(context, obj.getString("message"),FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    FancyToast.makeText(context, "Network unstable, please try again",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                FancyToast.makeText(context, "Network unstable, please try again",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
//                error.printStackTrace();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("id_menu", String.valueOf(id_menu));
//                params.put("id_reservasi", String.valueOf(id_reservasi));
//                params.put("jumlah", String.valueOf(jumlah));
//
//                return params;
//            }
//        };
//        queue.add(stringRequest);
//    }
}
