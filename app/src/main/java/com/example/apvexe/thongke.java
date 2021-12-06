package com.example.apvexe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class thongke extends AppCompatActivity {
    PieChart anyChartView;
    TextView sove, sochuyen, soban;
    private String linkdatabase;
    private ArrayList<Xe> xeArrayList = new ArrayList<>();
    private ArrayList<Hoadon> hoadons = new ArrayList<>();
    private DatabaseReference reference;
    int Tongve=0;
    int Tongban=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke);
        anyChartView = findViewById(R.id.bieudo);
        sove = findViewById(R.id.textView9);
        sochuyen = findViewById(R.id.textView10);
        linkdatabase = getResources().getString(R.string.link_RealTime_Database);
        soban = findViewById(R.id.textView11);
        getSupportActionBar().hide();
        findViewById(R.id.thoat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Tongve = 0;
        Tongban=0;
        sochuyen.setText("Số chuyến: 0 chuyến");
        sove.setText("Còn lại: 0 vé");
        soban.setText("Đã bán: 0 vé");


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
                    Tongve = Tongve + xe.getSoluongve();
                    sochuyen.setText("Số chuyến: "+xeArrayList.size()+" chuyến");
                    sove.setText("Còn lại: "+Tongve+" vé");
                }

            }

            @Override
            public void onChildChanged(@NonNull   DataSnapshot snapshot, @Nullable   String previousChildName) {

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

        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("Hoadon");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Hoadon hoadon = snapshot.getValue(Hoadon.class);
                if (hoadon.getIdnhaxe().equals(user.getUid())) {
                    hoadons.add(hoadon);
                    Tongban = Tongban + Integer.parseInt(hoadon.getSoluongve());
                    soban.setText("Đã bán: "+Tongban+" vé");
                    bieudotron(Tongve,xeArrayList.size(),Tongban);

                }

            }

            @Override
            public void onChildChanged(@NonNull   DataSnapshot snapshot, @Nullable   String previousChildName) {

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

    public void  bieudotron(int tongChi, int tongThu, int tong) {
        int time[] = {tongChi,tongThu,tong};
        String activity[] ={"Tổng vé","Tổng chuyến","Tổng bán"};
        List<PieEntry> pieEntires = new ArrayList<>();
        for( int i = 0 ; i<time.length;i++){
            pieEntires.add(new PieEntry(time[i],activity[i]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntires,"");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(10);
        dataSet.setValueTextColor(Color.rgb(255,255,255));
        anyChartView.setData(data);
        anyChartView.setCenterTextSize(15);
        anyChartView.setCenterTextColor(Color.rgb(62,68,103));
        anyChartView.setCenterText("NHÀ XE");
        anyChartView.setDrawHoleEnabled (true);
        anyChartView.setHoleRadius(43);
        anyChartView.getDescription().setEnabled(false);
        anyChartView.animateXY(1000, 1000);
        anyChartView.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String sales = String.valueOf(e.getY());
                Toast.makeText(thongke.this,"Giá trị là "+sales,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });


    }
}