package com.lawrenxiusbenny.roemah_soto_android.model;


import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;

import java.util.ArrayList;

public class Midtrans {
    public static int id;
    public static String name;
    public static String email;
    public static String phone;

    public Midtrans(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
