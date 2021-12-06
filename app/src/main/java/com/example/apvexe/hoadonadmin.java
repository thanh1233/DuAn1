package com.example.apvexe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class hoadonadmin extends AppCompatActivity {
    RecyclerView rcvHoaDon;
    private String linkdatabase;
    private DatabaseReference reference;
    private ArrayList<Hoadon> hoadonArrayList = new ArrayList<>();
    private HoadonAdapter nhaXeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoadonadmin);
        linkdatabase = getResources().getString(R.string.link_RealTime_Database);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        getSupportActionBar().setTitle("Duyệt hóa đơn");


        rcvHoaDon = findViewById(R.id.rcvHoaDon);
        getLisviewDatabasefirebase();

        nhaXeAdapter = new HoadonAdapter();
        nhaXeAdapter = new HoadonAdapter(hoadonadmin.this, R.layout.itemhoadon, hoadonArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(hoadonadmin.this);
        rcvHoaDon.setLayoutManager(linearLayoutManager);
        hoadonArrayList = new ArrayList<>();
        nhaXeAdapter.setData(hoadonArrayList);
        rcvHoaDon.setAdapter(nhaXeAdapter);
    }

    private void getLisviewDatabasefirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("Hoadon");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Hoadon xe = snapshot.getValue(Hoadon.class);
                if (xe.getIdnhaxe().equals(user.getUid())) {
                    hoadonArrayList.add(xe);
                    nhaXeAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(@NonNull   DataSnapshot snapshot, @Nullable   String previousChildName) {
                Hoadon xe = snapshot.getValue(Hoadon.class);
                if (xe.getIdnhaxe().equals(user.getUid())) {
                    if (xe == null || hoadonArrayList == null || hoadonArrayList.isEmpty()) {
                        return;
                    }
                    for (int i = 0; i < hoadonArrayList.size(); i++) {
                        if (xe.getIdhd() == hoadonArrayList.get(i).getIdhd()) {
                            hoadonArrayList.set(i, xe);
                            break;
                        }
                    }
                    nhaXeAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) {

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