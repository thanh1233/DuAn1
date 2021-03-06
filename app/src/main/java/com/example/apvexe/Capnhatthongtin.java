package com.example.apvexe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Capnhatthongtin extends AppCompatActivity {
    private String linkdatabase;
    private String linkstorage = "gs://apvexe-6847e.appspot.com";
    private DatabaseReference reference;
    private EditText tilUsername, tilHoTen, tilMail, tilPhone, verysdt;
    private static final int REQUEST_IMAGE_OPEN = 2;
    private ImageView imgImageView;
    private Button guilai, hoantat, tieptuc;
    private LinearLayout linersendotp;
    private TextView xoatk, tilBirthday;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();
    private String textverificationId;
    FirebaseStorage storage = FirebaseStorage.getInstance(linkstorage);
    EditText autootp;
    //test
    final StorageReference storageRef = storage.getReference();
    Calendar calendar = Calendar.getInstance();
    StorageReference mountainsRef = storageRef.child("image"+ calendar.getTimeInMillis() +".png");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capnhatthongtin);
        mappingView();
        tieptuc.setVisibility(View.GONE);

        imgImageView.setEnabled(false);
                tilPhone.setEnabled(false);
        tilMail.setEnabled(false);
                tilHoTen.setEnabled(false);

        linkdatabase = getResources().getString(R.string.link_RealTime_Database);

        mappingView();
        tilBirthday = (TextView) findViewById(R.id.til_birtday_capNhatActivity);
        tilBirthday.setEnabled(false);
        tilBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonngay();
            }
        });

        findViewById(R.id.xoataikhoan).setOnClickListener(v -> {
            deleteUser();
        });
        hoantat.setEnabled(true);
        requestSMSPermission();
        doccode();
        imgImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_IMAGE_OPEN);
            }
        });

        findViewById(R.id.thoat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ToggleButton toggle = (ToggleButton) findViewById(R.id.update);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tieptuc.setVisibility(View.VISIBLE);
                    tilBirthday.setEnabled(true);
                    imgImageView.setEnabled(true);
                    tilPhone.setEnabled(true);
                    tilMail.setEnabled(true);
                    tilHoTen.setEnabled(true);
                } else {
                    tieptuc.setVisibility(View.GONE);
                    imgImageView.setEnabled(false);
                    tilPhone.setEnabled(false);
                    tilMail.setEnabled(false);
                    tilHoTen.setEnabled(false);
                    tilBirthday.setEnabled(false);
                }
            }
        });

        showAllUserData();


        findViewById(R.id.btn_ok_capNhatActivity).setOnClickListener(v -> {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                return;
            }
            reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("Profile");
            String numberphone = tilPhone.getText().toString().trim();
            Calendar calendar = Calendar.getInstance();
            String nameUser = tilHoTen.getText().toString().trim();
            String verifyphone =numberphone.substring(1,10) ;


            StorageReference mountainsRef = storageRef.child("image"+ calendar.getTimeInMillis() +".png");
            Boolean checkError = true;
            if(tilHoTen.getText().toString().trim().isEmpty()){
                tilHoTen.setError("T??n kh??ng ???????c ????? tr???ng");
                checkError = false;
            }

            if(!Pattern.matches("0\\d{3}\\d{3}\\d{3}", numberphone)){
                tilPhone.setError("S??? ??i???n tho???i sai ?????nh d???ng 0xxxxxxxxx");
                checkError = false;
            }

            if(tilBirthday.getText().toString().trim().isEmpty()){
                tilBirthday.setError("Ng??y sinh kh??ng ???????c ????? tr???ng");
                checkError = false;
            }
            if(tilPhone.getText().toString().trim().isEmpty()){
                tilPhone.setError("S??? ??i???n tho???i kh??ng ???????c ????? tr???ng");
                checkError = false;

            }

            if(checkError){
                tieptuc.setVisibility(View.GONE);
                hoantat.setVisibility(View.VISIBLE);
                linersendotp.setVisibility(View.VISIBLE);
                guilai.isEnabled();
                //get s??? ??t ????? g???i otp
                Toast.makeText(Capnhatthongtin.this, "+84"+verifyphone,Toast.LENGTH_SHORT).show();
                onClickVerifyPhone("+84"+verifyphone);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        new CountDownTimer(60000 + 100, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                guilai.setText("00:"+ millisUntilFinished/1000);
                            }

                            @Override
                            public void onFinish() {
                                guilai.setText("G???i l???i");
                                guilai.setEnabled(true);
                            }
                        }.start();

                    }
                },1000);

                hoantat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean checkError = true;
                        if(verysdt.getText().toString().trim().isEmpty()){
                            verysdt.setError("M?? OTP kh??ng ???????c ????? tr???ng");
                            checkError = false;

                        }

                        if(checkError) {
                            hoantat.setEnabled(false);
                            String OTPcode = verysdt.getText().toString().trim();
                            onClickSendOTPCode(OTPcode);
                        }
                    }
                });

                guilai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickSendOTPCodeAgain("+84"+verifyphone);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                new CountDownTimer(60000 + 100, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        guilai.setText("00:"+ millisUntilFinished/1000);
                                    }

                                    @Override
                                    public void onFinish() {
                                        guilai.setText("G???i l???i");
                                        guilai.setEnabled(true);
                                    }
                                }.start();

                            }
                        },1000);
                    }
                });
            }
        });

    }

    private void requestSMSPermission()
    {
        String permission = Manifest.permission.RECEIVE_SMS;

        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED)
        {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(Capnhatthongtin.this, permission_list,56);
        }
    }

    private void onClickSendOTPCodeAgain(String verifyphone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(verifyphone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                   // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Toast.makeText(Capnhatthongtin.this, "X??c th???c kh??ng th??nh c??ng",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                textverificationId = "";
                                textverificationId = verificationId;
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void doccode(){
        verysdt = (EditText) findViewById(R.id.nhapotp);
        new OTP_Receiver().setEditText(verysdt);
    }


    private void onClickVerifyPhone(String verifyphone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(verifyphone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                   // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Toast.makeText(Capnhatthongtin.this, "X??c th???c kh??ng th??nh c??ng",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                textverificationId = "";
                                textverificationId = verificationId;
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void onClickSendOTPCode(String otPcode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(textverificationId, otPcode);
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("??ang upload file...");
        pDialog.setCancelable(false);
        pDialog.show();
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
        String nameUser = tilHoTen.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        Users users = new Users();
        users.setBirthday(tilBirthday.getText().toString().trim());
        users.setNumberphone(tilPhone.getText().toString().trim());
        reference.setValue(users);
        // Get the data from an ImageView as bytes
        imgImageView.setDrawingCacheEnabled(true);
        imgImageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(Capnhatthongtin.this, "L???i!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nameUser)
                                .setPhotoUri(Uri.parse(String.valueOf(uri)))
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    startActivity(new Intent(Capnhatthongtin.this, TrangChu.class));
                                                    finishAffinity();
                                                }
                                            },1000);
                                        }
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        });
//                        } else {
//
////                        }
//                    }
//                });

    }





    public void deleteUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(Capnhatthongtin.this);
        aBuilder.setMessage("L??u ??: Sau khi x??c nh???n x??a t??i kho???n th?? b???n s??? kh??ng th??? kh??i ph???c l???i t??i kho???n. B???n ch???c ch????");
        aBuilder.setTitle("X??a t??i kho???n");
        aBuilder.setPositiveButton("?????ng ??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                final ProgressDialog progressDialog = ProgressDialog.show(Capnhatthongtin.this,"Th??ng B??o","??ang ????ng xu???t...");
                Handler handler = new Handler();
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users");
                                    reference.child(user.getUid()).removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull  DatabaseReference ref) {

                                        }
                                    });
                                }
                            }
                        });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(Capnhatthongtin.this,"???? x??a th??nh c??ng",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Capnhatthongtin.this, login.class));
                        finishAffinity();
                    }
                },3000);
            }
        });
        //n??t kh??ng
        aBuilder.setNegativeButton("H???y", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        aBuilder.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            Uri full = data.getData();
            ImageView imgv = findViewById(R.id.imgavatar);
            imgv.setImageURI(full);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showAllUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();
        tilHoTen.setText(name);
        tilMail.setText(email);
        if (photoUrl != null) {
            Picasso.get().load(photoUrl).into(imgImageView);
        }

        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("Profile");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String birthdayFromDB = dataSnapshot.child("birthday").getValue(String.class);
                String phoneFromDB = dataSnapshot.child("numberphone").getValue(String.class);

                tilBirthday.setText(birthdayFromDB);
                tilPhone.setText(phoneFromDB);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void chonngay() {
        Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DAY_OF_MONTH);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                tilBirthday.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam,thang,ngay);
        datePickerDialog.show();
    }

    private void mappingView() {

        xoatk = (TextView) findViewById(R.id.xoataikhoan);
        imgImageView = (ImageView) findViewById(R.id.imgavatar);
        tilHoTen = (EditText)  findViewById(R.id.til_hoTen_capNhatActivity);
        tilMail = findViewById(R.id.til_mail_capNhatActivity);
        tilPhone = (EditText)  findViewById(R.id.til_phone_capNhatActivity);
        hoantat = (Button) findViewById(R.id.hoantat);
        tieptuc = (Button) findViewById(R.id.btn_ok_capNhatActivity);
        guilai = (Button) findViewById(R.id.guilai);
        verysdt = (EditText) findViewById(R.id.nhapotp);
        linersendotp = (LinearLayout) findViewById(R.id.linersendotp);

    }

    @Override
    public void onBackPressed() {
        Boolean checkError = true;

        if(tilPhone.getText().toString().trim().isEmpty()){
            tilPhone.setError("S??? ??i???n tho???i kh??ng ???????c ????? tr???ng");
            checkError = false;
        }

        if(tilBirthday.getText().toString().trim().isEmpty()){
            tilBirthday.setError("Ng??y sinh kh??ng ???????c ????? tr???ng");
            checkError = false;
        }
        if (checkError) {
            startActivity(new Intent(Capnhatthongtin.this, MainActivity.class));
            finishAffinity();
        }
    }
}