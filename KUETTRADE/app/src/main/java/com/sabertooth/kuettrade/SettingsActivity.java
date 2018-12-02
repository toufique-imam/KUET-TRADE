package com.sabertooth.kuettrade;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.sabertooth.kuettrade.store_and_user_nav_settings.cp;
import static com.sabertooth.kuettrade.store_and_user_nav_settings.pp;
import static com.sabertooth.kuettrade.store_and_user_nav_settings.user_x;

public class SettingsActivity extends AppCompatActivity {
    ImageView proPic,coverPic;
    TextView name,email,address,phone1,phone2;
    Button pass;
    FloatingActionButton fab;
    private void toaster(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        proPic=findViewById(R.id.image_view_settings_profile_pic);
        coverPic=findViewById(R.id.image_view_settings_cover_pic);
        name=findViewById(R.id.text_view_setting_user_name);
        email=findViewById(R.id.text_view_setting_user_mail);
        address=findViewById(R.id.text_view_setting_user_address);
        phone1=findViewById(R.id.text_view_setting_user_phone_1);
        phone2=findViewById(R.id.text_view_setting_user_phone_2);
        pass=findViewById(R.id.button_setting_show_password);
        fab=findViewById(R.id.fab_setting);
        try{
            proPic.setImageBitmap(pp.getBitmap());
        }
        catch (Exception e){
            toaster(e.getMessage());
        }try{
            coverPic.setImageBitmap(cp.getBitmap());
        }
        catch (Exception e){
            toaster(e.getMessage());
        }
        name.setText(user_x.Name);
        email.setText(user_x.Email);
        address.setText(user_x.Address);
        phone2.setText(user_x.Phone2);
        phone1.setText(user_x.Phone1);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass.setText(user_x.password);
            }
        });
    }
}
