package com.example.apvexe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class SigUp extends AppCompatActivity {
    private TextInputLayout edtEmail,edtPassword,edtPassword2;
    private Button BtnSigUp,regToLoginBtn;
    private ProgressDialog diaLog;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private   CheckBox chk;
    public String linkdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sig_up);
        getSupportActionBar().hide();
        linkdatabase = getResources().getString(R.string.link_RealTime_Database);
       chk = (CheckBox) findViewById(R.id.chunhaxe);
        initui();
        inilistener();




    }

    private void inilistener() {
        BtnSigUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickSingUp();
            }
        });

    }

    private void onclickSingUp() {

        String strEmail = edtEmail.getEditText().getText().toString().trim();
        String strPass = edtPassword.getEditText().getText().toString().trim();
        String strPass2 = edtPassword2.getEditText().getText().toString().trim();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Boolean checkError = true;
        if(strEmail.isEmpty()) {
            edtEmail.setError("Email không được để trống");
            checkError = false;
        }
        else if (!Pattern.matches("^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$", strEmail)){
            edtEmail.setError("Email sai định dạng!");
            checkError = false;
        }
        if(strPass2.isEmpty()){
            edtPassword2.setError("Nhập lại mật khẩu không được bỏ trống!");
            checkError = false;
        }
        if(strPass.length()<6){
            edtPassword.setError("Mật khẩu quá yếu cần đủ 6 ký tự!");
            checkError = false;
        }
        if(!strPass2.equals(strPass)){
            edtPassword2.setError("Nhập lại mật khẩu không khớp!");
            checkError = false;
        }


        if(checkError) {
            boolean checked = chk.isChecked();
            // Check which checkbox was clicked
            if (checked) {
                auth.createUserWithEmailAndPassword(strEmail, strPass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    // Sign in success, update UI with the signed-in user's information
                                    ArrayList<String> admin = new ArrayList<>();
                                    admin.add("Admin");
                                    reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid());
                                    reference.child("User").setValue(admin);
                                    String name = user.getDisplayName();

                                    if (name ==  null || name.equals("")) {
                                        Intent introIntent = new Intent(SigUp.this, Capnhatthongtin.class);
                                        startActivity(introIntent);
                                        finishAffinity();
                                    } else {
                                        startActivity(new Intent(SigUp.this, MainActivity.class));
                                        finishAffinity();
                                    }

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(SigUp.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            } else {
                    auth.createUserWithEmailAndPassword(strEmail, strPass)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        // Sign in success, update UI with the signed-in user's information
                                        ArrayList<String> admin = new ArrayList<>();
                                        admin.add("");
                                        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid());
                                        reference.child("User").setValue(admin);
                                        String name = user.getDisplayName();

                                        if (name ==  null || name.equals("")) {
                                            Intent introIntent = new Intent(SigUp.this, Capnhatthongtin.class);
                                            startActivity(introIntent);
                                            finishAffinity();
                                        } else {
                                            startActivity(new Intent(SigUp.this, TrangChu.class));
                                            finishAffinity();
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.

                                        Toast.makeText(SigUp.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

            }
        }


    }

    private  void initui(){

        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_pasword);
        edtPassword2 = findViewById(R.id.edt_pasword2);
        BtnSigUp = findViewById(R.id.btn_sing_up);
        diaLog = new ProgressDialog(this);

    }

}