package com.lawrenxiusbenny.roemah_soto_android.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.lawrenxiusbenny.roemah_soto_android.CartFragment;
import com.lawrenxiusbenny.roemah_soto_android.LoginActivity;
import com.lawrenxiusbenny.roemah_soto_android.R;
import com.lawrenxiusbenny.roemah_soto_android.api.PesananApi;
import com.lawrenxiusbenny.roemah_soto_android.dialog.LoadingDialog;
import com.lawrenxiusbenny.roemah_soto_android.model.Pesanan;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.PUT;

public class PesananRecyclerViewAdapter extends RecyclerView.Adapter<PesananRecyclerViewAdapter.PesananViewHolder> {
    private List<Pesanan> pesananList;
    private List<Pesanan> pesananListFiltered;
    private Context context;
    private View view;
    MeowBottomNavigation bottomNavigation;
    TextView txtTotalHarga;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    private Activity activity;

    private SharedPreferences sPreferences;
    public static final String KEY_ID = "id_customer";
    int id_customer;

    private LoadingDialog loadingDialog;

    public PesananRecyclerViewAdapter(Activity act, Context context, List<Pesanan> productList) {
        this.activity = act;
        this.context = context;
        this.pesananList = productList;
        this.pesananListFiltered = productList;
        this.loadingDialog = new LoadingDialog(this.activity);
    }

    @NonNull
    @Override
    public PesananRecyclerViewAdapter.PesananViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_cart, parent, false);

        return new PesananRecyclerViewAdapter.PesananViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PesananRecyclerViewAdapter.PesananViewHolder holder, int position) {
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipe, String.valueOf(pesananListFiltered.get(position).id_pesanan));
        viewBinderHelper.closeLayout(String.valueOf(pesananListFiltered.get(position).id_pesanan));
        holder.bindData(pesananListFiltered.get(position));
    }

    @Override
    public int getItemCount() {
        return (pesananListFiltered != null) ? pesananListFiltered.size() : 0;
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString();
                if (userInput.isEmpty()) {
                    pesananListFiltered = pesananList;
                }
                else {
                    List<Pesanan> filteredList = new ArrayList<>();
                    for(Pesanan pesanan : pesananList) {
                        if(String.valueOf(pesanan.getNama_menu()).toLowerCase().contains(userInput) ||
                                pesanan.getStringHarga().toLowerCase().contains(userInput) ||
                                pesanan.getStringSubTotal().toLowerCase().contains(userInput)) {
                            filteredList.add(pesanan);
                        }
                    }
                    pesananListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = pesananListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                pesananListFiltered = (ArrayList<Pesanan>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class PesananViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNamaMenu, txtHargaMenu, txtJumlahPesanan, txtSubTotal;
        private ImageView ivGambar;
        private LinearLayout mParent;
        private SwipeRevealLayout swipe;

        private TextView txtEdit;
        private TextView txtDelete;
//
//        private MaterialButton btnOrder;
//        private MaterialTextView txtNot;

        public PesananViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGambar = itemView.findViewById(R.id.gambar_menu_pesanan);
            txtNamaMenu = itemView.findViewById(R.id.txtNamaMenuPesanan);
            txtHargaMenu = itemView.findViewById(R.id.txtHargaMenuPesanan);
            txtJumlahPesanan = itemView.findViewById(R.id.txtJumlahPesanan);
            txtSubTotal = itemView.findViewById(R.id.subTotalPesanan);
            mParent = itemView.findViewById(R.id.linear_layout_pesanan);
            swipe = itemView.findViewById(R.id.swipeLayout);
            txtEdit = itemView.findViewById(R.id.textEdit);
            txtDelete = itemView.findViewById(R.id.textDelete);
        }

        void bindData(Pesanan pesanan) {
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

            Glide.with(context)
                    .load("https://api.roemahsoto.xyz/Gambar_menu/" + pesanan.getGambar_menu())
                    .placeholder(shimmerDrawable)
                    .into(ivGambar);

            txtNamaMenu.setText(pesanan.getNama_menu());
            if (pesanan.getHarga_menu() == 0) {
                txtHargaMenu.setText("Free");
                txtSubTotal.setText("Free");
            } else {
                txtHargaMenu.setText("IDR " + formatter.format(pesanan.getHarga_menu()));
                txtSubTotal.setText("IDR " + formatter.format(pesanan.getSub_total()));
            }
            txtJumlahPesanan.setText(String.valueOf(pesanan.getJumlah_pesanan()));

            txtEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog;
                    dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_tambah_edit);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    MaterialButton btnCancel = dialog.findViewById(R.id.btnCancel);
                    MaterialButton btnSave = dialog.findViewById(R.id.btnAddEdit);

                    TextInputEditText txtInputJumlah = dialog.findViewById(R.id.txtInputJumlahEdtTambah);
                    TextInputEditText txtInputNote = dialog.findViewById(R.id.txtInputNoteEdtTambah);

                    ImageView imgAdd = dialog.findViewById(R.id.ivTambahEdit);
                    TextView txtNamaAdd = dialog.findViewById(R.id.namaMenuAdd);
                    TextView txtHargaAdd = dialog.findViewById(R.id.HargaMenuAdd);
                    TextView title = dialog.findViewById(R.id.orderTitle);
                    title.setText("UBAH PESANAN");

                    txtNamaAdd.setText(pesanan.getNama_menu());
                    txtInputJumlah.setText(String.valueOf(pesanan.getJumlah_pesanan()));
                    if(pesanan.getCatatan().equalsIgnoreCase("null")){
                        txtInputNote.setText("");
                    }else{
                        txtInputNote.setText(String.valueOf(pesanan.getCatatan()));
                    }


                    if (pesanan.getHarga_menu() == 0) {
                        txtHargaAdd.setText("Free");
                    } else {
                        txtHargaAdd.setText("IDR " + formatter.format(pesanan.getHarga_menu()));
                    }

                    Glide.with(context)
                            .load("https://api.roemahsoto.xyz/Gambar_menu/" + pesanan.getGambar_menu())
                            .placeholder(shimmerDrawable)
                            .into(imgAdd);

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    btnSave.setText("Simpan");

                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (txtInputJumlah.getText().toString().length() < 1) {
                                txtInputJumlah.setError("harus minimal 1");
                            } else {
                                updatePesanan(pesanan.getId_pesanan(), Integer.valueOf(txtInputJumlah.getText().toString()), txtInputNote.getText().toString());
                                dialog.dismiss();
                                Fragment fragment = new CartFragment();
                                loadFragment(fragment);
//                                getPesanan();
                            }
                        }
                    });
                }
            });

            txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog;
                    dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_delete);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    Button btnCancel = dialog.findViewById(R.id.btnCloseDelete);
                    Button btnDelete = dialog.findViewById(R.id.btnDelete);

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            deleteData(pesanan.getId_pesanan());
                            Fragment fragment = new CartFragment();
                            loadFragment(fragment);
                        }
                    });
                }
            });
        }

        public void loadFragment(Fragment fragment) {
            FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fragment_cart, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        public void updatePesanan(int id, int jumlah, String catatan) {
            //Pendeklarasian queue
            RequestQueue queue = Volley.newRequestQueue(context);
            loadingDialog.startLoadingDialog();
            StringRequest stringRequest = new StringRequest(PUT, PesananApi.ROOT_UPDATE + id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        loadingDialog.dismissDialog();
                        JSONObject obj = new JSONObject(response);
                        FancyToast.makeText(context, obj.getString("OUT_MESSAGE"), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                    } catch (JSONException e) {
                        loadingDialog.dismissDialog();
                        e.printStackTrace();
                        FancyToast.makeText(context, "Jaringan tidak stabil, silahkan coba lagi", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingDialog.dismissDialog();
                    FancyToast.makeText(context, "Jaringan tidak stabil, silahkan coba lagi", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("jumlah_pesanan", String.valueOf(jumlah));
                    params.put("catatan", String.valueOf(catatan));
                    return params;
                }
            };
            queue.add(stringRequest);
        }

        public void deleteData(int id){
            //Pendeklarasian queue
            RequestQueue queue = Volley.newRequestQueue(context);
            loadingDialog.startLoadingDialog();
            StringRequest stringRequest = new StringRequest(DELETE, PesananApi.ROOT_DELETE + id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        loadingDialog.dismissDialog();
                        JSONObject obj = new JSONObject(response);
                        FancyToast.makeText(context, obj.getString("OUT_MESSAGE"),FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                    } catch (JSONException e) {
                        loadingDialog.dismissDialog();
                        e.printStackTrace();
                        FancyToast.makeText(context, "Jaringan tidak stabil, silahkan coba lagi", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingDialog.dismissDialog();
                    FancyToast.makeText(context, "Jaringan tidak stabil, silahkan coba lagi", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            }){
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();

                    return params;
                }
            };
            queue.add(stringRequest);
        }
    }
}
