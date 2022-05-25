package com.lawrenxiusbenny.roemah_soto_android.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.lawrenxiusbenny.roemah_soto_android.LoginActivity;
import com.lawrenxiusbenny.roemah_soto_android.MainActivity;
import com.lawrenxiusbenny.roemah_soto_android.R;
import com.lawrenxiusbenny.roemah_soto_android.api.PesananApi;
import com.lawrenxiusbenny.roemah_soto_android.dialog.LoadingDialog;
import com.lawrenxiusbenny.roemah_soto_android.model.Menu;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.MenuViewHolder>{
    private List<Menu> menuList;
    private List<Menu> menuListFiltered;
    private Context context;
    private Activity activity;



    MeowBottomNavigation bottomNavigation;

    private SharedPreferences sPreferences;
    public static final String KEY_ID = "id_customer";
    int id_customer;

    private LoadingDialog loadingDialog;

    public MenuRecyclerViewAdapter(Activity act, Context context, List<Menu> productList) {

        this.activity = act;
        this.context = context;
        this.menuList = productList;
        this.menuListFiltered = productList;
        this.loadingDialog = new LoadingDialog(this.activity);
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_menu, parent, false);
        View view2 = layoutInflater.inflate(R.layout.activity_main,parent,false);
        bottomNavigation = view2.findViewById(R.id.bottom_navigation);

        return new MenuRecyclerViewAdapter.MenuViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MenuRecyclerViewAdapter.MenuViewHolder holder, int position) {
        final Menu menu = menuListFiltered.get(position);

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

        if(menu.getHarga_menu() == 0){
            holder.txtHarga.setText("Free");
        }else{
            holder.txtHarga.setText("IDR "+ formatter.format(menu.getHarga_menu()));
        }

        Glide.with(context)
                .load("https://api.roemahsoto.xyz/Gambar_menu/"+menu.getGambar_menu())
                .placeholder(shimmerDrawable)
                .into(holder.ivGambar);

        if(menu.getId_status_menu() == 0) {
            holder.txtNot.setVisibility(View.VISIBLE);

            holder.btnOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog;
                    dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_kosong);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    MaterialButton btnClose = dialog.findViewById(R.id.closeBtnKosong);
                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }else{
            holder.btnOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    sPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                    id_customer = sPreferences.getInt(KEY_ID,Context.MODE_PRIVATE);
                    if(id_customer==0){
                        Dialog dialog;
                        dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dialog_not_login);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                        Button btnClose = dialog.findViewById(R.id.closeBtnNotLogin);
                        Button btnGoLogin = dialog.findViewById(R.id.btnGoLoginOrder);

                        btnGoLogin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i;
                                i = new Intent(context,LoginActivity.class);
                                context.startActivity(i);
                            }
                        });

                        btnClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }else{
                        Dialog dialog;
                        dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dialog_tambah_edit);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                        MaterialButton btnCancel = dialog.findViewById(R.id.btnCancel);
                        MaterialButton btnAdd = dialog.findViewById(R.id.btnAddEdit);

                        TextInputEditText txtInputJumlah = dialog.findViewById(R.id.txtInputJumlahEdtTambah);
                        TextInputEditText txtInputNote = dialog.findViewById(R.id.txtInputNoteEdtTambah);

                        ImageView imgAdd = dialog.findViewById(R.id.ivTambahEdit);
                        TextView txtNamaAdd = dialog.findViewById(R.id.namaMenuAdd);
                        TextView txtHargaAdd = dialog.findViewById(R.id.HargaMenuAdd);

                        txtNamaAdd.setText(menu.getNama_menu());
                        if(menu.getHarga_menu()==0){
                            txtHargaAdd.setText("Free");
                        }else{
                            txtHargaAdd.setText("IDR "+ formatter.format(menu.getHarga_menu()));
                        }

                        Glide.with(context)
                                .load("https://api.roemahsoto.xyz/Gambar_menu/"+menu.getGambar_menu())
                                .placeholder(shimmerDrawable)
                                .into(imgAdd);

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(txtInputJumlah.getText().toString().length() < 1){
                                    txtInputJumlah.setError("should be at least 1");
                                }else{
                                    addPesanan(menu.getId_menu(),id_customer,Integer.parseInt(txtInputJumlah.getText().toString()),txtInputNote.getText().toString());
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (menuListFiltered != null) ? menuListFiltered.size() : 0;
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNama, txtHarga, txtdeskripsi;
        private ImageView ivGambar;
        private LinearLayout mParent;
        private Button btnOrder;
        private MaterialTextView txtNot;

        public MenuViewHolder(@NonNull View itemView) {
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
                    menuListFiltered = menuList;
                }
                else {
                    List<Menu> filteredList = new ArrayList<>();
                    for(Menu menu : menuList) {
                        if(String.valueOf(menu.getNama_menu()).toLowerCase().contains(userInput) ||
                                menu.getStringHarga().toLowerCase().contains(userInput) ||
                                menu.getDeskripsi_menu().toLowerCase().contains(userInput)) {
                            filteredList.add(menu);
                        }
                    }
                    menuListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = menuListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                menuListFiltered = (ArrayList<Menu>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void addPesanan(final int id_menu, final int id_customer, final int jumlah_pesanan, final String catatan){
        RequestQueue queue = Volley.newRequestQueue(context);

        loadingDialog.startLoadingDialog();
        StringRequest stringRequest = new StringRequest(POST, PesananApi.ROOT_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loadingDialog.dismissDialog();
                    JSONObject obj = new JSONObject(response);
                    FancyToast.makeText(context, obj.getString("OUT_MESSAGE"),FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                } catch (JSONException e) {
                    loadingDialog.dismissDialog();
                    e.printStackTrace();
                    FancyToast.makeText(context, "Network unstable, please try again",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissDialog();
                FancyToast.makeText(context, "Network unstable, please try again",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_menu", String.valueOf(id_menu));
                params.put("id_customer", String.valueOf(id_customer));
                params.put("jumlah_pesanan", String.valueOf(jumlah_pesanan));
                params.put("catatan", catatan);

                return params;
            }
        };
        queue.add(stringRequest);
    }
}
