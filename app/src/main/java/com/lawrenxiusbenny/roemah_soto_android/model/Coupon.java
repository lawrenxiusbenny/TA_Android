package com.lawrenxiusbenny.roemah_soto_android.model;

public class Coupon {
    public int id_kupon_customer;
    public int id_customer;
    public int id_kupon_diskon;

    public String nama_kupon;
    public int persentase_potongan;
    public int jumlah_point_tukar;
    public String deskripsi_kupon;
    public String created_at;

    public Coupon(){

    }

    //untuk claim coupon list ke my coupon
    public Coupon(int id_customer, int id_kupon_diskon) {
        this.id_customer = id_customer;
        this.id_kupon_diskon = id_kupon_diskon;
    }

    public Coupon(int id_kupon_customer, int id_customer, int id_kupon_diskon, String nama_kupon, int persentase_potongan, int jumlah_point_tukar, String deskripsi_kupon, String created_at) {
        this.id_kupon_customer = id_kupon_customer;
        this.id_customer = id_customer;
        this.id_kupon_diskon = id_kupon_diskon;
        this.nama_kupon = nama_kupon;
        this.persentase_potongan = persentase_potongan;
        this.jumlah_point_tukar = jumlah_point_tukar;
        this.deskripsi_kupon = deskripsi_kupon;
        this.created_at = created_at;
    }

    public int getId_kupon_customer() {
        return id_kupon_customer;
    }

    public void setId_kupon_customer(int id_kupon_customer) {
        this.id_kupon_customer = id_kupon_customer;
    }

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public int getId_kupon_diskon() {
        return id_kupon_diskon;
    }

    public void setId_kupon_diskon(int id_kupon_diskon) {
        this.id_kupon_diskon = id_kupon_diskon;
    }

    public String getNama_kupon() {
        return nama_kupon;
    }

    public void setNama_kupon(String nama_kupon) {
        this.nama_kupon = nama_kupon;
    }

    public int getPersentase_potongan() {
        return persentase_potongan;
    }

    public void setPersentase_potongan(int persentase_potongan) {
        this.persentase_potongan = persentase_potongan;
    }

    public int getJumlah_point_tukar() {
        return jumlah_point_tukar;
    }

    public void setJumlah_point_tukar(int jumlah_point_tukar) {
        this.jumlah_point_tukar = jumlah_point_tukar;
    }

    public String getDeskripsi_kupon() {
        return deskripsi_kupon;
    }

    public void setDeskripsi_kupon(String deskripsi_kupon) {
        this.deskripsi_kupon = deskripsi_kupon;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
