package com.abhishek.ocassionkeys;

import android.Manifest;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.abhishek.ocassionkeys.Prevalant.Prevalant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity
{
    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private Button confirmOrderBtn,b1;
    public RadioGroup rdogrp;
    public RadioButton rdo1,rdo2;



    public String totalAmount = "";




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);


        totalAmount = getIntent().getStringExtra("Total Price");
        //amt1=totalAmount;

        /*Intent t = new Intent();
        t.putExtra("Total1", String.valueOf(totalAmount));
        startActivity(t);*/

        Toast.makeText(this, "Total Price =  Rs. " + totalAmount, Toast.LENGTH_SHORT).show();




        confirmOrderBtn = (Button) findViewById(R.id.confirm_final_order_btn);
        nameEditText = (EditText) findViewById(R.id.shippment_name);
        phoneEditText = (EditText) findViewById(R.id.shippment_phone_number);
        addressEditText = (EditText) findViewById(R.id.shippment_address);
        cityEditText = (EditText) findViewById(R.id.shippment_city);
        rdogrp=(RadioGroup)findViewById(R.id.rdogrp);
        rdo1 = (RadioButton) findViewById(R.id.rdo1);
        rdo2 = (RadioButton) findViewById(R.id.rdo2);
        /*rupi=(RadioButton) findViewById(R.id.rdo_upi);
        rgrp=(RadioGroup)findViewById(R.id.rdo_layout);*/
        //b1=(Button) findViewById(R.id.b1);
            confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Check();
                    onSend();

                }
            });


        }


    private void Check()
    {
        if (TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your full name.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your phone number.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your address.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your city name.", Toast.LENGTH_SHORT).show();
        }
        else if (rdogrp.getCheckedRadioButtonId()==-1)
        {
            Toast.makeText(this, "Please Select a mode of payment", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ConfirmOrder();
        }
    }

    public void onSend(){
        String message;
        String phoneNumber = Prevalant.currentOnlineUsers.getPhone();
        //String usrname= username.getText().toString();
        //String smsMessage = message.getText().toString();

        /*if(phoneNumber == null || phoneNumber.length() == 0 ||
                smsMessage == null || smsMessage.length() == 0){
            return;
        }*/

        if(checkPermission(Manifest.permission.SEND_SMS)){
            message = "Thank you for your purchase " +nameEditText.getText().toString() + " ,continue to shop with us!\nYour total order amount is Rs." +totalAmount+ "\nThe order will be shipped to this address: "+addressEditText.getText().toString()+ "\nWe will contact you at this number: "+phoneEditText.getText().toString()+ "\nYou will receive an SMS once the order is verified by the admin \n\nShipment Status: Unverified";
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> messagelist = smsManager.divideMessage(message);
            smsManager.sendMultipartTextMessage(phoneNumber, null, messagelist, null, null);

            //smsManager.sendTextMessage(phoneNumber, null, "Thank you for your purchase " +nameEditText.getText().toString() + " ,continue to shop with us!\n Your total order amount is Rs." +totalAmount+ "\n The order will be shipped to this address: "+addressEditText.getText().toString()+ "\n We will contact you at this number: "+phoneEditText.getText().toString()+ "\n You will receive an SMS once the order is verified by the admin \n Shipment Status: Unverified", null, null);
            //Toast.makeText(this, "Congratulations, your account has been created Successfully.", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

/*
    private void Check1()
    {
        if (TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your full name.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your phone number.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your address.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your city name.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ConfirmOrder1();
        }
    }
*/



    private void ConfirmOrder()
    {
        final String r1,r2,paytype;
        r1=rdo1.getText().toString();
        r2=rdo2.getText().toString();
        if(rdo1.isChecked()){
            paytype=r1;
        }
        else {
            paytype=r2;
        }

        final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());




        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalant.currentOnlineUsers.getPhone());

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("address", addressEditText.getText().toString());
        ordersMap.put("city", cityEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "not shipped");
        ordersMap.put("paymentmode", paytype);

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Prevalant.currentOnlineUsers.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "your final order has been placed successfully.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();

                                       /* if(rdo2.isChecked())
                                        {
                                            Intent intent = new Intent(ConfirmFinalOrderActivity.this, UPIpayment.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);

                                        }
                                        else {*/




                                        //}


                                    }
                                }
                            });
                }
            }
        });




    }

   /* private void ConfirmOrder1()
    {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());


        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalant.currentOnlineUsers.getPhone());

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("address", addressEditText.getText().toString());
        ordersMap.put("city", cityEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "not shipped");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Prevalant.currentOnlineUsers.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "your final order has been placed successfully.", Toast.LENGTH_SHORT).show();

                                        *//*Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();*//*
                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, UPIpayment.class);
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }
        });

    }*/

}
