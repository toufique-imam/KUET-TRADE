package com.sabertooth.kuettrade;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Objects;

public class Activity_signUP extends AppCompatActivity {
    EditText et_pass, et_mail;
    Button bt_action, bt_show;
    private FirebaseAuth mAuth;
    ProgressBar pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        bt_action = findViewById(R.id.button_sign_up_button);
        et_pass = findViewById(R.id.edit_text_sign_up_pass);
        et_mail = findViewById(R.id.edit_text_sign_up_mail);
        bt_show = findViewById(R.id.button_sign_up_show_password);
        pg = findViewById(R.id.progress_bar_signup);
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
                register_user();
            }
        });
    }

    private void register_user() {
        String email = et_mail.getText().toString().trim(), password = et_pass.getText().toString().trim();
        if (email.isEmpty()) {
            et_mail.setError("Email Shouldn't be Empty");
            et_mail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            et_pass.setError("Email Shouldn't be Empty");
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
        pg.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                pg.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    Intent intent=new Intent(getApplicationContext(),store_and_user_nav_settings.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "User Regestered Successfull", Toast.LENGTH_LONG).show();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException)
                        Toast.makeText(getApplicationContext(), "This Mail is Already Registered", Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
