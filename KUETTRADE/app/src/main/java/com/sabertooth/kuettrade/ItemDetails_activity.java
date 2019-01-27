package com.sabertooth.kuettrade;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static com.sabertooth.kuettrade.Adapter_1.pc_me;
import static com.sabertooth.kuettrade.Adapter_your_prod.use_me;

public class ItemDetails_activity extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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
    private FirebaseAuth mAuth;
    DatabaseReference UserRef,UpdateRef,deleteRef,delwishRef;
    TextView item_name,item_des,item_price;
    Product_class pc;
    CheckBox s,m,l,xl,xxl,xl_3,xl_4;
    MaterialButton order_btn,wishlist_btn,edit_btn,del_btn,del_wish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        ActivityCompat.requestPermissions(ItemDetails_activity.this, new String[]{Manifest.permission.CALL_PHONE},
                1);
        initialize();
        inten_init();
        process_intent_data();
        Firebase_init();
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
                    update_user(pc);
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
                delete_product(deleteRef,"You Sure want to Delete Your Product?");
                //Toast.makeText(ItemDetails_activity.this, "Not Yet Implemented", Toast.LENGTH_SHORT).show();
            }
        });
        del_wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_product(delwishRef,"You Sure want to Delete From Your Wishlist?");
            }
        });
    }

    private void delete_product(final DatabaseReference ref,String s) {
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogVIew = inflater.inflate(R.layout.delete_alert, null);
        dialogueBuilder.setView(dialogVIew);
        TextView tvtmp=dialogVIew.findViewById(R.id.delete_text);
        tvtmp.setText(s);
        final MaterialButton buttondont = dialogVIew.findViewById(R.id.button_no_dont);
        final MaterialButton buttonDelete=dialogVIew.findViewById(R.id.button_yes_delete);
       // dialogueBuilder.setTitle("Delete Product");
        final AlertDialog alertDialog=dialogueBuilder.create();
        alertDialog.show();
        buttondont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.removeValue();
                Toast.makeText(ItemDetails_activity.this, "Product Deleted", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                finish();
            }
        });
    }

    private void update_user(Product_class pc) {
        Log.e("DEBUG",UpdateRef.getKey());
        Log.e("DEBUG",UpdateRef.toString());
        String ss=UpdateRef.push().getKey();
        pc.wid=ss;
        UpdateRef.child(ss).setValue(pc);
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
    }

    private void initialize() {
        image_f=findViewById(R.id.image_view_details_product_pic_front);
        image_b=findViewById(R.id.image_view_details_product_pic_back);
        item_name=findViewById(R.id.text_view_details_item_name);
        item_des=findViewById(R.id.text_view_details_item_des);
        item_price=findViewById(R.id.text_view_details_item_price);
        order_btn=findViewById(R.id.button_order_button);
        wishlist_btn=findViewById(R.id.button_add_wishlist);
        edit_btn=findViewById(R.id.button_edit_button);
        del_btn=findViewById(R.id.button_delete);
        del_wish=findViewById(R.id.button_wishdel);
        s=findViewById(R.id.checkbox_S);
        m=findViewById(R.id.checkbox_M);
        l=findViewById(R.id.checkbox_L);
        xl=findViewById(R.id.checkbox_XL);
        xxl=findViewById(R.id.checkbox_XXL);
        xl_3=findViewById(R.id.checkbox_3XL);
        xl_4=findViewById(R.id.checkbox_4XL);
    }
    void inten_init() {
        Intent in=getIntent();
        boolean flg=Objects.requireNonNull(in.getExtras()).getBoolean("MY PRODUCT");
        if(flg){
            order_btn.setVisibility(View.GONE);
            wishlist_btn.setVisibility(View.GONE);
            del_wish.setVisibility(View.GONE);
            edit_btn.setVisibility(View.VISIBLE);
            del_btn.setVisibility(View.VISIBLE);
            pc=use_me;
        }
        else{
            del_wish.setVisibility(View.GONE);
            order_btn.setVisibility(View.VISIBLE);
            wishlist_btn.setVisibility(View.VISIBLE);
            edit_btn.setVisibility(View.GONE);
            del_btn.setVisibility(View.GONE);
            pc=pc_me;
        }
        boolean flg1=in.getExtras().getBoolean("WISH");
        if(flg1){
            del_wish.setVisibility(View.VISIBLE);
            wishlist_btn.setVisibility(View.GONE);
        }
        try{
            Picasso.get().load(pc.image_front).into(image_f);
            Picasso.get().load(pc.image_back).into(image_b);
            item_name.setText(pc.name);
            item_des.setText(pc.description);
            item_price.setText(pc.price+" tk");
        }catch (Exception e){
            Log.e("DEBUGME",e.getMessage());
        }
    }
    void process_intent_data() {
        if(pc.size.get(0))s.setChecked(true);
        if(pc.size.get(1))m.setChecked(true);
        if(pc.size.get(2))l.setChecked(true);
        if(pc.size.get(3))xl.setChecked(true);
        if(pc.size.get(4))xxl.setChecked(true);
        if(pc.size.get(5))xl_3.setChecked(true);
        if(pc.size.get(5) )xl_4.setChecked(true);
    }
    void Firebase_init() {
        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference("users");
        UpdateRef=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("wishlist");
        if(pc.wid!=null)delwishRef=UpdateRef.child(pc.wid);
        //Log.e("FUCK",pc.id);
        //Log.e("FUCK",pc.name);
        deleteRef=FirebaseDatabase.getInstance().getReference("products").child(pc.type).child(pc.id);
        //Log.e("FUCK",deleteRef.getPath().toString());
    }
}
