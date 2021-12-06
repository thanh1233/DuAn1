package com.example.apvexe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NhaXeCuaToiAdapter extends RecyclerView.Adapter<NhaXeCuaToiAdapter.ViewHolder> {

    private String linkdatabase;
    private Context context;
    private DatabaseReference reference;
    public  List<Xe> list;
    private int layout;
    private SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    private NumberFormat fm = new DecimalFormat("#,###");
    boolean isDark = false;

    public NhaXeCuaToiAdapter() {
    }

    public void setData(List<Xe> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public NhaXeCuaToiAdapter(Context context, ArrayList<Xe> list, Boolean isDark) {
        this.context = context;
        this.list = list;
        this.isDark = isDark;
    }


    public NhaXeCuaToiAdapter(Context context, ArrayList<Xe> list) {
        this.context = context;
        this.list = list;

    }
    public NhaXeCuaToiAdapter(Context context, int layout, ArrayList<Xe> list) {
        this.context = context;
        this.list = list;
        this.layout=layout;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tenmenu, diachi, giatien, chuyen;
        private ImageView hinhmenu;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenmenu = itemView.findViewById(R.id.tenmenu);
            diachi = itemView.findViewById(R.id.diachi);
            giatien = itemView.findViewById(R.id.giatien);
            hinhmenu = itemView.findViewById(R.id.hinhmenu);
            chuyen = itemView.findViewById(R.id.chuyen);

        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//        linkdatabase = context.getString(R.string.linkreutime);
        final Xe xe = list.get(position);
        if (xe == null) {
            return;
        }
        String linkHanhDB = xe.getAnh();
        Picasso.get().load(linkHanhDB).into(holder.hinhmenu);
        holder.tenmenu.setText(xe.getTen());
        holder.giatien.setText("Giá: "+fm.format(xe.getGiave())+"đ ");
        holder.diachi.setText("Khởi hành lúc: "+xe.getGiokhoihanh()+" ngày "+xe.getNgaykhoihanh());
        holder.chuyen.setText("Tuyến: "+xe.getTuyenchay()+" đén "+ xe.getDiemden());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SuaNhaXe.class);
                intent.putExtra("IDPHONGTRO", ""+ xe.getId());  // Truyền một String
                context.startActivity(intent);
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