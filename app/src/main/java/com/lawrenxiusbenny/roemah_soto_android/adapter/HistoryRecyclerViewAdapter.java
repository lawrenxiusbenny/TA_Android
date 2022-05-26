package com.lawrenxiusbenny.roemah_soto_android.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.lawrenxiusbenny.roemah_soto_android.LoginActivity;
import com.lawrenxiusbenny.roemah_soto_android.R;
import com.lawrenxiusbenny.roemah_soto_android.ShowPesananTransaksiActivity;
import com.lawrenxiusbenny.roemah_soto_android.api.PesananApi;
import com.lawrenxiusbenny.roemah_soto_android.api.TransactionApi;
import com.lawrenxiusbenny.roemah_soto_android.dialog.LoadingDialog;

import com.lawrenxiusbenny.roemah_soto_android.model.Pesanan;
import com.lawrenxiusbenny.roemah_soto_android.model.Transaction;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.HistoryViewHolder>{
    private List<Transaction> transactionList;
    private List<Transaction> transactionListFiltered;
    private Context context;

    private SharedPreferences sPreferences;
    public static final String KEY_ID = "id_transaksi";
    private SharedPreferences.Editor editor;

    public HistoryRecyclerViewAdapter(Context context, List<Transaction> productList) {
        this.context = context;
        this.transactionList = productList;
        this.transactionListFiltered= productList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_transaction, parent, false);

        return new HistoryRecyclerViewAdapter.HistoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HistoryRecyclerViewAdapter.HistoryViewHolder holder, int position) {
        final Transaction transaction = transactionListFiltered.get(position);

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
        String title = "";
        if(transaction.getQueue() < 10){
            title = "00"+transaction.getQueue();
        }else if(transaction.getQueue() < 100){
            title = "0"+transaction.getQueue();
        }else{
            title = String.valueOf(transaction.getQueue());
        }

        holder.txtTitle.setText(title);
        holder.txtId.setText(transaction.getId_transaksi());
        holder.txtMetode.setText(transaction.getMetode_pembayaran());
        holder.txtTotal.setText("IDR "+ formatter.format(transaction.getTotal_harga()));
        holder.txtTanggal.setText(transaction.getCreated_at());

        holder.mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                editor = sPreferences.edit();
                editor.putString(KEY_ID,holder.txtId.getText().toString());
                editor.commit();

                Intent i;
                i = new Intent(context, ShowPesananTransaksiActivity.class);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (transactionListFiltered != null) ? transactionListFiltered.size() : 0;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle, txtId, txtMetode, txtTotal, txtTanggal;
        private CardView mParent;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mParent = itemView.findViewById(R.id.transactionCard);
            txtTitle = itemView.findViewById(R.id.title_transaction_queue);
            txtId = itemView.findViewById(R.id.txtIdTransaksiHistory);
            txtMetode = itemView.findViewById(R.id.txtMetodePembayaranTransaksi);
            txtTotal = itemView.findViewById(R.id.txtTotalTransaksi);
            txtTanggal = itemView.findViewById(R.id.txtTanggalTransaksi);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString();
                if (userInput.isEmpty()) {
                    transactionListFiltered = transactionList;
                }
                else {
                    List<Transaction> filteredList = new ArrayList<>();
                    for(Transaction transaction : transactionList) {
                        if(String.valueOf(transaction.getId_transaksi()).toLowerCase().contains(userInput) ||
                                transaction.getCreated_at().toLowerCase().contains(userInput)){
                            filteredList.add(transaction);
                        }
                    }
                    transactionListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = transactionListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                transactionListFiltered = (ArrayList<Transaction>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
