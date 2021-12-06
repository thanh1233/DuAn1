package com.example.apvexe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apvexe.Hoadon;
import com.example.apvexe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HoadonAdapter extends RecyclerView.Adapter<HoadonAdapter.ViewHolder>{
    private String linkdatabase;
    private Context context;
    private DatabaseReference reference;
    public List<Hoadon> list;
    private int layout;
    private SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    private NumberFormat fm = new DecimalFormat("#,###");
    boolean isDark = false;

    public HoadonAdapter() {
    }

    public void setData(List<Hoadon> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public HoadonAdapter(Context context, ArrayList<Hoadon> list, Boolean isDark) {
        this.context = context;
        this.list = list;
        this.isDark = isDark;
    }


    public HoadonAdapter(Context context, ArrayList<Hoadon> list) {
        this.context = context;
        this.list = list;

    }
    public HoadonAdapter(Context context, int layout, ArrayList<Hoadon> list) {
        this.context = context;
        this.list = list;
        this.layout=layout;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tenhoadown, chuyen, sovene, ngaytt, tongtien,adminduyet,chuaduyet,daduyet;

        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ngaytt = itemView.findViewById(R.id.diachi2);
            tongtien = itemView.findViewById(R.id.diachi3);
            tenhoadown = itemView.findViewById(R.id.tenhoadown);
            chuyen = itemView.findViewById(R.id.chuyen);
            sovene = itemView.findViewById(R.id.sovene);
            adminduyet = itemView.findViewById(R.id.adminduyet);
            chuaduyet = itemView.findViewById(R.id.chuaduyet);
            daduyet = itemView.findViewById(R.id.daduyet);

        }


    }

    @NonNull
    @Override
    public HoadonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new HoadonAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoadonAdapter.ViewHolder holder, final int position) {
        linkdatabase = context.getString(R.string.link_RealTime_Database);
        final Hoadon hoadon = list.get(position);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        reference = FirebaseDatabase.getInstance(linkdatabase).getReference("users").child(user.getUid()).child("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ktadmin = dataSnapshot.child("0").getValue(String.class);
                if (ktadmin.equals("")) {
                    holder.adminduyet.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (hoadon.getTinhtrang().equals("false")) {
            holder.chuaduyet.setVisibility(View.VISIBLE);
            holder.daduyet.setVisibility(View.GONE);
        }

        if (hoadon.getTinhtrang().equals("true")) {
            holder.chuaduyet.setVisibility(View.GONE);
            holder.daduyet.setVisibility(View.VISIBLE);
        }


        if (hoadon == null) {
            return;
        }
        holder.tenhoadown.setText(hoadon.getTenxe());
        holder.chuyen.setText("Tuyến: "+hoadon.getTuyen()+" - "+hoadon.getDich());
        holder.sovene.setText("Số vé: "+hoadon.getSoluongve());
        holder.ngaytt.setText("Ngày thanh toán: "+hoadon.getNgaymua());
        holder.tongtien.setText("Tổng: "+hoadon.getTongtien());

        holder.adminduyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance(linkdatabase).getReference("Hoadon").child(hoadon.getIdhd()).child("tinhtrang");
                reference.setValue("true");
            }
        });


    }


    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }
    
}
