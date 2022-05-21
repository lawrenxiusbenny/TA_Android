package com.lawrenxiusbenny.roemah_soto_android.model;

public class Customer {
    public int id_customer;
    public String id_royalty_point;
    public String nama_customer;
    public String email_customer;
    public String telepon_customer;
    public String password_customer;
    public String tanggal_lahir_customer;

    public Customer() {
    }

    public Customer(int id_customer, String id_royalty_point, String nama_customer, String email_customer, String telepon_customer, String password_customer, String tanggal_lahir_customer) {
        this.id_customer = id_customer;
        this.id_royalty_point = id_royalty_point;
        this.nama_customer = nama_customer;
        this.email_customer = email_customer;
        this.telepon_customer = telepon_customer;
        this.password_customer = password_customer;
        this.tanggal_lahir_customer = tanggal_lahir_customer;
    }

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public String getId_royalty_point() {
        return id_royalty_point;
    }

    public void setId_royalty_point(String id_royalty_point) {
        this.id_royalty_point = id_royalty_point;
    }

    public String getNama_customer() {
        return nama_customer;
    }

    public void setNama_customer(String nama_customer) {
        this.nama_customer = nama_customer;
    }

    public String getEmail_customer() {
        return email_customer;
    }

    public void setEmail_customer(String email_customer) {
        this.email_customer = email_customer;
    }

    public String getTelepon_customer() {
        return telepon_customer;
    }

    public void setTelepon_customer(String telepon_customer) {
        this.telepon_customer = telepon_customer;
    }

    public String getPassword_customer() {
        return password_customer;
    }

    public void setPassword_customer(String password_customer) {
        this.password_customer = password_customer;
    }

    public String getTanggal_lahir_customer() {
        return tanggal_lahir_customer;
    }

    public void setTanggal_lahir_customer(String tanggal_lahir_customer) {
        this.tanggal_lahir_customer = tanggal_lahir_customer;
    }
}
