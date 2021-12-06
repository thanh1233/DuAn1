package com.example.apvexe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class chuyencuatoi1 extends AppCompatActivity {
    private RecyclerView rcv;
    private String linkdatabase;
    private DatabaseReference reference;
    private ArrayList<Xe> xeArrayList = new ArrayList<>();
    private NhaXeCuaToiAdapter nhaXeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuyencuatoi1);
        findViewById(R.id.themxe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(chuyencuatoi1.this, ThemXe.class));
            }
        });
        linkdatabase = getResources().getString(R.string.link_RealTime_Database);
        rcv = findViewById(R.id.rcv_NhaTro);
        getSupportActionBar().setTitle("Danh sách chuyến của tôi");
        getLisviewDatabasefirebase("");

        findViewById(R.id.thongke).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(chuyencuatoi1.this, thongke.class));
            }
        });

        findViewById(R.id.duyetve).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(chuyencuatoi1.this, hoadonadmin.class));
            }
        });


        nhaXeAdapter = new NhaXeCuaToiAdapter();
        nhaXeAdapter = new NhaXeCuaToiAdapter(chuyencuatoi1.this, R.layout.itemsanpham, xeArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(chuyencuatoi1.this);
        rcv.setLayoutManager(linearLayoutManager);
        xeArrayList = new ArrayList<>();
        nhaXeAdapter.setData(xeArrayList);
        rcv.setAdapter(nhaXeAdapter);
    }

    private void getLisviewDatabasefirebase(String key) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("danhsachxecuatoi");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Xe xe = snapshot.getValue(Xe.class);
                if (xe != null) {
                    xeArrayList.add(xe);
                    nhaXeAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull   DataSnapshot snapshot, @Nullable   String previousChildName) {
                Xe xe = snapshot.getValue(Xe.class);
                if (xe == null || xeArrayList == null || xeArrayList.isEmpty()) {
                    return;
                }
                for (int i = 0; i < xeArrayList.size(); i++) {
                    if (xe.getId() == xeArrayList.get(i).getId()) {
                        xeArrayList.set(i, xe);
                        break;
                    }
                }
                nhaXeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) {
                Xe xe = snapshot.getValue(Xe.class);
                if (xe == null || xeArrayList == null || xeArrayList.isEmpty()) {
                    return;
                }
                for (int i = 0; i < xeArrayList.size(); i++) {
                    if (xe.getId() == xeArrayList.get(i).getId()) {
                        xeArrayList.remove(xeArrayList.get(i));
                        break;
                    }
                }
                nhaXeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull   DataSnapshot snapshot, @Nullable   String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}