package com.example.apvexe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.apvexe.model.PaymentInfo;
//import com.example.apvexe.service.PaymentService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;

public class Xemthongtin extends AppCompatActivity {
    private TextView tent, gia, thoigian, chuyen, thanhtoan, lienhe, datcoc, sdt, tento;
    ImageView imgavatar;
    private String linkdatabase;
    private DatabaseReference reference;
    int soluongve = 1;
    int tong = 0;
    int Tongsove=0;
    private NumberFormat fm = new DecimalFormat("#,###");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xemthongtin);
        getSupportActionBar().setTitle("Th??ng tin chuy???n");
        clickrequest();
        tent = findViewById(R.id.tennhaxe);
        gia = findViewById(R.id.giatien);
        thoigian = findViewById(R.id.ngaygio);
        chuyen = findViewById(R.id.chuyendi);
        thanhtoan = findViewById(R.id.thanhtoan);
        lienhe = findViewById(R.id.goi);
        datcoc = findViewById(R.id.coc);
        sdt = findViewById(R.id.sddt);
        imgavatar = findViewById(R.id.imgavatar);
        tento = findViewById(R.id.til_hoTen_capNhatActivity);
        linkdatabase = getResources().getString(R.string.link_RealTime_Database);
        Intent intent = getIntent();
        String value1 = intent.getStringExtra("IDPHONGTRO");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        lienhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("Danhsachxe").child(value1);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String sdta = dataSnapshot.child("sdt").getValue(String.class);

                        AlertDialog.Builder aBuilder = new AlertDialog.Builder(v.getContext());
                        aBuilder.setMessage("S??? g???i ??i???n ?????n "+ sdta);
                        aBuilder.setPositiveButton("G???i", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String number = sdta;
                                if (number.trim().length() > 0) {
                                    String dial = "tel:" + number;
                                    v.getContext().startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

                                } else {
                                    Toast.makeText(Xemthongtin.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        aBuilder.setNegativeButton("H???y b???", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        aBuilder.show();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("Danhsachxe").child(value1);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String diemden = dataSnapshot.child("diemden").getValue(String.class);
                String ten = dataSnapshot.child("ten").getValue(String.class);
                String ngaykhoihanh = dataSnapshot.child("ngaykhoihanh").getValue(String.class);
                String giokhoihanh = dataSnapshot.child("giokhoihanh").getValue(String.class);
                int soLuong = dataSnapshot.child("soluongve").getValue(Integer.class);
                String tuyenchay = dataSnapshot.child("tuyenchay").getValue(String.class);
                int giave = dataSnapshot.child("giave").getValue(Integer.class);
                String sdta = dataSnapshot.child("sdt").getValue(String.class);
                String linkHanhDB = dataSnapshot.child("anh").getValue(String.class);
                Picasso.get().load(linkHanhDB).into(imgavatar);
                tento.setText(ten);
                tent.setText("?????i l?? b??n v??:  "+ten);
                gia.setText("Gi?? v??:  "+fm.format(giave)+ " VND        S??? l?????ng v??:  "+String.valueOf(soLuong) );
                thoigian.setText("Kh???i h??nh l??c:  " + giokhoihanh+ "p  --  Ng??y: "+ ngaykhoihanh );
                chuyen.setText("Tuy???n:  "+tuyenchay+"   -->   "+diemden);
                sdt.setText("Hotline:  "+sdta);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Xemthongtin.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.thanhtoan);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                TextView tennhaxe, tuyen, ngaydi,tongso,idnhaxe, giaven, tenkhach, ngaysinh, sdt, next, unnext, tongtien, soluong, thanhtoan, huy;
                tennhaxe = dialog.findViewById(R.id.tenhaxea);
                tuyen = dialog.findViewById(R.id.tuyen);
                ngaydi = dialog.findViewById(R.id.ngay);
                giaven = dialog.findViewById(R.id.gia);
                tenkhach = dialog.findViewById(R.id.textView16);
                ngaysinh = dialog.findViewById(R.id.textView17);
                sdt = dialog.findViewById(R.id.textView18);
                next = dialog.findViewById(R.id.next);
                unnext = dialog.findViewById(R.id.unnexxt);
                tongtien = dialog.findViewById(R.id.tong);
                soluong = dialog.findViewById(R.id.soluong);
                thanhtoan = dialog.findViewById(R.id.thanhtoanngay);
                huy = dialog.findViewById(R.id.huybo);
                tongso = dialog.findViewById(R.id.tongso);
                idnhaxe = dialog.findViewById(R.id.idnhaxe);
                soluongve = 1;
                tong=0;
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    return;
                }

                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("Profile");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String ngaysinhkhach = dataSnapshot.child("birthday").getValue(String.class);
                        String sdtkhach = dataSnapshot.child("numberphone").getValue(String.class);
                        ngaysinh.setText("Ng??y sinh: "+ngaysinhkhach);
                        sdt.setText("S??? ??i???n tho???i: "+sdtkhach);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                tenkhach.setText("T??n kh??ch h??ng: "+user.getDisplayName());


                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("Danhsachxe").child(value1);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String diemden = dataSnapshot.child("diemden").getValue(String.class);
                        String ten = dataSnapshot.child("ten").getValue(String.class);
                        String ngaykhoihanh = dataSnapshot.child("ngaykhoihanh").getValue(String.class);
                        String giokhoihanh = dataSnapshot.child("giokhoihanh").getValue(String.class);
                        int soLuong = dataSnapshot.child("soluongve").getValue(Integer.class);
                        String tuyenchay = dataSnapshot.child("tuyenchay").getValue(String.class);
                        int giave = dataSnapshot.child("giave").getValue(Integer.class);
                        String sdta = dataSnapshot.child("sdt").getValue(String.class);
                        String linkHanhDB = dataSnapshot.child("anh").getValue(String.class);
                        Picasso.get().load(linkHanhDB).into(imgavatar);
                        tennhaxe.setText("T??n nh?? xe: "+ten);
                        tuyen.setText("Tuy???n: "+tuyenchay+"    ?????n    "+diemden);
                        ngaydi.setText("Ng??y kh???i h??nh:  " + ngaykhoihanh+ " v??o l??c:  "+ giokhoihanh );
                        giaven.setText("Gi?? v??:  "+fm.format(giave)+ " VND ");
                        tong = giave;
                        tongso.setText(""+soLuong);
                        tongtien.setText(""+fm.format(tong)+" VND ");

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                soluong.setText(""+ soluongve);
                huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Integer.parseInt(tongso.getText().toString().trim()) == 0) {
                            tongtien.setText("H???t v??!");
                            Toast.makeText(getApplicationContext(), "H???t v??", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (soluongve == 1) {
                            Toast.makeText(getApplicationContext(), "?????t gi???i h???n nh??? nh???t", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        soluongve--;
                        soluong.setText(""+ soluongve);
                        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("Danhsachxe").child(value1);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int giave = dataSnapshot.child("giave").getValue(Integer.class);
                                tong = tong - giave;
                                tongtien.setText(""+fm.format(tong)+" VND ");
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

                unnext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Integer.parseInt(tongso.getText().toString().trim()) == 0) {
                            tongtien.setText("H???t v??!");
                            Toast.makeText(getApplicationContext(), "H???t v??", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (soluongve == Integer.parseInt(tongso.getText().toString().trim())) {
                            Toast.makeText(getApplicationContext(), "?????t gi???i h???n l???n nh???t", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        soluongve++;
                        soluong.setText(""+ soluongve);
                        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("Danhsachxe").child(value1);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int giave = dataSnapshot.child("giave").getValue(Integer.class);
                                tong = tong + giave;
                                tongtien.setText(""+fm.format(tong)+" VND ");
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });




                //ph???n thanh to??n ????y n?? @ - @
                thanhtoan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                       cd





                        if (Integer.parseInt(tongso.getText().toString().trim()) == 0) {
                            tongtien.setText("H???t v??!");
                            Toast.makeText(getApplicationContext(), "V?? ???? h???t kh??ng th??? thanh to??n", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Tongsove = 0;
                        Calendar calendar = Calendar.getInstance();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user == null) {
                            return;
                        }

                        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("Danhsachxe").child(value1);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String id = dataSnapshot.child("idnhaxe").getValue(String.class);
                                int soLuong = dataSnapshot.child("soluongve").getValue(Integer.class);
                                String iudnhaxe = dataSnapshot.child("idnhaxe").getValue(String.class);
                                String diaiem = dataSnapshot.child("diemden").getValue(String.class);
                                String tuyen = dataSnapshot.child("tuyenchay").getValue(String.class);
                                String tennhaxe = dataSnapshot.child("ten").getValue(String.class);
                                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                                Hoadon hoadon = new Hoadon();
                                hoadon.setIdhd(""+ calendar.getTimeInMillis());
                                hoadon.setIdkhach(user.getUid());
                                hoadon.setIdnhaxe(id);
                                hoadon.setTenkh(user.getDisplayName());
                                hoadon.setTongtien(tongtien.getText().toString().trim());
                                hoadon.setSoluongve(soluong.getText().toString().trim());
                                hoadon.setTinhtrang("false");
                                hoadon.setNgaymua(currentDate);
                                hoadon.setTenxe(tennhaxe);
                                hoadon.setDich(diaiem);
                                hoadon.setTuyen(tuyen);
                                Tongsove = soLuong - Integer.parseInt(soluong.getText().toString());
                                tongso.setText(""+Tongsove);
                                idnhaxe.setText(iudnhaxe);

                                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("Hoadon");
                                reference.child(""+ calendar.getTimeInMillis()).setValue(hoadon);

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        SweetAlertDialog pDialog = new SweetAlertDialog(Xemthongtin.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("??ang thanh to??n..");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pDialog.dismiss();
                                dialog.dismiss();
                                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("Danhsachxe").child(value1).child("soluongve");
                                reference.setValue(Integer.parseInt(tongso.getText().toString().trim()));
                                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(""+idnhaxe.getText().toString().trim()).child("danhsachxecuatoi").child(value1).child("soluongve");
                                reference.setValue(Integer.parseInt(tongso.getText().toString().trim()));

                            }
                        },3000);


                    }
                });



                dialog.show();
            }
        });


    }
    private void clickrequest() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return;
        }
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("N???u b???n kh??ng cho ph??p ch???c n??ng g???i ??i???n kh??ng th??? s??? d???ng ???????c!\n \nN???u b???n mu???n b???t l???i n?? th?? h??y v??o ph???n [Setting] > [Quy???n truy c???p] > [B???t c??c quy???n truy c???p]")
                .setPermissions(Manifest.permission.CALL_PHONE)
                .check();
    }
}

