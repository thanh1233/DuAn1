package com.example.apvexe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText username, pasword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        pasword = findViewById(R.id.pasword);
        getSupportActionBar().hide();
        findViewById(R.id.signup_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,SigUp.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickSingUp();
            }
        });
    }

    private void onclickSingUp() {
        mAuth = FirebaseAuth.getInstance();

        String emailDN = username.getText().toString().trim();
        String mauKhauDN = pasword.getText().toString().trim();

        Boolean checkError = true;
        if(emailDN.isEmpty()){
            username.setError("Email không được để trống!");
            checkError = false;
        }else if(!Pattern.matches("^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$", emailDN)){
            username.setError("Email sai định dạng!");
            checkError = false;
        }

        if(mauKhauDN.isEmpty()){
            pasword.setError("Mật khẩu không được để trống!");
            checkError = false;
        }

        if (checkError == true) {
            mAuth.signInWithEmailAndPassword(emailDN, mauKhauDN)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                SweetAlertDialog pDialog = new SweetAlertDialog(login.this, SweetAlertDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Loading ...");
                                pDialog.setCancelable(true);
                                pDialog.show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pDialog.dismiss();
                                        String name = user.getDisplayName();
                                        if (name ==  null || name.equals("")) {
                                            Intent introIntent = new Intent(login.this, Capnhatthongtin.class);
                                            startActivity(introIntent);
                                            finishAffinity();
                                        } else {
                                            startActivity(new Intent(login.this, TrangChu.class));
                                            finishAffinity();
                                        }

                                    }
                                }, 2000);

                            } else {
                                new SweetAlertDialog(login.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Sai tài khoản hoặc mật khẩu!")
                                        .show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return;
        } else {
            String name = currentUser.getDisplayName();

            if (name ==  null || name.equals("")) {
                Intent introIntent = new Intent(login.this, Capnhatthongtin.class);
                startActivity(introIntent);
                finishAffinity();
            } else {
                startActivity(new Intent(login.this, TrangChu.class));
                finishAffinity();
            }
        }



    }
}