package com.example.apvexe.fragmant;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apvexe.NhaXeAdapter;
import com.example.apvexe.R;
import com.example.apvexe.ThemXe;
import com.example.apvexe.Xe;
import com.example.apvexe.chuyencuatoi1;
import com.example.apvexe.hoadoncuatoi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class homefragmant extends Fragment {
    View view;
    private RecyclerView rcv;
    private String linkdatabase;
    private TextView  thanhtoancuatoi, timkiem;
    private DatabaseReference reference;
    private ArrayList<Xe> xeArrayList = new ArrayList<>();
    private NhaXeAdapter nhaXeAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fag_home,container,false);
        linkdatabase = getResources().getString(R.string.link_RealTime_Database);
        rcv = view.findViewById(R.id.rcv_NhaTro);
        timkiem = view.findViewById(R.id.timkiem);




        thanhtoancuatoi = view.findViewById(R.id.thanhtoancuatoi);
        thanhtoancuatoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), hoadoncuatoi.class));
            }
        });
        getLisviewDatabasefirebase("");


        nhaXeAdapter = new NhaXeAdapter();
        nhaXeAdapter = new NhaXeAdapter(getActivity(), R.layout.itemsanpham, xeArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcv.setLayoutManager(linearLayoutManager);
        xeArrayList = new ArrayList<>();
        nhaXeAdapter.setData(xeArrayList);
        rcv.setAdapter(nhaXeAdapter);

        timkiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null) {
                    xeArrayList.clear();
                    getLisviewDatabasefirebase(s.toString());
                } else {
                    xeArrayList.clear();
                    getLisviewDatabasefirebase("");
                }
            }
        });
        return view;
    }

    private void getLisviewDatabasefirebase(String key) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("Danhsachxe");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               Xe xe = snapshot.getValue(Xe.class);
                if (xe != null) {
                    if (xe.getTuyenchay().contains(key) || xe.getDiemden().contains(key)) {
                        xeArrayList.add(xe);
                    }
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
