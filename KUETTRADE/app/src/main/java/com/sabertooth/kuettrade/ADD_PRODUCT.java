package com.sabertooth.kuettrade;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

import static com.sabertooth.kuettrade.store_and_user_nav_settings.user_x;

public class ADD_PRODUCT extends AppCompatActivity {
    ImageView front_img, back_img;
    private static final int REQ_CODE = 1;
    Uri imageUri;
    int imagenow;
    Bitmap bitmap;
    String image_url, front_url, back_url;
    CheckBox cb;
    ArrayList<String> cat, size;
    EditText product_name, description, price, amount;
    Button add_product;
    ProgressDialog pDialog;
    boolean f1, f2;
    boolean image_indicator;
    User_class user_c = user_x;
    Product_class pc;
    DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__product);
        f1 = f2 = false;
        pc = new Product_class();
        image_indicator = false;
        front_img = findViewById(R.id.image_view_product_pic_f);
        back_img = findViewById(R.id.image_view_product_pic_b);
        product_name = findViewById(R.id.edit_text_product_name);
        description = findViewById(R.id.edit_text_product_descriptio);
        amount = findViewById(R.id.edit_text_product_quantity);
        price = findViewById(R.id.edit_text_product_price);
        add_product = findViewById(R.id.button_add_product);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
        front_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = null;
                imageUri = null;
                f1 = true;
                image_indicator = true;
                imagenow = R.id.image_view_product_pic_f;
                showImageChooser();
            }
        });
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = null;
                imageUri = null;
                f2 = true;
                image_indicator = false;
                imagenow = R.id.image_view_product_pic_b;
                showImageChooser();
            }
        });
    }

    private void toaster(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private void addProduct() {
        String nam, dess;
        Integer pric, amoun;
        pric = new Integer(0);
        amoun = new Integer(0);
        cat = new ArrayList<>();
        size = new ArrayList<>();
        cb = findViewById(R.id.checkbox_t_shirt_half);
        if (cb.isChecked()) cat.add("T Shirt Half Sleeve");
        //cb.setChecked(false);
        cb = findViewById(R.id.checkbox_t_shirt_full);
        if (cb.isChecked()) cat.add("T Shirt Full Sleeve");
        //cb.setChecked(false);
        cb = findViewById(R.id.checkbox_polo_half);
        if (cb.isChecked()) cat.add("Polo Shirt Half Sleeve");
        //cb.setChecked(false);
        cb = findViewById(R.id.checkbox_polo_full);
        if (cb.isChecked()) cat.add("Polo Shirt Full Sleeve");
        //cb.setChecked(false);
        cb = findViewById(R.id.checkbox_hoodie);
        if (cb.isChecked()) cat.add("Hoodie");
        //cb.setChecked(false);
        cb = findViewById(R.id.checkbox_S);
        if (cb.isChecked()) size.add("S");
        //cb.setChecked(false);
        cb = findViewById(R.id.checkbox_S);
        if (cb.isChecked()) size.add("M");
        //cb.setChecked(false);
        cb = findViewById(R.id.checkbox_S);
        if (cb.isChecked()) size.add("L");
        //cb.setChecked(false);
        cb = findViewById(R.id.checkbox_S);
        if (cb.isChecked()) size.add("XL");
        //cb.setChecked(false);
        cb = findViewById(R.id.checkbox_S);
        if (cb.isChecked()) size.add("XXL");
        //cb.setChecked(false);
        cb = findViewById(R.id.checkbox_S);
        if (cb.isChecked()) size.add("3XL");
        //cb.setChecked(false);
        cb = findViewById(R.id.checkbox_S);
        if (cb.isChecked()) size.add("4XL");
        //cb.setChecked(false);
        nam = product_name.getText().toString();
        dess = description.getText().toString();
        try {
            pric = Integer.valueOf((price.getText().toString()));
        } catch (Exception e) {
            toaster(e.getMessage());
            pric = 0;
        }
        try {
            amoun = Integer.valueOf((amount.getText().toString()));
        } catch (Exception e) {
            amoun = 0;
            toaster(e.getMessage());
        }
        try {
            if (amoun > 0 && pric > 0 && !nam.isEmpty() && !size.isEmpty() && !cat.isEmpty() &&  !front_url.isEmpty()) {
                pc.amount = amoun;
                pc.address = user_c.Address;
                pc.description = dess;
                pc.image_back = back_url;
                pc.image_front = front_url;
                pc.name = nam;
                pc.phone1 = user_c.Phone1;
                pc.phone2 = user_c.Phone2;
                pc.price = pric;
                pc.size = size;
                pc.uid = user_c.uid;
                upload_product();
            } else {
                toaster("Invalid Input");
            }
        } catch (Exception e) {
            toaster(e.getMessage());
        }
    }

    private void upload_product() {
        if (!user_c.uid.isEmpty() &&  f1) {
            productRef = FirebaseDatabase.getInstance().getReference("products");
            DatabaseReference tmp;
            for (String s : cat) {
                tmp = productRef.child(s);
                String idx = tmp.push().getKey();
                try {
                    pc.id = idx;
                    pc.type=s;
                    tmp.child(idx).setValue(pc);
                    toaster("Product Added");
                } catch (Exception e) {
                    toaster(e.getMessage());
                }
            }
        }
    }


    private void showImageChooser() {
        Intent intt = new Intent();
        intt.setType("image/*");
        intt.setAction(Intent.ACTION_GET_CONTENT);
        try {
            startActivityForResult(Intent.createChooser(intt, "Select Picture"), REQ_CODE);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int req_code, int res_code, Intent data) {
        super.onActivityResult(req_code, res_code, data);
        if (req_code == REQ_CODE && res_code == RESULT_OK && null != data) {
            imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                //Bitmap result=Bitmap.createScaledBitmap(bitmap,200,200,false);
                //           imageUri=getImageUri(getApplicationContext(),result);
                ImageView imageView = findViewById(imagenow);
                imageView.setImageBitmap(bitmap);
                String loc;
                if (image_indicator) loc = "front";
                else loc = "back";
                uploadImageInFirebaseStorage(loc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageInFirebaseStorage(String loc) {
        pDialog.setMessage("Uploading Product Image");
        pDialog.show();
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference(loc + "/" + System.currentTimeMillis() + ".jpg");
        if (imageUri != null) {
            //       Log.d("LOG_PH4", imageUri.toString());
            //pgb.setVisibility(View.VISIBLE);
            final StorageReference photoStorageReference = profileImageRef.child("profilepics/" + System.currentTimeMillis() + ".jpg");
            photoStorageReference.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return photoStorageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    pDialog.dismiss();
                    //      pgb.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        image_url = downloadUri.toString();
                        if (image_indicator)
                            front_url = image_url;
                        else
                            back_url = image_url;
                        //                 Log.d("WHAT", image_url);
                    } else {
                        Toast.makeText(getApplicationContext(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
