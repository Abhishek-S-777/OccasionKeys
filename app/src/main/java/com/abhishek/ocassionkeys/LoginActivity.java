package com.abhishek.ocassionkeys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abhishek.ocassionkeys.Model.Users;
import com.abhishek.ocassionkeys.Prevalant.Prevalant;
import com.google.auto.value.AutoOneOf;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private ImageView logo, ivSignIn, btnTwitter;
    private EditText Inputemail,Inputpassword,InputPhone;
    private TextView forgotPass, signUp, admin,admintitle,usertv;
    private Button btnSignIn,btnSignIn1;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String parentDbName = "Users";

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeGUI();
        admintitle.setVisibility(View.INVISIBLE);
        usertv.setVisibility(View.INVISIBLE);
        btnSignIn1.setVisibility(View.INVISIBLE);
        //Inputemail=(EditText)findViewById(R.id.atvEmailLog);
        Inputpassword=(EditText)findViewById(R.id.atvPass);
        InputPhone=(EditText)findViewById(R.id.atvphone);


        user = firebaseAuth.getCurrentUser();

        if(user != null) {
            finish();
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //final String mail=Inputemail.getText().toString();
                final String pass=Inputpassword.getText().toString();
                final String ph=InputPhone.getText().toString();




                if(validateInput(ph, pass)){
                    signUser(ph, pass);
                }

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, PWresetActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
                //startActivity(new Intent(LoginActivity.this,PWresetActivity.class));
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSignIn1.setVisibility(View.VISIBLE);
                admintitle.setVisibility(View.VISIBLE);
                btnTwitter.setVisibility(View.INVISIBLE);
                forgotPass.setVisibility(View.INVISIBLE);
                signUp.setVisibility(View.INVISIBLE);
                usertv.setVisibility(View.VISIBLE);
                admin.setVisibility(View.INVISIBLE);
                btnSignIn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(InputPhone.getText().toString().equals("Admin") && Inputpassword.getText().toString().equals("admin123")){
                            Toast.makeText(LoginActivity.this,"Welcome Admin!, Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,AdmincategoryActivity.class));
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Admin Login Failed, Not an admin", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        usertv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admintitle.setVisibility(View.INVISIBLE);
                btnTwitter.setVisibility(View.VISIBLE);
                forgotPass.setVisibility(View.VISIBLE);
                signUp.setVisibility(View.VISIBLE);
                usertv.setVisibility(View.INVISIBLE);
                admin.setVisibility(View.VISIBLE);
            }
        });





    }



    public void signUser(final String ph, final String pass){


        final DatabaseReference db;
        db= FirebaseDatabase.getInstance().getReference();

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(ph).exists())
                {
                    Users usersData = dataSnapshot.child("Users").child(ph).getValue(Users.class);

                    if (usersData.getPhone().equals(ph))
                    {
                        if (usersData.getPassword().equals(pass))
                        {
                            Toast.makeText(LoginActivity.this, "User Logged in Successfully.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            Prevalant.currentOnlineUsers=usersData;
                            startActivity(intent);

                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with this " + ph + " ID does not exists.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        progressDialog.setMessage("Verifying...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));

                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }


    private void initializeGUI(){



        logo = findViewById(R.id.ivLogLogo);
        ivSignIn = findViewById(R.id.ivSignIn);
        btnTwitter = findViewById(R.id.ivFacebook);

        forgotPass = findViewById(R.id.tvForgotPass);
        signUp = findViewById(R.id.tvSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn1 = findViewById(R.id.btnSignIn1);
        progressDialog = new ProgressDialog(this);
        admin = findViewById(R.id.tvadmin);
        admintitle=findViewById(R.id.admintitle);
        usertv=findViewById(R.id.tvuser);

        firebaseAuth = FirebaseAuth.getInstance();

    }


    public boolean validateInput(String inph, String inpassword){

        if(inph.isEmpty()){
           Inputemail.setError("Phone field is empty.");
            return false;
        }
        if(inpassword.isEmpty()){
            Inputpassword.setError("Password is empty.");
            return false;
        }

        return true;
    }

}
