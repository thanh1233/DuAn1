package com.example.apvexe;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.apvexe.fragmant.caiDat;
import com.example.apvexe.fragmant.homefragmant;
import com.example.apvexe.fragmant.nhantin;
import com.example.apvexe.fragmant.caiDat;
import com.example.apvexe.fragmant.homefragmant;
import com.example.apvexe.fragmant.nhantin;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class TrangChu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public String linkRealTime;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    FrameLayout frameLayout;
    private DatabaseReference reference;
    private BottomNavigationView bottomNavigationView;
    private  static final int FRAGMENT_HOME = 0;
    private  static final int FRAGMENT_NHANTIN = 1;
    private  static final int FRAGMENT_CAIDAT = 2;
    private int CurrenrFragent = FRAGMENT_HOME;
    private static final  int FRAGMENT_THONGTIN = 4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        linkRealTime = getResources().getString(R.string.link_RealTime_Database);
        //findViewById
        frameLayout = findViewById(R.id.frame_layout);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView gmailtext = (TextView) headerView.findViewById(R.id.gmailnguoidung);
        TextView tennguoidung = (TextView) headerView.findViewById(R.id.tennguoidung);
        ImageView avatar = (ImageView) headerView.findViewById(R.id.avatar);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        tennguoidung.setText(name);
        gmailtext.setText(email);
        Picasso.get().load(photoUrl).into(avatar);

        setBottomNavigationView();

        reference = FirebaseDatabase.getInstance(linkRealTime).getReference("users").child(user.getUid()).child("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ktadmin = dataSnapshot.child("0").getValue(String.class);
                if (ktadmin.equals("Admin")) {
                    replaceFragnent(new nhantin());
                    navigationView.setCheckedItem(R.id.btnthongke);
                    setTitle("Chuyến xe của bạn");
                    bottomNavigationView.getMenu().findItem(R.id.btnthongke).setChecked(true);
                } else {
                    replaceFragnent(new homefragmant());
                    navigationView.setCheckedItem(R.id.btnthongke);
                    setTitle("CHỌN XE");
                    bottomNavigationView.getMenu().findItem(R.id.btnthongke).setChecked(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.btnchitieu:
                        setTitle("NHẮN TIN");
                        replaceFragnent(new nhantin());
                        navigationView.setCheckedItem(R.id.btnchitieu);
                        CurrenrFragent = FRAGMENT_NHANTIN;
                        break;

                    case R.id.btnthongke:
                        setTitle("CHỌN XE");
                        replaceFragnent(new homefragmant());
                        navigationView.setCheckedItem(R.id.btnthongke);
                        CurrenrFragent = FRAGMENT_HOME;
                        break;

                    case R.id.btncaidat:
                        setTitle("CÀI ĐẶT");
                        replaceFragnent(new caiDat());
                        navigationView.setCheckedItem(R.id.btncaidat);
                        CurrenrFragent = FRAGMENT_CAIDAT;
                        break;
                }

                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {
       
    }

    private void  replaceFragnent(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.chonChuyen) {
            if (FRAGMENT_HOME != CurrenrFragent) {
                setTitle("CHỌN XE");
                replaceFragnent(new homefragmant());
                bottomNavigationView.getMenu().findItem(R.id.btnthongke).setChecked(true);
                CurrenrFragent = FRAGMENT_HOME;
            }
        } else if (id == R.id.nhanTin) {
            if (FRAGMENT_NHANTIN != CurrenrFragent) {
                setTitle("NHẮN TIN");
                replaceFragnent(new nhantin());
                bottomNavigationView.getMenu().findItem(R.id.btnchitieu).setChecked(true);
                CurrenrFragent = FRAGMENT_NHANTIN;

            }
        }
        else if (id == R.id.caiDat) {
            if (FRAGMENT_CAIDAT != CurrenrFragent) {
                setTitle("CÀI ĐẶT");
                replaceFragnent(new caiDat());
                bottomNavigationView.getMenu().findItem(R.id.btnthongke).setChecked(true);
                CurrenrFragent = FRAGMENT_CAIDAT;

            }
        }
//        else if (id == R.id.gioithieu) {
//            final ProgressDialog progressDialog = ProgressDialog.show(TrangChu.this,"Thông Báo","Đang tìm kiếm bản cập nhật...");
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    progressDialog.dismiss();
//                    Toast.makeText(TrangChu.this, "Chưa có bản cập nhật mới!", Toast.LENGTH_SHORT).show();
//                }
//            },2500);
//
//        }
//        else if (id == R.id.doimatkhau) {
//            if (FRAGMENT_DOIMK != currenFragment) {
//                setTitle("ĐỔI MẬT KHẨU");
//                Intent introIntent = new Intent(TrangChu.this, doiMatKhauActivity.class);
//                startActivity(introIntent);
//                currenFragment = FRAGMENT_DOIMK;
//            }
//        }
//        else if (id == R.id.intro) {
//            if (FRAGMENT_GIOITHIEU != currenFragment) {
//                setTitle("PHÒNG TRỌ ĐÃ ĐĂNG");
//                replaceFragment(new PhongTroCuaToi());
//                bottomNavigationView.getMenu().findItem(R.id.btnthongke).setChecked(false);
//                currenFragment = FRAGMENT_GIOITHIEU;
//
//            }
//        }
        else if (id == R.id.capnhat) {
                setTitle("THÔNG TIN NGƯỜI DÙNG");
                navigationView.setCheckedItem(R.id.capnhat);
                Intent introIntent = new Intent(TrangChu.this, Capnhatthongtin.class);
                startActivity(introIntent);

        }
//
//        else if (id == R.id.thongtin) {
//            setTitle("GIỚI THIỆU");
//            Intent introIntent = new Intent(TrangChu.this, thongTinApp.class);
//            startActivity(introIntent);
//        }
//
//        else if (id == R.id.phienban) {
//            Toast.makeText(TrangChu.this, "Phiên bản v1.0.1 mới nhất!", Toast.LENGTH_SHORT).show();
//        }
//
        else if (id == R.id.logout) {
            AlertDialog.Builder aBuilder = new AlertDialog.Builder(TrangChu.this);
            aBuilder.setMessage("Bạn muốn đăng xuất?");
            aBuilder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user == null) {
                        return;
                    }

                    final ProgressDialog progressDialog = ProgressDialog.show(TrangChu.this,"Thông Báo","Đang đăng xuất...");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(TrangChu.this, login.class));
                        }
                    },2500);
                }
            });
            //nút không
            aBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            aBuilder.show();
        }
//        else if (id == R.id.ghichu) {
//            if (FRAGMENT_GHICHU != currenFragment) {
//                setTitle("CÀI ĐẶT");
//                replaceFragment(new Setting_Fragment());
//                bottomNavigationView.getMenu().findItem(R.id.btncaidat).setChecked(true);
//                currenFragment = FRAGMENT_GHICHU;
//            }
//        }
//        else if (id == R.id.donggopykien) {
////            if (FRAGMENT_GOPY != currenFragment) {
////                setTitle("PHẢN HỒI NGƯỜI DÙNG");
////                replaceFragment(new PhanHoiNguoiDung());
////                currenFragment = FRAGMENT_GOPY;
////            }
//        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}