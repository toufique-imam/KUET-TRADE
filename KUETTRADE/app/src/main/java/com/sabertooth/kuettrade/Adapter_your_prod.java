package com.sabertooth.kuettrade;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


class viewholder_your_prod extends RecyclerView.ViewHolder {
    ImageView img_f, img_b;
    TextView name_, price_,cata_;
    CardView cv;

    public viewholder_your_prod(@NonNull View itemView) {
        super(itemView);
        cata_=itemView.findViewById(R.id.text_view_my_prod_cat);
        img_f = itemView.findViewById(R.id.image_view_my_prod_f);
        img_b = itemView.findViewById(R.id.image_view_my_prod_b);
        cv = itemView.findViewById(R.id.card_view_your_products);
        name_ = itemView.findViewById(R.id.text_view_my_prod_name);
        price_ = itemView.findViewById(R.id.text_view_my_prod_price);
    }
}

public class Adapter_your_prod extends RecyclerView.Adapter<viewholder_your_prod> {
    Context context;
    static  Product_class use_me;
    ArrayList<Product_class> tmp_data;
    public Adapter_your_prod(Context context, ArrayList<Product_class> tmp_data) {
        this.context = context;
        this.tmp_data = tmp_data;
    }

    @NonNull
    @Override
    public viewholder_your_prod onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.your_products_for_recycle_view, viewGroup, false);
            return new viewholder_your_prod(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder_your_prod viewholder_your_prod, final int i) {
        try {
            Picasso.get().load(tmp_data.get(i).image_front).into(viewholder_your_prod.img_f);
            Picasso.get().load(tmp_data.get(i).image_back).into(viewholder_your_prod.img_b);
        } catch (Exception e) {
            Log.e("LOGME", e.getMessage());
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        viewholder_your_prod.cata_.setText(tmp_data.get(i).type);
        viewholder_your_prod.price_.setText(tmp_data.get(i).price+"/-");
        viewholder_your_prod.name_.setText(tmp_data.get(i).name);
        viewholder_your_prod.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                use_me=tmp_data.get(i);
                Intent intent=new Intent(context,ItemDetails_activity.class);
                intent.putExtra("MY PRODUCT",true);
                intent.putExtra("idx",i);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return tmp_data.size();
    }
}
