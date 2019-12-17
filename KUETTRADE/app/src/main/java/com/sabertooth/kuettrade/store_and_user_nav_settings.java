package com.sabertooth.kuettrade;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class store_and_user_nav_settings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //String API="http://api.apixu.com/v1/current.json?key=b349e54ea3394aa1b9f90815182911&q=Fulbari";
    static User_class user_x;
    DatabaseReference UserRef, productsref;
    private FirebaseAuth mAuth;
    ImageView coverPic;
    CircleImageView propic;
    LinearLayout nav_lin_layout;
    TextView nav_user_name, nav_user_mail,weather_loc,weather_temp;
    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;
    View headerView;
    ProgressDialog pg;
    RecyclerView tsh, tsf, psh, psf, hod;
    Adapter_1 adp1, adp2, adp3, adp4, adp5;
    LinearLayoutManager llm1, llm2, llm3, llm4, llm5;
    static ArrayList<Product_class> ar_tsh, ar_tsf, ar_psh, ar_psf, ar_hod, my_items;

    private void toaster(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        RetriveUserInfo();
        fetch_data();
        log_print();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_and_user_nav_settings);
        initialize();
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(store_and_user_nav_settings.this,WishList_activity.class);
                startActivity(intent);
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        RetriveUserInfo();
        fetch_data();
        log_print();
      /*  mcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  get_weather gc=new get_weather();
               // gc.execute();
            }
        });*/
    }

    private void initialize() {
        user_x = new User_class();
        ar_hod = new ArrayList<>();
        ar_psf = new ArrayList<>();
        ar_psh = new ArrayList<>();
        ar_tsf = new ArrayList<>();
        ar_tsh = new ArrayList<>();
        my_items = new ArrayList<>();
        //mcv=findViewById(R.id.weather_update);
        hod = findViewById(R.id.recycler_view_hoodie);
        psf = findViewById(R.id.recycler_view_polo_shirt_full);
        psh = findViewById(R.id.recycler_view_polo_shirt_half);
        tsf = findViewById(R.id.recycler_view_t_shirt_full);
        tsh = findViewById(R.id.recycler_view_t_shirt_half);
        productsref = FirebaseDatabase.getInstance().getReference("products");
        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference("users");
        pg=new ProgressDialog(store_and_user_nav_settings.this);
        pg.setMessage("Downloading Data.........");
        pg.show();
        RetriveUserInfo();
        fetch_data();
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        coverPic = headerView.findViewById(R.id.image_view_nav_cover);
        propic = headerView.findViewById(R.id.imageView_nav_pro_pic);
        nav_lin_layout = headerView.findViewById(R.id.linear_layout_nav);
        nav_user_name = headerView.findViewById(R.id.text_view_nav_user_name);
        nav_user_mail = headerView.findViewById(R.id.text_view_nav_user_mail);
       // get_weather gc=new get_weather();
        //gc.execute();
        pg.dismiss();
    }

    void log_print() {
        Log.e("WHAT", ar_tsf.size() + "");
        Log.e("WHAT", ar_tsh.size() + "");
        Log.e("WHAT", ar_psf.size() + "");
        Log.e("WHAT", ar_psh.size() + "");
        Log.e("WHAT", ar_hod.size() + "");
    }

    private void fetch_data() {
        my_items.clear();
        DatabaseReference hood = productsref.child("Hoodie");
        hood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ar_hod.clear();
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    Product_class pc = x.getValue(Product_class.class);
                    if (pc!=null && pc.uid.equals(user_x.uid))
                        my_items.add(pc);
                    else ar_hod.add(pc);
                }
                adp1 = new Adapter_1(getApplicationContext(), ar_hod);
                llm1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                hod.setAdapter(adp1);
                hod.setLayoutManager(llm1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toaster(databaseError.getMessage());
            }
        });
        DatabaseReference polofull = productsref.child("Polo Shirt Full Sleeve");
        polofull.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ar_psf.clear();
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    Product_class pc = x.getValue(Product_class.class);
                    if (pc != null && pc.uid.equals(user_x.uid))
                        my_items.add(pc);
                    else ar_psf.add(pc);
                }
                adp2 = new Adapter_1(getApplicationContext(), ar_psf);
                llm2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                psf.setLayoutManager(llm2);
                psf.setAdapter(adp2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toaster(databaseError.getMessage());
            }
        });
        DatabaseReference polohalf = productsref.child("Polo Shirt Half Sleeve");
        polohalf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ar_psh.clear();
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    Product_class pc = x.getValue(Product_class.class);
                    if (pc != null && pc.uid.equals(user_x.uid))
                        my_items.add(pc);
                    else ar_psh.add(pc);
                }
                adp3 = new Adapter_1(getApplicationContext(), ar_psh);
                llm3 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                psh.setAdapter(adp3);
                psh.setLayoutManager(llm3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toaster(databaseError.getMessage());
            }
        });
        DatabaseReference tsfull = productsref.child("T Shirt Full Sleeve");
        tsfull.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ar_tsf.clear();
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    Product_class pc = x.getValue(Product_class.class);
                    if (pc != null && pc.uid.equals(user_x.uid))
                        my_items.add(pc);
                    else  ar_tsf.add(pc);
                }
                adp4 = new Adapter_1(getApplicationContext(), ar_tsf);
                llm4 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                tsf.setLayoutManager(llm4);
                tsf.setAdapter(adp4);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toaster(databaseError.getMessage());
            }
        });
        DatabaseReference tshalf = productsref.child("T Shirt Half Sleeve");
        tshalf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ar_tsh.clear();
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    Product_class pc = x.getValue(Product_class.class);
                    if (pc != null && Objects.equals(pc.uid, user_x.uid))
                        my_items.add(pc);
                    else ar_tsh.add(pc);
                }
                adp5 = new Adapter_1(getApplicationContext(), ar_tsh);
                llm5 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                tsh.setAdapter(adp5);
                tsh.setLayoutManager(llm5);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toaster(databaseError.getMessage());
            }
        });

    }

    private void RetriveUserInfo() {
        ProgressDialog pgb = new ProgressDialog(this);
        pgb.setCancelable(false);
        pgb.setMessage("Downloading Data......");
        pgb.show();
        mAuth = FirebaseAuth.getInstance();
        UserRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("proPicUrl")) {
                        String downloadurl = dataSnapshot.child("proPicUrl").getValue().toString();
                        user_x.proPicUrl = downloadurl;
                        //Log.d("ProPic",downloadurl);
                        Picasso.get().setLoggingEnabled(true);
                        try {
                            Picasso.get().load(downloadurl).into(propic);
                        } catch (Exception e) {
                            toaster(e.getMessage());
                        }
                    }
                    if (dataSnapshot.hasChild("Name"))
                        user_x.Name = dataSnapshot.child("Name").getValue().toString();
                    if (dataSnapshot.hasChild("Email"))
                        user_x.Email = dataSnapshot.child("Email").getValue().toString();
                    if (dataSnapshot.hasChild("coverPicUrl"))
                        user_x.coverPicUrl = dataSnapshot.child("coverPicUrl").getValue().toString();
                    if (dataSnapshot.hasChild("Phone1"))
                        user_x.Phone1 = dataSnapshot.child("Phone1").getValue().toString();
                    if (dataSnapshot.hasChild("Phone2"))
                        user_x.Phone2 = dataSnapshot.child("Phone2").getValue().toString();
                    if (dataSnapshot.hasChild("Address"))
                        user_x.Address = dataSnapshot.child("Address").getValue().toString();
                    if (dataSnapshot.hasChild("uid"))
                        user_x.uid = dataSnapshot.child("uid").getValue().toString();
                    if (dataSnapshot.hasChild("password"))
                        user_x.password = dataSnapshot.child("password").getValue().toString();
                    nav_user_mail.setText(user_x.Email);
                    nav_user_name.setText(user_x.Name);
                    try {
                        Picasso.get().load(user_x.coverPicUrl).into(coverPic);
                    } catch (Exception e) {
                        toaster(e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        pgb.dismiss();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.store_and_user_nav_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_wish) {
            Intent intent=new Intent(getApplicationContext(),WishList_activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_add_product) {
            Intent intent = new Intent(getApplicationContext(), ADD_PRODUCT.class);
            startActivity(intent);
        } else if (id == R.id.nav_my_products) {
            Intent intent = new Intent(getApplicationContext(), Your_Products.class);
            startActivity(intent);
        } else if (id == R.id.nav_user_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_user_logout) {
            AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogVIew = inflater.inflate(R.layout.delete_alert, null);
            dialogueBuilder.setView(dialogVIew);
            TextView tvtmp=dialogVIew.findViewById(R.id.delete_text);
            tvtmp.setText("You Sure want to logOut?");
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
                    mAuth.signOut();
                    Intent intent = new Intent(getApplicationContext(), SignIn_Activity.class);
                    finish();
                    startActivity(intent);
                }
            });

        } else if (id == R.id.nav_send) {
            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "nuhash1083s@gmail.com" });
            Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            Email.putExtra(Intent.EXTRA_TEXT, "Hello Developer Kuet Trade's...," + "");
            startActivity(Intent.createChooser(Email, "Send Feedback:"));
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
