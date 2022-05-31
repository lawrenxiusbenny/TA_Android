package com.lawrenxiusbenny.roemah_soto_android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lawrenxiusbenny.roemah_soto_android.CheckoutActivity;
import com.lawrenxiusbenny.roemah_soto_android.LoginActivity;
import com.lawrenxiusbenny.roemah_soto_android.R;
import com.lawrenxiusbenny.roemah_soto_android.dialog.LoadingDialog;
import com.lawrenxiusbenny.roemah_soto_android.model.Coupon;

import java.util.List;

public class ChooseCouponRecyclerViewAdapter extends RecyclerView.Adapter<ChooseCouponRecyclerViewAdapter.ChooseCouponViewHolder> {
    private List<Coupon> couponList;
    private List<Coupon> couponListFiltered;
    private Activity activity;
    private Context context;
    private LoadingDialog loadingDialog;


    private SharedPreferences sPreferences;
    public static final String KEY_ID_KUPON = "id_kupon_customer";
    public static final String KEY_NAMA_KUPON = "nama_kupon";
    public static final String KEY_PERSENTASE_POTONGAN = "persentase_potongan";

    private SharedPreferences.Editor editor;
    private int id_kupon_customer = 0;
    private String nama_kupon = "";
    private int persentase_potongan = 0;

    public ChooseCouponRecyclerViewAdapter(Activity act, Context context, List<Coupon> productList) {
        this.couponList = productList;
        this.couponListFiltered = productList;
        this.activity = act;
        this.context = context;
        this.loadingDialog = new LoadingDialog(this.activity);
    }

    @NonNull
    @Override
    public ChooseCouponRecyclerViewAdapter.ChooseCouponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_my_coupon, parent, false);

        return new ChooseCouponRecyclerViewAdapter.ChooseCouponViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseCouponRecyclerViewAdapter.ChooseCouponViewHolder holder, int position) {
        final Coupon coupon = couponListFiltered.get(position);

        String persentase = coupon.getPersentase_potongan()+"%";
        holder.txtPersentase.setText(persentase);
        holder.txtNamaKupon.setText(coupon.getNama_kupon());
        holder.txtDeskripsi.setText(coupon.getDeskripsi_kupon());
        String claimAt = "claim at "+coupon.getCreated_at();
        holder.txtClaimAt.setText(claimAt);
        holder.mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                setPreferences(coupon.getId_kupon_customer(), coupon.getNama_kupon(), coupon.getPersentase_potongan());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                        Intent i = new Intent(context, CheckoutActivity.class);
                        context.startActivity(i);
                    }
                },2000);
            }
        });
    }

    public void setPreferences(int id_kupon_customer, String nama_kupon, int persentase_potongan){
        sPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sPreferences.edit();
        editor.putInt(KEY_ID_KUPON,id_kupon_customer);
        editor.putString(KEY_NAMA_KUPON,nama_kupon);
        editor.putInt(KEY_PERSENTASE_POTONGAN,persentase_potongan);
        editor.commit();
    }

    @Override
    public int getItemCount() {
        return (couponListFiltered != null) ? couponListFiltered.size() : 0;
    }

    public class ChooseCouponViewHolder extends RecyclerView.ViewHolder {

        private TextView txtPersentase, txtNamaKupon, txtDeskripsi, txtClaimAt;
        private CardView mParent;

        public ChooseCouponViewHolder(@NonNull View itemView) {
            super(itemView);
            mParent = itemView.findViewById(R.id.myCouponCard);
            txtPersentase = itemView.findViewById(R.id.txtPersentasePotonganMy);
            txtNamaKupon = itemView.findViewById(R.id.txtNamaKuponMy);
            txtDeskripsi = itemView.findViewById(R.id.txtDeskripsiMy);
            txtClaimAt = itemView.findViewById(R.id.txtClaimAt);
        }
    }
}
