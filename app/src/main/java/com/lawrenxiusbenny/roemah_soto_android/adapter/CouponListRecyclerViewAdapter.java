package com.lawrenxiusbenny.roemah_soto_android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lawrenxiusbenny.roemah_soto_android.R;
import com.lawrenxiusbenny.roemah_soto_android.api.CouponApi;
import com.lawrenxiusbenny.roemah_soto_android.api.PesananApi;
import com.lawrenxiusbenny.roemah_soto_android.dialog.LoadingDialog;
import com.lawrenxiusbenny.roemah_soto_android.model.Coupon;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class CouponListRecyclerViewAdapter extends RecyclerView.Adapter<CouponListRecyclerViewAdapter.CouponListViewHolder> {
    private List<Coupon> couponList;
    private List<Coupon> couponListFiltered;

    private Context context;

    private SharedPreferences sPreferences;
    private SharedPreferences.Editor editor;
    public static final String KEY_POINT = "jumlah_point";
    public static final String KEY_ID = "id_customer";
    private int jumlah_point = 0;
    private int id_customer = 0;

    private LoadingDialog loadingDialog;

    public CouponListRecyclerViewAdapter(Activity act, Context context, List<Coupon> productList) {
        this.context = context;
        this.couponList = productList;
        this.couponListFiltered = productList;
        this.loadingDialog = new LoadingDialog(act);
    }

    @NonNull
    @Override
    public CouponListRecyclerViewAdapter.CouponListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_coupon_list, parent, false);

        return new CouponListRecyclerViewAdapter.CouponListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponListRecyclerViewAdapter.CouponListViewHolder holder, int position) {
        final Coupon coupon = couponListFiltered.get(position);

        String persentase = coupon.getPersentase_potongan()+"%";
        holder.txtPersentase.setText(persentase);
        holder.txtNamaKupon.setText(coupon.getNama_kupon());
        holder.txtDeskripsi.setText(coupon.getDeskripsi_kupon());

        String jumlahPoint = coupon.getJumlah_point_tukar()+" point";
        holder.txtJumlahPoint.setText(jumlahPoint);

        holder.btnClaimCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                jumlah_point = sPreferences.getInt(KEY_POINT, context.MODE_PRIVATE);
                id_customer = sPreferences.getInt(KEY_ID,context.MODE_PRIVATE);
                if(jumlah_point < coupon.getJumlah_point_tukar()){
                    FancyToast.makeText(context, "Jumlah poinmu tidak cukup untuk klaim kupon ini", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }else{
                    claimCoupon(id_customer,coupon.getId_kupon_diskon(),coupon.getJumlah_point_tukar());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return (couponListFiltered != null) ? couponListFiltered.size() : 0;
    }

    public class CouponListViewHolder extends RecyclerView.ViewHolder {

        private TextView txtPersentase, txtNamaKupon, txtDeskripsi, txtJumlahPoint;
        private Button btnClaimCoupon;

        public CouponListViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPersentase = itemView.findViewById(R.id.txtPersentasePotonganList);
            txtNamaKupon = itemView.findViewById(R.id.txtNamaKuponList);
            txtDeskripsi = itemView.findViewById(R.id.txtDeskripsiList);
            txtJumlahPoint = itemView.findViewById(R.id.txtJumlahPoint);
            btnClaimCoupon = itemView.findViewById(R.id.btnClaimCouponList);
        }
    }

    public void claimCoupon(int id_customer, int id_kupon_diskon, int jumlah_point_tukar){
        RequestQueue queue = Volley.newRequestQueue(context);

        loadingDialog.startLoadingDialog();
        StringRequest stringRequest = new StringRequest(POST, CouponApi.ROOT_ADD_COUPON_CUSTOMER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loadingDialog.dismissDialog();
                    JSONObject obj = new JSONObject(response);
                    FancyToast.makeText(context, obj.getString("OUT_MESSAGE"),FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                    editor = sPreferences.edit();
                    int jumlahSesudah = 0;
                    
                    jumlahSesudah = jumlah_point - jumlah_point_tukar;
                    editor.putInt(KEY_POINT,jumlahSesudah);
                    editor.commit();
                } catch (JSONException e) {
                    loadingDialog.dismissDialog();
                    e.printStackTrace();
                    FancyToast.makeText(context, "Jaringan tidak stabil, silahkan coba lagi",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissDialog();
                FancyToast.makeText(context, "Jaringan tidak stabil, silahkan coba lagi",FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_customer", String.valueOf(id_customer));
                params.put("id_kupon_diskon", String.valueOf(id_kupon_diskon));
                params.put("jumlah_point_tukar", String.valueOf(jumlah_point_tukar));

                return params;
            }
        };
        queue.add(stringRequest);
    }
}
