package com.sabertooth.kuettrade;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Activity_signUP extends AppCompatActivity {
    EditText et_pass, et_mail, et_name, et_address, et_phone_1, et_phone_2;
    private static final int REQ_CODE = 1;
    ImageView coverImg, proImg;
    MaterialButton bt_action, bt_show;
    private FirebaseAuth mAuth;
    Uri imageUri;
    int imagenow,imgwid,imghight;
    Bitmap bitmap;
    String image_url, profle_pic_url, cover_pic_url;
    ProgressDialog pDialog;
    Boolean f1, f2;
    Boolean image_indicator;
    DatabaseReference databaseUser;
    Display screen;

    String user_name, address, phone1, phone2, email, password;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(), SignIn_Activity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        screen=getWindowManager().getDefaultDisplay();
        f1 = f2 = image_indicator = false;
        mAuth = FirebaseAuth.getInstance();
        //TextView tv=findViewById(R.id.text_view_t_shirt_half);
        bt_action = findViewById(R.id.button_sign_up_button);
        et_name = findViewById(R.id.edit_text_sign_up_user_name);
        et_mail = findViewById(R.id.edit_text_sign_up_user_mail);
        et_address = findViewById(R.id.edit_text_sign_up_user_address);
        et_phone_1 = findViewById(R.id.edit_text_sign_up_user_phone_1);
        et_phone_2 = findViewById(R.id.edit_text_sign_up_user_phone_2);
        et_pass = findViewById(R.id.edit_text_sign_up_user_password);
        coverImg = findViewById(R.id.image_view_sign_up_cover_pic);
        proImg = findViewById(R.id.image_view_sign_up_profile_pic);
        bt_show = findViewById(R.id.button_sign_up_show_password);
        pDialog = new ProgressDialog(Activity_signUP.this);
        pDialog.setCancelable(false);
        proImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = null;
                imageUri = null;
                f1 = true;
                image_indicator = true;
                imghight=imgwid=150;
                imagenow = R.id.image_view_sign_up_profile_pic;
                showImageChooser();
            }
        });
        coverImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = null;
                imageUri = null;
                f2 = true;
                image_indicator = false;
                imghight=160;
                imgwid=screen.getWidth();
                imagenow = R.id.image_view_sign_up_cover_pic;
                showImageChooser();
            }
        });
        bt_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_pass.getInputType() == 1) {
                    bt_show.setText("Show");
                    et_pass.setInputType(129);
                } else {
                    bt_show.setText("Hide");
                    et_pass.setInputType(1);
                }
            }
        });
        bt_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.setMessage("Registering...");
                pDialog.setCancelable(false);
                register_user();
            }
        });
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
            Uri imageUrix = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUrix);
                Bitmap result=Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2,false);
                imageUri=getImageUri(getApplicationContext(),result);
                ImageView imageView = findViewById(imagenow);
                imageView.setImageBitmap(bitmap);
                String loc;
                if (image_indicator) loc = "front";
                else loc = "back";
                uploadImageInFirebaseStorage(loc);
            } catch (IOException e) {
                e.printStackTrace();
                toaster(e.getMessage());
            }
        }
    }
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
       return Uri.parse(path);
    }
    private void register_user() {
        user_name = et_name.getText().toString();
        address = et_address.getText().toString();
        phone1 = et_phone_1.getText().toString();
        phone2 = et_phone_2.getText().toString();
        email = et_mail.getText().toString().trim();
        password = et_pass.getText().toString().trim();
        if (email.isEmpty()) {
            et_mail.setError("Email Shouldn't be Empty");
            et_mail.requestFocus();
            return;
        }
        if (user_name.isEmpty()) {
            et_name.setError("Name Shouldn't be Empty");
            et_name.requestFocus();
            return;
        }
        if (address.isEmpty()) {
            et_address.setError("Address Shouldn't be Empty");
            et_address.requestFocus();
            return;
        }
        if (phone1.isEmpty()) {
            et_phone_1.setError("Phone Number 1 Shouldn't be Empty");
            et_phone_1.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            et_pass.setError("Email Shouldn't be Empty");
            et_pass.requestFocus();
            return;
        }
        if (!f1) {
            Toast.makeText(getApplicationContext(), "Profile Image Shouldn't be Empty", Toast.LENGTH_LONG).show();
            et_pass.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_mail.setError("Invalid Email Address");
            et_mail.requestFocus();
            return;
        }
        if (password.length() < 6) {
            et_pass.setError("Invalid Password");
            et_pass.requestFocus();
            return;
        }
        pDialog.setMessage("Signing You Up....");
        pDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    update_user();
                    pDialog.dismiss();
                    Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification();
                    Toast.makeText(Activity_signUP.this, "Verification Mail is sent. Please Verify Your Mail", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), SignIn_Activity.class);
                    startActivity(intent);
                } else {
                    pDialog.dismiss();
                    if (task.getException() instanceof FirebaseAuthUserCollisionException)
                        Toast.makeText(getApplicationContext(), "This Mail is Already Registered", Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void update_user() {
        Log.d("CAME","update_user");
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && profle_pic_url != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(user_name)
                    .setPhotoUri(Uri.parse(profle_pic_url))
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //Toast.makeText(getApplicationContext(), "updated Profile", Toast.LENGTH_LONG).show();
                        Log.d("CAME","updated_user");
                        uploadUserDatainDatabase();
                    }
                }
            });
        }
    }

    private void toaster(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private void uploadUserDatainDatabase() {
        Log.d("CAME","upload_data_user");
        FirebaseUser user_ = mAuth.getCurrentUser();
        if (user_ != null) {
            String uid = user_.getUid();
            if (!uid.isEmpty()) {
                databaseUser = FirebaseDatabase.getInstance().getReference("users");
                User_class user_now = new User_class(uid, user_name, email, address, phone1, phone2, profle_pic_url, cover_pic_url,password);
                try {
                    Log.d("CAME","uploading_data_user");
                    databaseUser.child(uid).setValue(user_now);
                } catch (Exception e) {
                    toaster(e.getMessage());
                }
            } else {
                toaster("UId Is Empty");
            }
        } else {
            toaster("USER is empty");
        }
        Log.d("CAME","uploaded_data_user");
        finish();
    }
    private void uploadImageInFirebaseStorage(String loc) {
        pDialog.setMessage("Uploading User Image");
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
                            profle_pic_url = image_url;
                        else
                            cover_pic_url = image_url;
       //                 Log.d("WHAT", image_url);
                    } else {
                        Toast.makeText(getApplicationContext(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
