package com.sabertooth.kuettrade;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignIn_Activity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    EditText mail, password;
    Button showpass, signin;
    ProgressBar pgg;
    static FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            finish();
            Intent intent = new Intent(getApplicationContext(), store_and_user_nav_settings.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(SignIn_Activity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_);
        ActivityCompat.requestPermissions(SignIn_Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        auth = FirebaseAuth.getInstance();
        TextView tvSignUp = findViewById(R.id.text_view_signUP);
        pgg = findViewById(R.id.progress_bar_signin);
        mail = findViewById(R.id.edit_text_sign_in_mail);
        password = findViewById(R.id.edit_text_sign_in_pass);
        showpass = findViewById(R.id.button_sign_in_show_password);
        signin = findViewById(R.id.button_sign_in_button);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent it = new Intent(getApplicationContext(), Activity_signUP.class);
                startActivity(it);
            }
        });
        showpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getInputType() == 129) {
                    password.setInputType(1);
                    showpass.setText("Hide");
                } else {
                    password.setInputType(129);
                    showpass.setText("Show");
                }
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin() {
        String email, pass;
        email = mail.getText().toString();
        pass = password.getText().toString();
        if (email.isEmpty()) {
            mail.setError("Email Shouldn't be Empty");
            mail.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            password.setError("Email Shouldn't be Empty");
            password.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mail.setError("Invalid Email Address");
            mail.requestFocus();
            return;
        }
        if (password.length() < 6) {
            password.setError("Invalid Password");
            password.requestFocus();
            return;
        }
        pgg.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                pgg.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    Toast.makeText(getApplicationContext(), "User Login Successfull", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), store_and_user_nav_settings.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "This Mail is Already Registered", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
