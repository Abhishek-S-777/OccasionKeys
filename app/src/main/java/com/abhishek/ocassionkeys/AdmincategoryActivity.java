package com.abhishek.ocassionkeys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

/*import com.example.chmarax.logregform.HomeActivity;
import com.example.codingcafe.ecommerce.MainActivity;
import com.example.codingcafe.ecommerce.R;*/

public class AdmincategoryActivity extends AppCompatActivity
{
    private ImageView shamiana,lights,stage,dj,utencils,chairs;
    /*private ImageView glasses, hatsCaps, walletsBagsPurses, shoes;
    private ImageView headPhonesHandFree, Laptops, watches, mobilePhones;*/

    private Button LogoutBtn, CheckOrdersBtn, maintainProductsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admincategory);


        LogoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn = (Button) findViewById(R.id.check_orders_btn);
        maintainProductsBtn = (Button) findViewById(R.id.maintain_btn);



        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdmincategoryActivity.this, HomeActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });


        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdmincategoryActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdmincategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });


        shamiana = (ImageView) findViewById(R.id.shamiana);
        stage = (ImageView) findViewById(R.id.stage);
        utencils = (ImageView) findViewById(R.id.utencils);
        dj  = (ImageView) findViewById(R.id.djsets);
        chairs = (ImageView) findViewById(R.id.chairs);
        lights  = (ImageView) findViewById(R.id.lights);




        shamiana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdmincategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Shamiana");
                startActivity(intent);
            }
        });


        stage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdmincategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Stages");
                startActivity(intent);
            }
        });


        utencils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdmincategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Utencils");
                startActivity(intent);
            }
        });


        dj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdmincategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "DJ Sets");
                startActivity(intent);
            }
        });


        chairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdmincategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "chairs");
                startActivity(intent);
            }
        });


        lights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdmincategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Lights");
                startActivity(intent);
            }
        });




    }
}
