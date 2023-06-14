package com.example.contactmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactmanager.MainActivity;
import com.example.contactmanager.R;
import com.example.contactmanager.db.entity.Contact;
import com.example.contactmanager.db.entity.Information;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.MyViewHolder> {

    // 1- Variable
    private Context context;
    private ArrayList<Information> infoList;
    private MainActivity mainActivity;

    // 2- ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView info;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.info = itemView.findViewById(R.id.info);
        }
    }

    public InfoAdapter(Context context, ArrayList<Information> infoList, MainActivity mainActivity){
        this.context = context;
        this.infoList = infoList;
        this.mainActivity = mainActivity;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.info_list_item,parent,false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int positions) {
        final Information information = infoList.get(positions);

        holder.info.setText(information.getNumber());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.UpdateInfo(positions);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mainActivity.DeleteInfo(positions);
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return infoList.size();
    }
}
