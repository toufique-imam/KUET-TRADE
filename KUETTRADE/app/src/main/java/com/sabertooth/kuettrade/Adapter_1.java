package com.sabertooth.kuettrade;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class viewHolder extends RecyclerView.ViewHolder {
    ImageView img;
    TextView name_,price_;
    CardView cardView;
    public viewHolder(@NonNull View itemView) {
        super(itemView);
        cardView=itemView.findViewById(R.id.item_card);
        img=itemView.findViewById(R.id.item_image);
        name_=itemView.findViewById(R.id.item_name);
        price_=itemView.findViewById(R.id.item_price);
    }
}
public class Adapter_1  extends  RecyclerView.Adapter<viewHolder>{
    Context ctx;
    static Product_class pc_me;
    ArrayList<Product_class>tmp_data;
    public Adapter_1(Context cntxt,ArrayList<Product_class>tmp){
        ctx=cntxt;
        pc_me=new Product_class();
        tmp_data=tmp;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflat=LayoutInflater.from(ctx).inflate(R.layout.item_view,viewGroup,false);
        return new viewHolder(inflat);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, final int i) {
        pc_me=tmp_data.get(i);
        Picasso.get().load(tmp_data.get(i).image_front).into(viewHolder.img);
        viewHolder.name_.setText(tmp_data.get(i).name);
        viewHolder.price_.setText(tmp_data.get(i).price+"");
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent show=new Intent(ctx,ItemDetails_activity.class);
                show.putExtra("idx",i);
                show.putExtra("MY PRODUCT",false);
                show.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(show);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tmp_data.size();
    }
}
