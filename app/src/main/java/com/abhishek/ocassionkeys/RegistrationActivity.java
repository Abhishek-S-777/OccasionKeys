
package com.abhishek.ocassionkeys;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    DatabaseReference db;

    private ImageView logo, joinus;
    private AutoCompleteTextView username, email, password, cpassword,phno;
    private Button signup;
    private TextView signin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db=firebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        if(checkPermission(Manifest.permission.SEND_SMS)){
            //send.setEnabled(true);
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        initializeGUI();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String inputName = username.getText().toString().trim();
                final String inputPw = password.getText().toString().trim();
                final String inputEmail = email.getText().toString().trim();
                final String inputCPw= cpassword.getText().toString().trim();
                final String inputPh= phno.getText().toString().trim();

                /*final String uname=username.getText().toString().trim();
                final String mail=email.getText().toString().trim();
                final String pass=password.getText().toString().trim();*/

                if(validateInput(inputName, inputPw, inputEmail, inputCPw, inputPh))
                {
                    //registerUser(inputName, inputPw, inputEmail);
                    addUser();

                    Intent intent = new Intent(getBaseContext(),LoginActivity.class);
                    startActivity(intent);
                }





            }
        });



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),LoginActivity.class);
                startActivity(intent);
                /*startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));*/
            }
        });



        /*View constraintLayout= findViewById(R.id.ll);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();*/
/*
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(this, R.color.color1),
                        ContextCompat.getColor(this, R.color.color2),
                        ContextCompat.getColor(this, R.color.color3),
                        ContextCompat.getColor(this, R.color.color4)});

        findViewById(R.id.ll).setBackground(gradientDrawable);*/


    }



    private void initializeGUI(){

        logo = findViewById(R.id.ivRegLogo);
        joinus = findViewById(R.id.ivJoinUs);
        username = findViewById(R.id.atvUsernameReg);
        email =  findViewById(R.id.atvEmailReg);
        password = findViewById(R.id.atvPasswordReg);
        cpassword= findViewById(R.id.atvPasswordcfrmReg);
        signin = findViewById(R.id.tvSignIn);
        phno=findViewById(R.id.atvPhnoReg);
        signup = findViewById(R.id.btnSignUp);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

/*
    private void registerUser(final String inputName, final String inputPw, String inputEmail) {

        progressDialog.setMessage("Verifying...");
        progressDialog.show();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


            firebaseAuth.createUserWithEmailAndPassword(inputEmail,inputPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    //addUser();
                                    Toast.makeText(RegistrationActivity.this,"You've been registered successfully!.", Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));




                    }

                    else{
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this,"Phone number already exists.", Toast.LENGTH_SHORT).show();
                    }

                }


            });

    }
*/


    public void onSend(){
        String phoneNumber = phno.getText().toString();
        String usrname= username.getText().toString();
        //String smsMessage = message.getText().toString();

        /*if(phoneNumber == null || phoneNumber.length() == 0 ||
                smsMessage == null || smsMessage.length() == 0){
            return;
        }*/

        if(checkPermission(Manifest.permission.SEND_SMS)){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, "Thank you " + usrname + " for Choosing Occasion Keys, you have been successfully registered", null, null);
            Toast.makeText(this, "Congratulations, your account has been created Successfully.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }



   /* private void sendUserData(String username, String password){

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference users = firebaseDatabase.getReference("users");
        UserProfile user = new UserProfile(username, password);
        users.push().setValue(user);

    }*/

    private boolean validateInput(String inName, String inPw, String inEmail, String inCPw, String inPh){

        if(inName.isEmpty()){
            username.setError("Username is empty.");
            return false;
        }
        if(inPw.isEmpty()){
            password.setError("Password is empty.");
            return false;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(inEmail).matches()){
            email.setError("Please Enter an valid Email ID.");
            return false;
        }
        if(!android.util.Patterns.PHONE.matcher(inPh).matches()){
            phno.setError("Please Enter a Valid Phone number");
            return false;
        }
        if(!inCPw.equals(inPw)){
            cpassword.setError("Passwords Not matching");
            return false;
        }

        return true;
    }

    private void addUser(){
        final String uname=username.getText().toString().trim();
        final String mail=email.getText().toString().trim();
        final String pass=password.getText().toString().trim();
        final String phono=phno.getText().toString().trim();

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if (!(dataSnapshot.child("Users").child(phono).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phono);
                    userdataMap.put("password", pass);
                    userdataMap.put("email", mail);
                    userdataMap.put("name", uname);

                    db.child("Users").child(phono).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegistrationActivity.this, "Congratulations, your account has been created Successfully.", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        onSend ();

                                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegistrationActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {

                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "This " + phono + " already exists.", Toast.LENGTH_LONG).show();
                    Toast.makeText(RegistrationActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);*/
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
