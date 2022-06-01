package com.lawrenxiusbenny.roemah_soto_android.model;

public class Transaction {
    public String id_transaksi;
    public int id_customer;
    public int id_karyawan;
    public int id_kupon_customer;
    public double total_harga;
    public String metode_pembayaran;
    public String nama_metode;
    public String status_transaksi;
    public String va_number_or_link_payment;
    public String created_at;

    public int queue;


    public Transaction(String id_transaksi, int id_customer, int id_karyawan, int id_kupon_customer, double total_harga, String metode_pembayaran, String nama_metode, String status_transaksi, String va_number_or_link_payment, String created_at, int queue) {
        this.id_transaksi = id_transaksi;
        this.id_customer = id_customer;
        this.id_karyawan = id_karyawan;
        this.id_kupon_customer = id_kupon_customer;
        this.total_harga = total_harga;
        this.metode_pembayaran = metode_pembayaran;
        this.nama_metode = nama_metode;
        this.status_transaksi = status_transaksi;
        this.va_number_or_link_payment = va_number_or_link_payment;
        this.created_at = created_at;
        this.queue = queue;
    }

    public int getQueue() {
        return queue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public Transaction() {
    }

    public String getVa_number_or_link_payment() {
        return va_number_or_link_payment;
    }

    public void setVa_number_or_link_payment(String va_number_or_link_payment) {
        this.va_number_or_link_payment = va_number_or_link_payment;
    }

    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public int getId_karyawan() {
        return id_karyawan;
    }

    public void setId_karyawan(int id_karyawan) {
        this.id_karyawan = id_karyawan;
    }

    public int getId_kupon_customer() {
        return id_kupon_customer;
    }

    public void setId_kupon_customer(int id_kupon_customer) {
        this.id_kupon_customer = id_kupon_customer;
    }

    public double getTotal_harga() {
        return total_harga;
    }

    public String getStringTotalHarga(){
        if(total_harga == 0 ){
            return "";
        }else{
            return String.valueOf(total_harga);
        }
    }


    public void setTotal_harga(double total_harga) {
        this.total_harga = total_harga;
    }

    public String getMetode_pembayaran() {
        return metode_pembayaran;
    }

    public void setMetode_pembayaran(String metode_pembayaran) {
        this.metode_pembayaran = metode_pembayaran;
    }

    public String getNama_metode() {
        return nama_metode;
    }

    public void setNama_metode(String nama_metode) {
        this.nama_metode = nama_metode;
    }

    public String getStatus_transaksi() {
        return status_transaksi;
    }

    public void setStatus_transaksi(String status_transaksi) {
        this.status_transaksi = status_transaksi;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

