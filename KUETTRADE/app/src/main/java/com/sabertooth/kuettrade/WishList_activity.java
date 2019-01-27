package com.sabertooth.kuettrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WishList_activity extends AppCompatActivity {
    RecyclerView rv;
    GridLayoutManager gridLayoutManager;
    Adapter_1 adpx;
    ArrayList<Product_class>tmp_data;
    FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();
        get_data();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_activity);
        auth=FirebaseAuth.getInstance();
        rv=findViewById(R.id.recycler_view_wishList);
        tmp_data=new ArrayList<>();
        get_data();
    }
    void get_data(){
        tmp_data.clear();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("users").child(auth.getCurrentUser().getUid()).child("wishlist");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot xd:dataSnapshot.getChildren()){
                    Product_class pc=xd.getValue(Product_class.class);
                    tmp_data.add(pc);
                }
                adpx=new Adapter_1(WishList_activity.this,tmp_data);
                gridLayoutManager=new GridLayoutManager(WishList_activity.this,2);
                rv.setAdapter(adpx);
                rv.setLayoutManager(gridLayoutManager);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(WishList_activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
