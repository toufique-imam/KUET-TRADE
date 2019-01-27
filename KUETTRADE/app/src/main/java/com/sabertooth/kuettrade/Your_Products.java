package com.sabertooth.kuettrade;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.sabertooth.kuettrade.store_and_user_nav_settings.my_items;

public class Your_Products extends AppCompatActivity {
    ArrayList<Product_class> tmp;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    Adapter_your_prod adapter_your_prod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your__products);
        tmp=my_items;
        recyclerView=findViewById(R.id.recycler_view_your_products);
        linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        adapter_your_prod=new Adapter_your_prod(this,tmp);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter_your_prod);
    }
}
