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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.lawrenxiusbenny.roemah_soto_android.R;
import com.lawrenxiusbenny.roemah_soto_android.api.PesananApi;
import com.lawrenxiusbenny.roemah_soto_android.dialog.LoadingDialog;
import com.lawrenxiusbenny.roemah_soto_android.model.Menu;
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

public class ShowPesananRecyclerViewAdapter extends RecyclerView.Adapter<ShowPesananRecyclerViewAdapter.ShowPesananViewHolder> {
    private List<Pesanan> pesananList;
    private List<Pesanan> pesananListFiltered;
    private Context context;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    private Activity activity;

    private SharedPreferences sPreferences;
    public static final String KEY_ID = "id_customer";
    int id_customer;

    private LoadingDialog loadingDialog;

    public ShowPesananRecyclerViewAdapter(Activity act, Context context, List<Pesanan> productList) {
        this.activity = act;
        this.context = context;
        this.pesananList = productList;
        this.pesananListFiltered = productList;
        this.loadingDialog = new LoadingDialog(this.activity);
    }

    @NonNull
    @Override
    public ShowPesananRecyclerViewAdapter.ShowPesananViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_show_pesanan, parent, false);

        return new ShowPesananRecyclerViewAdapter.ShowPesananViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowPesananRecyclerViewAdapter.ShowPesananViewHolder holder, int position) {
        final Pesanan pesanan = pesananListFiltered.get(position);
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
                .into(holder.ivGambar);

        holder.txtNamaMenu.setText(pesanan.getNama_menu());
        if (pesanan.getHarga_menu() == 0) {
            holder.txtHargaMenu.setText("Free");
            holder.txtSubTotal.setText("Free");
        } else {
            holder.txtHargaMenu.setText("IDR " + formatter.format(pesanan.getHarga_menu()));
            holder.txtSubTotal.setText("IDR " + formatter.format(pesanan.getSub_total()));
        }
        holder.txtJumlahPesanan.setText(String.valueOf(pesanan.getJumlah_pesanan()));

    }

    @Override
    public int getItemCount() {
        return (pesananListFiltered != null) ? pesananListFiltered.size() : 0;
    }

    public class ShowPesananViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNamaMenu, txtHargaMenu, txtJumlahPesanan, txtSubTotal;
        private ImageView ivGambar;
        private CardView mParent;

        public ShowPesananViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGambar = itemView.findViewById(R.id.gambar_menu_show_pesanan);
            txtNamaMenu = itemView.findViewById(R.id.txtNamaMenuShowPesanan);
            txtHargaMenu = itemView.findViewById(R.id.txtHargaMenuShowPesanan);
            txtJumlahPesanan = itemView.findViewById(R.id.txtJumlahShowPesanan);
            txtSubTotal = itemView.findViewById(R.id.subTotalShowPesanan);
            mParent = itemView.findViewById(R.id.layout_item_show_pesanan);
        }
    }
}
