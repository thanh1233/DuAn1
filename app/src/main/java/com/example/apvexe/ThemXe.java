package com.example.apvexe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ThemXe extends AppCompatActivity {
    private String linkdatabase;
    private String linkstorage = "gs://apvexe-6847e.appspot.com";
    private DatabaseReference reference;
    private EditText tilUsername, tilHoTen, tilMail, tilMail2, tilPhone, verysdt, gia, soluong;
    private static final int REQUEST_IMAGE_OPEN = 2;
    private ImageView imgImageView;
    private Button guilai, hoantat, tieptuc;
    private LinearLayout linersendotp;
    private TextView xoatk, tilBirthday, gio;
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
        setContentView(R.layout.activity_them_xe);
        mappingView();
        linkdatabase = getResources().getString(R.string.link_RealTime_Database);

        tilBirthday = (TextView) findViewById(R.id.til_birtday_capNhatActivity);
        tilBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonngay();
            }
        });
        showAllUserData();
        gio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
chongio();
            }
        });

        imgImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_IMAGE_OPEN);
            }
        });


        findViewById(R.id.btn_ok_capNhatActivity).setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                return;
            }


            Boolean checkError = true;
            if(tilHoTen.getText().toString().trim().isEmpty()){
                tilHoTen.setError("Tên không được để trống");
                checkError = false;
            }


            if(tilMail.getText().toString().trim().isEmpty()){
                tilMail.setError("Không được để trống");
                checkError = false;
            }

            if(tilMail2.getText().toString().trim().isEmpty()){
                tilMail2.setError("Không được để trống");
                checkError = false;
            }

            if(gia.getText().toString().trim().isEmpty()){
                gia.setError("Không được để trống");
                checkError = false;
            }

            if(soluong.getText().toString().trim().isEmpty()){
                soluong.setError("Không được để trống");
                checkError = false;
            }

            if(tilBirthday.getText().toString().trim().isEmpty()){
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Chưa chọn ngày khởi hành")
                        .show();
                checkError = false;
            }

            if(gio.getText().toString().trim().isEmpty()){
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Chưa chọn giờ khởi hành")
                        .show();
                checkError = false;
            }

            if(checkError){
                SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Đang thêm...");
                pDialog.setCancelable(false);
                pDialog.show();

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
                        Toast.makeText(ThemXe.this, "Lỗi!!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Calendar calendar = Calendar.getInstance();
                                Xe xe = new Xe();
                                xe.setId(""+ calendar.getTimeInMillis());
                                xe.setTen(tilHoTen.getText().toString().trim());
                                xe.setDiemden(tilMail2.getText().toString().trim());
                                xe.setTuyenchay(tilMail.getText().toString().trim());
                                xe.setGiave(Integer.parseInt(gia.getText().toString().trim()));
                                xe.setSoluongve(Integer.parseInt(soluong.getText().toString().trim()));
                                xe.setNgaykhoihanh(tilBirthday.getText().toString().trim());
                                xe.setGiokhoihanh(gio.getText().toString().trim());
                                xe.setSdt(tilPhone.getText().toString().trim());
                                xe.setAnh(uri.toString().trim());
                                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("danhsachxecuatoi");
                                reference.child(""+ calendar.getTimeInMillis()).setValue(xe);
                                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("Danhsachxe");
                                reference.child(""+ calendar.getTimeInMillis()).setValue(xe);
                                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("danhsachxecuatoi").child(""+ calendar.getTimeInMillis());
                                reference.child("idnhaxe").setValue(user.getUid());
                                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("Danhsachxe").child(""+ calendar.getTimeInMillis());
                                reference.child("idnhaxe").setValue(user.getUid());
                                startActivity(new Intent(ThemXe.this, TrangChu.class));
                                finishAffinity();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }
                });

            }

        });






    }


    private void showAllUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("Profile");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phoneFromDB = dataSnapshot.child("numberphone").getValue(String.class);
                tilPhone.setText(phoneFromDB);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

private void chongio() {
    // Get Current Time
    final Calendar c = Calendar.getInstance();
    int mHour = c.get(Calendar.HOUR_OF_DAY);
    int mMinute = c.get(Calendar.MINUTE);

    // Launch Time Picker Dialog
    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay,
                                      int minute) {

                    gio.setText(hourOfDay + ":" + minute);
                }
            }, mHour, mMinute, false);
    timePickerDialog.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            Uri full = data.getData();
            ImageView imgv = findViewById(R.id.imgavatar);
            imgv.setImageURI(full);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void mappingView() {
        gio = findViewById(R.id.gio);
        xoatk = (TextView) findViewById(R.id.xoataikhoan);
        imgImageView = (ImageView) findViewById(R.id.imgavatar);
        tilHoTen = (EditText)  findViewById(R.id.til_hoTen_capNhatActivity);
        gia = (EditText)  findViewById(R.id.gia);
        soluong = (EditText)  findViewById(R.id.soluong);
        tilMail = findViewById(R.id.til_mail_capNhatActivity);
        tilMail2 = findViewById(R.id.til_mail2_capNhatActivity);
        tilPhone = (EditText)  findViewById(R.id.til_phone_capNhatActivity);
        hoantat = (Button) findViewById(R.id.hoantat);
        tieptuc = (Button) findViewById(R.id.btn_ok_capNhatActivity);
        guilai = (Button) findViewById(R.id.guilai);
        verysdt = (EditText) findViewById(R.id.nhapotp);
        linersendotp = (LinearLayout) findViewById(R.id.linersendotp);

    }
}