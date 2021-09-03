package com.abhishek.ocassionkeys;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AutoCompleteTextView;
import com.google.firebase.database.DataSnapshot;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.abhishek.ocassionkeys.Model.Users;
import com.abhishek.ocassionkeys.Prevalant.Prevalant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

public class PWresetActivity extends AppCompatActivity {

    private ImageView ivLogo,ivPWreset;
    private TextView tvInfo, tvSignin;
    private AutoCompleteTextView atvEmail;
    private Button btnReset;
    private FirebaseAuth firebaseAuth;
    private String check = "";
    private String state = "";
    private String savenewpwd = "";



    @Override
    protected void onStart() {
        super.onStart();
        if (check.equals("settings"))
        {


        }
        else if (check.equals("login"))
        {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwreset);
        check = getIntent().getStringExtra("check");
        initializeGUI();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUser();
                /*if(state.equals("updated"))
                {
                    onSend();
                }*/

                /*String email = atvEmail.getText().toString();

                if (email.isEmpty()) {
                    atvEmail.setError("Please, fill the email field.",null);
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PWresetActivity.this, "Email has been sent successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PWresetActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(PWresetActivity.this, "Invalid email address.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                  }*/

            }
        });


        tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PWresetActivity.this, LoginActivity.class));
            }
        });


    }


        public void onSend(){

        String newpass= Prevalant.currentOnlineUsers.getPassword();

        /*String data="";
        Intent i = new Intent();
        Bundle extras = i.getExtras();
        if(extras != null) {
            data = extras.getString("newpass");
        }*/
        String phoneNumber = atvEmail.getText().toString();
        String password= newpass;
        //String smsMessage = message.getText().toString();

        if(phoneNumber == null || phoneNumber.length() == 0 ||
                password == null || password.length() == 0){
            return;
        }

        if(checkPermission(Manifest.permission.SEND_SMS)){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, "Forgot Password ? your new password is "+password+ " ,Thank you!", null, null);
            Toast.makeText(this, "Password Changed, New password sent to registered mobile number", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }






    public void verifyUser()
    {

        final String phono=atvEmail.getText().toString();
        if(!phono.equals("")){

            final DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Users").child(phono);
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String mPhone=dataSnapshot.child("phone").getValue().toString();
                        //if(phono.equals( mPhone))
                        //{
                        AlertDialog.Builder builder= new AlertDialog.Builder(PWresetActivity.this);
                        builder.setTitle("New Password");

                        final EditText newPassword = new EditText(PWresetActivity.this);
                        newPassword.setHint("Please Enter the New password");
                        builder.setView(newPassword);

                        savenewpwd = newPassword.getText().toString();

                        /*Intent i = new Intent(PWresetActivity.this, null);
                        i.putExtra("newpass", newpwd);
                        startActivity(i);*/

                        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(!newPassword.getText().toString().equals(""))
                                {
                                    db.child("password").setValue(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){


                                               /* Handler handler = new Handler(Looper.getMainLooper());
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        onSend();
                                                        // Run your task here
                                                    }
                                                }, 5000 );*/
                                                //String newpass= Prevalant.currentOnlineUsers.getPassword();

                                                /*String data="";
                                                Intent i = new Intent();
                                                Bundle extras = i.getExtras();
                                                if(extras != null) {
                                                    data = extras.getString("newpass");
                                                }*/
                                                Toast.makeText(PWresetActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(PWresetActivity.this, "New Password Sent to your registered mobile number", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(PWresetActivity.this, LoginActivity.class);
                                                intent.putExtra("check", "settings");
                                                startActivity(intent);

                                                String phoneNumber = atvEmail.getText().toString();
                                                String password= newPassword.getText().toString();
                                                //String smsMessage = message.getText().toString();

                                                if(phoneNumber == null || phoneNumber.length() == 0 ||
                                                        password == null || password.length() == 0){
                                                    return;
                                                }

                                                if(checkPermission(Manifest.permission.SEND_SMS)){
                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage(phoneNumber, null, "Forgot Password ? your new password is "+password+ ", Thank you!", null, null);
                                                    //Toast.makeText(this, "Password Changed, New password sent to registered mobile number", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    //Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                                                }





                                            }
                                        }
                                    });



                                }
                            }

                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                        builder.show();



                    }
                    else {
                        Toast.makeText(PWresetActivity.this, "This User does not exists.", Toast.LENGTH_SHORT).show();

                    }


                }

                //}

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }
        else {
            Toast.makeText(PWresetActivity.this, "Please Complete the Field", Toast.LENGTH_SHORT).show();

        }





    }


    private void initializeGUI(){

        ivLogo = findViewById(R.id.ivLogLogo);
        ivPWreset = findViewById(R.id.ivPassReset);
        tvInfo = findViewById(R.id.tvPWinfo);
        tvSignin = findViewById(R.id.tvGoBack);
        atvEmail = findViewById(R.id.atvEmailRes);
        btnReset = findViewById(R.id.btnReset);

        firebaseAuth = FirebaseAuth.getInstance();

    }
}
