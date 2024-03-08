package com.example.and103_ph44245lab1.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.example.and103_ph44245lab1.R;
import com.example.and103_ph44245lab1.cities.City;


import java.util.ArrayList;

public class cities_adapter extends RecyclerView.Adapter<cities_adapter.ViewHolder>{
    Context context;
    ArrayList<City> list;

    private cities_adapter.ViewHolder currentViewHolder;


    public cities_adapter(ArrayList<City> list) {
        this.list = list;
    }



    @NonNull
    @Override
    public cities_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cities,parent,false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull cities_adapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_Name_cities.setText(list.get(position).getName());
        holder.tv_state.setText(list.get(position).getState());
        holder.tv_country.setText(list.get(position).getCountry());
        holder.tv_capital.setChecked(list.get(position).isCapital());
        holder.tv_population.setText(String.valueOf(list.get(position).getPopulation()));
        holder.tv_regions1.setText(list.get(position).getRegions().get(0));
        holder.tv_regions2.setText(list.get(position).getRegions().get(1));
        currentViewHolder = holder; // Lưu trữ holder hiện tại
        // Lưu trữ hình ảnh hiện tại của đối tượng đang được hiển thị

 }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_Name_cities,tv_state, tv_country,tv_population,tv_regions1,tv_regions2;
        CheckBox tv_capital;
        ConstraintLayout mainLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Name_cities = itemView.findViewById(R.id.tv_Name_cities);
            tv_state = itemView.findViewById(R.id.tv_state);
            tv_country = itemView.findViewById(R.id.tv_country);
            tv_capital = itemView.findViewById(R.id.tv_capital);
            tv_population = itemView.findViewById(R.id.tv_population);
            tv_regions1 = itemView.findViewById(R.id.tv_regions1);
            tv_regions2 = itemView.findViewById(R.id.tv_regions2);
        }
    }
}
