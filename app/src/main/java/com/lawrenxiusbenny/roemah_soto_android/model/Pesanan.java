package com.lawrenxiusbenny.roemah_soto_android.model;

public class Pesanan {
    public int id_pesanan;
    public int id_menu;
    public int id_customer;

    public double sub_total;
    public String catatan;

    public String nama_menu;
    public String gambar_menu;
    public double harga_menu;
    public int jumlah_pesanan;

    public Pesanan() {
    }

    public Pesanan(int id_pesanan, int id_menu, int id_customer, double sub_total, String catatan, String nama_menu, String gambar_menu, double harga_menu, int jumlah_pesanan) {
        this.id_pesanan = id_pesanan;
        this.id_menu = id_menu;
        this.id_customer = id_customer;
        this.sub_total = sub_total;
        this.catatan = catatan;
        this.nama_menu = nama_menu;
        this.gambar_menu = gambar_menu;
        this.harga_menu = harga_menu;
        this.jumlah_pesanan = jumlah_pesanan;
    }

    public Pesanan(int id_menu, int id_customer, int jumlah_pesanan, String catatan) {
        this.id_menu = id_menu;
        this.id_customer = id_customer;
        this.jumlah_pesanan = jumlah_pesanan;
        this.catatan = catatan;
    }

    public int getId_pesanan() {
        return id_pesanan;
    }

    public void setId_pesanan(int id_pesanan) {
        this.id_pesanan = id_pesanan;
    }

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public double getSub_total() {
        return sub_total;
    }

    public void setSub_total(double sub_total) {
        this.sub_total = sub_total;
    }

    public String getStringSubTotal(){
        if(sub_total == 0 ){
            return "";
        }else{
            return String.valueOf(sub_total);
        }
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getGambar_menu() {
        return gambar_menu;
    }

    public void setGambar_menu(String gambar_menu) {
        this.gambar_menu = gambar_menu;
    }

    public double getHarga_menu() {
        return harga_menu;
    }

    public String getStringHarga(){
        if(harga_menu == 0 ){
            return "";
        }else{
            return String.valueOf(harga_menu);
        }
    }

    public void setHarga_menu(double harga_menu) {
        this.harga_menu = harga_menu;
    }

    public int getJumlah_pesanan() {
        return jumlah_pesanan;
    }

    public void setJumlah_pesanan(int jumlah_pesanan) {
        this.jumlah_pesanan = jumlah_pesanan;
    }
}
