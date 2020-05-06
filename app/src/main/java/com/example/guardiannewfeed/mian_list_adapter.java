package com.example.guardiannewfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class mian_list_adapter extends ArrayAdapter<articles> {

    public mian_list_adapter(@NonNull Context context, @NonNull List<articles> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView =convertView;
        if(listItemView == null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }


        TextView titleView= listItemView.findViewById(R.id.titleView);
        TextView typeView= listItemView.findViewById(R.id.typeView);
        TextView sectionView= listItemView.findViewById(R.id.sectionView);

        articles curretnarticle=getItem(position);

        titleView.setText(curretnarticle.getTitle());
        typeView.setText(curretnarticle.getType());
       sectionView.setText(curretnarticle.getSectinName());


        return listItemView;
    }
}
