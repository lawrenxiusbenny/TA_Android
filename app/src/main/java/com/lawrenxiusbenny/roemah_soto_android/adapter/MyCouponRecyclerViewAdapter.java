package com.lawrenxiusbenny.roemah_soto_android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.lawrenxiusbenny.roemah_soto_android.R;
import com.lawrenxiusbenny.roemah_soto_android.dialog.LoadingDialog;
import com.lawrenxiusbenny.roemah_soto_android.model.Coupon;
import com.lawrenxiusbenny.roemah_soto_android.model.Pesanan;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class MyCouponRecyclerViewAdapter extends RecyclerView.Adapter<MyCouponRecyclerViewAdapter.MyCouponViewHolder> {
    private List<Coupon> couponList;
    private List<Coupon> couponListFiltered;


    public MyCouponRecyclerViewAdapter(List<Coupon> productList) {
        this.couponList = productList;
        this.couponListFiltered = productList;
    }

    @NonNull
    @Override
    public MyCouponRecyclerViewAdapter.MyCouponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_my_coupon, parent, false);

        return new MyCouponRecyclerViewAdapter.MyCouponViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCouponRecyclerViewAdapter.MyCouponViewHolder holder, int position) {
        final Coupon coupon = couponListFiltered.get(position);

        String persentase = coupon.getPersentase_potongan()+"%";
        holder.txtPersentase.setText(persentase);
        holder.txtNamaKupon.setText(coupon.getNama_kupon());
        holder.txtDeskripsi.setText(coupon.getDeskripsi_kupon());
        String claimAt = "klaim pada "+coupon.getCreated_at();
        holder.txtClaimAt.setText(claimAt);
    }

    @Override
    public int getItemCount() {
        return (couponListFiltered != null) ? couponListFiltered.size() : 0;
    }

    public class MyCouponViewHolder extends RecyclerView.ViewHolder {

        private TextView txtPersentase, txtNamaKupon, txtDeskripsi, txtClaimAt;

        public MyCouponViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPersentase = itemView.findViewById(R.id.txtPersentasePotonganMy);
            txtNamaKupon = itemView.findViewById(R.id.txtNamaKuponMy);
            txtDeskripsi = itemView.findViewById(R.id.txtDeskripsiMy);
            txtClaimAt = itemView.findViewById(R.id.txtClaimAt);
        }
    }
}
