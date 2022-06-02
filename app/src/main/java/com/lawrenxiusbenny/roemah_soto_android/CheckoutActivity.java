package com.lawrenxiusbenny.roemah_soto_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lawrenxiusbenny.roemah_soto_android.adapter.PesananRecyclerViewAdapter;
import com.lawrenxiusbenny.roemah_soto_android.adapter.ShowPesananRecyclerViewAdapter;
import com.lawrenxiusbenny.roemah_soto_android.api.PesananApi;
import com.lawrenxiusbenny.roemah_soto_android.api.TransactionApi;
import com.lawrenxiusbenny.roemah_soto_android.dialog.LoadingDialog;
import com.lawrenxiusbenny.roemah_soto_android.model.Midtrans;
import com.lawrenxiusbenny.roemah_soto_android.model.Pesanan;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;


public class CheckoutActivity extends AppCompatActivity implements TransactionFinishedCallback {

    private RecyclerView recyclerView;
    private ShowPesananRecyclerViewAdapter adapter;
    private List<Pesanan> listPesanan;

    private Button btnChooseCoupon, btnContinue;

    private Chip chipChosen;
    private ChipGroup chipGroupChosen;

    private TextView txtTotalHarga, txtTitleChosenCoupon;

    final LoadingDialog loadingDialog = new LoadingDialog(CheckoutActivity.this);

    private AutoCompleteTextView exposedDropDownPayment;
    private String[] ddPayment = new String[] {"Cash","Cashless"};
    private String payment = "Cash";
    private ConstraintLayout layoutCheckout;
    private ImageButton btnBack;

    ShimmerFrameLayout shimmerFrameLayout;

    private SharedPreferences sPreferences;
    public static final String KEY_ID = "id_customer";
    public static final String KEY_NAMA_CUSTOMER = "nama_customer";
    public static final String KEY_EMAIL_CUSTOMER = "email_customer";
    public static final String KEY_TELEPON_CUSTOMER = "telepon_customer";
    public static final String KEY_ID_KUPON = "id_kupon_customer";
    public static final String KEY_NAMA_KUPON = "nama_kupon";
    public static final String KEY_PERSENTASE_POTONGAN = "persentase_potongan";
    public static final String KEY_PAYMENT = "va_number_or_link_payment";
    private SharedPreferences.Editor editor;
    private int id_customer = 0;
    private static String nama_customer="";
    private static String email_customer="";
    private static String telepon_customer="";
    private int id_kupon_customer = 0;
    private String nama_kupon = "";
    private int persentase_potongan = 0;
    private double harga = 0;
    private double discount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        getDataFromPreferences();
        getViewFromId();
        setDropDown();
        loadPesanan();
        initMidtransSdk();

        shimmerFrameLayout.setVisibility(View.VISIBLE);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPreferences();
                Intent i;
                i = new Intent(view.getContext(),MainActivity.class);
                startActivity(i);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog;
                dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.dialog_confirm_checkout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Button btnCancel = dialog.findViewById(R.id.closeBtnCheckout);
                Button btnContinue = dialog.findViewById(R.id.btnContinueDialog);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(payment.equalsIgnoreCase("Cashless")){
                            dialog.dismiss();
                            MidtransSDK.getInstance().setTransactionRequest(transactionRequest(persentase_potongan,listPesanan,String.valueOf(id_customer),harga,1,"nama_customer"));
                            MidtransSDK.getInstance().startPaymentUiFlow(view.getContext());
                        }else{
                            dialog.dismiss();
                            addDataTransactionCash(id_customer,id_kupon_customer,harga,"Cash","Belum Lunas");
                            Intent i = new Intent(CheckoutActivity.this,MainActivity.class);
                            startActivity(i);
                        }
                    }
                });


            }
        });

        chipChosen.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipGroupChosen.setVisibility(view.GONE);
                txtTitleChosenCoupon.setVisibility(view.GONE);
                btnChooseCoupon.setVisibility(view.VISIBLE);

                harga = harga + discount;
                discount = 0;
                NumberFormat formatter = new DecimalFormat("#,###");
                txtTotalHarga.setText("IDR "+ formatter.format(harga));

                //set sPreferences
                setPreferences();
            }
        });

        btnChooseCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(view.getContext(),ChooseCouponActivity.class);
                startActivity(i);
            }
        });
    }

    public void getDataFromPreferences(){
        sPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        id_customer = sPreferences.getInt(KEY_ID,Context.MODE_PRIVATE);
        nama_customer = sPreferences.getString(KEY_NAMA_CUSTOMER,String.valueOf(Context.MODE_PRIVATE));
        email_customer = sPreferences.getString(KEY_EMAIL_CUSTOMER,String.valueOf(Context.MODE_PRIVATE));
        telepon_customer = sPreferences.getString(KEY_TELEPON_CUSTOMER,String.valueOf(Context.MODE_PRIVATE));
        id_kupon_customer = sPreferences.getInt(KEY_ID_KUPON,Context.MODE_PRIVATE);
        nama_kupon = sPreferences.getString(KEY_NAMA_KUPON,String.valueOf(MODE_PRIVATE));
        persentase_potongan = sPreferences.getInt(KEY_PERSENTASE_POTONGAN,Context.MODE_PRIVATE);
    }

    public void getViewFromId(){
        btnContinue = findViewById(R.id.btnContinue);
        btnChooseCoupon = findViewById(R.id.btnChooseCoupon);
        btnBack = findViewById(R.id.btn_back_checkout);
        txtTitleChosenCoupon = findViewById(R.id.txtTitleChosenCoupon);
        txtTotalHarga = findViewById(R.id.totalHargaCheckout);
        chipChosen = findViewById(R.id.chipChosenCoupon);
        chipGroupChosen = findViewById(R.id.chipGroupChosenCoupon);
        shimmerFrameLayout = findViewById(R.id.shimmer_layout_checkout);
        layoutCheckout = findViewById(R.id.layoutViewCheckout);
    }

    public void initMidtransSdk(){
        SdkUIFlowBuilder.init()
                .setContext(this)
                .setMerchantBaseUrl(BuildConfig.BASE_URL)
                .setClientKey(BuildConfig.CLIENT_KEY)
                .setTransactionFinishedCallback(this)
                .enableLog(true)
                .setColorTheme(new CustomColorTheme("#FFE51255","#B61548","#FFE51255"))
                .buildSDK();
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {
        Intent i = new Intent(CheckoutActivity.this,MainActivity.class);
        if(result.getResponse() != null){
            switch (result.getStatus()){
                case TransactionResult.STATUS_SUCCESS:
//                    FancyToast.makeText(CheckoutActivity.this, "Transaction Finished",FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                    addDataTransactionSuccess(id_customer,id_kupon_customer, harga,"Cashless","Lunas",result.getResponse().getPaymentType());
                    startActivity(i);
                    break;
                case TransactionResult.STATUS_PENDING:
//                    FancyToast.makeText(CheckoutActivity.this, "Transaction Pending, more info in your transaction history",FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                    String va_number_or_link_payment = "";
                    if(result.getResponse().getPaymentType().equalsIgnoreCase("bank_transfer")){
                        if(result.getResponse().getBcaVaNumber() != null){
                            va_number_or_link_payment = result.getResponse().getBcaVaNumber();
                        }else if(result.getResponse().getBniVaNumber() != null){
                            va_number_or_link_payment = result.getResponse().getBniVaNumber();
                        }else if(result.getResponse().getBriVaNumber() != null){
                            va_number_or_link_payment = result.getResponse().getBriVaNumber();
                        }else if(result.getResponse().getPermataVANumber() != null){
                            va_number_or_link_payment = result.getResponse().getPermataVANumber();
                        }
                    } else if(result.getResponse().getPaymentType().equalsIgnoreCase("gopay")){
                        va_number_or_link_payment = result.getResponse().getDeeplinkUrl();
                    }else if(result.getResponse().getPaymentType().equalsIgnoreCase("shopeepay")){
                        va_number_or_link_payment = result.getResponse().getDeeplinkUrl();
                    }
                    paymentPendingHandle(va_number_or_link_payment);
                    addDataTransactionPending(id_customer,id_kupon_customer, harga,"Cashless","Belum Lunas",result.getResponse().getPaymentType(),va_number_or_link_payment);
                    startActivity(i);
                    break;
                case TransactionResult.STATUS_FAILED:
                    FancyToast.makeText(CheckoutActivity.this, "Transaction Failed",FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    break;
            }
            result.getResponse().getValidationMessages();
        }else if(result.isTransactionCanceled()){
            FancyToast.makeText(CheckoutActivity.this, "Transaction Canceled",FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
        }else{
            if(result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)){
                FancyToast.makeText(CheckoutActivity.this, "Transaction Invalid",FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }else{
                FancyToast.makeText(CheckoutActivity.this, "Transaction Finished with failure",FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        }
    }

    public void paymentPendingHandle(String va_number_or_link_payment){
        editor = sPreferences.edit();
        editor.putString(KEY_PAYMENT,va_number_or_link_payment);
        editor.commit();
    }

    public static CustomerDetails customerDetails(){
        CustomerDetails cd = new CustomerDetails();
        cd.setFirstName(nama_customer);
        cd.setEmail(email_customer);
        cd.setPhone(telepon_customer);
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddress("");
        shippingAddress.setCity("");
        shippingAddress.setCountryCode("");
        shippingAddress.setPostalCode("");
        cd.setShippingAddress(shippingAddress);
        return cd;
    }

    public static TransactionRequest transactionRequest(int persentase_potongan, List<Pesanan> listPesanan, String id, double price, int qty, String name){
        TransactionRequest request = new TransactionRequest(System.currentTimeMillis()+" ",price);
        request.setCustomerDetails(customerDetails());

        ArrayList<ItemDetails> itemDetails = new ArrayList<>();
        for(int i=0;i<listPesanan.size();i++){
            if(persentase_potongan != 0){
                double discount =  (persentase_potongan * listPesanan.get(i).getHarga_menu())/100;
                double harga =  listPesanan.get(i).getHarga_menu() - discount;
                ItemDetails details = new ItemDetails("Menu-"+listPesanan.get(i).getId_pesanan(), harga,
                        listPesanan.get(i).getJumlah_pesanan(),listPesanan.get(i).getNama_menu());
                itemDetails.add(details);
            }else{
                ItemDetails details = new ItemDetails("Menu-"+listPesanan.get(i).getId_pesanan(), listPesanan.get(i).getHarga_menu(),
                        listPesanan.get(i).getJumlah_pesanan(),listPesanan.get(i).getNama_menu());
                itemDetails.add(details);
            }
        }
        request.setItemDetails(itemDetails);
        return request;
    }

    public void setDropDown(){
        exposedDropDownPayment = findViewById(R.id.ddPayment);
        exposedDropDownPayment.setText(payment);
        ArrayAdapter<String> adapterPayment = new ArrayAdapter<>(Objects.requireNonNull(this),
                R.layout.list_item, R.id.item_list, ddPayment);
        exposedDropDownPayment.setAdapter(adapterPayment);
        exposedDropDownPayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                payment = ddPayment[i];
            }
        });
    }

    public void loadPesanan(){
        shimmerFrameLayout.startShimmer();
        setAdapter();
        getPesanan();
    }

    public void setAdapter(){
        listPesanan = new ArrayList<Pesanan>();
        adapter = new ShowPesananRecyclerViewAdapter(this,this,listPesanan);
        recyclerView = findViewById(R.id.recycler_view_checkout);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getPesanan(){
        RequestQueue queue = Volley.newRequestQueue(this);

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, PesananApi.ROOT_SELECT_ALL+id_customer, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    if(id_kupon_customer == 0){
                        btnChooseCoupon.setVisibility(View.VISIBLE);
                    }else{
                        btnChooseCoupon.setVisibility(View.GONE);
                        txtTitleChosenCoupon.setVisibility(View.VISIBLE);
                        chipGroupChosen.setVisibility(View.VISIBLE);
                        String title;
                        title = persentase_potongan+"% off | "+nama_kupon;
                        chipChosen.setText(title);
                    }
                    layoutCheckout.setVisibility(View.VISIBLE);


                    JSONArray jsonArray = response.getJSONArray("OUT_DATA");
                    if(!listPesanan.isEmpty())
                        listPesanan.clear();

                    int total =0;

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

                    harga = response.getDouble("TOTAL_HARGA");
                    discount = (persentase_potongan * harga)/100;
                    harga = harga - discount;
                    NumberFormat formatter = new DecimalFormat("#,###");

                    txtTotalHarga.setText("IDR "+ formatter.format(harga));
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    public void setPreferences(){
        editor = sPreferences.edit();
        editor.putInt(KEY_ID_KUPON,0);
        editor.putString(KEY_NAMA_KUPON,"");
        editor.putInt(KEY_PERSENTASE_POTONGAN,0);
        editor.commit();
    }

    public void addDataTransactionSuccess(int id_customer, int id_kupon_customer, double total_harga, String metode_pembayaran, String status_transaksi, String nama_metode){
        RequestQueue queue = Volley.newRequestQueue(this);

        loadingDialog.startLoadingDialog();
        StringRequest stringRequest = new StringRequest(POST, TransactionApi.ROOT_ADD_TRANSACTION_CASH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loadingDialog.dismissDialog();
                    JSONObject obj = new JSONObject(response);
                    String status;
                    status = obj.getString("OUT_STAT");
                    if(status.equalsIgnoreCase("T")){
                        FancyToast.makeText(CheckoutActivity.this, obj.getString("OUT_MESSAGE"),FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                    }else{
                        FancyToast.makeText(CheckoutActivity.this, obj.getString("OUT_MESSAGE"),FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }

                } catch (JSONException e) {
                    loadingDialog.dismissDialog();
                    e.printStackTrace();
                    FancyToast.makeText(CheckoutActivity.this, "Jaringan tidak stabil, silahkan coba lagi",FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissDialog();
                FancyToast.makeText(CheckoutActivity.this, "Jaringan tidak stabil, silahkan coba lagi",FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_customer", String.valueOf(id_customer));
                params.put("id_karyawan", "1");
                params.put("id_kupon_customer", String.valueOf(id_kupon_customer));
                params.put("total_harga", String.valueOf(total_harga));
                params.put("metode_pembayaran", metode_pembayaran);
                params.put("status_transaksi", status_transaksi);
                params.put("nama_metode", nama_metode);
                params.put("device", "mobile");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void addDataTransactionPending(int id_customer, int id_kupon_customer, double total_harga, String metode_pembayaran, String status_transaksi, String nama_metode, String va_or_link){
        RequestQueue queue = Volley.newRequestQueue(this);

        loadingDialog.startLoadingDialog();
        StringRequest stringRequest = new StringRequest(POST, TransactionApi.ROOT_ADD_TRANSACTION_CASH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loadingDialog.dismissDialog();
                    JSONObject obj = new JSONObject(response);
                    String status;
                    status = obj.getString("OUT_STAT");
                    if(status.equalsIgnoreCase("T")){
                        FancyToast.makeText(CheckoutActivity.this, obj.getString("OUT_MESSAGE"),FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                    }else{
                        FancyToast.makeText(CheckoutActivity.this, obj.getString("OUT_MESSAGE"),FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }

                } catch (JSONException e) {
                    loadingDialog.dismissDialog();
                    e.printStackTrace();
                    FancyToast.makeText(CheckoutActivity.this, "Jaringan tidak stabil, silahkan coba lagi",FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissDialog();
                FancyToast.makeText(CheckoutActivity.this, "Jaringan tidak stabil, silahkan coba lagi",FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_customer", String.valueOf(id_customer));
                params.put("id_karyawan", "1");
                params.put("id_kupon_customer", String.valueOf(id_kupon_customer));
                params.put("total_harga", String.valueOf(total_harga));
                params.put("metode_pembayaran", metode_pembayaran);
                params.put("status_transaksi", status_transaksi);
                params.put("nama_metode", nama_metode);
                params.put("va_number_or_link_payment", va_or_link);
                params.put("device", "mobile");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void addDataTransactionCash(int id_customer, int id_kupon_customer, double total_harga, String metode_pembayaran, String status_transaksi){
        RequestQueue queue = Volley.newRequestQueue(this);

        loadingDialog.startLoadingDialog();
        StringRequest stringRequest = new StringRequest(POST, TransactionApi.ROOT_ADD_TRANSACTION_CASH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    loadingDialog.dismissDialog();
                    JSONObject obj = new JSONObject(response);
                    String status;
                    status = obj.getString("OUT_STAT");
                    if(status.equalsIgnoreCase("T")){
                        FancyToast.makeText(CheckoutActivity.this, obj.getString("OUT_MESSAGE"),FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                    }else{
                        FancyToast.makeText(CheckoutActivity.this, obj.getString("OUT_MESSAGE"),FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }

                } catch (JSONException e) {
                    loadingDialog.dismissDialog();
                    e.printStackTrace();
                    FancyToast.makeText(CheckoutActivity.this, "Jaringan tidak stabil, silahkan coba lagi",FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissDialog();
                FancyToast.makeText(CheckoutActivity.this, "Jaringan tidak stabil, silahkan coba lagi",FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_customer", String.valueOf(id_customer));
                params.put("id_karyawan", "1");
                params.put("id_kupon_customer", String.valueOf(id_kupon_customer));
                params.put("total_harga", String.valueOf(total_harga));
                params.put("metode_pembayaran", metode_pembayaran);
                params.put("status_transaksi", status_transaksi);
                params.put("device", "mobile");
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
