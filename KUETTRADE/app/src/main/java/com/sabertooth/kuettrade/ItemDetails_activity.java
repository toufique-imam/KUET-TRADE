package com.sabertooth.kuettrade;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import static com.sabertooth.kuettrade.Adapter_1.pc_me;
import static com.sabertooth.kuettrade.Adapter_your_prod.use_me;

public class ItemDetails_activity extends AppCompatActivity {
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(ItemDetails_activity.this, "Permission denied to make a Call", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
    ImageView image_f,image_b;
    TextView item_name,item_des;
    Product_class pc;
    int x;
    Button order_btn,wishlist_btn,edit_btn,del_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        ActivityCompat.requestPermissions(ItemDetails_activity.this, new String[]{Manifest.permission.CALL_PHONE},
                1);
        initialize();
        Intent in=getIntent();
        boolean flg=Objects.requireNonNull(in.getExtras()).getBoolean("MY PRODUCT");
        if(flg){
            order_btn.setVisibility(View.GONE);
            wishlist_btn.setVisibility(View.GONE);
            edit_btn.setVisibility(View.VISIBLE);
            del_btn.setVisibility(View.VISIBLE);
            pc=use_me;
        }
        else{
            order_btn.setVisibility(View.VISIBLE);
            wishlist_btn.setVisibility(View.VISIBLE);
            edit_btn.setVisibility(View.GONE);
            del_btn.setVisibility(View.GONE);
            pc=pc_me;
        }
        try{
            Picasso.get().load(pc.image_front).into(image_f);
            Picasso.get().load(pc.image_back).into(image_b);
            item_name.setText(pc.name);
            item_des.setText(pc.description);
        }catch (Exception e){
            Log.e("DEBUGME",e.getMessage());
        }
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri="tel:"+pc.phone1;
                Intent it = new Intent(Intent.ACTION_CALL);
                it.setData(Uri.parse(uri));
                startActivity(it);
            }
        });
        wishlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ItemDetails_activity.this, "Under Progress", Toast.LENGTH_SHORT).show();
            }
        });
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ItemDetails_activity.this, "Not Yet Implemented", Toast.LENGTH_SHORT).show();
            }
        });
        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ItemDetails_activity.this, "Not Yet Implemented", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {
        image_f=findViewById(R.id.image_view_details_product_pic_front);
        image_b=findViewById(R.id.image_view_details_product_pic_back);
        item_name=findViewById(R.id.text_view_details_item_name);
        item_des=findViewById(R.id.text_view_details_item_des);
        order_btn=findViewById(R.id.button_order_button);
        wishlist_btn=findViewById(R.id.button_add_wishlist);
        edit_btn=findViewById(R.id.button_edit_button);
        del_btn=findViewById(R.id.button_delete);
    }
}
